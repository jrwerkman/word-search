package nl.jrwer.challenges.wordsearch.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSearchLetterMap extends WordSearchBase {
	
	public Map<Character, List<Coord>> letters = new HashMap<>();
	public char[][] grid;
	public boolean [][] used;
	public int height, width;
	public List<String> words;
	
	public WordSearchLetterMap(String gridFile, String wordsFile) {
		super(gridFile, wordsFile);
	}
	
	@Override
	public void prepare() throws Exception {
		loadGrid();
		loadWords();
	}
	
	@Override
	public String execute() {
		for(String word : words)
			findWord(word.toCharArray());
		
		return unusedChars();
	}
	
	public void findWord(char[] word) {
		List<Coord> l = letters.get(word[0]);
		
		for(Coord c : l) {
			if(findAndMarkWord(c.y, c.x, word)) {
				wordsFound++;
				return;
			}
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
			used[y + (i * dir.ver)][x + (i * dir.hor)] = true;
		
		return true;
	}
	
	public boolean equals(int y, int x, char c) {
		if(y < 0 || x < 0 || x >= width || y >= height)
			return false;
		
		return grid[y][x] == c;
	}
	
	public String unusedChars() {
		StringBuilder sb = new StringBuilder();
		
		for(int y=0; y<this.height; y++) 
			for(int x=0; x<this.width; x++) 
				if(!used[y][x])
					sb.append(grid[y][x]);
		
		return sb.toString();
	}
	
	public void loadGrid() throws Exception{
		List<String> lines = load(gridFile);
		
		this.height = lines.size();
		this.width = lines.get(0).length();
		this.grid = new char[this.height][this.width];
		this.used = new boolean[this.height][this.width];
		
		for(int y=0; y<this.height; y++) {
			char[] line = lines.get(y).toCharArray();
			this.grid[y] = line;
			
			for(int x=0; x<width; x++) {
				if(letters.containsKey(line[x])) {
					letters.get(line[x]).add(new Coord(x, y));
				} else {
					List<Coord> c = new ArrayList<>();
					c.add(new Coord(x, y));
					
					letters.put(line[x], c);
				}
			}
		}
	}
	
	public void loadWords() throws Exception {
		this.words = load(wordsFile);
	}
	

	@Override
	public int words() {
		return words.size();
	}
	
	class Coord {
		final int x, y;
		
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
