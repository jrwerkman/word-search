package nl.jrwer.challenges.wordsearch.creater.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

// TODO load words from big to small
public class BigToSmallWordList implements IWordList {
	/**
	 * https://github.com/dwyl/english-words
	 */
	private static final String WORD_LIST_EN = "words_alpha.txt";
	
	/**
	 * https://github.com/OpenTaal/opentaal-wordlist 
	 */
	private static final String WORD_LIST_NL = "words_nl.txt";
	
	private final Map<Integer, List<String>> words = new HashMap<>();
	private final int[] keys;
	
	private int currentKeyIndex;
	private int currentWordListIndex = 0;
	private boolean hasNext = false;
	private List<String> currentList;
	
	public BigToSmallWordList() {
		loadWords();
		this.keys = new int[this.words.size()];
		
		int i=0;
		for(Integer key : this.words.keySet()) {
			this.keys[i] = key;
			i++;
		}
		
		Arrays.sort(this.keys);
		this.currentKeyIndex = this.keys[this.keys.length - 1];
	}
	
	private int loadWords() {
		int wordCount = 0;
		
        try (InputStream inputStream = BigToSmallWordList.class.getClassLoader().getResourceAsStream(WORD_LIST_NL);
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        	String line;
        	
            while ((line = reader.readLine()) != null) {
            	if(line.isBlank())
            		continue;
            	
            	// skip 1 letter words
            	if(line.length() > 2 && Pattern.matches("[\\dA-Za-z]+", line)) {
            		int wordLength = line.length();
            		
            		if(words.containsKey(wordLength)) {
            			words.get(wordLength).add(line.toUpperCase());
            		} else {
            			List<String> l = new ArrayList<>();
            			l.add(line.toUpperCase());
            			
            			words.put(wordLength, l);
            		}
            		
            		wordCount++;
            	}
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return wordCount;
	}
	
	@Override
	public void randomize() {
		hasNext = true;
		currentKeyIndex = this.keys.length - 1;
		currentWordListIndex = 0;
		
		for(List<String> w : words.values())
			Collections.shuffle(w, RANDOM);
		
		currentList = this.words.get(this.keys[this.currentKeyIndex]);
	}
	
	@Override
	public boolean hasNext() {
		return hasNext;
	}
	
	@Override
	public String next() {
		if(currentList == null)
			return null;
		
		try {
			return currentList.get(currentWordListIndex);
		} finally {
			currentWordListIndex++;
			
			if(currentWordListIndex >= currentList.size()) {
				currentKeyIndex--;
				currentWordListIndex = 0;
				
				
				if(currentKeyIndex < 0) {
					hasNext = false;
					currentList = null;
				} else {
					currentList = this.words.get(this.keys[this.currentKeyIndex]);
				}
			}
		}
	}
	
	@Override
	public String getRandomWord() {
		int entry = RANDOM.nextInt(keys.length);
		List<String> wordsEntry = words.get(keys[entry]);
		
		return wordsEntry.get(RANDOM.nextInt(wordsEntry.size()));
	}
}
