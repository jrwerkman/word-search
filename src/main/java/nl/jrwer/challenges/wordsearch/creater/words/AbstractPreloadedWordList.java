package nl.jrwer.challenges.wordsearch.creater.words;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public abstract class AbstractPreloadedWordList extends AbstractWordList {
	/**
	 * https://github.com/dwyl/english-words
	 */
	private static final String WORD_LIST_EN = "words_alpha.txt";
	
	/**
	 * https://github.com/OpenTaal/opentaal-wordlist 
	 */
	private static final String WORD_LIST_NL = "words_nl.txt";	
	
	protected final Language language; 
	
	protected AbstractPreloadedWordList(int min, int max, Language language) {
		super(min, max);
		
		this.language = language;
	}
	
	protected String getLanguageFileName() {
		switch(language) {
		case EN:
			return WORD_LIST_EN;
		case NL:
			return WORD_LIST_NL;
		default:
			throw new RuntimeException("Unsupported language: " + language.name());
		}
	}
	
	protected void loadWords() {
        try (InputStream inputStream = FakeRandomWordList.class.getClassLoader().getResourceAsStream(getLanguageFileName());
        		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        	String line;
        	
            while ((line = reader.readLine()) != null) {
            	if(line.isBlank())
            		continue;
            	
            	// skip 1 letter words
            	if(line.length() > minimumWordLength && line.length() < maximumWordLength 
            			&& Pattern.matches("[\\dA-Za-z]+", line))
            		addWord(line);
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void addWord(String word);
	
}
