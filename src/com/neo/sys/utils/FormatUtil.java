package com.neo.sys.utils;

import javax.xml.ws.Endpoint;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * Java8 格式化日期的线程安全方法
 */
public class FormatUtil {

	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String formatDate(Date date){
		if(date == null){
			return "";
		}
		return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault()).format(dateFormat);
	}
	public static String formatTime(Date date){
		if(date == null){
			return "";
		}
		return LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault()).format(timeFormat);
	}

	public static Date parseDate(String date){
		date += " 00:00:00";
		try {
			//return new Date(LocalDateTime.parse(date,timeFormat).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());//中国时间
            return Date.from(LocalDateTime.parse(date,timeFormat).atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseTime(String date){
		try {
			//return new Date(LocalDateTime.parse(date,timeFormat).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
            return Date.from(LocalDateTime.parse(date,timeFormat).atZone(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
