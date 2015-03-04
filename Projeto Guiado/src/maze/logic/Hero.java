package maze.logic;

public class Hero extends Element{
	private boolean alive = true;
	private boolean armed = false;
	
	public Hero(int x, int y) {
		super(x, y);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isArmed() {
		return armed;
	}

	public void setArmed(boolean armed) {
		this.armed = armed;
	}
}
