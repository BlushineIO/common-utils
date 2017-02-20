package com.spiddekauga.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Various time methods
 */
public class Time {
/** ISO Date */
public static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

/**
 * Create a simple date format from the ISO date
 * @return simple date format with ISO date
 */
public static SimpleDateFormat createIsoDateFormat() {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE, Locale.ENGLISH);
	simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	return simpleDateFormat;
}

/**
 * Create a simple date format from the ISO date in local format
 * @return simple date format with ISO date
 */
public static SimpleDateFormat createIsoDateFormatLocal() {
	return new SimpleDateFormat(ISO_DATE, Locale.ENGLISH);
}

/**
 * Causes the currently executing thread to sleep (temporarily cease execution) for the specified
 * number of milliseconds, subject to the precision and accuracy of system timers and schedulers.
 * The thread does not lose ownership of any monitors.
 * @param millis the length of time to sleep in milliseconds
 * @throws IllegalArgumentException if the value of {@code millis} is negative
 */
public static void sleep(long millis) {
	try {
		Thread.sleep(millis);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}
}
