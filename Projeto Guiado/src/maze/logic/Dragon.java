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

	/**
	 * 
	 * @return true if the Dragon is alive, false otherwise
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets the Dragon as dead or alive.
	 * @param alive true to set as alive, false to set as dead
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * @return true if the Dragon is sleeping, false otherwise
	 */
	public boolean isSleeping() {
		return sleeping;
	}

	/**
	 * Sets the Dragon as sleeping or not.
	 * @param sleeping true to set as sleeping, false otherwise
	 */
	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}

	/**
	 * 
	 * @return current {@link Dragon_mode}
	 */
	public Dragon_mode getMode() {
		return mode;
	}

	/**
	 * Changes the current {@link Dragon_mode}.
	 * @param mode new {@link Dragon_mode}
	 */
	public void setMode(Dragon_mode mode) {
		this.mode = mode;
	}
	
	/**
	 * Sets the Dragon's position
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void setCoords(int x, int y)
	{
		super.setX(x);
		super.setY(y);
	}

	/**
	 * @return whether the Dragon can throw fire to the hero or not.
	 */
	public boolean canFire() {
		return canFire;
	}

	/**
	 * Sets whether the Dragon can throw fire to the hero or not.
	 * @param canFire true means the Dragon is able to throw fire, false means it isn't
	 */
	public void setFireAbility(boolean canFire) {
		this.canFire = canFire;
	}

}
