package nl.jrwer.challenges.wordsearch.creater;

public class WordSearchCreater {
	/**
	 * TODO
	 * - without sentence
	 * - with predefined list
	 * 		- fill unused with random letters 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Puzzle puzzle = new Puzzle(100, 100, "Novadoc");
			puzzle.create();
			System.out.println(puzzle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
