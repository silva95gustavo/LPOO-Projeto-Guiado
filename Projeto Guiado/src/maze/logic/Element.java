package maze.logic;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Element implements Serializable {
	private int x, y;
	
	/**
	 * Constructor.
	 * @param x x coordinate of the element
	 * @param y y coordinate of the element
	 */
	public Element(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return x coordinate of the element
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 
	 * @return y coordinate of the element
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Changes the element's position.
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Changes the x coordinate of the element.
	 * @param x new x coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Changes the y coordinate of the element.
	 * @param y new y coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
}
