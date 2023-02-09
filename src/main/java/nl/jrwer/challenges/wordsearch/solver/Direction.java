package nl.jrwer.challenges.wordsearch.solver;

public enum Direction {
	RIGHT(1, 0),
	LEFT(-1, 0),
	BOTTOM(0, 1),
	TOP(0, -1),
	LEFT_TOP(-1, -1),
	LEFT_BOTTOM(-1, 1),
	RIGHT_TOP(1, -1),
	RIGHT_BOTTOM(1, 1);
	
	final int hor, ver;
	
	private Direction(int hor, int ver) {
		this.hor = hor;
		this.ver = ver;
	}
}
