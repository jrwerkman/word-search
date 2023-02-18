package nl.jrwer.challenges.wordsearch.creater.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomWordList extends AbstractPreloadedWordList {
	private final List<String> words = new ArrayList<>();	
	private int count = 0;

	public RandomWordList(Language language) {
		this(2, Integer.MAX_VALUE, language);
	}
	
	public RandomWordList(int minWordLength, int maxWordLength, Language language) {
		super(minWordLength, maxWordLength, language);
		loadWords();
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

	@Override
	protected void addWord(String word) {
		words.add(word.toUpperCase());
	}

}
