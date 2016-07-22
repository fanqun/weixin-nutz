package net.hzfanqun.weixin.baidu.pojo;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户地理位置model
 * 
 * @author fanqun
 * @date 2016-01-27
 */

@Table("user_location")
public class UserLocation {
	@Id
	private int id;
	@Column("open_id")
	private String openId;
	@Column
	@ColDefine(width = 128)
	private String label;
	@Column
	private String lng;
	@Column
	private String lat;
	@Column("bd09_lng")
	private String bd09Lng;
	@Column("bd09_lat")
	private String bd09Lat;
	@Column("create_time")
	private Date ct;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

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

	public String getBd09Lng() {
		return bd09Lng;
	}

	public void setBd09Lng(String bd09Lng) {
		this.bd09Lng = bd09Lng;
	}

	public String getBd09Lat() {
		return bd09Lat;
	}

	public void setBd09Lat(String bd09Lat) {
		this.bd09Lat = bd09Lat;
	}

	public Date getCt() {
		return ct;
	}

	public void setCt(Date ct) {
		this.ct = ct;
	}

	@Override
	public String toString() {
		return "UserLocation [id=" + id + ", openId=" + openId + ", lng=" + lng + ", lat=" + lat + ", bd09Lng="
				+ bd09Lng + ", bd09Lat=" + bd09Lat + ", ct=" + ct + "]";
	}
}
