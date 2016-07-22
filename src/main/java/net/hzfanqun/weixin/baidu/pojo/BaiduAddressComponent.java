package net.hzfanqun.weixin.baidu.pojo;

/**
 * 经纬度坐标到地址的转换服务
 * addressComponent
 * 
 * @author fanqun
 * @version 创建时间：2016年1月28日  上午6:53:04
 */
public class BaiduAddressComponent {
	// 街道名
	private String street;
	// 区县名 
	private String district;
	// 城市名 
	private String city;
	// 省名 
	private String province;
	// 国家
	private String country;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@Override
	public String toString() {
		return "BaiduAddressComponent [street=" + street + ", district=" + district + ", city=" + city + ", province="
				+ province + ", country=" + country + "]";
	}

}
