package maze.logic;

import java.util.Random;

public class MazeBuilder {	
	public static final int MIN_REC_SIDE = 8;
	
	public char[][] matrix;
	public Exit exit;
	
	public MazeBuilder(int side)
	{
		matrix = new char[side][side];
	}
	
	public Maze build() {
		generateMatrix();
		return new Maze(matrix, exit);
	}
	
	private void generateMatrix()
	{
		fillMatrix(Maze.wallChar);
		
		generateMapExit();
		
		int start_x, start_y;
		
		if (exit.getX() == 0)
		{
			start_x = 1;
			start_y = exit.getY();
		}
		else if (exit.getY() == 0)
		{
			start_x = exit.getX();
			start_y = 1;
		}
		else if (exit.getX() == matrix.length - 1)
		{
			start_x = matrix.length - ((matrix.length % 2 == 0) ? 3 : 2);
			start_y = exit.getY();
		}
		else
		{
			start_x = exit.getX();
			start_y = matrix.length - ((matrix.length % 2 == 0) ? 3 : 2);
		}
		matrix[start_y][start_x] = ' ';
		
		generateMapWalls(start_x, start_y);

		generateMapFixEven();
		
		removeSomePaths();
	}

	private void fillMatrix(char fill) {
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix.length; x++) {
				matrix[x][y] = fill;
			}
		}
	}

	private void generateMapExit() {
		Random r = new Random();
		int n = 2 * r.nextInt(matrix.length / 2 - 2) + 1;
		int x = 0, y = 0;
		switch(r.nextInt(4))
		{
		case 0:
			y = 0;
			x = n;
			break;
		case 1:
			y = matrix.length - 1;
			x = n;
			break;
		case 2:
			x = 0;
			y = n;
			break;
		case 3:
			x = matrix.length - 1;
			y = n;
			break;
		}
		exit = new Exit(x, y);
	}
	
	private void generateMapWalls(int x, int y) {
		int[] directions = {0, 1, 2, 3};
		directions = randomizeArray(directions);
		for (int i = 0; i < directions.length; i++) {
			switch (directions[i]) {
			case 0: // cima
				if (removeMapWall(x, y, x, y - 2))
					generateMapWalls(x, y - 2);
				break;
			case 1: // baixo
				if (removeMapWall(x, y, x, y + 2))
					generateMapWalls(x, y + 2);
				break;
			case 2: // esquerda
				if (removeMapWall(x, y, x - 2, y))
					generateMapWalls(x - 2, y);
				break;
			case 3: // direita
				if (removeMapWall(x, y, x + 2, y))
					generateMapWalls(x + 2, y);
				break;
			}
		}
	}
	
	private boolean removeMapWall(int x, int y, int new_x, int new_y) {
		if (new_x > 0 && new_x < matrix.length - 1 && new_y > 0 && new_y < matrix.length - 1 && matrix[new_y][new_x] == Maze.wallChar)
		{
			matrix[new_y][new_x] = ' ';
			matrix[(new_y + y) / 2][(new_x + x) / 2] = ' ';
			return true;
		}
		return false;
	}
	
	private static int[] randomizeArray(int[] array) {
		Random r = new Random();
		for (int i = 0; i < array.length; i++) {
			int n = r.nextInt(array.length);
			int val = array[i];
			array[i] = array[n];
			array[n] = val;
		}
		return array;
	}
	
	private void generateMapFixEven() {
		// Fix for even side
		if (matrix.length % 2 == 0)
		{
			Random r = new Random();
			int n;

			// Duplicate column
			n = r.nextInt(matrix.length - 4) + 2;
			for (int x = matrix.length - 2; x > n; x--) {
				for (int y = 0; y < matrix.length; y++) {
					matrix[y][x] = matrix[y][x - 1];
				}
			}
			for (int y = 0; y < matrix.length; y++) {
				if (matrix[y][n - 1] != matrix[y][n + 1])
					matrix[y][n] = Maze.wallChar;
			}

			// Correct exit position
			if (exit.getX() < matrix.length - 1 && n <= exit.getX())
				exit.setX(exit.getX() + 1);

			// Duplicate line
			n = r.nextInt(matrix.length - 4) + 2;
			for (int y = matrix.length - 2; y > n; y--) {
				for (int x = 0; x < matrix.length; x++) {
					matrix[y][x] = matrix[y - 1][x];
				}
			}
			for (int x = 0; x < matrix.length; x++) {
				if (matrix[n - 1][x] != matrix[n + 1][x])
					matrix[n][x] = Maze.wallChar;
			}
			// Correct exit position
			if (exit.getY() < matrix.length - 1 && n <= exit.getY())
				exit.setY(exit.getY() + 1);
		}
	}
	
	private void removeSomePaths()
	{
		Random rand = new Random();
		int x, y;
		int attempts = 20;
		
		while(attempts-- > 0)
		{
			x = rand.nextInt(matrix.length-2) + 1;
			y = rand.nextInt(matrix.length-2) + 1;
			
			if(isValidForRemoval(x, y))
				matrix[y][x] = ' ';
		}
	}
	
	private boolean isValidForRemoval(int x, int y)
	{
		if(matrix[y][x] == Maze.wallChar)
		{
			if(matrix[y+1][x] == Maze.wallChar && matrix[y-1][x] == Maze.wallChar && matrix[y][x-1] == ' ' && matrix[y][x+1] == ' ')
				return true;
			
			if(matrix[y+1][x] == ' ' && matrix[y-1][x] == ' ' && matrix[y][x-1] == Maze.wallChar && matrix[y][x+1] == Maze.wallChar)
				return true;
		}
		
		
		return false;
	}
	
}
