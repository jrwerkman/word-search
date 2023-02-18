package nl.jrwer.challenges.wordsearch;

import java.util.ArrayList;
import java.util.List;

import nl.jrwer.challenges.wordsearch.creater.Puzzle;
import nl.jrwer.challenges.wordsearch.creater.words.BigToSmallWordList;
import nl.jrwer.challenges.wordsearch.creater.words.FakeRandomWordList;
import nl.jrwer.challenges.wordsearch.creater.words.IWordList;
import nl.jrwer.challenges.wordsearch.creater.words.Language;
import nl.jrwer.challenges.wordsearch.creater.words.RandomWordList;
import nl.jrwer.challenges.wordsearch.creater.words.WordList;

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
			
//			puzzle("grid.txt", "words.txt", 20, 20, "Novadoc", new BigToSmallWordList(Language.NL));
//			puzzle("grid.txt", "words.txt", 20, 20, "Novadoc", new RandomWordList(Language.NL));
//			puzzle("grid.txt", "words.txt", 20, 20, "Novadoc", new WordList(words);
			puzzle("grid.txt", "words.txt", 100, 100, "Novadoc", new FakeRandomWordList(4, 20));
			
//			puzzle("grid.txt", "words.txt", 20, 20, 10);
//			puzzle("grid.txt", "words.txt", 20, 20, words);
			
			long end = System.currentTimeMillis();

			System.out.println("Time: " + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void puzzle(String gridFile, String wordsFile, 
//			int height, int width, String word) throws Exception {
//		Puzzle puzzle = new Puzzle(height, width, word);
//		puzzle.create(gridFile, wordsFile);
//
//		System.out.println(puzzle);
//	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, String sentence, IWordList wordList) throws Exception {
		Puzzle puzzle = new Puzzle(height, width, sentence, wordList);
		puzzle.create(gridFile, wordsFile);

		System.out.println(puzzle);
	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, int length, IWordList wordList) throws Exception {
		Puzzle puzzle = new Puzzle(height, width, length, wordList);
		puzzle.create(gridFile, wordsFile);

		System.out.println(puzzle);
	}
	
	public static void puzzle(String gridFile, String wordsFile, 
			int height, int width, List<String> wordList) throws Exception {
		List<String> l = new ArrayList<>();
		l.addAll(wordList);
		
		IWordList wl = new WordList(l);
		
		Puzzle puzzle = new Puzzle(height, width, "", wl);
		puzzle.create(gridFile, wordsFile);
		
		System.out.println(puzzle);
	}
}
