package com.spiddekauga.utils;

/**
 * Fired when a progress bar (or something else) should be updated.
 */
public class ProgressEvent {
private long mComplete;
private long mTotal;
private String mMessage;

/**
 * Create a new progress event
 * @param complete how many parts have been completed
 * @param total total number of parts to process
 */
public ProgressEvent(long complete, long total) {
	this(complete, total, null);
}

/**
 * Create a new progress event
 * @param complete how many parts have been completed
 * @param total total number of parts to process
 * @param message an optional message for the progress
 */
public ProgressEvent(long complete, long total, String message) {
	mComplete = complete;
	mTotal = total;
	mMessage = message;
}

/**
 * @return how many parts have been completed (out of {@link #getTotal()}).
 */
public long getComplete() {
	return mComplete;
}

/**
 * @return total number of parts that should be processed
 */
public long getTotal() {
	return mTotal;
}

/**
 * @return message for the progress. Can be null.
 */
public String getMessage() {
	return mMessage;
}

/**
 * @return true if the progress is complete
 */
public boolean isComplete() {
	return mComplete == mTotal;
}

/**
 * @return get percentage. 0 if {@link #getTotal()} equals to 0.
 */
public float getPercentage() {
	if (mTotal != 0) {
		return (float) (((double) mComplete) / mTotal) * 100;
	} else {
		return 0;
	}
}
}
