package nl.jrwer.challenges.wordsearch.creater;

import java.util.ArrayList;
import java.util.List;

public class WordSearchCreater {
	
	public static List<String> words = List.of(
			"outcome",
		    "relation",
		    "management",
		    "writer",
		    "indication",
		    "beer",
		    "menu",
		    "ratio",
		    "length",
		    "opportunity");
	
	/**
	 * https://github.com/OpenTaal/opentaal-wordlist 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			
			puzzle("grid.txt", "words.txt", 20, 20, "Novadoc");
//			puzzle("grid.txt", "words.txt", 20, 20, 10);
//			puzzle("grid.txt", "words.txt", 20, 20, words);
			
			long end = System.currentTimeMillis();

			System.out.println("Time: " + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, String word) throws Exception {
		Puzzle puzzle = new Puzzle(height, width, word);
		puzzle.create(gridFile, wordsFile);

		System.out.println(puzzle);
	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, int length) throws Exception {
		Puzzle puzzle = new Puzzle(height, width, length);
		puzzle.create(gridFile, wordsFile);

		System.out.println(puzzle);
	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, List<String> wordList) throws Exception {
		List<String> l = new ArrayList<>();
		l.addAll(wordList);
		
		Puzzle puzzle = new Puzzle(height, width, l);
		puzzle.create(gridFile, wordsFile);
		
		System.out.println(puzzle);
	}
}
