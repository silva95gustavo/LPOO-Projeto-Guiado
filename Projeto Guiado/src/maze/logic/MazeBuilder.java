package maze.logic;

import java.util.Random;

public class MazeBuilder {
	public static final int MIN_REC_SIDE = 8;
	private static final char paredeChar = 'X';
	private static final char saidaChar = 'S';
	
	public int matrixSide;
	public char[][] matrix;
	public int saidaX, saidaY;
	
	public MazeBuilder(int side)
	{
		matrix = new char[side][side];
	}
	
	public Maze build() {
		generateMatrix();
		return new Maze(matrix, saidaX, saidaY);
	}
	
	private void generateMatrix()
	{
		matrix = new char[matrixSide][matrixSide];
		fillMatrix(paredeChar);
		
		generateMapExit();
		
		int start_x, start_y;
		
		if (saidaX == 0)
		{
			start_x = 1;
			start_y = saidaY;
		}
		else if (saidaY == 0)
		{
			start_x = saidaX;
			start_y = 1;
		}
		else if (saidaX == matrixSide - 1)
		{
			start_x = matrixSide - ((matrixSide % 2 == 0) ? 3 : 2);
			start_y = saidaY;
		}
		else
		{
			start_x = saidaX;
			start_y = matrixSide - ((matrixSide % 2 == 0) ? 3 : 2);
		}
		matrix[start_y][start_x] = ' ';
		
		generateMapWalls(start_x, start_y);

		generateMapFixEven();
	}

	private void fillMatrix(char fill) {
		for (int y = 0; y < matrixSide; y++) {
			for (int x = 0; x < matrixSide; x++) {
				matrix[x][y] = fill;
			}
		}
	}

	private void generateMapExit() {
		Random r = new Random();
		int n = 2 * r.nextInt(matrixSide / 2 - 2) + 1;
		switch(r.nextInt(4))
		{
		case 0:
			saidaY = 0;
			saidaX = n;
			break;
		case 1:
			saidaY = matrixSide - 1;
			saidaX = n;
			break;
		case 2:
			saidaX = 0;
			saidaY = n;
			break;
		case 3:
			saidaX = matrixSide - 1;
			saidaY = n;
			break;
		}
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
		if (new_x > 0 && new_x < matrixSide - 1 && new_y > 0 && new_y < matrixSide - 1 && matrix[new_y][new_x] == paredeChar)
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
		if (matrixSide % 2 == 0)
		{
			Random r = new Random();
			int n;

			// Duplicate column
			n = r.nextInt(matrixSide - 4) + 2;
			for (int x = matrixSide - 2; x > n; x--) {
				for (int y = 0; y < matrixSide; y++) {
					matrix[y][x] = matrix[y][x - 1];
				}
			}
			for (int y = 0; y < matrixSide; y++) {
				if (matrix[y][n - 1] != matrix[y][n + 1])
					matrix[y][n] = paredeChar;
			}

			// Correct exit position
			if (saidaX < matrixSide - 1 && n <= saidaX)
				saidaX++;

			// Duplicate line
			n = r.nextInt(matrixSide - 4) + 2;
			for (int y = matrixSide - 2; y > n; y--) {
				for (int x = 0; x < matrixSide; x++) {
					matrix[y][x] = matrix[y - 1][x];
				}
			}
			for (int x = 0; x < matrixSide; x++) {
				if (matrix[n - 1][x] != matrix[n + 1][x])
					matrix[n][x] = paredeChar;
			}
			// Correct exit position
			if (saidaY < matrixSide - 1 && n <= saidaY)
				saidaY++;
		}
	}
}
