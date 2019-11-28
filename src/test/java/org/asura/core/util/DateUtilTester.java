package org.asura.core.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class DateUtilTester {

	@Test
	public void testGetTimeString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals("13:45:56", DateUtil.getTimeString(calendar.getTime()));
	}

	@Test
	public void testGetDateString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15);
		assertEquals("2017-03-15", DateUtil.getDateString(calendar.getTime()));
	}

	@Test
	public void testGetDateTimeStringDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals("2017-03-15 13:45:56", DateUtil.getDateTimeString(calendar.getTime()));
	}

	@Test
	public void testGetDateTimeStringDateString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals("2017 03 15:13:56:45", DateUtil.getDateTimeString(calendar.getTime(), "yyyy MM dd:HH:ss:mm"));
	}

	@Test
	public void testGetTodayDate() {
		assertEquals(DateUtil.getDateString(new Date()), DateUtil.getTodayDate());
	}

	@Test
	public void testGetTodayDateTime() {
		assertEquals(DateUtil.getDateTimeString(new Date()), DateUtil.getTodayDateTime());
	}

	@Test
	public void testGetDateFromStringString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals(DateUtil.getDateString(calendar.getTime()),
				DateUtil.getDateString(DateUtil.getDateFromString("2017-03-15 13:45:56")));
		
		assertNull(DateUtil.getDateFromString("2017-03-15"));
	}

	@Test
	public void testGetDateFromStringStringString() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals(DateUtil.getDateTimeString(calendar.getTime()),
				DateUtil.getDateTimeString(DateUtil.getDateFromString("2017 03 15:13:45:56", "yyyy MM dd:HH:mm:ss")));
		
		assertNull(DateUtil.getDateFromString("2017 03 15:13:45:56", "yyyy MM dd:HH:mm:ssdddd"));
	}

	@Test
	public void testGetDateFromStringStringStringLocale() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 13, 45, 56);
		assertEquals(DateUtil.getDateTimeString(calendar.getTime()), DateUtil.getDateTimeString(
				DateUtil.getDateFromString("2017 03 15:13:45:56", "yyyy MM dd:HH:mm:ss", Locale.ENGLISH)));
		
		assertNull(DateUtil.getDateFromString("2017 03 15:13:45:56", "yyyy MM dd:HH:mm:ssdddd", Locale.ENGLISH));
	}

	@Test
	public void testGetClosestDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 2, 15, 21, 45, 56);
		Date da = calendar.getTime();
		List<Date> dates = new ArrayList<>();
		calendar.set(2017, 2, 13, 21, 45, 56);
		dates.add(calendar.getTime());
		calendar.set(2017, 2, 16, 21, 45, 56);
		dates.add(calendar.getTime());
		calendar.set(2017, 2, 22, 21, 45, 56);
		dates.add(calendar.getTime());
		calendar.set(2017, 2, 21, 21, 45, 56);
		dates.add(calendar.getTime());
		calendar.set(2017, 2, 13, 21, 55, 56);
		dates.add(calendar.getTime());
		calendar.set(2017, 2, 16, 21, 33, 56);
		Date closestDate = calendar.getTime();
		dates.add(closestDate);

		assertEquals(closestDate, DateUtil.getClosestDate(da, dates));
	}

	@Test
	public void testGetDayByDay() {
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		assertEquals(calendar.getTime(), DateUtil.getDayByDay(currentDate, 5));
	}

	@Test
	public void testGetPeriodStringString() {
		List<String> dates = DateUtil.getPeriod("2017-06-20", "2017-06-25");
		List<String> expectedDates = new ArrayList<>();
		expectedDates.add("2017-06-20");
		expectedDates.add("2017-06-21");
		expectedDates.add("2017-06-22");
		expectedDates.add("2017-06-23");
		expectedDates.add("2017-06-24");
		expectedDates.add("2017-06-25");
		assertThat(dates, is(expectedDates));
	}

	@Test
	public void testGetPeriodString() {
		List<String> dates = DateUtil.getPeriod("2017-06-20|2017-06-25");
		List<String> expectedDates = new ArrayList<>();
		expectedDates.add("2017-06-20");
		expectedDates.add("2017-06-21");
		expectedDates.add("2017-06-22");
		expectedDates.add("2017-06-23");
		expectedDates.add("2017-06-24");
		expectedDates.add("2017-06-25");
		assertThat(dates, is(expectedDates));
		
		assertEquals(0, DateUtil.getPeriod("2017-06-20").size());
	}

	@Test
	public void testGetPeriodByRelativeDays() {
		List<String> dates = DateUtil.getPeriodByRelativeDays("2017-06-20", 5);
		List<String> expectedDates = new ArrayList<>();
		expectedDates.add("2017-06-20");
		expectedDates.add("2017-06-21");
		expectedDates.add("2017-06-22");
		expectedDates.add("2017-06-23");
		expectedDates.add("2017-06-24");
		expectedDates.add("2017-06-25");
		assertThat(dates, is(expectedDates));
		
		dates = DateUtil.getPeriodByRelativeDays("2017-06-20", -5);
		expectedDates.clear();
		expectedDates.add("2017-06-20");
		expectedDates.add("2017-06-19");
		expectedDates.add("2017-06-18");
		expectedDates.add("2017-06-17");
		expectedDates.add("2017-06-16");
		expectedDates.add("2017-06-15");
		assertThat(dates, is(expectedDates));
	}

}
