package maze.logic;

public class Dart extends Element{
	private boolean dropped = true;

	public Dart(int x, int y) {
		super(x, y);
	}
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
