package nl.jrwer.challenges.wordsearch.creater.words;

import java.util.Random;

public interface IWordList {
	static final Random RANDOM = new Random();
	
	void randomize();
	boolean hasNext();
	String next();
	String getRandomWord();
}
