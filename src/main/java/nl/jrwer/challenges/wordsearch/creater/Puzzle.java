package nl.jrwer.challenges.wordsearch.creater;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import nl.jrwer.challenges.wordsearch.creater.words.IWordList;
import nl.jrwer.challenges.wordsearch.solver.Direction;

public class Puzzle extends AbstractPuzzle {

	
	protected final Map<Character, List<Coord>> letterCoords = new HashMap<>();
//	private final Map<Coord, String> usedWords = new HashMap<>(); 
	

	protected final IWordList words;
	
	public Puzzle(int width, int height, IWordList words) {
		this(width, height, "", words);
	}
	
	public Puzzle(int width, int height, int emptySpots, IWordList words) {
		this(width, height, getRandomSentence(emptySpots), words);
	}
	
	public Puzzle(int width, int height, String sentence, IWordList words) {
	    super(width, height, sentence);
	    
		this.words = words; 
		
		for(int x=0; x<width; x++)
			Arrays.fill(grid[x], '#');
	}
	
    @Override
	public void create() {
		placeSentenceRandom();
		placeWords();
		
		for(Coord c : unused)
			grid[c.x][c.y] = getRandomChar();
	}
	
	public void create(String gridOutput, String wordsOutput) throws IOException {
		create();
		
		gridToFile(gridOutput);
		wordsToFile(wordsOutput);
	}

	/**
	 * TODO keep list of coords of letter, for first letter of word.
	 * TODO if not placable, try for every unused letter, or every first letter
	 */
	private void placeWords() {
		words.randomize();
		
		while(words.hasNext()) {
			String wordString = words.next();
			
			if(usedWords.contains(wordString) || isSubWord(wordString))
				continue;
			
			char[] word = wordString.toCharArray();
			
			if(isWordInGrid(word))
				continue;
			
			Direction direction = getRandomDirection();
			
			Coord c = getRandomCoord();
			
			if(canPlace(word, direction, c)) {
				System.out.println(wordString);
				placeWord(wordString, word, direction, c);
			} else {
				// TODO if not enough try other directions
				c = placeAtKnownLocation(word, direction);
				
				if(c != null) {
					System.out.println(wordString);
					placeWord(wordString, word, direction, c);
				}
			}
			
			if(used.size() == amountLetters) {
				System.out.println("ready");
				break;
			}
		}
	}
	
	private boolean isWordInGrid(char[] word) {
		// TODO check for double words

		List<Coord> coords = letterCoords.get(word[0]);
		
		if(coords != null)
			for(Coord c : coords)
				if(isWordInGrid(word, c))
					return true;
		
		return false;
	}
	
	private boolean isWordInGrid(char[] word, Coord c) {
		for(Direction direction : Direction.values())
			if(isWordInGrid(word, c, direction))
				return true;
		
		return false;
	}

	private boolean isWordInGrid(char[] word, Coord c, Direction direction) {
		for(char letter : word) {
			if(!inBounds(c))
				return false;
			
			if(grid[c.x][c.y] != letter)
				return false;
			
			c = new Coord(c, direction);
		}
		
		return true;
	}

	private Coord placeAtKnownLocation(char[] word, Direction direction) {
		List<Coord> charCoords = letterCoords.get(word[0]);
		
		if(charCoords != null)
			for(Coord c : charCoords)
				if(canPlace(word, direction, c))
					return c;
		
		for(Coord c: unused) 
			if(canPlace(word, direction, c))
				return c;
		
		return null;
	}
	
	private void placeWord(String wordString, char[] word, Direction direction, Coord coord) {
		Coord c = coord;
		
		for(char letter : word) {
			if(grid[c.x][c.y] == '#') {
				grid[c.x][c.y] = letter;
				
				if(letterCoords.containsKey(letter)) {
					letterCoords.get(letter).add(c);
				} else {
					List<Coord> coords = new ArrayList<>();
					coords.add(c);
					
					letterCoords.put(letter, coords);
				}
			} else if(grid[c.x][c.y] != letter) {
				throw new RuntimeException();
			}
			
			used.add(c);
			unused.remove(c);
			
			c = new Coord(c, direction);
		}
		
		usedWords.add(wordString);
	}
	
//	private boolean canPlace(char[] word, Coord coord) {
//		for(Direction direction : Direction.values())
//			if(!canPlace(word, direction, coord))
//				return false;
//		
//		return true;
//	}
	
	/**
	 * Checks:
	 * - if words is in grid
	 * - if sentence letters are not used
	 * - if coord contains a letter it is the same as the new word
	 * - if all letters are used, dont add the word
	 * - if word is a sub word of a used word and vice versa
	 * @param word
	 * @param direction
	 * @param coord
	 * @return
	 */
	private boolean canPlace(char[] word, Direction direction, Coord coord) {
		boolean unusedLetter = false;
		Coord currentCoords = coord;
		
		for(char letter : word) {
			if(dontUse.contains(currentCoords) 
					|| !inBounds(currentCoords) 
					|| !isCoordFree(currentCoords, letter))
				return false;
			
			
			if(!used.contains(currentCoords))
			 	unusedLetter = true;
			
			currentCoords = new Coord(currentCoords, direction);
		}
		
		return unusedLetter;
	}
	
	private boolean inBounds(Coord c) {
		return c.x >= 0 && c.x < width && c.y >= 0 && c.y < height;
	}
	
	private boolean isCoordFree(Coord c, char letter) {
		char currentChar = grid[c.x][c.y];
		
		return currentChar == '#' || currentChar == letter;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++)
				sb.append(grid[x][y]).append(' ');
			
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
