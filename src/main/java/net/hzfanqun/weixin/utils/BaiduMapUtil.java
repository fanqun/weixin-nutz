package net.hzfanqun.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import it.sauronsoftware.base64.Base64;
import net.hzfanqun.weixin.baidu.pojo.BaiduAddressComponent;
import net.hzfanqun.weixin.baidu.pojo.BaiduPlace;
import net.hzfanqun.weixin.baidu.pojo.UserLocation;

/**
 * 百度地图操作类
 * 
 * @author fanqun
 * @date 2016-01-27
 */
public class BaiduMapUtil {
	/**
	 * 圆形区域检索
	 * 
	 * @param query
	 *            检索关键词
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return List<BaiduPlace>
	 * @throws UnsupportedEncodingException
	 */
	public static List<BaiduPlace> searchPlace(String query, String lng, String lat) throws Exception {
		// 拼装请求地址
		String requestUrl = "http://api.map.baidu.com/place/v2/search?&query=QUERY&location=LAT,LNG&radius=2000&output=xml&scope=2&page_size=10&page_num=0&ak=eUUTwRTc6ExNhelVQsgUH4BG7vuyiDKF";
		requestUrl = requestUrl.replace("QUERY", URLEncoder.encode(query, "UTF-8"));
		requestUrl = requestUrl.replace("LAT", lat);
		requestUrl = requestUrl.replace("LNG", lng);
		// 调用Place API圆形区域检索
		//String respXml = Http.get(requestUrl).getContent();
		
		List<BaiduPlace> placeList = new ArrayList<>();
		Element ele = Xmls.getEle(Xmls.xml(Http.get(requestUrl).getStream()).getDocumentElement(), "results");
		Xmls.eachChildren(ele, new Each<Element>() {
			public void invoke(int index, Element _ele, int length) throws ExitLoop, ContinueLoop, LoopException {
				Map<String, Object> tmp = Xmls.asMap(_ele, false);
				//System.out.println(tmp);
				//System.out.println(Lang.map2Object(tmp, BaiduPlace.class));
				placeList.add(Lang.map2Object(tmp, BaiduPlace.class));
			}
		});

		//System.out.println(placeList);
		// 解析返回的xml
		//List<BaiduPlace> placeList = parsePlaceXml(respXml);
		return placeList;
	}

	/**
	 * 经纬度坐标到地址的转换服务
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return BaiduAddressComponent
	 * @throws Exception
	 */
	public static BaiduAddressComponent queryAddress(String lng, String lat) throws Exception {
		// 拼装请求地址
		String requestUrl = "http://api.map.baidu.com/geocoder/v2/?location=LAT,LNG&ak=eUUTwRTc6ExNhelVQsgUH4BG7vuyiDKF&callback=renderReverse&output=xml";
		requestUrl = requestUrl.replace("LAT", lat);
		requestUrl = requestUrl.replace("LNG", lng);
		// 调用Geocoding API 逆地理编码服务
		System.out.println(Xmls.asMap(Xmls.getEle(Xmls.xml(Http.get(requestUrl).getStream()).getDocumentElement(), "result/addressComponent")));
		String respXml = Http.get(requestUrl).getContent();
		System.out.println(respXml);
		return Lang.map2Object(Xmls.asMap(Xmls.getEle(Xmls.xml(Http.get(requestUrl).getStream()).getDocumentElement(), "result/addressComponent")),BaiduAddressComponent.class);
	}


	/**
	 * 根据Place组装图文列表
	 * 
	 * @param placeList
	 * @param bd09Lng
	 *            经度
	 * @param bd09Lat
	 *            纬度
	 * @return List<Article>
	 */
	public static List<WxArticle> makeArticleList(List<BaiduPlace> placeList, String bd09Lng, String bd09Lat) {
		// 项目的根路径
		String basePath = "http://wx.heishitech.com/";
		String imagePath = "http://wx.heishitech.com/images/";
		List<WxArticle> list = new ArrayList<>();
		BaiduPlace place = null;
		for (int i = 0; i < placeList.size(); i++) {
			place = placeList.get(i);
			WxArticle article = new WxArticle();
			article.setTitle(place.getName() + "\n距离约" + place.getDetail_info().getDistance() + "米");
			// P1表示用户发送的位置（坐标转换后），p2表示当前POI所在位置
			article.setUrl(String.format(basePath + "route.jsp?p1=%s,%s&p2=%s,%s", bd09Lng, bd09Lat, place.getLocation().getLng(), place.getLocation().getLat()));
			// 将首条图文的图片设置为大图
			if (i == 0)
				article.setPicUrl(imagePath + "poisearch.png");
			else
				article.setPicUrl(imagePath + "navi.png");
			list.add(article);
		}
		return list;
	} 


	/**
	 * 将微信定位的坐标转换成百度坐标（GCJ-02 -> Baidu）
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return UserLocation
	 */
	
	
	public static UserLocation convertCoord(String lng, String lat) {
		// 百度坐标转换接口
		String convertUrl = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x={x}&y={y}";
		convertUrl = convertUrl.replace("{x}", lng);
		convertUrl = convertUrl.replace("{y}", lat);

		UserLocation location = new UserLocation();
		try {
			NutMap map = Json.fromJson(NutMap.class,Json.fromJson(NutMap.class,Http.get(convertUrl).getContent()).getString("result"));
			// 对转换后的坐标进行Base64解码
			location.setBd09Lng(Base64.decode(map.getString("x"), "UTF-8"));
			location.setBd09Lat(Base64.decode(map.getString("y"), "UTF-8"));
		} catch (Exception e) {
			location = null;
			e.printStackTrace();
		}
		return location;
	} 

	/**
	 * 通过百度地图坐标转换API将微信定位的坐标转换成百度坐标（soso地图 -> Baidu）
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @return UserLocation
	 * @throws Exception 
	 */
	public static UserLocation geoconv(String lng, String lat) {
		// 百度坐标转换接口
		String convertUrl = "http://api.map.baidu.com/geoconv/v1/?coords={x},{y}&from=3&to=5&ak=eUUTwRTc6ExNhelVQsgUH4BG7vuyiDKF";
		convertUrl = convertUrl.replace("{x}", lng);
		convertUrl = convertUrl.replace("{y}", lat);

		UserLocation location = new UserLocation();
		try {
			NutMap map = Json.fromJson(NutMap.class,Json.fromJson(NutMap.class,Http.get(convertUrl).getContent()).getString("result"));
			location.setLat(lat);
			location.setLng(lng);
			location.setBd09Lng(map.getString("x"));
			location.setBd09Lat(map.getString("y"));
		} catch (Exception e) {
			location = null;
			e.printStackTrace();
		}
		return location;
	} 

	public static void main(String[] args) throws Exception {
		searchPlace("酒店","116.404","39.915");
		System.out.println(queryAddress("116.404","39.915"));
		geoconv("116.404","39.915");
	}
	
}
