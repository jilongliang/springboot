package com.flong.springboot.core.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Date;
/**
 * @Description	DateUtil
 * @ClassName	DateUtil
 * @Date		2018年2月3日 下午1:38:38
 * @Author		liangjl
 * @Copyright (c) All Rights Reserved, 2018.
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {

	
	/**
	 * @Description 获取两者之间的day
	 * @Author		liangjl
	 * @Date		2018年2月3日 下午1:38:48
	 * @param endDate
	 * @param nowDate
	 * @return 参数
	 * @return String 返回类型 
	 * @throws
	 */
	public static long getBetweeDay(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    long day = diff / nd;
	    return day;
	}
	
	 
	
	/**
	 * @Description 获取两者之间的hour
	 * @Author		liangjl
	 * @Date		2018年2月3日 下午1:38:48
	 * @param endDate
	 * @param nowDate
	 * @return 参数
	 * @return String 返回类型 
	 * @throws
	 */
	public static long getBetweeHour(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少天
	    // 计算差多少小时
	    long hour = diff % nd / nh;
	    return hour;
	}
	
	/**
	 * @Description 获取两者之间的分
	 * @Author		liangjl
	 * @Date		2018年2月3日 下午1:38:48
	 * @param endDate
	 * @param nowDate
	 * @return 参数
	 * @return String 返回类型 
	 * @throws
	 */
	public static long getBetweeMin(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少分钟
	    long min = diff % nd % nh / nm;
	    // 计算差多少秒//输出结果
	    return min;
	}
	
	/**
	 * @Description 获取两者之间的秒(second)
	 * @Author		liangjl
	 * @Date		2018年2月3日 下午1:38:48
	 * @param endDate
	 * @param nowDate
	 * @return 参数
	 * @return String 返回类型 
	 * @throws
	 */
	public static long getBetweeSecond(Date endDate, Date nowDate) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    long ns = 1000;
	    // 获得两个时间的毫秒时间差异
	    long diff = endDate.getTime() - nowDate.getTime();
	    // 计算差多少秒//输出结果
	    long  second =diff % nd % nh % nm / ns;
	    return second;
	    //return day + "天" + hour + "小时" + min + "分钟";
	}
	

	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		//		System.out.println(formatDate(parseDate("2010/3/6")));
		//		System.out.println(getDate("yyyy年MM月dd日 E"));
		//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
		//		System.out.println(time/(24*60*60*1000));
	}

	
	
	
}