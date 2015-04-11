package maze.logic;

@SuppressWarnings("serial")
public class DroppableElement extends Element {
	protected boolean dropped = true;
	
	public DroppableElement(int x, int y) {
		super(x, y);
	}
	
	public DroppableElement(int x, int y, boolean dropped) {
		super(x, y);
		this.dropped = dropped;
	}

	/**
	 * 
	 * @return true if dropped, false otherwise
	 */
	public boolean isDropped() {
		return dropped;
	}
	
	/**
	 * Changes the dropped state.
	 * @param dropped new dropped state
	 */
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
