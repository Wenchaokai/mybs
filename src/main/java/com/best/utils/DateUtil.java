package com.best.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ClassName:DateUtil Description:
 * 
 * @author ChaoKai Wen email:wenchaokai@gmail.com
 * @version
 * @Date 2013-8-26
 */
public class DateUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat year = new SimpleDateFormat("yyyy");

	public static String getCurrentDateString() {
		Date date = new Date(System.currentTimeMillis());
		String sDateTime = sdf.format(date);
		return sDateTime;
	}

	public static String getNextSevenDate(String dateString) throws ParseException {
		Date date = sdf.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 7);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getNextDate(String dateString) throws ParseException {
		Date date = sdf.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getPreDate(String dateString) throws ParseException {
		Date date = sdf.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getPreSevenDate() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -6);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getPreMonthDate() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -29);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getPreDate(Integer pre) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, pre);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static String getPreDate() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static List<String> getDate() {
		List<String> res = new ArrayList<String>();
		for (int index = -28; index < 0; index++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, index);
			String sDateTime = sdf.format(cal.getTime());
			res.add(sDateTime);
		}
		return res;
	}

	public static List<String> getDateTimes(String startTime, String endTime) throws ParseException {
		List<String> res = new ArrayList<String>();
		String currentTime = startTime;
		while (!currentTime.equals(endTime)) {
			res.add(currentTime);
			currentTime = getNextDate(currentTime);
		}
		res.add(endTime);
		return res;
	}

	public static List<String> getYears() throws ParseException {

		List<String> res = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, i * (-1));
			res.add(year.format(cal.getTime()));
		}
		return res;
	}

	public static List<String> getMonth() throws ParseException {
		List<String> res = new ArrayList<String>();
		res.add("01");
		res.add("02");
		res.add("03");
		res.add("04");
		res.add("05");
		res.add("06");
		res.add("07");
		res.add("08");
		res.add("09");
		res.add("10");
		res.add("11");
		res.add("12");
		return res;
	}

	public static String getPreMonth(String dateString) throws ParseException {
		Date date = sdf.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static List<String> getListDate() throws ParseException {
		List<String> dates = new ArrayList<String>();
		String date = getFirstDate();
		dates.add(date);
		for (int index = -1; index > -8; index--) {
			date = getPreMonth(date);
			dates.add(date);
		}
		return dates;
	}

	public static String getFirstDate() throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return sdf.format(cal.getTime());
	}

	public static String getPreSevenDate(String dateString) throws ParseException {
		Date date = sdf.parse(dateString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -8);
		String sDateTime = sdf.format(cal.getTime());
		return sDateTime;
	}

	public static long betweenDayTime(String startTime, String endTime) throws ParseException {
		if (startTime == null) {
			if (null == endTime) {
				startTime = getPreSevenDate();
				endTime = getCurrentDateString();
			} else {
				startTime = getPreSevenDate(endTime);
			}
		} else if (null == endTime) {
			endTime = getNextSevenDate(startTime);
		}
		Date d1 = sdf.parse(startTime);
		Date d2 = sdf.parse(endTime);
		long daysBetween = (d2.getTime() - d1.getTime() + 1000000) / (3600 * 24 * 1000);
		return daysBetween;
	}

	public static void main(String[] args) throws ParseException {
		List<String> dates = getListDate();
		System.out.println(getCurrentDateString());
		System.out.println(getNextDate(getCurrentDateString()));
		System.out.println(getFirstDate());
		System.out.println(getPreMonth(getFirstDate()));
	}
}
