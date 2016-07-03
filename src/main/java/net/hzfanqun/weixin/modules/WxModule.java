package net.hzfanqun.weixin.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.http.Http;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Files;
import org.nutz.lang.LoopException;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.weixin.bean.WxOutMsg;
import org.nutz.weixin.bean.WxTemplateData;
import org.nutz.weixin.bean.WxUser;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

import com.xiaoleilu.hutool.util.SecureUtil;

import net.hzfanqun.weixin.bean.HBWxUser;
import net.hzfanqun.weixin.bean.User;
import net.hzfanqun.weixin.utils.Oauth2Util;

@IocBean(fields = "wxHandler")
public class WxModule {
    private final static Log log = Logs.get();

    private WxHandler wxHandler;

    @Inject("wxApi")
    private WxApi2 wxApi;

    @Inject("config")
    private PropertiesProxy conf;

    @Inject
    private Dao dao;

    @Inject
    private Dao dao_hb;

    public WxModule() {
        Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
    }

    /**
     * 微信入口
     * 
     * @author fanqun(hzfanqun@gmail.com)
     * @date 2016年5月23日 上午6:12:53
     * @param key
     *            外部传入，多个公众号开发时用于区分
     * @param req
     * @return
     * @throws IOException
     */
    @At({"/weixin", "/weixin/?"})
    @Fail("http:200")
    public View msgIn(String key, HttpServletRequest req) throws IOException {
        return Wxs.handle(getWxHandler(key), req, key);
    }

    public WxHandler getWxHandler(String key) {
        return wxHandler;
    }

    @At("/cwxmenu")
    public WxResp createWxMenux() {
        String json = Files.read("custom/wxmenu.json");
        WxResp resp = wxApi.menu_create(new NutMap(json));
        log.info(Json.toJson(resp));
        return resp;
    }

    @At("/sendtmsg")
    public WxResp sendTemplateMsg() {
        String touser = "o7Qdztw5hwdZ0x9R-TDsNol9yQR0";
        String template_id = conf.get("templateid");
        Map<String, WxTemplateData> data = new HashMap<>();
        data.put("first", new WxTemplateData("尊敬的用户您好，您的设备运行发生故障", ""));
        data.put("keyword1", new WxTemplateData("111", ""));
        Date now = new Date(); // 获取当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        data.put("keyword2", new WxTemplateData(dateFormat.format(now), ""));
        data.put("keyword3", new WxTemplateData("AAA", ""));
        data.put("keyword4", new WxTemplateData("DDDD", ""));
        data.put("remark", new WxTemplateData("\n请及时检查恢复！谢谢！", ""));
        WxResp resp = wxApi.template_send(touser, template_id, null, data);
        log.info(Json.toJson(resp));
        return resp;
    }

    @At("/getusers")
    public void getUsers() {
        wxApi.user_get(new Each<String>() {
            public void invoke(int index, String _ele, int length)
                    throws ExitLoop, ContinueLoop, LoopException {
                WxResp resp = wxApi.user_info(_ele, "zh_CN");
                log.info(Json.toJson(resp));
                System.out.println(index
                                   + " : "
                                   + _ele
                                   + ", nickname: "
                                   + resp.user().getNickname());
                User usr = Json.fromJson(User.class, Json.toJson(resp.user()));
                usr.setCreateTime(new Date());
                dao.insert(usr);
            }
        });
    }

    @At("/token")
    @Ok("raw")
    public String getAccessToken() {
        return wxApi.getAccessToken();
    }

    @At("/api/hb/send")
    public Object msgsend(HttpServletRequest req) throws IOException {
        // 从request中取得输入流
        InputStream inputStream = req.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        // 每次读取的内容
        String line = null;
        // 最终读取的内容
        StringBuffer buffer = new StringBuffer();

        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        // 释放资源
        inputStream.close();
        inputStream = null;
        br.close();
        br = null;

        System.out.println("buffer: " + buffer);
        @SuppressWarnings("rawtypes")
        Map map = (Map) Json.fromJson(buffer.toString());
        System.out.println(map.get("content"));

        WxOutMsg omsg = new WxOutMsg();
        return wxApi.send(omsg.setMsgType("text")
                              .setContent((String) map.get("content"))
                              .setToUserName("o7Qdztw5hwdZ0x9R-TDsNol9yQR0"))
                    .ok();

    }

    public void adduser(String hbUsername, String openid, String nickName) {

        HBWxUser wxuser = dao_hb.fetch(HBWxUser.class, Cnd.where("openid", "=", openid));
        if (wxuser != null) {
            dao_hb.delete(wxuser);
        }
        wxuser = new HBWxUser();
        wxuser.setOpenId(openid);
        wxuser.setHbUsername(hbUsername);
        wxuser.setNickName(nickName);
        dao_hb.insert(wxuser);
    }

    public void oauthUrlTest() {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        url = url.replace("APPID", conf.get("appid"))
                .replace("REDIRECT_URI",
                        Oauth2Util.urlEncodeUTF8(conf.get("oauth.redirect")
                                + "/hb/oauth/service"));
        System.out.println(url);
    }

    @SuppressWarnings("rawtypes")
    @At("/hb/oauth/service")
    @Ok("re:jsp:hb.bindhb")
    public String oauthService(HttpServletRequest req) {
        String openid = (String) req.getSession().getAttribute("openid");

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID", conf.get("appid"))
                 .replace("SECRET", conf.get("appsecret"))
                 .replace("CODE", req.getParameter("code"));
        Map map = (Map) Json.fromJson(Http.get(url).getContent());
        System.out.println("map=" + map );
        System.out.println("openid=" + openid + ",  errcode=" +map.get("errcode"));

        if (map.get("errcode") == null) {
            openid = (String) map.get("openid");
            req.getSession().setAttribute("openid", openid);
            System.out.println("openid=" + openid);
        }
        HBWxUser wxuser = dao_hb.fetch(HBWxUser.class,
                                       Cnd.where("openid", "=", openid).and("openid",
                                                                            "NOT IS",
                                                                            null));
        if (wxuser != null) {
            req.setAttribute("hbUsername", wxuser.getHbUsername());
            return "jsp:hb.isBinding";
        }
        return null;
    }

    @At("/hb/login/validate")
    @Ok("re:jsp:hb.bind_ok")
    public String hbLoginValidation(HttpServletRequest req,
                                    @Param("hbUsername") String username,
                                    @Param("password") String password,
                                    @Param("openid") String openid) {

        String encode = SecureUtil.encrypt(password, SecureUtil.MD5, "utf-8");
        Record re = dao_hb.fetch("user_",
                                 Cnd.where("username", "=", username)
                                    .and("password", "=", encode)
                                    .and("archived", "=", false));
        if (re == null) {
            req.setAttribute("errmsg", "账号或密码验证失败");
            return "jsp:hb.bind_error";
        }

        req.setAttribute("hbUsername", username);
        WxResp resp = wxApi.user_info(openid, "zh_CN");
        WxUser wuser = resp.user();
        adduser(username, wuser.getOpenid(), wuser.getNickname());
        return null;
    }

}
