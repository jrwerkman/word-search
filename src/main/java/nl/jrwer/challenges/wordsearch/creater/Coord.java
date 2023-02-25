package nl.jrwer.challenges.wordsearch.creater;

import nl.jrwer.challenges.wordsearch.solver.Direction;

class Coord {
	final int x,y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord(Coord c, Direction d) {
		this.x = c.x + d.hor;
		this.y = c.y + d.ver;
	}		
	
	@Override
	public int hashCode() {
		return x << 8 | y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coord) {
			Coord c = (Coord) obj;
			
			return x == c.x && y == c.y;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return x + "," + y;
	}
}