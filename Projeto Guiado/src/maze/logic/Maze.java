package maze.logic;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Maze implements Serializable {
	public static final char wallChar = 'X';
	public static final char exitChar = 'S';
	
	private char[][] matrix;
	private Exit exit;
	
	public Maze(char[][] matrix, Exit exit) {
		this.matrix = matrix;
		this.exit = exit;
	}

	public char[][] getMatrix() {
		return matrix;
	}
	
	public int getSide() {
		return matrix.length;
	}
	
	public Exit getExit() {
		return exit;
	}
	
	public void setExitVisible(boolean visible) {
		exit.setVisible(visible);
	}
	
	public boolean isEmptyCell(int x, int y)
	{
		return matrixCoords(x, y) == ' ';
	}
	
	public char matrixCoords(int x, int y)
	{
		return matrix[y][x];
	}

	public boolean isWall(int x, int y)
	{
		return matrixCoords(x, y) == wallChar;
	}
	
	public boolean isExit(int x, int y)
	{
		return x == exit.getX() && y == exit.getY();
	}
	
	public static int coordDistSquare(int x1, int y1, int x2, int y2)
	{
		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	}
	
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
