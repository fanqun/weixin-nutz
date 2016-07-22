package net.hzfanqun.weixin.baidu.pojo;

public class DetailInfo {
	
	private String tag;
	private String detail_url;
	private int distance;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDetail_url() {
		return detail_url;
	}
	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getDistance() {
		return distance;
	}
	
	@Override
	public String toString() {
		return "DetailInfo [tag=" + tag + ", detail_url=" + detail_url + ", distance=" + distance + "]";
	}

}
