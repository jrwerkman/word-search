package nl.jrwer.challenges.wordsearch.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSearchLetterMapArray extends WordSearchBase {
	public Map<Character, List<Integer>> letters = new HashMap<>();

	public char[] grid;
	public boolean[] used;
	public int height, width;
	public List<String> words;
	
	public WordSearchLetterMapArray(String gridFile, String wordsFile) {
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
		List<Integer> coords = letters.get(word[0]);
		
		for(int coord : coords)
			if(findAndMarkWord(coord, word)) {
				wordsFound++;
				return;
			}
	}
	
	public boolean findAndMarkWord(int coord, char[] word) {
		int y = coord / width;
		int x = coord % width;
		
		return find(x, y, word, 1, 0) || find(x, y, word, -1, 0) 
				|| find(x, y, word, 0, 1) || find(x, y, word, 0, -1)
				|| find(x, y, word, -1, -1) || find(x, y, word, -1, 1)
				|| find(x, y, word, 1, -1) || find(x, y, word, 1, 1);
	}
	
	public boolean find(int x, int y, char[] word, int xDir, int yDir) {
		int wordLength = word.length;
		int boundX = x + ((wordLength-1) * xDir);
		int boundY = y + ((wordLength-1) * yDir);
		
		if(boundX < 0 || boundY < 0 || boundX >= width || boundY >= height)
			return false;
		
		for(int i=1; i<wordLength; i++) 
			if(grid[((y + (i * yDir)) * width) + (x + (i * xDir))] != word[i])
				return false;
	
		for(int i=0; i<wordLength; i++)
			used[((y + (i * yDir)) * width) + (x + (i * xDir))] = true;
		
		return true;
		
	}
	
	public String unusedChars() {
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<this.height * this.width; i++) 
			if(!used[i])
				sb.append(grid[i]);
		
		return sb.toString();
	}
	
	public void loadGrid() throws Exception {
		List<String> lines = load(gridFile);
		this.height = lines.size();
		this.width = lines.get(0).length();
		this.grid = new char[this.height * this.width];
		this.used = new boolean[this.height * this.width];
		
		for(int y=0; y<this.height; y++) {
			char[] line = lines.get(y).toCharArray();
			System.arraycopy(line, 0, grid, y * width, width);

			for(int x=0; x<this.width; x++) {
				int coord = (y*width) + x;
				
				if(letters.containsKey(line[x])) {
					letters.get(line[x]).add(coord);
				} else {
					List<Integer> c = new ArrayList<>();
					c.add(coord);
					
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
}
