/**
 * @Package com.manyouren.android.util    
 * @Title: TimeUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-10 下午12:01:28 
 * @version V1.0   
 */
package com.manyou.wei.util;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-10 下午12:01:28 
 *  
 */
import static android.text.format.DateUtils.FORMAT_NUMERIC_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

import com.manyou.wei.entity.PlanController;

/**
 * Utilities for dealing with dates and times
 */
public class DateUtils {

	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss"); // "yyyy-MM-dd HH:mm:ss"

	public static final SimpleDateFormat DEFAULT_SHORT_DATE = new SimpleDateFormat(
			"MM-dd HH:mm");

	public static String getTime(Date date) {

		return DATE_FORMAT_DATE.format(date);
	}

	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * long time to string
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);

		// 北京时区GMT+8
		Calendar beijingcal = Calendar.getInstance();
		beijingcal.clear();
		beijingcal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		beijingcal.setTimeInMillis(cal.getTimeInMillis());

		return dateFormat.format(beijingcal.getTime());
	}

	/**
	 * long time to string
	 */
	public static String convertServerTime(long timeInMillis) {
		// 服务器的createTime是秒数
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis * 1000);

		// 北京时区GMT+8
		Calendar beijingcal = Calendar.getInstance();
		beijingcal.clear();
		beijingcal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		beijingcal.setTimeInMillis(cal.getTimeInMillis());

		return DEFAULT_SHORT_DATE.format(beijingcal.getTime());
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * Get relative time for date
	 * 
	 * @param date
	 * @return relative time
	 */
	public static CharSequence getRelativeTime(final Date date) {
		long now = System.currentTimeMillis();
		if (Math.abs(now - date.getTime()) > 60000)
			return android.text.format.DateUtils.getRelativeTimeSpanString(
					date.getTime(), now, MINUTE_IN_MILLIS, FORMAT_SHOW_DATE
							| FORMAT_SHOW_YEAR | FORMAT_NUMERIC_DATE);
		else
			return "just now";
	}

	/**
	 * this method is useful
	 * 
	 * @param first
	 * @param last
	 * @return
	 * @return String
	 */
	public static String getPlanDate(Date first, Date last) {
		if (first == null)
			return null;
		String time = getPlanDigitalDate(first, last);

		time += "  " + PlanController.DayOfWeek[first.getDay()] + "出发";

		return time;
	}
	
	public static String getPlanDate(String first, String last) {

		try {
			return getPlanDigitalDate(new SimpleDateFormat(
					"yyyy-MM-dd").parse(first), new SimpleDateFormat(
					"yyyy-MM-dd").parse(last));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getPlanDigitalDate(String first, String last) {
		try {
			return getPlanDigitalDate(new SimpleDateFormat(
					"yyyy-MM-dd").parse(first), new SimpleDateFormat(
					"yyyy-MM-dd").parse(last));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 时间处理方法 根据startDate 和 endDate 得到 xx/xx-xx/xx
	 * 
	 */
	public static String getPlanDigitalDate(Date first, Date last) {
		if (first == null)
			return null;
		String time = "";
		String format = "yyyy.MM.dd";

		Date now = new Date();
		int year = now.getYear();
		if (year == first.getYear()) {

			if (first.getMonth() > 10) {
				if (first.getDate() > 10) {
					format = "MM.dd";
				} else {
					format = "MM.d";
				}
			} else {
				if (first.getDate() > 10) {
					format = "M.dd";
				} else {
					format = "M.d";
				}
			}
		} else {
			format = "yyyy." + format;
		}
		time = new SimpleDateFormat(format).format(first);

		if (first.compareTo(last) == 0) {
			return time;
		}

		time += "~";

		if (year == last.getYear()) {

			if (last.getMonth() > 10) {
				if (last.getDate() > 10) {
					format = "MM.dd";
				} else {
					format = "MM.d";
				}
			} else {
				if (last.getDate() > 10) {
					format = "M.dd";
				} else {
					format = "M.d";
				}
			}
		} else {
			format = "yyyy." + format;
		}
		time += new SimpleDateFormat(format).format(last);

		return time;
	}

	/**
	 * this method is useful
	 * 
	 * @param first
	 * @param last
	 * @return
	 * @return String
	 */
	public static String getSimplePlanDate(Date first, Date last) {
		if (first == null)
			return null;
		String time = "";
		if (first.getMonth() > 10) {
			if (first.getDate() > 10) {
				time = new SimpleDateFormat("MM月dd日").format(first);
			} else {
				time = new SimpleDateFormat("MM月d日").format(first);
			}
		} else {
			if (first.getDate() > 10) {
				time = new SimpleDateFormat("M月dd日").format(first);
			} else {
				time = new SimpleDateFormat("M月d日").format(first);
			}
		}
		time += " 出发";
		if (last.after(first)) {
			int rangeDay = 1 + (int) ((last.getTime() - first.getTime()) / 86400 / 1000);
			time += " " + rangeDay + "天";
		} else {
			time += " 当日";
		}
		return time;
	}

	public static int getAgeFromDateString(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date old = (Date) sdf.parse(str);
			Date now = new Date();
			long diff = now.getTime() - old.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			int year = (int) days / 365;
			return year + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}