package maze.logic;

public class Dragon extends Element{
	private boolean alive = true;
	private boolean sleeping = false;
	public static enum Dragon_mode {DGN_STILL, DGN_RAND, DGN_RAND_SLP};
	private Dragon_mode mode = Dragon_mode.DGN_STILL;
	
	public Dragon() {
		super(0, 0);
	}
	
	public Dragon(int x, int y) {
		super(x, y);
	}
	
	public Dragon(int x, int y, Dragon_mode mode) {
		super(x, y);
		this.mode = mode;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	public Dragon_mode getMode() {
		return mode;
	}

	public void setMode(Dragon_mode mode) {
		this.mode = mode;
	}
	
	public void setCoords(int x, int y)
	{
		super.setX(x);
		super.setY(y);
	}

}
