package nl.jrwer.challenges.wordsearch.creater.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordList extends AbstractWordList {
	private final List<String> words = new ArrayList<>();	
	private int count = 0;

	public WordList(List<String> words) {
		super(2, Integer.MAX_VALUE);
		
		this.words.addAll(words);
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
			return words.get(count).toUpperCase();
		} finally {
			count++;
		}
	}

	@Override
	public String getRandomWord() {
		return words.get(RANDOM.nextInt(words.size())).toUpperCase();
	}

}
