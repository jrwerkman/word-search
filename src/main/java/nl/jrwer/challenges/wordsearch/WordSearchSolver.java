package nl.jrwer.challenges.wordsearch;

import nl.jrwer.challenges.wordsearch.solver.WordSearch2dArray;
import nl.jrwer.challenges.wordsearch.solver.WordSearchArray;
import nl.jrwer.challenges.wordsearch.solver.WordSearchBase;
import nl.jrwer.challenges.wordsearch.solver.WordSearchLetterMap;
import nl.jrwer.challenges.wordsearch.solver.WordSearchObject;

public class WordSearchSolver {

	public static void main(String[] args) {
		// big --> 1200
//		execute(System.nanoTime(), new WordSearchArray());
		
		// big --> 1500
//		execute(System.nanoTime(), new WordSearch2dArray());
		
		// big --> much slower
//		execute(System.nanoTime(), new WordSearchObject());
		
		// big --> 400
		execute(System.nanoTime(), new WordSearchLetterMap());
	}
	
	public static void all() {
		WordSearchBase[] searchers = new WordSearchBase[] {
				new WordSearchObject(),
				new WordSearchLetterMap(),
				new WordSearch2dArray(),
				new WordSearchArray(),
		};
		
		
		for(WordSearchBase s : searchers)
			execute(System.nanoTime(), s);
		
	}
	
	public static void execute(long start, WordSearchBase w) {
		long afterLoading = 0L;
		String unused = null;

		try {
			w.prepare();
			afterLoading = System.nanoTime();
			unused = w.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.nanoTime();
		
		StringBuilder sb = new StringBuilder();
		sb.append(w.getClass().getSimpleName()).append('\n');
		sb.append("Words found: ").append(w.wordsFound).append('\n');
		sb.append("Unused letters: ").append(unused.length() > 100 ? unused.length() : unused.toString()).append('\n');
		sb.append("Time loading data: ").append(afterLoading - start).append(" nanoseconds").append('\n');
		sb.append("Time processing data: ").append(end - afterLoading).append(" nanoseconds").append('\n');
		sb.append("Time processing data: ").append((end - afterLoading) / 1000000).append(" milisecond").append('\n');
		sb.append("Total time: ").append(end - start).append(" nanoseconds").append('\n').append('\n');
		
		System.out.println(sb);
	}
}
