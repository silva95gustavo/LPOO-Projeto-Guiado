package maze.test;

import static org.junit.Assert.*;

import java.util.Random;

import maze.logic.DefaultMaze;
import maze.logic.Dragon;
import maze.logic.Exit;
import maze.logic.Game;
import maze.logic.GameData;
import maze.logic.Maze;
import maze.logic.MazeBuilder;

import org.junit.Test;

public class TestMapGeneration {

	@Test
	public void testMultipleDragons() {
		Game tGame = new Game(20, 5, Dragon.Dragon_mode.DGN_RAND_SLP);

		GameData data = tGame.getGameData();

		assertEquals(5.0, data.getDragons().length, 0.0);
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[0].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[1].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[2].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[3].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[4].getMode());

	}
	
	@Test
	public void testMapSize() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_RAND_SLP, true);
		GameData data = tGame.getGameData();
		
		assertEquals(data.getMap().getSide(), DefaultMaze.defaultMatrix.length);
		
		tGame = new Game(18);
		data = tGame.getGameData();
		assertEquals(data.getMap().getSide(), 18);
	}
	
	// a) the maze boundaries must have exactly one exit and everything else walls
		// b) the exist cannot be a corner
		private boolean checkBoundaries(Maze m) {
			int countExit = 0;
			int n = m.getSide();
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					if (i == 0 || j == 0 || i == n - 1 || j == n - 1)
						if (m.isExit(i, j))
							if ((i == 0 || i == n-1) && (j == 0 || j == n-1))
								return false;
							else
								countExit++;
						else if (!m.isWall(i, j))
							return false;
			return countExit == 1;
		}


		// d) there cannot exist 2x2 (or greater) squares with blanks only 
		// e) there cannot exit 2x2 (or greater) squares with blanks in one diagonal and walls in the other
		// d) there cannot exist 3x3 (or greater) squares with walls only
		private boolean hasSquare(Maze maze, char[][] square) {
			char [][] m = maze.getMatrix();
			for (int i = 0; i < m.length - square.length; i++)
				for (int j = 0; j < m.length - square.length; j++) {
					boolean match = true;
					for (int x = 0; x < square.length; x++)
						for (int y = 0; y < square.length; y++) {
							if (m[i+x][j+y] != square[x][y])
								match = false;
						}
					if (match)
						return true;
				}		
			return false; 
		}

		// c) there must exist a path between any blank cell and the maze exit 
		private boolean checkExitReachable(Maze maze) {
			Exit e = maze.getExit();
			char [][] m = deepClone(maze.getMatrix());
			visit(m, e.getY()-1, e.getX());
			visit(m, e.getY()+1, e.getX());
			visit(m, e.getY(), e.getX()-1);
			visit(m, e.getY(), e.getX()+1);
			
			for (int i = 0; i < m.length; i++)
				for (int j = 0; j < m.length; j++)
					if (m[i][j] != 'X' && m[i][j] != 'V')
					{
						return false;
					}
			
			return true; 
		}
		
		// auxiliary method used by checkExitReachable
		// marks a cell as visited (V) and proceeds recursively to its neighbors
		private void visit(char[][] m, int i, int j) {
			if (i < 0 || i >= m.length || j < 0 || j >= m.length)
				return;
			if (m[i][j] == 'X' || m[i][j] == 'V')
				return;
			m[i][j] = 'V';
			visit(m, i-1, j);
			visit(m, i+1, j);
			visit(m, i, j-1);
			visit(m, i, j+1);
		}

		// Auxiliary method used by checkExitReachable.
		// Gets a deep clone of a char matrix.
		private char[][] deepClone(char[][] m) {
			char[][] c = m.clone();
			for (int i = 0; i < m.length; i++)
				c[i] = m[i].clone();
			return c;
		}

		// Checks if all the arguments (in the variable arguments list) are not null and distinct
		private <T> boolean notNullAndDistinct(T ... args) {
			for (int i = 0; i < args.length - 1; i++)
				for (int j = i + 1; j < args.length ; j++)
					if (args[i] == null || args[j] == null || args[i].equals(args[j]))
						return false;
			return true;
		}
				
		@Test
		public void testRandomMazeGenerator() throws Exception {
			int numMazes = 1000;
			int maxSize = 101; // can change to any odd number >= 8
			
			char[][] badWalls = {
					{'X', 'X', 'X'},
					{'X', 'X', 'X'},
					{'X', 'X', 'X'}};
			char[][] badSpaces = {
					{' ', ' '},
					{' ', ' '}};
			char[][] badDiag1 = {
					{'X', ' '},
					{' ', 'X'}};
			char[][] badDiag2 = {
					{' ', 'X'},
					{'X', ' '}};
			Random rand = new Random(); 
			for (int i = 0; i < numMazes; i++) {
				int size = maxSize == 8? 8 : 8 + 2 * rand.nextInt((maxSize - 8)/2);
				Maze m = new MazeBuilder(size).build();
				assertTrue("Invalid maze boundaries in maze:\n" + m, checkBoundaries(m));			
				assertTrue("Maze exit not reachable in maze:\n" + m, checkExitReachable(m));			
				assertNotNull("Invalid walls in maze:\n" + m, ! hasSquare(m, badWalls));
				assertNotNull("Invalid spaces in maze:\n" + m, ! hasSquare(m, badSpaces));
				assertNotNull("Invalid diagonals in maze:\n" + m, ! hasSquare(m, badDiag1));
				assertNotNull("Invalid diagonals in maze:\n" + m, ! hasSquare(m, badDiag2));	
			}	
		}
}
