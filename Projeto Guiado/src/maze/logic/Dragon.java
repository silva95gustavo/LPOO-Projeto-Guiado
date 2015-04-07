package maze.logic;

@SuppressWarnings("serial")
public class Dragon extends Element{
	private boolean alive = true;
	private boolean sleeping = false;
	private boolean canFire = true;
	
	/**
	 * Dragon modes
	 */
	public static enum Dragon_mode {
		/**
		 * Not moving
		 */
		DGN_STILL,

		/**
		 * Random movement
		 */
		DGN_RAND,

		/**
		 * Random movement mixed with sleeping
		 */
		DGN_RAND_SLP,

		/**
		 * Sleeping
		 */
		DGN_SLP
	};
	
	private Dragon_mode mode = Dragon_mode.DGN_STILL;

	public Dragon() {
		super(1, 3);
	}
	
	public Dragon(int x, int y) {
		super(x, y);
	}
	
	public Dragon(int x, int y, Dragon_mode mode) {
		super(x, y);
		this.mode = mode;
		switch(mode)
		{
		case DGN_SLP:
			sleeping = true;
			break;
		default:
			break;
		}
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

	public boolean canFire() {
		return canFire;
	}

	public void setFireAbility(boolean canFire) {
		this.canFire = canFire;
	}

}
