package nl.jrwer.challenges.wordsearch.creater.words;

public abstract class AbstractWordList implements IWordList {
	protected final int minimumWordLength;
	protected final int maximumWordLength;
	
	protected AbstractWordList(int min, int max) {
		this.minimumWordLength = min;
		this.maximumWordLength = max;
	}
}
