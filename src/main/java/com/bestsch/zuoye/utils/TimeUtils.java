package com.bestsch.zuoye.utils;



import com.bestsch.zuoye.enums.TimeType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {

	/**
	 * 获取某月的第一天 @Title:getLastDayOfMonth @Description: @param:@param
	 * year @param:@param month @param:@return @return:String @throws
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}
	/**
	 * 获取某月的最后一天 @Title:getLastDayOfMonth @Description: @param:@param
	 * year @param:@param month @param:@return @return:String @throws
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}

	/**
	 * 获取这个月的第一天 @Title:getFirstDayOfCurrentMonth @Description: @param:@param
	 * year @param:@param month @param:@return @return:String @throws
	 */
	public static String getFirstDayOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		// 获取某月最大天数
		int lastDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}
	/**
	 * 获取这个月的最后一天 @Title:getLastDayOfCurrentMonth @Description: @param:@param
	 * year @param:@param month @param:@return @return:String @throws
	 */
	public static String getLastDayOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	
	
   public static String getFirstAndLastOfMonth(String dataStr,String dateFormat,String resultDateFormat) throws ParseException{
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat(dateFormat).parse(dataStr));
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = new SimpleDateFormat(resultDateFormat).format(c.getTime());
        System.out.println("===============first:"+first);

        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = new SimpleDateFormat(resultDateFormat).format(ca.getTime());
        System.out.println("===============last:"+last);
        return first+"_"+last;
    }

    /**
     * 每周的第一天
     * @return
     * @throws ParseException
     */
    public static String getFirstOfWeek(){
        Calendar cal = Calendar.getInstance();
        Date current = new Date();
        cal.setTime(current);
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = 0;
        } else {
            d = 1 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return data1;

    }
    
    /**
     * 每周的最后一天
     * @return
     *
     * @author gaodakui
     * @throws ParseException 
     * @date 2019年5月5日 上午9:33:34
     */
    public static String getLastOfWeek(){
        Calendar cal = Calendar.getInstance();
        Date current = new Date();
        cal.setTime(current);
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = 6;
        } else {
            d = 7 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周结束日期
        String data2 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return data2;

    }

	public static long  timeFmat(long ms){
		long timeNow = new Date().getTime();
		long d = (timeNow - ms)/1000;
		long d_days = Math.round(d / (24*60*60));
		long d_hours = Math.round(d / (60*60));
		long d_minutes = Math.round(d / 60);
		return d_minutes;

	}
	public static double  timeMinutes(long ms){
		long d = ms/1000;
		//保留两位小数
		double s=(double)d/60;
		double d_minutes = (double) (Math.round(s*100)/100.0);
		return Math.round(d_minutes);

	}
	public static double  timeHours(long ms){
		long d = ms/1000;
		//保留两位小数
		double s=(double)d/(60*60);
		double d_hours = (double)Math.round(s*100)/100.0;
		return d_hours;

	}

	/*public static void main(String[] args) {
    	long ms=285000;
		long d = ms/1000;
		//保留两位小数
		double s=(double)d/(60*60);
		double d_hours = Math.round(s*100)/100.0;
		System.out.println(d_hours);
		System.out.println("--"+Math.round(d_hours));
	}*/
	/*
	 * 毫秒转化时分秒毫秒
	 */
	public static String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuffer sb = new StringBuffer();
		if(day > 0) {
			sb.append(day+"天");
		}
		if(hour > 0) {
			sb.append(hour+"小时");
		}
		if(minute > 0) {
			sb.append(minute+"分");
		}
		if(second > 0) {
			sb.append(second+"秒");
		}
		if(milliSecond > 0) {
			sb.append(milliSecond+"毫秒");
		}
		return sb.toString();
	}
	public static Map<String, String> selectTime(Integer index){
		try{
			Map<String, String> map = new HashMap<>();

			String startTime = "";
			String endTime = "";
			// indexStr 0代表什么，1代表什么...
			int currentYear = CalendarUtils.getYear();
			int currentMonth = CalendarUtils.getMonth();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String halfOfJan = ChinaDate.lunarToSolar(currentYear + "0115", false);
			Date halfJan = sdf.parse(halfOfJan);
			if(TimeType.CURRENT_TERM.getId() == index){
				if(new Date().getTime() >= halfJan.getTime()){
					if(currentMonth < 9){
						startTime = halfOfJan + " 00:00:00";
						endTime = currentYear + "-08-31 23:59:59";
					}else{
						startTime = currentYear + "-09-01 00:00:00";
						endTime = ChinaDate.lunarToSolar((currentYear+1) + "0115", false) + " 23:59:59";
					}
				}else{
					startTime = (currentYear-1) + "-09-01 00:00:00";
					endTime = halfOfJan + " 23:59:59";
				}
			}else if(TimeType.CURRENT_MONTH.getId() == index){
				startTime = TimeUtils.getFirstDayOfCurrentMonth() + " 00:00:00";
				endTime = TimeUtils.getLastDayOfCurrentMonth() + " 23:59:59";
			}else if(TimeType.CURRENT_WEEK.getId() == index){
				startTime = TimeUtils.getFirstOfWeek() + " 00:00:00";
				endTime = TimeUtils.getLastOfWeek() + " 23:59:59";
			}else if(TimeType.TODAY.getId() == index){
				startTime = sdf.format(new Date()) + " 00:00:00";
				endTime = sdf.format(new Date()) + " 23:59:59";
			}else if(TimeType.TOTAL.getId() == index){

			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		selectTime(1);
	}
	/**
	 * 获取两个日期之间的所有日期
	 *
	 * @param startTime
	 *            开始日期
	 * @param endTime
	 *            结束日期
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime) {

		// 返回的日期集合
		List<String> days = new ArrayList<String>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);

			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);

			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return days;
	}


//	public static void main(String[] args) throws ParseException {
//		String lastDay = getLastDayOfMonth(2012, 2);
//		System.out.println("获取2012年2月的最后一天：" + lastDay);
//
//		String firstDay = getFirstDayOfMonth(2014, 2);
//		System.out.println("获取2014年2月的第一天：" + firstDay);
//
//		String currentLDay = getLastDayOfCurrentMonth();
//		System.out.println("获取本月的最后一天：" + currentLDay);
//
//		String currentFDay = getFirstDayOfCurrentMonth();
//		System.out.println("获取本月的第一天：" + currentFDay);
//
//		System.out.println("*********************");
//		String firstOfWeek = getFirstOfWeek();
//		System.out.println("获取本周的第一天：" + firstOfWeek);
//
//		String lastOfWeek = getLastOfWeek();
//		System.out.println("获取本周的最后一天：" + lastOfWeek);
//	}

}
