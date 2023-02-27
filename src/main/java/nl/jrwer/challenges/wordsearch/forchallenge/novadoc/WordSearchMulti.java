package nl.jrwer.challenges.wordsearch.forchallenge.novadoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.AZCoordSet;
import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.CharArrayCollection;
import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.CharArrayCollection.JRChar;
import nl.jrwer.challenges.wordsearch.forchallenge.novadoc.collection.CoordCollection;
import nl.jrwer.challenges.wordsearch.solver.Direction;

public class WordSearchMulti {
//	public static final String GRID = "woordzoeker.txt";
//	public static final String LIST = "woordenlijst.txt";
	public static final String GRID = "1000x1000_grid.txt";
	public static final String LIST = "1000x1000_words.txt";

	public static AZCoordSet azCoordSet = new AZCoordSet();
	public static char[] grid;
	public static boolean[] used;
	public static int height, width, total;
	
	public static CharArrayCollection words = new CharArrayCollection();
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		// LOAD
		loadGrid();
		loadWords();
		
		int cores =  Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(cores); // creates a pool of 5 threads
		
		// PROCESS
		for (int i = 0; i < cores; i++) 
			executor.execute(new WordSearchThread());
		
		executor.shutdown();
		while (!executor.isTerminated()) {}
		
		// PRINT RESULT
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<total; i++) 
			if(!used[i])
				sb.append(grid[i]);
		
		System.out.println(sb.toString());

		long end = System.currentTimeMillis();
		System.out.println((end - start) + " ns");
	}
	
	public static class WordSearchThread implements Runnable {
		
		@Override
		public void run() {
			while(words.hasNext()) {
				JRChar jrWord = words.peek();
				
				if(jrWord == null)
					continue;

				findWord(jrWord.arr);
			}
		}
		
		public void findWord(char[] word) {
			CoordCollection coords = azCoordSet.get(word[0]);
			
			for(int i=0; i<coords.size(); i++) {
				Coord c = coords.get(i);
				
				if(findAndMarkWord(c.y, c.x, word))
					return;
			}
		}	
		
		public boolean findAndMarkWord(int y, int x, char[] word) {
			return find(y, x, word, Direction.RIGHT) || find(y, x, word, Direction.LEFT) 
					|| find(y, x, word, Direction.BOTTOM) || find(y, x, word, Direction.TOP)
					|| find(y, x, word, Direction.LEFT_TOP) || find(y, x, word, Direction.LEFT_BOTTOM)
					|| find(y, x, word, Direction.RIGHT_TOP) || find(y, x, word, Direction.RIGHT_BOTTOM);
		}	
		
		public boolean find(int y, int x, char[] word, Direction dir) {
			for(int i=1; i<word.length; i++)
				if(!equals(y + (i * dir.ver), x + (i * dir.hor), word[i]))
					return false;
			
			for(int i=0; i<word.length; i++)
				used[((y + (i * dir.ver)) * width) + (x + (i * dir.hor))] = true;
			
			return true;
		}
		
		public boolean equals(int y, int x, char c) {
			if(y < 0 || x < 0 || x >= width || y >= height)
				return false;
			
			return grid[(y * width) +  x] == c;
		}
	}
	
	public static void loadGrid() throws Exception{
		List<String> lines = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(GRID));) {
			String line;
			
			while ((line = br.readLine()) != null)
				lines.add(line.replace(" ", "").replace("\t", ""));
		}
		
		height = lines.size();
		width = lines.get(0).length();
		total = height * width;
		grid = new char[total];
		used = new boolean[total];
		
		for(int y=0; y<height; y++) {
			char[] line = lines.get(y).toCharArray();
			System.arraycopy(lines.get(y).toCharArray(), 0, grid, y * width, width);
			
			for(int x=0; x<width; x++) 
				azCoordSet.add(line[x], new Coord(x, y));
		}
	}
	
	public static void loadWords() throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(LIST));) {
			String word;
			
			while ((word = br.readLine()) != null)
				words.add(word.toCharArray());
		}
	}
}
