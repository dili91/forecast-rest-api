package com.myself.weather;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Date utility class
 * @author andreadilisio
 *
 */
public class Utils {

	/**
	 * Get current date in Unix epoch time
	 * @return the current date in Unix epoch time
	 */
	public static long nowToEpochMillis() {
		return OffsetDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli();
	}

	/**
	 * Return the corresponding hour to the given epoch time in milliseconds (0h-23h)
	 * @param ts unix epoch time in millisecond
	 * @return the corresponding hour of the day
	 */
	public static int getHour(long ts) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(ts);
		return c.get(Calendar.HOUR_OF_DAY);
	}
}
