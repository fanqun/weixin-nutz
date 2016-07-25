package net.hzfanqun.weixin.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import net.hzfanqun.weixin.baidu.pojo.UserLocation;


/**
 * Mysql数据库操作类
 * 
 * @author fanqun
 * @date 2016-01-27
 */
public class LocationMySQLUtil {
	/**
	 * 获取Mysql数据库连接
	 * 
	 * @return Connection
	 */
	private Connection getConn() {
		Connection conn = null;

		// IP、端口、用户名和密码
		String host = "localhost";
		String port = "8590";
		String username = "root";
		String password = "";
		// 数据库名称
		String dbName = "test";
		// JDBC URL
		String url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);

		try {
			// 加载MySQL驱动
			Class.forName("com.mysql.jdbc.Driver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 保存用户地理位置
	 * 
	 * @param openId 用户的OpenID
	 * @param lng 用户发送的经度
	 * @param lat 用户发送的纬度
	 * @param bd09_lng 经过百度坐标转换后的经度
	 * @param bd09_lat 经过百度坐标转换后的纬度
	 */
	public static void saveUserLocation(String openId, String lng, String lat, String bd09_lng, String bd09_lat) {
		String sql = "insert into user_location(open_id, lng, lat, bd09_lng, bd09_lat, create_time) values (?, ?, ?, ?, ?, ?)";
		try {
			Connection conn = new LocationMySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ps.setString(2, lng);
			ps.setString(3, lat);
			ps.setString(4, bd09_lng);
			ps.setString(5, bd09_lat);
			ps.setTimestamp(6, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
			ps.executeUpdate();
			// 释放资源
			ps.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户最后一次发送的地理位置
	 * 
	 * @param openId 用户的OpenID
	 * @return UserLocation
	 */
	public static UserLocation getLastLocation(String openId) {
		UserLocation userLocation = null;
		String sql = "select open_id, lng, lat, bd09_lng, bd09_lat from user_location where open_id=? order by id desc limit 0,1";
		try {
			Connection conn = new LocationMySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				userLocation = new UserLocation();
				userLocation.setOpenId(rs.getString("open_id"));
				userLocation.setLng(rs.getString("lng"));
				userLocation.setLat(rs.getString("lat"));
				userLocation.setBd09Lng(rs.getString("bd09_lng"));
				userLocation.setBd09Lat(rs.getString("bd09_lat"));
			}
			// 释放资源
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userLocation;
	}
}
