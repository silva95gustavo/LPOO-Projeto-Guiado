package maze.logic;

public class Sword extends Element{
	private boolean dropped = true;
	
	public Sword(int x, int y) {
		super(x, y);
	}
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
