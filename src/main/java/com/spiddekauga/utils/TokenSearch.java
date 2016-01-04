package com.spiddekauga.utils;

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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.spiddekauga.utils.Strings.TokenizePatterns;

/**
 * A class that helps one search for objects with auto-complete functionality. I.e. it
 * creates tokens that can be searched
 * @param <Searchable> The object type that is stored and searchable
 */
public class TokenSearch<Searchable> {
	private List<Searchable> mObjects = new ArrayList<>();
	private Multimap<String, Searchable> mTokenObjects = ArrayListMultimap.create();

	/**
	 * Add a search token to the object that should be searchable
	 * @param object the object that should be found if these are auto-completed.
	 * @param tokenizePattern how the words should be tokenized
	 * @param texts the words that should be auto-completed. The words will be tokenized
	 *        according to the tokenize pattern specified
	 */
	public void add(Searchable object, TokenizePatterns tokenizePattern, String... texts) {
		mObjects.add(object);

		for (String text : texts) {
			String[] tokens = Strings.tokenize(tokenizePattern, text).split(" ");

			for (String token : tokens) {
				mTokenObjects.put(token, object);
			}
		}
	}

	/**
	 * Search for objects
	 * @param searchString when searching for more than two words AND both words has to be
	 *        found for the object. If this is empty every object is returned
	 * @return found objects sorted by relevance
	 */
	public List<Searchable> search(String searchString) {
		if (searchString.trim().isEmpty()) {
			return mObjects;
		}

		final Map<Searchable, AtomicInteger> relevanceCounts = new HashMap<>();
		Map<Searchable, Set<String>> usesTokens = new HashMap<>();
		int tokenCount = 0;

		// Find
		String[] searchWords = searchString.trim().split(" ");
		for (String token : searchWords) {
			if (!token.isEmpty()) {
				tokenCount++;

				Collection<Searchable> foundByToken = mTokenObjects.get(token);

				for (Searchable foundObject : foundByToken) {
					AtomicInteger relevanceCount = relevanceCounts.get(foundObject);

					if (relevanceCount == null) {
						relevanceCount = new AtomicInteger(1);
						relevanceCounts.put(foundObject, relevanceCount);
					} else {
						relevanceCount.incrementAndGet();
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

		foundAndSorted.sort(new Comparator<Searchable>() {
			@Override
			public int compare(Searchable o1, Searchable o2) {
				AtomicInteger count1 = relevanceCounts.get(o1);
				AtomicInteger count2 = relevanceCounts.get(o2);
				return count2.get() - count1.get();
			}
		});

		return foundAndSorted;
	}

}
