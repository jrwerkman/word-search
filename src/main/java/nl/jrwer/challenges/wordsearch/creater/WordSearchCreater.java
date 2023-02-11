package nl.jrwer.challenges.wordsearch.creater;

public class WordSearchCreater {
	public static void main(String[] args) {
		try {
			Puzzle puzzle = new Puzzle(10, 10, "Novadoc");
			puzzle.create();
			System.out.println(puzzle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
