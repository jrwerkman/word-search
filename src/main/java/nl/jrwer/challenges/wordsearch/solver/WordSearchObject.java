package nl.jrwer.challenges.wordsearch.solver;

import java.util.List;

public class WordSearchObject extends WordSearchBase {
	
	public Elem[][] grid;
	public int height, width;
	public List<String> words;
	
	public WordSearchObject(String gridFile, String wordsFile) {
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
		for(int x=0; x<this.width; x++)
			for(int y=0; y<this.height; y++) 
				if(grid[y][x].c == word[0] && findAndMarkWord(y, x, word)) {
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
			grid[y + (i * dir.ver)][x + (i * dir.hor)].found = true;
		
		return true;
		
	}
	
	public boolean equals(int y, int x, char c) {
		if(y < 0 || x < 0 || x >= width || y >= height)
			return false;
		
		return grid[y][x].c == c;
	}
	
	public String unusedChars() {
		StringBuilder sb = new StringBuilder();
		
		for(int y=0; y<this.height; y++) 
			for(int x=0; x<this.width; x++) 
				if(!grid[y][x].found)
					sb.append(grid[y][x].c);
		
		return sb.toString();
	}
	
	public void loadGrid() throws Exception {
		List<String> lines = load(gridFile);
		this.height = lines.size();
		this.width = lines.get(0).length();
		this.grid = new Elem[this.height][this.width];
		
		for(int y=0; y<this.height; y++) {
			char[] line = lines.get(y).toCharArray();
			
			for(int x=0; x<this.width; x++) 
				this.grid[y][x] = new Elem(line[x]);
		}
	}
	
	public void loadWords() throws Exception {
		this.words = load(wordsFile);
	}
	

	@Override
	public int words() {
		return words.size();
	}

	class Elem {
		final char c;
		boolean found = false;
		
		public Elem(char c) {
			this.c = c;
		}
	}
}
