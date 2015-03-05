package maze.logic;

public class Dragon extends Element{
	private boolean alive = true;
	private boolean sleeping = false;
	private int mode = 0;		// 0 - Stopped, 1 - Moving random, 2 - Moving & sleeping
	
	public static final int DRAGON_STOP_MODE = 0;
	public static final int DRAGON_MOVE_MODE = 0;
	public static final int DRAGON_MV_SLP_MODE = 0;
	
	public Dragon() {
		super(0, 0);
	}
	
	public Dragon(int x, int y) {
		super(x, y);
	}
	
	public Dragon(int x, int y, int mode) {
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

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setCoords(int x, int y)
	{
		super.setX(x);
		super.setY(y);
	}

}
