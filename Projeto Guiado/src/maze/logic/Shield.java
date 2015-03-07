package maze.logic;

public class Shield extends Element{
	private boolean dropped = true;

	public Shield(int x, int y) {
		super(x, y);
	}
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
