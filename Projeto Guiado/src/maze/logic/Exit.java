package maze.logic;

@SuppressWarnings("serial")
public class Exit extends Element{
	private boolean visible = false;
	
	public Exit(int x, int y) {
		super(x, y);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
