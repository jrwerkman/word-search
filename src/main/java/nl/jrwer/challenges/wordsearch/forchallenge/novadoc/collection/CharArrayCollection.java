package nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection;

import java.util.Arrays;

public class CharArrayCollection {
	private JRChar[] collection;
	private int lastIndex = 0;
	
	public CharArrayCollection() {
		this(1000000);
	}
	
	public CharArrayCollection(int initialArraySize) {
		this.collection = new JRChar[initialArraySize];
	}
	
	public void add(char[] value) {
		collection[lastIndex] = new JRChar(value);
		lastIndex++;
		
		if(lastIndex == collection.length)
			collection = Arrays.copyOf(collection, collection.length * 2);
	}
	
	public char[] get(int index) {
		return collection[index].arr;
	}
	
	public int size() {
		return lastIndex;
	}
	
	private class JRChar {
		final char[] arr;
		
		public JRChar(char[] arr) {
			this.arr = arr;
		}
	}
}
