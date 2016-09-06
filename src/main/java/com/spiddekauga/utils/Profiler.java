package com.spiddekauga.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Measures how long time things take
 */
public class Profiler {

private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#,###.000");
private int mPadding = 14;
private long mStartTime;
private long mEndTime;
private List<TimeWrapper> mIntermediateTimes = new ArrayList<>();

/**
 * Start measuring the time. This automatically resets the profiler
 */
public void start() {
	reset();
	mStartTime = System.nanoTime();
}

/**
 * Reset the profiler
 */
public void reset() {
	mStartTime = 0;
	mEndTime = 0;
	mIntermediateTimes.clear();
}

/**
 * Stop measuring the time
 */
public void stop() {
	mEndTime = System.nanoTime();
}

/**
 * Add an intermediate stop
 * @param name the name of the point
 */
public void addIntermediateTime(String name) {
	mIntermediateTimes.add(new TimeWrapper(name));
}

/**
 * Same as {@link #toString(String)} with null as the header argument
 */
@Override
public String toString() {
	return toString(null);
}

/**
 * Get string with all information (used for printing)
 * @param header optional header for the information (can be null)
 * @return string with all information
 */
public String toString(String header) {
	StringBuilder stringBuilder = new StringBuilder();

	// Header
	if (header != null) {
		stringBuilder.append(header).append('\n');
	}


	// Total time
	long totalTime = mEndTime - mStartTime;
	stringBuilder.append("Total Time: ").append(timeInHumanReadableFormat(totalTime)).append("\n");


	// Intermediate times
	long previousTime = mStartTime;
	for (TimeWrapper intermediateTime : mIntermediateTimes) {
		// Time
		long diffTime = intermediateTime.time - previousTime;
		previousTime = intermediateTime.time;

		String diffTimeString = Strings.padRight(timeInHumanReadableFormat(diffTime), mPadding);
		stringBuilder.append(diffTimeString);

		// Percentage
		double percentageTime = ((double) diffTime) / totalTime * 100;
		String percentageTimeString = String.format(Locale.ENGLISH, "%.2f", percentageTime);
		stringBuilder.append(percentageTimeString).append("%    ");

		// Name
		stringBuilder.append(intermediateTime.name).append("\n");
	}


	return stringBuilder.toString();
}

/**
 * Get the time in human-readable format. Only microseconds will be shown, never the granularity of
 * nano-seconds
 * @param nanoseconds time in nanoseconds to be shown in human readable format
 */
private static String timeInHumanReadableFormat(long nanoseconds) {
	long microseconds = nanoseconds / 1000;
	double milliseconds = microseconds / 1000.0;
	String time = TIME_FORMAT.format(milliseconds);
	time += "ms";
	return time;
}

/**
 * Change the padding (total number of characters) in the intermediate times
 * @param padding total number of characters in intermediate times before the name
 */
public void setPadding(int padding) {
	mPadding = padding;
}

/**
 * Wrapper for time and name
 */
private static class TimeWrapper {
	private String name;
	private long time = System.nanoTime();

	private TimeWrapper(String name) {
		this.name = name;
	}
}
}
