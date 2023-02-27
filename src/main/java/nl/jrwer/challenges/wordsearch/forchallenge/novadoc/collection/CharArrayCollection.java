package nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection;

import java.util.Arrays;

public class CharArrayCollection {
	private JRChar[] collection;
	private int lastIndex = 0;
	private int counter = 0;
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
	
	public synchronized void reset() {
		counter = 0;
	}
	
	public synchronized boolean hasNext() {
		return counter < lastIndex;
	}
	
	public synchronized JRChar peek() {
		try {
			if(counter < lastIndex)
				return collection[counter];
		} finally {
			counter++;
		}
		
		return null;
	}
	
	public char[] get(int index) {
		return collection[index].arr;
	}
	
	public int size() {
		return lastIndex;
	}
	
	public class JRChar {
		public final char[] arr;
		
		public JRChar(char[] arr) {
			this.arr = arr;
		}
	}
}
