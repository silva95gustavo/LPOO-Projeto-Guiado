package maze.logic;

public class DefaultMaze extends Maze {
	private static final char[][] defaultMatrix = {
		{ 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' },
		{ 'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', ' ', ' ', ' ', ' ', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', 'X', ' ', 'X', ' ', 'X' },
		{ 'X', ' ', 'X', 'X', ' ', ' ', ' ', ' ', ' ', 'X' },
		{ 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' }
	};
	public DefaultMaze() {
		super(defaultMatrix, new Exit(9, 5));
	}
}
