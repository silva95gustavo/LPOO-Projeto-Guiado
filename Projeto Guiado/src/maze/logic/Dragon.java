package maze.logic;

public class Dragon extends Element{
	private boolean alive = true;
	private boolean sleeping = false;
	
	public Dragon(int x, int y) {
		super(x, y);
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

}
