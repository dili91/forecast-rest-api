package com.myself.weather.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date utility class
 * @author andreadilisio
 *
 */
public class DateUtils {

	/**
	 * Get current date in Unix epoch time
	 * @return the current date in Unix epoch time
	 */
	public static long nowToEpochMillis() {
		return OffsetDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli();
	}

	/**
	 * Return the given Unix epoch timestamp in a Date object with UTC timezone
	 * @param ts unix epoch time in seconds
	 * @return the corresponding hour of the day
	 */
	public static int getHour(long ts) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.setTime(new Date((long) ts));
		return c.get(Calendar.HOUR_OF_DAY);
	}
}
