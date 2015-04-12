package maze.logic;

@SuppressWarnings("serial")
public class Exit extends Element{
	private boolean visible = false;
	
	public Exit(int x, int y) {
		super(x, y);
	}
	
	/**
	 * 
	 * @return whether the Exit is visible/open or not
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Changes Exit visibility
	 * @param visible new visibility
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
