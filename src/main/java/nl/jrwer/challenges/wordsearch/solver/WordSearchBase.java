package nl.jrwer.challenges.wordsearch.solver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * https://ptolemypress.com/making-the-worlds-largest-word-search-puzzle/
 * 
 * https://mike42.me/words/
 * @author jrwer
 *
 */
public abstract class WordSearchBase {
	public static final String FILE_GRID = "grid3.txt";
	public static final String FILE_WORDS = "words3.txt";
//	public static final String FILE_GRID = "grid2.txt";
//	public static final String FILE_WORDS = "words2.txt";
//	public static final String FILE_GRID = "grid_big.txt";
//	public static final String FILE_WORDS = "words_big.txt";
	
	public List<String> load(String file) throws Exception {
		List<String> lines = new ArrayList<>();
		
		try (InputStream is = WordSearchBase.class.getClassLoader().getResourceAsStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			
			while ((line = br.readLine()) != null)
				lines.add(line.replace(" ", "").replace("\t", ""));
		}

		return lines;
	}
	
	public abstract void prepare() throws Exception;
	public abstract String execute();
}
