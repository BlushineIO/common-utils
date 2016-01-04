package com.spiddekauga.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private Multimap<String, Searchable> mObjects = ArrayListMultimap.create();

	/**
	 * Add a search token to the object that should be searchable
	 * @param object the object that should be found if these are auto-completed.
	 * @param tokenizePattern how the words should be tokenized
	 * @param texts the words that should be auto-completed. The words will be tokenized
	 *        according to the tokenize pattern specified
	 */
	public void add(Searchable object, TokenizePatterns tokenizePattern, String... texts) {
		for (String text : texts) {
			String[] tokens = Strings.tokenize(tokenizePattern, text).split(" ");

			for (String token : tokens) {
				mObjects.put(token, object);
			}
		}
	}

	/**
	 * Search for objects
	 * @param searchString when searching for more than two words AND both words has to be
	 *        found for the object
	 * @return found objects sorted by relevance
	 */
	public List<Searchable> search(String searchString) {
		final Map<Searchable, AtomicInteger> foundObjects = new HashMap<>();

		// Find
		String[] searchWords = searchString.trim().split(" ");
		for (String token : searchWords) {
			Collection<Searchable> foundByToken = mObjects.get(token);

			for (Searchable foundObject : foundByToken) {
				AtomicInteger foundCount = foundObjects.get(foundObject);

				if (foundCount == null) {
					foundCount = new AtomicInteger(1);
					foundObjects.put(foundObject, foundCount);
				} else {
					foundCount.incrementAndGet();
				}
			}
		}

		// Sort
		List<Searchable> foundAndSorted = new ArrayList<>();
		foundAndSorted.addAll(foundObjects.keySet());
		foundAndSorted.sort(new Comparator<Searchable>() {
			@Override
			public int compare(Searchable o1, Searchable o2) {
				AtomicInteger count1 = foundObjects.get(o1);
				AtomicInteger count2 = foundObjects.get(o2);
				return count2.get() - count1.get();
			}
		});

		return foundAndSorted;
	}

}
