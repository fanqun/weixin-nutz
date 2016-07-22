package net.hzfanqun.weixin.baidu.pojo;

/**
 * 地址信息
 * 
 * @author fanqun
 * @date 2016-01-26
 */
public class BaiduPlace implements Comparable<BaiduPlace> {
	
	@Override
	public String toString() {
		return "BaiduPlace [name=" + name + ", location=" + location + ", address=" + address + ", telephone="
				+ telephone + ", detail_info=" + detail_info + "]";
	}

	// 名称
	private String name;
	// 位置
	private Location location;
	// 详细地址
	private String address;
	// 联系电话
	private String telephone;
	// 距离
	private DetailInfo detail_info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public DetailInfo getDetail_info() {
		return detail_info;
	}

	public void setDetail_info(DetailInfo detail_info) {
		this.detail_info = detail_info;
	}

	@Override
	public int compareTo(BaiduPlace baiduPlace) {
		return this.detail_info.getDistance() - baiduPlace.getDetail_info().getDistance();
	}
}


