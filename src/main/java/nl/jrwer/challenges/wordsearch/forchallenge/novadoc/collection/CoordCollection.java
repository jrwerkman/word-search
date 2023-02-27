package nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection;

import java.util.Arrays;

import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.Coord;

public class CoordCollection {
	private Coord[] collection;
	private int lastIndex = 0;
	
	public CoordCollection() {
		this(10000);
	}
	
	public CoordCollection(int initialArraySize) {
		this.collection = new Coord[initialArraySize];
	}
	
	public void add(Coord value) {
		collection[lastIndex] = value;
		lastIndex++;
		
		if(lastIndex == collection.length)
			collection = Arrays.copyOf(collection, collection.length * 2);
	}
	
	public Coord get(int index) {
		return collection[index];
	}
	
	public int size() {
		return lastIndex;
	}
}
