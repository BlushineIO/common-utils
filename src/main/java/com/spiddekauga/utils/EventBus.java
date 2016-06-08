package com.spiddekauga.utils;

import com.squareup.otto.Bus;

/**
 * Default Otto event bus for an application.
 * @see Bus
 */
public class EventBus extends Bus {
private static EventBus mInstance = null;

/**
 * Enforces singleton pattern
 */
protected EventBus() {
}

/**
 * Get singleton instance
 * @return get instance
 */
public static EventBus getInstance() {
	if (mInstance == null) {
		mInstance = new EventBus();
	}
	return mInstance;
}
}
