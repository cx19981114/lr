package cn.lr.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil {
	public static String timeStampToString(long timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置格式
		String timeText = format.format(timestamp); // 获得带格式的字符串
		return timeText;
	}

	public static Timestamp stringToTimeStamp(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 设置要读取的时间字符串格式
		Date date;
		date = format.parse(time);
		// 转换为Date类
		Long timestamp = date.getTime();
		return new java.sql.Timestamp(timestamp);
		// 获得时间戳
	}
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 设置格式
		String timeText = format.format(date); // 获得带格式的字符串
		return timeText;
	}
	
	public static Date stringTodate(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(time);
		return date;
	}
}
