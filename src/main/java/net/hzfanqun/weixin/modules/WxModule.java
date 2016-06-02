package net.hzfanqun.weixin.modules;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Dao;
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
import org.nutz.weixin.bean.WxTemplateData;
import org.nutz.weixin.spi.WxApi2;
import org.nutz.weixin.spi.WxHandler;
import org.nutz.weixin.spi.WxResp;
import org.nutz.weixin.util.Wxs;

import net.hzfanqun.weixin.bean.User;

@IocBean(fields = "wxHandler")
public class WxModule  {
	private final static Log log = Logs.get();

	private WxHandler wxHandler;

	@Inject("wxApi")
	private WxApi2 wxApi;

	@Inject("config")
	private PropertiesProxy conf;

	@Inject
	private Dao dao;

	public WxModule() {
		Wxs.enableDevMode(); // 开启debug模式,这样就会把接收和发送的内容统统打印,方便查看
	}

	/**
	 * 微信入口
	 * 
	 * @author fanqun(hzfanqun@gmail.com)
	 * @date 2016年5月23日 上午6:12:53
	 * @param key 外部传入，多个公众号开发时用于区分
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@At({ "/weixin", "/weixin/?" })
	@Fail("http:200")
	public View msgIn(String key, HttpServletRequest req) throws IOException {
		return Wxs.handle(getWxHandler(key), req, key);
	}

	public WxHandler getWxHandler(String key) {
		return wxHandler;
	}
	
	@At("/cwxmenu")
	public WxResp createWxMenux(){
		String json = Files.read("custom/wxmenu.json");
		WxResp resp = wxApi.menu_create(new NutMap(json));
		log.info(Json.toJson(resp));
		return resp;
	}
	
	@At("/sendtmsg")
	public WxResp sendTemplateMsg(){
		String touser="o7Qdztw5hwdZ0x9R-TDsNol9yQR0";
		String template_id = conf.get("templateid");
		Map<String, WxTemplateData> data = new HashMap<>();
		data.put("first", new WxTemplateData("尊敬的用户您好，您的设备运行发生故障",""));
		data.put("keyword1", new WxTemplateData("111",""));
		Date now = new Date(); //获取当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		data.put("keyword2", new WxTemplateData(dateFormat.format(now),""));
		data.put("keyword3", new WxTemplateData("AAA",""));
		data.put("keyword4", new WxTemplateData("DDDD",""));
		data.put("remark", new WxTemplateData("\n请及时检查恢复！谢谢！",""));
		WxResp resp = wxApi.template_send(touser, template_id, null, data);
		log.info(Json.toJson(resp));
		return resp;
	}
	
	@At("/getusers")
	public void getUsers() {
		wxApi.user_get(new Each<String>() {
			public void invoke(int index, String _ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				WxResp resp = wxApi.user_info(_ele, "zh_CN");
				log.info(Json.toJson(resp));
				System.out.println(index + " : " + _ele + ", nickname: " + resp.user().getNickname());
				User usr = Json.fromJson(User.class, Json.toJson(resp.user()));
				usr.setCreateTime(new Date());
				dao.insert(usr);
			}
		});
	}
	
	@At()
	@Ok("raw")
	public String getAccessToken(){
		return wxApi.getAccessToken();
	}

}
