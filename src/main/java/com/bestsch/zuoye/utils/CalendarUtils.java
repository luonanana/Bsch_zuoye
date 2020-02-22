package com.bestsch.zuoye.utils;

import java.util.Calendar;

public class CalendarUtils {
	
	private static Calendar c = Calendar.getInstance(); 
	
	public static int getYear(){
		return c.get(Calendar.YEAR);
	}
	
	public static int getMonth(){
		return c.get(Calendar.MONTH)+1;	//获取月份
	}
	
	public static int getDay(){
		return c.get(Calendar.DATE);
	}
	
	public static int get12Hour(){
		return c.get(Calendar.HOUR);
	}
	
	public static int get24Hour(){
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(){
		return c.get(Calendar.MINUTE);
	}
	
	public static int getSecond(){
		return c.get(Calendar.SECOND);
	}
	
	public static int getDayOfWeek(){
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
