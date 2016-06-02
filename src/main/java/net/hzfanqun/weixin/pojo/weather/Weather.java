package net.hzfanqun.weixin.pojo.weather;

public class Weather {
	private String date;
	private String high;
	private String low;
	private DayNight day;
	private DayNight night;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public DayNight getDay() {
		return day;
	}
	public void setDay(DayNight day) {
		this.day = day;
	}
	public DayNight getNight() {
		return night;
	}
	public void setNight(DayNight night) {
		this.night = night;
	}
}
