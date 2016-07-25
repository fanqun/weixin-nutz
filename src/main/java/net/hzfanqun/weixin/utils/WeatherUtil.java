package net.hzfanqun.weixin.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.json.Json;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Xmls;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxArticle;
import org.w3c.dom.Element;

import net.hzfanqun.weixin.weather.pojo.Weather;
import net.hzfanqun.weixin.weather.pojo.WeatherResp;
import net.hzfanqun.weixin.weather.pojo.Zhishu;

/**
 * 获取天气
 * 
 * @author fanqun
 * @version 创建时间：2016年5月25日 下午13:52:06
 */
public class WeatherUtil {

	/**
	 * 根据城市id获取天气，返回XML格式数据
	 * 
	 * @param citykey
	 * @return
	 */
	public static String getWeatherRespXml(String citykey) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("citykey", citykey);
		String response = Http.post("http://wthrcdn.etouch.cn/WeatherApi", parms, "UTF-8", "UTF-8");
		return response;
	}

	/**
	 * 获取天气指数
	 * 
	 * @return
	 */
	public static String queryWeatherIndex(String citykey) {
		if (citykey == null)
			return null;
		StringBuffer sb = new StringBuffer();
		String fromXml = WeatherUtil.getWeatherRespXml(citykey);
		sb.append(WeatherUtil.getCurrentWeather(fromXml).getCity()).append("天气指数 \n");
		List<Zhishu> zhishu = new ArrayList<>();
		Element ele = Xmls.getEle(Xmls.xml(Lang.ins(fromXml)).getDocumentElement(), "zhishus");
		Xmls.eachChildren(ele, new Each<Element>() {
			public void invoke(int index, Element _ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				Map<String, Object> tmp = Xmls.asMap(_ele, false);
				zhishu.add(Json.fromJson(Zhishu.class,Json.toJson(tmp)));
			}
		});
		for (Zhishu z : zhishu) {
			sb.append(z.getName()).append("，").append(z.getValue()).append("，").append(z.getDetail()).append("\n");
		}

		return sb.toString();
	}
	
	/**
	 * 获取天气预报信息
	 * 
	 * @param cityKey
	 * 
	 */
	public static List<WxArticle> queryWeatherInfo(String cityKey) {
		List<WxArticle> articleList = new ArrayList<>();
		WxArticle articleTop = new WxArticle();
		StringBuffer sb = new StringBuffer();
		String fromXml = WeatherUtil.getWeatherRespXml(cityKey);
		WeatherResp wr = WeatherUtil.getCurrentWeather(fromXml);
		sb.append(wr.getCity()).append("天气预报 \n");
		sb.append("温度: ").append(wr.getWendu()).append("℃ ");
		sb.append("风力: ").append(wr.getFengxiang()).append(wr.getFengli()).append("\n");
		sb.append("湿度: ").append(wr.getShidu()).append(" ");
		sb.append("更新: ").append(wr.getUpdatetime());
		articleTop.setTitle(sb.toString());
		articleTop.setDescription(sb.toString());
		articleTop.setPicUrl("http://wx.heishitech.com/images/top.jpg");
		articleTop.setUrl("");
		articleList.add(articleTop);
		List<Weather> weather = new ArrayList<>();
		weather = WeatherUtil.getForecastWeather(fromXml);
		for (int i = 0; i < weather.size(); i++) {
			WxArticle article = new WxArticle();
			Weather w = weather.get(i);
			Date time = new Date();
			// System.out.println(new Date());
			SimpleDateFormat format = new SimpleDateFormat("HH");
			int x = Integer.parseInt(format.format(time));
			// 当日18点以后显示晚上天气
			if (i == 0 && x > 17) {
				System.out.println("x=" + x);
				article.setTitle(w.getDate() + " " + w.getNight().getType() + " " + w.getLow().replace("低温", "").trim() + "~" + w.getHigh().replace("高温", "").trim());
				article.setPicUrl("http://wx.heishitech.com/images/" + WeatherUtil.getWeatherPic(w.getNight().getType().replace("转", "_").replace("到", "_")));
			} else {
				article.setTitle(w.getDate() + " " + w.getDay().getType() + " " + w.getLow().replace("低温", "").trim() + "~" + w.getHigh().replace("高温", "").trim());
				article.setPicUrl("http://wx.heishitech.com/images/" + WeatherUtil.getWeatherPic(w.getDay().getType().replace("转", "_").replace("到", "_")));
			}
			article.setDescription("");
			article.setUrl("");
			articleList.add(article);
			sb.append(w.getDate()).append(" ");
			sb.append(w.getLow().replace("低温", "").trim()).append("~").append(w.getHigh().replace("高温", "").trim()).append("\n");
			sb.append("    白天: ").append(w.getDay().getType()).append(" ").append(w.getDay().getFengxiang() + w.getDay().getFengli()).append("\n");
			sb.append("    晚上: ").append(w.getNight().getType()).append(" ").append(w.getNight().getFengxiang() + w.getNight().getFengli()).append("\n");
		}
		return articleList;
	}
	
	/**
	 * 获取当前天气情况
	 * 
	 * @param fromXML
	 * @return
	 */
	public static WeatherResp getCurrentWeather(String fromXML) {
		return Json.fromJson(WeatherResp.class, Json.toJson(Xmls.xmlToMap(fromXML)));
	}	

	/**
	 * 获取天气预报
	 * 
	 * @param fromXML
	 * @return
	 */
	public static List<Weather> getForecastWeather(String fromXML) {
		List<Weather> weather = new ArrayList<>();
		
		Element ele = Xmls.getEle(Xmls.xml(Lang.ins(fromXML)).getDocumentElement(), "forecast");
		Xmls.eachChildren(ele, new Each<Element>() {
			public void invoke(int index, Element _ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				Map<String, Object> tmp = Xmls.asMap(_ele, false);
				weather.add(Json.fromJson(Weather.class,Json.toJson(tmp)));
			}
		});
		
		return weather;
	}	
	
	/**
	 * 根据天气类型获取显示图片名称
	 * 
	 * @param type
	 * @return
	 */
	public static String getWeatherPic(String type) {
		String picUrl = "weather_" + ChineseToEnglish.getPinYin(type) + ".png";
		return picUrl;
	}

	
	public static void main(String[] args) {
		List<Weather> weather = new ArrayList<>();;
		
		String fromXml = getWeatherRespXml("101210101");
		Element ele = Xmls.getEle(Xmls.xml(Lang.ins(fromXml)).getDocumentElement(), "forecast");

		Xmls.eachChildren(ele, new Each<Element>() {
			public void invoke(int index, Element _ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				Map<String, Object> tmp = Xmls.asMap(_ele, false);
				System.out.println(index+1 + " of " + length + " >> " + Json.toJson(tmp));
				weather.add(Json.fromJson(Weather.class,Json.toJson(tmp)));
			}
		});

		System.out.println(Json.toJson(weather));
		
		
		NutMap map = Xmls.xmlToMap(fromXml);
		
		WeatherResp wr = WeatherUtil.getCurrentWeather(fromXml);

		System.out.println(fromXml);
		System.out.println(wr.getCity());
		System.out.println(map.size());
		System.out.println(map.get("environment"));
	}

}
