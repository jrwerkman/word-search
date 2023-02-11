package nl.jrwer.challenges.wordsearch.creater;

public class WordSearchCreater {
	/**
	 * https://github.com/OpenTaal/opentaal-wordlist 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			
			Puzzle puzzle = new Puzzle(20, 20, "Novadoc");
			puzzle.create("grid.txt", "words.txt");
			
			long end = System.currentTimeMillis();

			System.out.println(puzzle);
			System.out.println("Time: " + (end - start));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
