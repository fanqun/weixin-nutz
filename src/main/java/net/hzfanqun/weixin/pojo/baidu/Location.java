package net.hzfanqun.weixin.pojo.baidu;

public class Location {
	// 经度
	private String lng;
	// 纬度
	private String lat;

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "Location [lng=" + lng + ", lat=" + lat + "]";
	}
}
