package maze.logic;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Maze implements Serializable {
	/**
	 * Character that represents a wall in the internal matrix
	 */
	public static final char wallChar = 'X';
	
	private char[][] matrix;
	private Exit exit;
	
	/**
	 * Constructor
	 * @param matrix char array containing the walls and the empty paths
	 * @param exit {@link Exit}
	 */
	public Maze(char[][] matrix, Exit exit) {
		this.matrix = matrix;
		this.exit = exit;
	}

	/**
	 * 
	 * @return char array containing the walls and the empty paths
	 */
	public char[][] getMatrix() {
		return matrix;
	}
	
	/**
	 * 
	 * @return size of the maze
	 */
	public int getSide() {
		return matrix.length;
	}
	
	/**
	 * 
	 * @return maze's {@link Exit}
	 */
	public Exit getExit() {
		return exit;
	}
	
	/**
	 * Updates the {@link Exit} visibility
	 * @param visible true if visible, false if invisible
	 */
	public void setExitVisible(boolean visible) {
		exit.setVisible(visible);
	}
	
	/**
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if it's an empty cell, false if it's a wall
	 */
	public boolean isEmptyCell(int x, int y)
	{
		return matrixCoords(x, y) == ' ';
	}
	
	/**
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the char corresponding to the passed coordinates
	 */
	public char matrixCoords(int x, int y)
	{
		return matrix[y][x];
	}

	/**
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if it's a wall, false otherwise
	 */
	public boolean isWall(int x, int y)
	{
		return matrixCoords(x, y) == wallChar;
	}
	
	/**
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if it's an exit, false otherwise
	 */
	public boolean isExit(int x, int y)
	{
		return x == exit.getX() && y == exit.getY();
	}
	
	/**
	 * Calculates the squared distance between two coordinates
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the squared distance between the two coordinates
	 */
	public static int coordDistSquare(int x1, int y1, int x2, int y2)
	{
		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return true if coordinates 1 and 2 are next to each other, false otherwise
	 */
	public static boolean areAdjacent(int x1, int y1, int x2, int y2)
	{
		if(x1 == x2)
		{
			if(Math.abs(y2-y1)==1)
				return true;
		}
		else if(y1 == y2)
		{
			if(Math.abs(x2-x1)==1)
				return true;
		}

		return false;
	}

}
