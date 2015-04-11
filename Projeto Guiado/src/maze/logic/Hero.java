package maze.logic;

@SuppressWarnings("serial")
public class Hero extends Element{
	private boolean alive = true;
	private boolean armed = false;
	private boolean shielded = false;
	private int darts = 0;
	
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

	public int getDarts() {
		return darts;
	}

	public void setDarts(int darts) {
		this.darts = darts;
	}
	
	public boolean hasDarts()
	{
		return darts > 0;
	}

	public void catchDart()
	{
		darts++;
	}

	public void fireDart()
	{
		darts--;
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}
	
	public void catchShield()
	{
		shielded = true;
	}
}
