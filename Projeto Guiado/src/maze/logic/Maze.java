package maze.logic;

public class Maze {
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
	
	public void drawMatrix(int[] coords, char[] chars)
	{
		boolean printed = false;
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix.length; j++)
			{
				for(int coord = 0; coord < chars.length; coord++)
				{
					if(j==coords[2*coord] && i == coords[2*coord + 1])
					{
						printed=true;
						System.out.print(chars[coord]);
						break;
					}
				}
				
				if(!printed)
				{
					if (j == exit.getX() && i == exit.getY() && exit.isVisible())
						System.out.print(exitChar);
					else
						System.out.print(matrix[i][j]);						
				}
				
				printed = false;
				System.out.print(' ');
			}
			System.out.print('\n');
		}
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
		return matrixCoords(x, y) == exitChar;
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
