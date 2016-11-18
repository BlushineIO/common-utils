package com.spiddekauga.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Default Otto event bus for an application.
 * @see Bus
 */
public class EventBus extends Bus {
private static EventBus mInstance = null;
private static long mMainThreadId = 1;

/**
 * Enforces singleton pattern
 */
protected EventBus(ThreadEnforcer threadEnforcer) {
	super(threadEnforcer);
}

/**
 * Set main thread id
 * @param id id of the main thread
 */
public static void setMainThreadId(long id) {
	mMainThreadId = id;
}

/**
 * Get singleton instance
 * @return get instance
 */
public static EventBus getInstance() {
	if (mInstance == null) {
		ThreadEnforcer threadEnforcer = ThreadEnforcer.MAIN;

		// Check if this is an Android application
		try {
			threadEnforcer.enforce(null);
		}
		// Regular Java application, use custom enforcer
		catch (NoClassDefFoundError e) {
			threadEnforcer = new JavaMainThreadEnforcer();
		}
		mInstance = new EventBus(threadEnforcer);
	}
	return mInstance;
}

protected static class JavaMainThreadEnforcer implements ThreadEnforcer {
	@Override
	public void enforce(Bus bus) {
		if (Thread.currentThread().getId() != mMainThreadId) {
			throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Thread.currentThread().getId());
		}
	}
}
}
