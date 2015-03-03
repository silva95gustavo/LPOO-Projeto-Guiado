package maze.logic;

public class Hero extends Element{
	private boolean alive = true;
	
	public Hero(int x, int y) {
		super(x, y);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
