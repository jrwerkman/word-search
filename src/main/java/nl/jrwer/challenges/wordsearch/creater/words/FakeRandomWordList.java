package nl.jrwer.challenges.wordsearch.creater.words;

import nl.jrwer.challenges.wordsearch.creater.Puzzle;

// TODO load words from big to small
public class FakeRandomWordList extends AbstractWordList {
	private final int max;
	private int count = 0;
	
	public FakeRandomWordList(int minWordLength, int maxWordLength) {
		this(minWordLength, maxWordLength, Integer.MAX_VALUE);
	}
	
	public FakeRandomWordList(int minWordLength, int maxWordLength, int maxGeneratedWords) {
		super(minWordLength, maxWordLength);
		
		this.max = maxGeneratedWords;
	}
	
	@Override
	public void randomize() {
		count = 0;
	}
	
	@Override
	public boolean hasNext() {
		return count < max;
	}
	
	@Override
	public String next() {
		try {
			return getRandomWord();
		} finally {
			count++;
		}
	}
	
	@Override
	public String getRandomWord() {
		return generateWord();
	}
	
	private String generateWord() {
		int wordLength = RANDOM.nextInt(maximumWordLength - minimumWordLength) + minimumWordLength;
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<wordLength; i++)
			sb.append(Puzzle.getRandomChar());
		
		
		return sb.toString();
	}
}
