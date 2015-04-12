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
	 * @return whether the dragon is alive or not
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * Sets the dragon as dead or alive
	 * @param alive true if alive, false otherwise
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * 
	 * @return whether the dragon is sleeping or not
	 */
	public boolean isSleeping() {
		return sleeping;
	}

	/**
	 * Sets the dragon as sleeping or awake
	 * @param sleeping true if sleeping, false otherwise
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
	 * Updates current {@link Dragon_mode}
	 * @param mode new {@link Dragon_mode}
	 */
	public void setMode(Dragon_mode mode) {
		this.mode = mode;
	}
	
	/**
	 * Updates dragon's position
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void setCoords(int x, int y)
	{
		super.setX(x);
		super.setY(y);
	}

	/**
	 * 
	 * @return true if the dragon is able to throw fire, false otherwise
	 */
	public boolean canFire() {
		return canFire;
	}

	/**
	 * Makes the dragon able to throw fire or not
	 * @param canFire true if able to throw fire, false otherwise
	 */
	public void setFireAbility(boolean canFire) {
		this.canFire = canFire;
	}

}
