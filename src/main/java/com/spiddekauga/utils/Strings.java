package com.spiddekauga.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities
 * @author Matteus Magnusson <matteus.magnusson@spiddekauga.com>
 */
public class Strings {
/** Vowel pattern */
private static Pattern mVowelPattern = Pattern.compile("[aeiouAEIOU]");

	/**
	 * Pads the string with the specified character to the right
	 * @param <T> type of message to write
	 * @param message the string/value to pad
	 * @param n number of spaces total the string should contain (including padded)
	 * @param padChar the character to pad with
	 * @return padded string
	 */
	public static <T> String padRight(T message, int n, char padChar) {
		String spaceString = padRight(message, n);

		// Find first space
		int firstSpace = spaceString.length();
		for (int i = spaceString.length() - 1; i <= 0; --i) {
			if (spaceString.charAt(i) != ' ') {
				firstSpace = i + 1;
				break;
			}
		}

		// Convert spaces to padChar
		String padString = spaceString.substring(0, firstSpace);
		if (firstSpace < spaceString.length()) {
			padString += spaceString.substring(firstSpace).replace(' ', padChar);
		}
		return padString;
	}

	/**
	 * Pads the string with empty spaces to the right
	 * @param <T> type of the message to write
	 * @param message the string/value to pad
	 * @param n number of spaces total the string should contain (including padded)
	 * @return padded string
	 */
	public static <T> String padRight(T message, int n) {
		return String.format("%1$-" + n + "s", message);
	}

/**
	 * Pads the string with the specified character to the left
	 * @param <T> type of message to write
	 * @param message the string/value to pad
	 * @param n number of spaces total the string should contain (including padded)
	 * @param padChar the character to pad with
	 * @return padded string
	 */
	public static <T> String padLeft(T message, int n, char padChar) {
		String spaceString = padLeft(message, n);

		// Find last space
		int firstNonSpace = spaceString.length();
		for (int i = 0; i < spaceString.length(); ++i) {
			if (spaceString.charAt(i) != ' ') {
				firstNonSpace = i;
				break;
			}
		}

		// Convert spaces to padChar
		String padString = spaceString.substring(0, firstNonSpace).replace(' ', padChar);
		if (firstNonSpace < spaceString.length()) {
			padString += spaceString.substring(firstNonSpace);
		}
		return padString;
	}

	/**
	 * Pads the string with empty spaces to the left
	 * @param <T> type of the message to write
	 * @param message the string/value to pad
	 * @param n number of spaces total the string should contain (including padded)
	 * @return padded string
	 */
	public static <T> String padLeft(T message, int n) {
		return String.format("%1$" + n + "s", message);
	}

	/**
	 * @param word the word to check if it begins with a vowel or consonant.
	 * @return true if the word begins with a wovel, i.e. uses
	 */
	public static boolean beginsWithWovel(String word) {
		if (word != null && word.length() > 0) {
			Matcher matcher = mVowelPattern.matcher(word.substring(0, 1));
			return matcher.find();
		} else {
			return false;
		}
	}

	/**
	 * Convert an exception to string
	 * @param throwable the exception
	 * @return stack trace of throwable as a string
	 */
	public static String exceptionToString(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * text line breaks to HTML line breaks
	 * @param text regular text
	 * @return HTML formatted text
	 */
	public static String toHtmlString(String text) {
		return text.replace("\n", "<br />");
	}

	/**
	 * Get the array as a string with '; ' as the delimiter
	 * @param <T> array type
	 * @param array the array to create the list from
	 * @return string list separated by the delimiter
	 */
	public static <T> String toString(T[] array) {
		return toString(Arrays.asList(array));
	}

	/**
	 * Get the list as a string with '; ' as the delimiter.
	 * @param list the list to create a string list from
	 * @return string list separated by the delimiter
	 */
	public static String toString(Iterable<?> list) {
		return toString(list, "; ");
	}

/**
	 * Get the list as a string
	 * @param list the list to create a string list from
	 * @param delimiter how to delimit the elements
	 * @return string list separated by the delimiter
	 */
	public static String toString(Iterable<?> list, String delimiter) {
		StringBuilder stringBuilder = new StringBuilder();

		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().toString());

			if (iterator.hasNext()) {
				stringBuilder.append(delimiter);
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Get the array as a string
	 * @param <T> array type
	 * @param array the array to create the list from
	 * @param delimiter how to delimit the elements
	 * @return string list separated by the delimiter
	 */
	public static <T> String toString(T[] array, String delimiter) {
		return toString(Arrays.asList(array), delimiter);
	}

	/**
	 * Display number of seconds in minutes
	 * @param seconds number of seconds
	 * @return string time in minutes
	 */
	public static String secondsToTimeString(int seconds) {
		String minuteString = String.valueOf(seconds / 60);
		String secondString = String.valueOf(seconds % 60);

		// Append zeroes
		if (minuteString.length() == 1) {
			minuteString = "0" + minuteString;
		}
		if (secondString.length() == 1) {
			secondString = "0" + secondString;
		}

		return minuteString + ":" + secondString;
	}

	/**
	 * Count words number of words in a text
	 * @param text
	 * @return number of words in the text
	 */
	public static int wordCount(String text) {
		int cWords = 0;
		boolean prevCharWasWhitespace = true;
		for (int i = 0; i < text.length(); i++) {
			if (isWhitespace(text.charAt(i))) {
				prevCharWasWhitespace = true;
			} else {
				if (prevCharWasWhitespace) {
					cWords++;
				}
				prevCharWasWhitespace = false;

			}
		}
		return cWords;
	}

	/**
	 * Checks if a character is a whitespace or not
	 * @param character
	 * @return true if whitespace, false if not
	 */
	public static boolean isWhitespace(char character) {
		return character == ' ' || character == '\n' || character == '\t';
	}

	/**
	 * Tokenize the string according to the tokenize pattern. Splits into words by empty
	 * spaces
	 * @param pattern the tokenize pattern
	 * @param text the text to tokenize
	 * @return list of string tokens
	 */
	public static List<String> tokenize(TokenizePatterns pattern, String text) {
		return tokenize(pattern, text, " ");
	}

/**
 * Tokenize the string according to the tokenize pattern. Splits by places
 * @param pattern the tokenize pattern
 * @param text the text to tokenize
 * @param splitBy how to split into words that should be tokenized
 * @return list of string tokens
 */
public static List<String> tokenize(TokenizePatterns pattern, String text, String splitBy) {
	String[] words = text.split(" ");
	Set<String> tokens = new HashSet<>();

	switch (pattern) {
	case FROM_START:
		for (String word : words) {
			for (int i = 1; i <= word.length(); ++i) {
				tokens.add(word.substring(0, i));
			}
		}
		break;

	case ALL:
		for (String word : words) {
			for (int i = 0; i < word.length(); ++i) {
				for (int currentLength = 1; currentLength <= word.length() - i; ++currentLength) {
					tokens.add(word.substring(i, i + currentLength));
				}
			}
		}
		break;
	case SINGLE:
		tokens.add(text);
		break;

	case WORD:
		for (String word : words) {
			tokens.add(word);
		}
	}


	return new ArrayList<>(tokens);
}

/**
 * Return true if the string contains HTML markup tags or entities.
 * @param string String to test
 * @return true if string contains HTML markup tags or entities.
 */
public static boolean isHtml(String string) {
	return DetectHtml.isHtml(string);
}

/**
 * Detect HTML markup in a string This will detect tags or entities
 * @author dbennett455@gmail.com - David H. Bennett
 */
private static class DetectHtml {
	// adapted from post by Phil Haack and modified to match better
	final static String tagStart =
			"\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
	final static String tagEnd =
			"\\</\\w+\\>";
	final static String tagSelfClosing =
			"\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
	final static String htmlEntity =
			"&[a-zA-Z][a-zA-Z0-9]+;";
	final static Pattern htmlPattern = Pattern.compile(
			"(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")",
			Pattern.DOTALL
	);

	/**
	 * Will return true if s contains HTML markup tags or entities.
	 * @param string String to test
	 * @return true if string contains HTML
	 */
	static boolean isHtml(String string) {
		boolean ret = false;
		if (string != null) {
			ret = htmlPattern.matcher(string).find();
		}
		return ret;
	}

}
}
