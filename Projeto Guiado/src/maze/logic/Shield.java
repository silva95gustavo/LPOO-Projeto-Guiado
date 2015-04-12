package maze.logic;

@SuppressWarnings("serial")
public class Shield extends Element{
	private boolean dropped = true;

	public Shield(int x, int y) {
		super(x, y);
	}
	
	/**
	 * 
	 * @return true if the Shield is dropped, false if it has been caught by the hero
	 */
	public boolean isDropped() {
		return dropped;
	}
	
	/**
	 * Changes the dropped state of the Shield.
	 * @param dropped new dropped state
	 */
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
