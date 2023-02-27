package nl.jrwer.challenges.wordsearch.forchallenge.novadoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.AZCoordSet;
import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.CoordCollection;
import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.StringCollection;
import nl.jrwer.challenges.wordsearch.solver.Direction;

public class WordSearch2dArray {
	public static final String GRID = "woordzoeker.txt";
	public static final String LIST = "woordenlijst.txt";

	public static AZCoordSet azCoordSet = new AZCoordSet();
	public static char[][] grid;
	public static boolean [][] used;
	public static int height, width;
	public static StringCollection words = new StringCollection();
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		// LOAD
		loadGrid();
		loadWords();
		
		// PROCESS
		for(int i=0; i<words.size(); i++)
			findWord(words.get(i).toCharArray());
		
		// PRINT RESULT
		StringBuilder sb = new StringBuilder();
		
		for(int y=0; y<height; y++) 
			for(int x=0; x<width; x++) 
				if(!used[y][x])
					sb.append(grid[y][x]);
		
		System.out.println(sb.toString());

		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
	
	public static void findWord(char[] word) {
		CoordCollection coords = azCoordSet.get(word[0]);
		
		for(int i=0; i<coords.size(); i++) {
			Coord c = coords.get(i);
			
			if(findAndMarkWord(c.y, c.x, word))
				return;
		}
	}	
	
	public static boolean findAndMarkWord(int y, int x, char[] word) {
		return find(y, x, word, Direction.RIGHT) || find(y, x, word, Direction.LEFT) 
				|| find(y, x, word, Direction.BOTTOM) || find(y, x, word, Direction.TOP)
				|| find(y, x, word, Direction.LEFT_TOP) || find(y, x, word, Direction.LEFT_BOTTOM)
				|| find(y, x, word, Direction.RIGHT_TOP) || find(y, x, word, Direction.RIGHT_BOTTOM);
	}	
	
	public static boolean find(int y, int x, char[] word, Direction dir) {
		for(int i=1; i<word.length; i++)
			if(!equals(y + (i * dir.ver), x + (i * dir.hor), word[i]))
				return false;
	
		for(int i=0; i<word.length; i++)
			used[y + (i * dir.ver)][x + (i * dir.hor)] = true;
		
		return true;
	}
	
	public static boolean equals(int y, int x, char c) {
		if(y < 0 || x < 0 || x >= width || y >= height)
			return false;
		
		return grid[y][x] == c;
	}
	
	public static void loadGrid() throws Exception{
		List<String> lines = load(GRID);
		
		height = lines.size();
		width = lines.get(0).length();
		grid = new char[height][width];
		used = new boolean[height][width];
		
		for(int y=0; y<height; y++) {
			char[] line = lines.get(y).toCharArray();
			grid[y] = line;
			
			for(int x=0; x<width; x++) 
				azCoordSet.add(line[x], new Coord(x, y));
		}
	}
	
	public static void loadWords() throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(LIST));) {
			String word;
			
			while ((word = br.readLine()) != null)
				words.add(word);
		}
	}
	
	public static List<String> load(String file) throws Exception {
		List<String> lines = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line;
			
			while ((line = br.readLine()) != null)
				lines.add(line.replace(" ", "").replace("\t", ""));
		}

		return lines;
	}	
}
