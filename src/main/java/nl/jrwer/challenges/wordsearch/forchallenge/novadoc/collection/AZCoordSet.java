package nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection;

import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.Coord;

public class AZCoordSet {
	private final CoordCollection[] collections = new CoordCollection[26];
	
	public AZCoordSet() {
		for(int i=0; i<26; i++)
			collections[i] = new CoordCollection();
	}
	
	public void add(char character, Coord value) {
		collections[character - 65].add(value);
	}
	
	public CoordCollection get(char character) {
		return collections[character - 65];
	}
}
