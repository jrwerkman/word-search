package nl.jrwer.challenges.wordsearch.creater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordList {
	private static final String WORD_LIST = "words_alpha.txt";
	private final List<String> words = new ArrayList<>();
	private final int wordCount;
	private final Random random = new Random();
	
	public int count = 0;
	
	public WordList() {
		loadWords();
		this.wordCount = words.size();
	}
	
	private void loadWords() {
        try (InputStream inputStream = WordList.class.getClassLoader().getResourceAsStream(WORD_LIST);
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        	String line;
        	
            while ((line = reader.readLine()) != null) {
            	if(line.isBlank())
            		continue;
            	
            	// skip 1 letter words
            	if(line.length() > 2)
            		words.add(line);
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void randomize() {
		Collections.shuffle(words, random);
	}
	
	public boolean hasNext() {
		return count < words.size();
	}
	
	public String next() {
		try {
			return words.get(count);
		} finally {
			count++;
		}
	}
	
	
	
	public String getRandomWord() {
		return words.get(random.nextInt(wordCount)).toUpperCase();
	}
}
