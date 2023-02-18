package nl.jrwer.challenges.wordsearch.creater.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigToSmallWordList extends AbstractPreloadedWordList {
	private final Map<Integer, List<String>> words = new HashMap<>();
	private final int[] keys;
	
	private int currentKeyIndex;
	private int currentWordListIndex = 0;
	private boolean hasNext = false;
	private List<String> currentList;
	
	public BigToSmallWordList(Language language) {
		this(2, Integer.MAX_VALUE, language);
	}
	
	public BigToSmallWordList(int minWordLength, int maxWordLength, Language language) {
		super(minWordLength, maxWordLength, language);
		
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

	@Override
	protected void addWord(String word) {
		int wordLength = word.length();
		
		if(words.containsKey(wordLength)) {
			words.get(wordLength).add(word.toUpperCase());
		} else {
			List<String> l = new ArrayList<>();
			l.add(word.toUpperCase());
			
			words.put(wordLength, l);
		}
	}
}
