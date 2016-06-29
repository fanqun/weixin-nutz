package net.hzfanqun.weixin.modules;

import net.hzfanqun.weixin.bean.User;
import net.hzfanqun.weixin.pojo.baidu.BaiduPlace;
import net.hzfanqun.weixin.pojo.baidu.UserLocation;
import net.hzfanqun.weixin.utils.BaiduMapUtil;
import net.hzfanqun.weixin.utils.WeatherUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.weixin.bean.WxArticle;
import org.nutz.weixin.bean.WxInMsg;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.impl.BasicWxHandler;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

import java.util.Date;
import java.util.List;

/**
 * @author fanqun(hzfanqun@gmail.com)
 * @date 2016年5月22日 下午9:11:42
 */
@IocBean(name = "wxHandler", args = {"java:$config.get('token')", "java:$config.get('encodingAesKey')", "java:$config.get('appid')"})
public class MainWxHandler extends BasicWxHandler {
    private final static Log log = Logs.get();

    @Inject("wxApi")
    protected WxApi2 api;

    @Inject
    protected PropertiesProxy config;

    @Inject
    protected Dao dao;


    public MainWxHandler(String token, String aesKey, String appId) {
        super(token, aesKey, appId);
    }


    public WxOutMsg text(WxInMsg msg) {
        String content = msg.getContent();
        if (content.equals("附近")) {
            return Wxs.respText(getUsage());
        }
        // 周边搜索
        else if (content.startsWith("附近")) {
            String keyWord = content.replaceAll("附近", "").trim();
            // 获取用户最后一次发送的地理位置
            //UserLocation location = LocationMySQLUtil.getLastLocation(msg.getFromUserName());
            UserLocation location = dao.fetch(UserLocation.class, Cnd.where("openId", "=", msg.getFromUserName()).desc("ct"));
            //System.out.println("getLastLocation: " + location);
            // 未获取到
            if (null == location) {
                return Wxs.respText(getUsage());
            } else {
                // 根据转换后（纠偏）的坐标搜索周边POI
                List<BaiduPlace> placeList = null;
                try {
                    placeList = BaiduMapUtil.searchPlace(keyWord, location.getBd09Lng(), location.getBd09Lat());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 未搜索到POI
                if (null == placeList || 0 == placeList.size()) {
                    return Wxs.respText(String.format("/难过，您发送的位置附近未搜索到“%s”信息！", keyWord));
                } else {
                    List<WxArticle> articleList = BaiduMapUtil.makeArticleList(placeList, location.getBd09Lng(),
                            location.getBd09Lat());
                    // 回复图文消息
                    return Wxs.respNews(null, articleList);
                }
            }
        } else
            return Wxs.respText(null, msg.getContent()); // 否则,学舌！
    }

    public WxOutMsg eventClick(WxInMsg msg) {
        String eventKey = msg.getEventKey();
        log.info("eventKey: " + eventKey);
        if ("mk_index".equalsIgnoreCase(eventKey)) {
            return Wxs.respText(null, WeatherUtil.queryWeatherIndex("101210101"));
        } else if ("mk_forecast".equalsIgnoreCase(eventKey)) {
            WeatherUtil.queryWeatherInfo("101210101");
            return Wxs.respNews(null, WeatherUtil.queryWeatherInfo("101210101"));
        } else if ("mk_search".equalsIgnoreCase(eventKey)) {
            return Wxs.respText(getUsage());
        } else
            return defaultMsg(msg);
    }

    @Override
    public WxOutMsg eventSubscribe(WxInMsg msg) {
        User usr = dao.fetch(User.class, Cnd.where("openid", "=", msg.getFromUserName()));
        WxResp resp = api.user_info(msg.getFromUserName(), "zh_CN");
        if (usr == null) {
            usr = Json.fromJson(User.class, Json.toJson(resp.user()));
            usr.setCreateTime(new Date());
            dao.insert(usr);
        } else {
            usr = Json.fromJson(User.class, Json.toJson(resp.user()));
            usr.setCreateTime(null);
            dao.updateIgnoreNull(usr);
        }
        return Wxs.respText(null, "谢谢您的关注！");
    }

    @Override
    public WxOutMsg eventUnsubscribe(WxInMsg msg) {
        User usr = dao.fetch(User.class, Cnd.where("openid", "=", msg.getFromUserName()));
        if (usr != null) {
            System.out.println("delete user from DB. Nickname: " + usr.getNickname());
            dao.delete(User.class, usr.getId());
        }

        return super.eventUnsubscribe(msg);
    }


    @Override
    public WxOutMsg location(WxInMsg msg) {
        StringBuffer buffer = new StringBuffer();
        // System.out.println("地理位置消息");
        // System.out.println("Location_X=" + msg.getLocation_X() + ",Location_Y" + msg.getLocation_Y());
        // 用户发送的经纬度
        String lng = String.valueOf(msg.getLocation_Y());// requestMap.get("Location_Y");
        String lat = String.valueOf(msg.getLocation_X());// requestMap.get("Location_X");
        // 坐标转换后的经纬度
        // String bd09Lng = null;
        // String bd09Lat = null;
        // 调用接口转换坐标
        UserLocation userLocation = BaiduMapUtil.geoconv(lng, lat);
        if (null != userLocation) {
            userLocation.setOpenId(msg.getFromUserName());
            userLocation.setLabel(msg.getLabel());
            userLocation.setCt(new Date());
            //System.out.println(userLocation);
            // if (null != userLocation) {
            // bd09Lng = userLocation.getBd09Lng();
            // bd09Lat = userLocation.getBd09Lat();
            // }
            // 保存用户地理位置
            // LocationMySQLUtil.saveUserLocation(msg.getFromUserName(), lng, lat, bd09Lng, bd09Lat);
            dao.insert(userLocation);

            buffer.append("[愉快]").append("成功接收您的位置！").append("\n\n");
            buffer.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
            buffer.append("        附近学校").append("\n");
            buffer.append("        附近餐厅").append("\n");
            buffer.append("        附近银行").append("\n");
            buffer.append("        附近小区").append("\n");
            buffer.append("必须以“附近”两个字开头！");
        } else {
            buffer.append("[难过]").append("您的位置没有接收成功！请重新发送您的位置。");
        }

        return Wxs.respText(buffer.toString());
    }

    @Override
    public WxOutMsg defaultMsg(WxInMsg msg) {
        return Wxs.respText("这是缺省回复哦.你发送的类型是:" + msg.getMsgType());
    }

    /**
     * 使用说明
     *
     * @return
     */
    private static String getUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("周边搜索使用说明").append("\n\n");
        buffer.append("1）发送地理位置").append("\n");
        buffer.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n\n");
        buffer.append("2）指定关键词搜索").append("\n");
        buffer.append("格式：附近+关键词\n例如：附近ATM、附近学校、附近美食");
        return buffer.toString();
    }

}
