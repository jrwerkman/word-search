package nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection;

import java.util.Arrays;

public class StringCollection {
	private String[] collection;
	private int lastIndex = 0;
	
	public StringCollection() {
		this(1000000);
	}
	
	public StringCollection(int initialArraySize) {
		this.collection = new String[initialArraySize];
	}
	
	public void add(String value) {
		collection[lastIndex] = value;
		lastIndex++;
		
		if(lastIndex == collection.length)
			collection = Arrays.copyOf(collection, collection.length * 2);
	}
	
	public String get(int index) {
		return collection[index];
	}
	
	public int size() {
		return lastIndex;
	}
}
