package com.spiddekauga.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Helper class for exceptions
 */
public class Exceptions {
/**
 * Convert an exception to string
 * @param throwable the exception
 * @return stack trace of throwable as a string
 */
public static String toString(Throwable throwable) {
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	throwable.printStackTrace(pw);
	return sw.toString();
}

/**
 * Find a specific exception cause class recursively.
 * @param throwable the exception
 * @param searchForClass the class to search for (enough if the cause is assignable to this class)
 * @return first instance of searchForClass that was found to be the original cause, null if not
 * found
 */
@SuppressWarnings("unchecked")
public static <SearchForClass extends Throwable> SearchForClass findFirstCauseOf(Throwable throwable, Class<SearchForClass> searchForClass) {
	ArrayList<Throwable> causes = new ArrayList<>();
	causes.add(throwable);

	Throwable currentThrowable = throwable;
	while (currentThrowable.getCause() != null) {
		causes.add(currentThrowable.getCause());
		currentThrowable = currentThrowable.getCause();
	}

	// Iterate through all of them (backwards)
	ListIterator<Throwable> causeIt = causes.listIterator(causes.size());
	while (causeIt.hasPrevious()) {
		Throwable cause = causeIt.previous();
		if (searchForClass.isAssignableFrom(cause.getClass())) {
			return (SearchForClass) cause;
		}
	}

	return null;
}
}
