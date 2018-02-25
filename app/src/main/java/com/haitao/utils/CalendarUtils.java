package com.haitao.utils;


import java.util.Calendar;

/**
 * 时间相关的工具类
 */
public class CalendarUtils {
	/**
	 * 设置时间
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Calendar setCalendar(int year,int month,int date){
		Calendar cl = Calendar.getInstance();
		cl.set(year, month-1, date);
		return cl;
	}

	/**
	 * 获取当前时间的前一天时间
	 * @param cl
	 * @return
	 */
	public static Calendar getBeforeDay(Calendar cl){
		//使用roll方法进行向前回滚
		//cl.roll(Calendar.DATE, -1);
		//使用set方法直接进行设置
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day-1);
		return cl;
	}

	/**
	 * 获取当前时间的后一天时间
	 * @param cl
	 * @return
	 */
	public static Calendar getAfterDay(Calendar cl){
		//使用roll方法进行回滚到后一天的时间
		//cl.roll(Calendar.DATE, 1);
		//使用set方法直接设置时间值
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day+1);
		return cl;
	}

	/**
	 * 获取YYYY-mm-dd时间
	 * @param cl
	 */
	public static String formatCalendar(Calendar cl){
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH)+1;
		int day = cl.get(Calendar.DATE);
		return year+"-"+month+"-"+day;
	}

}
