package nl.jrwer.challenges.wordsearch.creater.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

// TODO load words from big to small
public class RandomWordList implements IWordList {
	/**
	 * https://github.com/dwyl/english-words
	 */
	private static final String WORD_LIST_EN = "words_alpha.txt";
	
	/**
	 * https://github.com/OpenTaal/opentaal-wordlist 
	 */
	private static final String WORD_LIST_NL = "words_nl.txt";
	
	private final List<String> words = new ArrayList<>();
	private final int wordCount;
	
	private int count = 0;
	
	public RandomWordList() {
		loadWords();
		this.wordCount = words.size();
	}
	
	private void loadWords() {
        try (InputStream inputStream = RandomWordList.class.getClassLoader().getResourceAsStream(WORD_LIST_NL);
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        	String line;
        	
            while ((line = reader.readLine()) != null) {
            	if(line.isBlank())
            		continue;
            	
            	// skip 1 letter words
            	if(line.length() > 2 && Pattern.matches("[\\dA-Za-z]+", line))
            		words.add(line.toUpperCase());
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void randomize() {
		count = 0;
		Collections.shuffle(words, RANDOM);
	}
	
	@Override
	public boolean hasNext() {
		return count < words.size();
	}
	
	@Override
	public String next() {
		try {
			return words.get(count);
		} finally {
			count++;
		}
	}
	
	@Override
	public String getRandomWord() {
		return words.get(RANDOM.nextInt(wordCount)).toUpperCase();
	}
}
