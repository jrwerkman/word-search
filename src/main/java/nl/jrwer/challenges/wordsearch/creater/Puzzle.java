package nl.jrwer.challenges.wordsearch.creater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import nl.jrwer.challenges.wordsearch.solver.Direction;

public class Puzzle {
	private final Random random = new Random();
	private final char[][] grid;
	private final char[] sentence;
	private final int width;
	private final int height;
	private final int amountLetters;
	private final Set<String> usedWords = new HashSet<>();
	private final Set<Coord> used = new HashSet<>();
	private final Set<Coord> dontUse = new HashSet<>();
	private final WordList words = new WordList();
	
	public Puzzle(int width, int height, String sentence) {
		this.grid = new char[width][height];
		
		// TODO temp
		for(int x=0; x<width; x++)
			Arrays.fill(grid[x], '#');
		
		this.sentence = sentence.toUpperCase().toCharArray();
		
		this.height = height;
		this.width = width;
		this.amountLetters = width * height;
	}
	
	public void create() {
		placeSentence();
		placeWords();
	}
	
	private void placeSentence() {
		List<Coord> randomCoords = new ArrayList<>();
		
		while(randomCoords.size() < sentence.length) {
			Coord c = getRandomCoord();
			
			if(!randomCoords.contains(c))
				randomCoords.add(c);
		}

		Collections.sort(randomCoords, new Comparator<Coord>() {
			public int compare(Coord c1, Coord c2) {
				int result = Integer.compare(c1.y, c2.y);
				
				if (result == 0) 
					result = Integer.compare(c1.x, c2.x);

				return result;
			}
		});
		
		int i=0;
		for(Coord c : randomCoords) {
			grid[c.x][c.y] = sentence[i];

			used.add(c);
			dontUse.add(c);

			i++;
		}
	}
	
	// TODO if word contains part of another word
	private void placeWords() {
		while(used.size() < amountLetters) {
			String word = words.getRandomWord();
			
			if(usedWords.contains(word))
				continue;
			
			char[] wordChars = word.toCharArray();
			Direction direction = getRandomDirection();
			
			Coord c = getRandomCoord();
			
			if(!dontUse.contains(c) && canPlace(word, wordChars, direction, c)) {
				System.out.println(word);
				placeWord(wordChars, direction, c);
				usedWords.add(word);
			}
		}
	}
	
	private void placeWord(char[] word, Direction direction, Coord coord) {
		Coord c = coord;
		
		for(char letter : word) {
			if(grid[c.x][c.y] == '#')
				grid[c.x][c.y] = letter;
			else if(grid[c.x][c.y] != letter)
				throw new RuntimeException();
			
			used.add(c);
			
			c = new Coord(c, direction);
		}
	}
	
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
	private boolean canPlace(String wordString, char[] word, Direction direction, Coord coord) {
		boolean unusedLetter = false;
		Coord currentCoords = coord;
		
		for(char letter : word) {
			if(dontUse.contains(currentCoords) 
					|| !inBounds(currentCoords) 
					|| !isCoordFree(currentCoords, letter)
					|| isSubWord(wordString))
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
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		
		return new Coord(x, y);
	}
	
	private Direction getRandomDirection() {
		Direction[] dirs = Direction.values();
		
		return dirs[random.nextInt(dirs.length)];
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
