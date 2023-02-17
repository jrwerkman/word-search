package nl.jrwer.challenges.wordsearch.solver;

import java.util.List;

public class WordSearch2dArray extends WordSearchBase {

	public char[][] grid;
	public boolean [][] used;
	public int height, width;
	public List<String> words;
	
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
		for(int x=0; x<this.width; x++)
			for(int y=0; y<this.height; y++) 
				if(grid[y][x] == word[0] && findAndMarkWord(y, x, word)) {
					wordsFound++;
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
	
	public void loadGrid() throws Exception {
		List<String> lines = load(FILE_GRID);
		this.height = lines.size();
		this.width = lines.get(0).length();
		this.grid = new char[this.height][this.width];
		this.used = new boolean[this.height][this.width];
		
		for(int y=0; y<this.height; y++)
			this.grid[y] = lines.get(y).toCharArray();
	}
	
	public void loadWords() throws Exception {
		this.words = load(FILE_WORDS);
	}
}
