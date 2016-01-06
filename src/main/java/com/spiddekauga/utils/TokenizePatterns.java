package com.spiddekauga.utils;

/**
 * Different tokenize patterns
 */
public enum TokenizePatterns {
	/** Create tokens only from start of the word. E.g. TOK -> [T, TO, TOK] */
	FROM_START,
	/** Create tokens for all parts of the word. E.g. TOK -> [T, O, K, TO, OK, TOK] */
	ALL,
	/** Create a single token for the whole. E.g. "TOK BAN" -> ["TOK BAN"] */
	SINGLE,
	/** Create whole word tokens for the string. E.g. "TOK BAN" -> ["TOK", "BAN"] */
	WORD
}