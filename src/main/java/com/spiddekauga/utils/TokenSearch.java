package com.spiddekauga.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class that helps one search for objects with auto-complete functionality. I.e. it creates
 * tokens that can be searched. Not the most efficient way, but OK at the moment.
 * @param <Searchable> The object type that is stored and searchable
 */
public class TokenSearch<Searchable> {
private Set<Searchable> mObjects = new HashSet<>();
private Multimap<String, Searchable> mTokenObjects = ArrayListMultimap.create();
private Multimap<Searchable, String> mObjectTokens = ArrayListMultimap.create();
private Multimap<Searchable, String> mFullWords = ArrayListMultimap.create();

/**
 * Update an object's search tokens. This will remove all previous search tokens from this object.
 * I.e. this is a shortcut method for first calling {@link #remove(Object)} and then {@link
 * #add(Object, TokenizePatterns, String...)}
 * @param object the object that should be found if these are auto-completed.
 * @param tokenizePattern how the words should be tokenized
 * @param texts the words that should be auto-completed. The words will be tokenized according to
 * the tokenize pattern specified
 */
public void update(Searchable object, TokenizePatterns tokenizePattern, String... texts) {
	remove(object);
	add(object, tokenizePattern, texts);
}

/**
 * Remove an object and its tokens from the search.
 * @param object the object that should be removed
 */
public void remove(Searchable object) {
	mObjects.remove(object);
	Collection<String> tokensToRemove = mObjectTokens.removeAll(object);
	if (tokensToRemove != null) {
		for (String token : tokensToRemove) {
			mTokenObjects.remove(token, object);
		}
	}
}

/**
 * Add a search token to the object that should be searchable
 * @param object the object that should be found if these are auto-completed.
 * @param tokenizePattern how the words should be tokenized
 * @param texts the words that should be auto-completed. The words will be tokenized according to
 * the tokenize pattern specified
 */
public void add(Searchable object, TokenizePatterns tokenizePattern, String... texts) {
	if (!mObjects.contains(object)) {
		mObjects.add(object);
	}

	for (String text : texts) {
		List<String> tokens = Strings.tokenize(tokenizePattern, text.toLowerCase());

		for (String token : tokens) {
			mTokenObjects.put(token, object);
			mObjectTokens.put(object, token);

		}

		// When we tokenize every part of the word we want to give search strings that
		// are in the beginning of a word higher relevance
		if (tokenizePattern == TokenizePatterns.ALL) {
			List<String> words = Strings.tokenize(TokenizePatterns.WORD, text.toLowerCase());
			mFullWords.putAll(object, words);
		}
	}
}

/**
 * Search for objects. Case insensitive
 * @param searchString when searching for more than two words AND both words has to be found for the
 * object. If this is empty every object is returned
 * @return found objects sorted by relevance. This list is just a copy and is always OK to change
 */
public List<Searchable> search(CharSequence searchString) {
	return search((String) searchString);
}

/**
 * Search for objects. Case insensitive
 * @param searchString when searching for more than two words AND both words has to be found for the
 * object. If this is empty every object is returned
 * @return found objects sorted by relevance. This list is just a copy and is always OK to change
 */
public List<Searchable> search(String searchString) {
	searchString = searchString.trim().toLowerCase();
	if (searchString.isEmpty()) {
		return new ArrayList<>(mObjects);
	}

	final Map<Searchable, AtomicInteger> relevanceCounts = new HashMap<>();
	Map<Searchable, Set<String>> usesTokens = new HashMap<>();
	int tokenCount = 0;

	// Find
	String[] searchWords = searchString.split(" ");
	for (String token : searchWords) {
		if (!token.isEmpty()) {
			tokenCount++;

			Collection<Searchable> foundByToken = mTokenObjects.get(token);

			for (Searchable foundObject : foundByToken) {
				AtomicInteger relevanceCount = relevanceCounts.get(foundObject);

				int relevance = getRelevance(foundObject, searchString);
				if (relevanceCount == null) {
					relevanceCount = new AtomicInteger(relevance);
					relevanceCounts.put(foundObject, relevanceCount);
				} else {
					relevanceCount.addAndGet(relevance);
				}

				Set<String> usesToken = usesTokens.get(foundObject);
				if (usesToken == null) {
					usesToken = new HashSet<>();
					usesTokens.put(foundObject, usesToken);
				}
				usesToken.add(token);
			}
		}
	}

	// Sort
	List<Searchable> foundAndSorted = new ArrayList<>();
	for (Entry<Searchable, Set<String>> entry : usesTokens.entrySet()) {
		if (entry.getValue().size() == tokenCount) {
			foundAndSorted.add(entry.getKey());
		}
	}

	java.util.Collections.sort(foundAndSorted, new Comparator<Searchable>() {
		@Override
		public int compare(Searchable o1, Searchable o2) {
			AtomicInteger count1 = relevanceCounts.get(o1);
			AtomicInteger count2 = relevanceCounts.get(o2);
			return count2.get() - count1.get();
		}
	});

	return foundAndSorted;
}

/**
 * Get the relevance for the object. Checks for all whole words of the object and adds 1 for each
 * word the searchString matches (at the start), and 2 if the searchString matches the whole word.
 * @param object object we want to get the relevance for.
 * @param searchString what the user search for
 * @return 1 + 1 for matching start of a word for the object
 */
private int getRelevance(Searchable object, String searchString) {
	int relevance = 1;

	Collection<String> words = mFullWords.get(object);
	if (words != null) {
		for (String word : words) {
			if (word.startsWith(searchString)) {
				relevance += 1;

				if (word.equals(searchString)) {
					relevance += 1;
				}
			}
		}
	}

	return relevance;
}

}
