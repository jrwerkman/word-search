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
import nl.jrwer.challenges.wordsearch.creater.words.RandomWordList;
import nl.jrwer.challenges.wordsearch.creater.words.WordList;
import nl.jrwer.challenges.wordsearch.solver.Direction;

public class Puzzle {
	private static final Random RANDOM = new Random();
	private final char[][] grid;
	private final char[] sentence;
	private final int width;
	private final int height;
	private final int amountLetters;
	
	private final Map<Character, List<Coord>> letterCoords = new HashMap<>();
//	private final Map<Coord, String> usedWords = new HashMap<>(); 
	
	private final Set<String> usedWords = new HashSet<>();
	private final Set<Coord> unused = new HashSet<>();
	private final Set<Coord> used = new HashSet<>();
	private final Set<Coord> dontUse = new HashSet<>();
	private final IWordList words;
	
	public Puzzle(int width, int height, List<String> words) {
		this(width, height, "", new WordList(words));
	}
	
	public Puzzle(int width, int height, int emptySpots) {
		this(width, height, getRandomSentence(emptySpots));
	}
	
	
	public Puzzle(int width, int height, String sentence) {
		this(width, height, sentence, new RandomWordList());
	}
	
	private Puzzle(int width, int height, String sentence, IWordList words) {
		this.words = words; 
		this.grid = new char[width][height];
		
		for(int x=0; x<width; x++)
			Arrays.fill(grid[x], '#');
		
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				unused.add(new Coord(x, y));
		
		this.sentence = sentence.toUpperCase().toCharArray();
		
		this.height = height;
		this.width = width;
		this.amountLetters = width * height;
	}
	
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
	
	private void gridToFile(String filename) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
			writer.write(this.toString());
		}
	}
	
	private void wordsToFile(String filename) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		for(String word : usedWords)
			sb.append(word).append('\n');
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
			writer.write(sb.toString());
		}
		
	}

//	private void placeSentence() {
//		List<Coord> sentenceCoords = new ArrayList<>();
//		sentenceCoords.addAll(unused);
//		
//		sortCoordsList(sentenceCoords);
//		placeSentence(sentenceCoords);
//	}
	
	private void placeSentenceRandom() {
		List<Coord> randomCoords = new ArrayList<>();
		
		while(randomCoords.size() < sentence.length) {
			Coord c = getRandomCoord();
			
			if(!randomCoords.contains(c))
				randomCoords.add(c);
		}

		sortCoordsList(randomCoords);
		placeSentence(randomCoords);
	}
	
	private void sortCoordsList(List<Coord> l) {
		Collections.sort(l, new Comparator<Coord>() {
			public int compare(Coord c1, Coord c2) {
				int result = Integer.compare(c1.y, c2.y);
				
				if (result == 0) 
					result = Integer.compare(c1.x, c2.x);

				return result;
			}
		});
	}
	
	private void placeSentence(List<Coord> coords) {
		int i=0;
		for(Coord c : coords) {
			grid[c.x][c.y] = sentence[i];

			used.add(c);
			dontUse.add(c);
			unused.remove(c);

			i++;
		}
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
	
	private boolean isSubWord(String word) {
		for(String used : usedWords)
			if(word.contains(used) || used.contains(word))
				return true;
		
		return false;
	}
	
	private Coord getRandomCoord() {
		int x = RANDOM.nextInt(width);
		int y = RANDOM.nextInt(height);
		
		return new Coord(x, y);
	}
	
	private Direction getRandomDirection() {
		Direction[] dirs = Direction.values();
		
		return dirs[RANDOM.nextInt(dirs.length)];
	}
	
	private static String getRandomSentence(int length) {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<length; i++)
			sb.append(getRandomChar());
		
		return sb.toString();
	}
	
	public static char getRandomChar() {
		return (char) (RANDOM.nextInt(26) + 65);
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
	
	class Coord {
		final int x,y;
		
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Coord(Coord c, Direction d) {
			this.x = c.x + d.hor;
			this.y = c.y + d.ver;
		}		
		
		@Override
		public int hashCode() {
			return x << 8 | y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Coord) {
				Coord c = (Coord) obj;
				
				return x == c.x && y == c.y;
			}
			return false;
		}
		
		@Override
		public String toString() {
			return x + "," + y;
		}
	}
}
