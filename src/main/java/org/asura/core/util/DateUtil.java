package org.asura.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 日期操作的工具类
 * 
 * @author shendiao
 *
 */
public class DateUtil {

	/**
	 * 用来获取<code>Date</code>的字符串展现形式，以HH:mm:ss的方式返回
	 * 
	 * @param date
	 *            日期
	 * @return 以HH:mm:ss形式的字符串
	 */
	public static String getTimeString(Date date) {
		return getDateTimeString(date, "HH:mm:ss");
	}

	/**
	 * 用来获取<code>Date</code>的字符串展现形式，以yyyy-MM-dd的方式返回
	 * 
	 * @param date
	 *            日期
	 * @return 以yyyy-MM-dd形式的字符串
	 */
	public static String getDateString(Date date) {
		return getDateTimeString(date, "yyyy-MM-dd");
	}

	/**
	 * 用来获取<code>Date</code>的字符串展现形式，以yyyy-MM-dd HH:mm:ss的方式返回
	 * 
	 * @param date
	 *            日期
	 * @return 以yyyy-MM-dd HH:mm:ss形式的字符串
	 */
	public static String getDateTimeString(Date date) {
		return getDateTimeString(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 用来获取<code>Date</code>的字符串展现形式，以指定的<tt>format</tt>形式返回，<tt>format</tt>应符合yyyyMMddHHmmss
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            返回的格式，应符合yyyyMMddHHmmss
	 * @return 以<tt>format</tt>形式的字符串
	 */
	public static String getDateTimeString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 获取当天的日期字符串，以yyyy-MM-dd的格式返回
	 * 
	 * @return 字符串，以yyyy-MM-dd的格式
	 */
	public static String getTodayDate() {
		return getDateTimeString(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取当天的日期字符串，以yyyy-MM-dd HH:mm:ss的格式返回
	 * 
	 * @return 字符串，以yyyy-MM-dd HH:mm:ss的格式
	 */
	public static String getTodayDateTime() {
		return getDateTimeString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据<tt>date</tt>字符串返回对应的<code>Date</code>对象，<tt>date</tt>字符串应符合yyyy-MM-dd
	 * HH:mm:ss的格式
	 * 
	 * @param date
	 *            <tt>date</tt>字符串，符合yyyy-MM-dd HH:mm:ss的格式
	 * @return <code>Date</code>对象，<code>null</code>如果<tt>date</tt>格式不正确
	 */
	public static Date getDateFromString(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return new Date(formatter.parse(date).getTime());
		} catch (ParseException localParseException) {
		}
		return null;
	}

	/**
	 * 根据<tt>date</tt>字符串返回对应的<code>Date</code>对象，<tt>date</tt>字符串应符合<tt>format</tt>指定的格式，<tt>format</tt>应符合yyyyMMddHHmmss
	 * 
	 * @param date
	 *            <tt>date</tt>字符串
	 * @param format
	 *            <tt>format</tt>为日期字符串的格式，应符合yyyyMMddHHmmss
	 * @return <code>Date</code>对象，<code>null</code>如果日期解析失败
	 */
	public static Date getDateFromString(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return new Date(formatter.parse(date).getTime());
		} catch (Exception localException) {
		}
		return null;
	}

	/**
	 * 指定local后，根据<tt>date</tt>字符串返回对应的<code>Date</code>对象，<tt>date</tt>字符串应符合<tt>format</tt>指定的格式，<tt>format</tt>应符合yyyyMMddHHmmss
	 * 
	 * @param date
	 *            <tt>date</tt>字符串
	 * @param format
	 *            <tt>format</tt>为日期字符串的格式，应符合yyyyMMddHHmmss
	 * @param locale
	 *            <code>Locale</code>指定时区
	 * @return <code>Date</code>对象，<code>null</code>如果日期解析失败
	 */
	public static Date getDateFromString(String date, String format, Locale locale) {
		SimpleDateFormat sf = new SimpleDateFormat(format, locale);
		try {
			return sf.parse(date);
		} catch (ParseException e) {
			
		}

		return null;
	}

	/**
	 * 从<code>dates</code>列表中找到距离指定<code>date</code>最近的<code>Date</code>对象并返回
	 * 
	 * @param date
	 *            指定<code>date</code>
	 * @param dates
	 *            Dates列表
	 * @return <code>Date</code>对象，<code>null</code>如果没有找到最近的Date对象
	 */
	public static Date getClosestDate(Date date, List<Date> dates) {
		long min = Long.MAX_VALUE;
		Date result = null;
		for (Date d : dates) {
			long value = Math.abs(date.getTime() - d.getTime());
			if (min > value) {
				min = value;
				result = d;
			}
		}

		return result;
	}

	/**
	 * 针对指定date，获取相隔count天的Date
	 * 
	 * @param date
	 *            指定<code>date</code>
	 * @param count
	 *            相隔天数
	 * @return <code>Date</code>对象
	 */
	public static Date getDayByDay(Date date, int count) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, count);
		date = calendar.getTime();

		return date;
	}

	/**
	 * 指定from日期字符串，格式为yyyy-MM-dd，指定to日期字符串，格式为yyyy-MM-dd，获取两个日期之间的日期列表，返回日期字符串格式为yyy-MM-dd
	 * 
	 * @param from
	 *            起始日期字符串，格式为yyyy-MM-dd
	 * @param to
	 *            截至日期字符串，格式为yyyy-MM-dd
	 * @return List日期列表，格式为yyyy-MM-dd
	 */
	public static List<String> getPeriod(String from, String to) {
		List<String> result = new ArrayList<String>();
		Date dstart = DateUtil.getDateFromString(from + " 00:00:00");
		Date dend = DateUtil.getDateFromString(to + " 00:00:00");

		if (dstart.before(dend)) {
			Calendar start = Calendar.getInstance();
			start.setTime(dstart);
			Calendar end = Calendar.getInstance();
			end.setTime(dend);

			for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				result.add(DateUtil.getDateString(date));
			}
		}

		return result;
	}

	/**
	 * 获取指定时间间隔的日期列表，period为包含起始date和截至date的字符串，中间以|分隔，date格式为yyyy-MM-dd
	 * 
	 * @param period
	 *            包含起始date和截至date的字符串，中间以|分隔，date格式为yyyy-MM-dd
	 * @return List日期列表，格式为yyyy-MM-dd
	 */
	public static List<String> getPeriod(String period) {
		List<String> result = new ArrayList<String>();
		if (period.contains("|")) {
			String from = period.split("\\|")[0];
			String to = period.split("\\|")[1];
			Date dstart = DateUtil.getDateFromString(from + " 00:00:00");
			Date dend = DateUtil.getDateFromString(to + " 00:00:00");

			Calendar start = Calendar.getInstance();
			start.setTime(dstart);
			Calendar end = Calendar.getInstance();
			end.setTime(dend);

			for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				result.add(DateUtil.getDateString(date));
			}
		} 

		return result;
	}

	/**
	 * 获取指定日期date及相对天数relativeDays之间的日期字符串列表，date的格式为yyyy-MM-dd
	 * 
	 * @param date
	 *            指定日期，格式为yyyy-MM-dd
	 * @param relativeDays
	 *            相对天数
	 * @return List日期列表，格式为yyyy-MM-dd
	 */
	public static List<String> getPeriodByRelativeDays(String date, int relativeDays) {
		List<String> periods = new ArrayList<String>();
		Date periodDate = DateUtil.getDateFromString(date + " 00:00:00");
		Calendar periodCalendar = Calendar.getInstance();
		periodCalendar.setTime(periodDate);
		periods.add(DateUtil.getDateString(periodDate));
		int count = 0;
		while (count != relativeDays) {
			if (relativeDays > 0) {
				periodCalendar.add(Calendar.DATE, 1);
				periods.add(DateUtil.getDateString(periodCalendar.getTime()));
				count++;
			} else if (relativeDays < 0) {
				periodCalendar.add(Calendar.DATE, -1);
				periods.add(DateUtil.getDateString(periodCalendar.getTime()));
				count--;
			}
		}
		return periods;
	}
}
