package maze.cli;

import java.util.Arrays;
import java.util.Scanner;

import maze.logic.Game;
import maze.logic.GameData;
import maze.logic.MazeBuilder;

public class AlphanumericInterface {
	private static final char dragao_char = 'D';			// Símbolo representativo do dragão
	private static final char espada_char = 'E';			// Símbolo da espada
	private static final char dragao_espada_char = 'F';		// Símbolo a representar quando dragão e espada estão coincidentes
	private static final char heroi_char = 'H';				// Símbolo do herói sem espada
	private static final char heroi_armado_char = 'A';		// Símbolo do herói com espada
	private static final char wall_char = 'X';
	private static final char exit_char = 'S';
	
	private Scanner s;
	
	public void start()
	{
		s = new Scanner(System.in);
		Game game = createGame();
		
		do{
			drawMap(game.getGameData());
		} while(game.turn(s.next()));
		s.close();
	}

	private Game createGame()
	{
		System.out.print("For this game, you can choose the dimmensions of the game map. The minimum size is 8,\nsince smaller sizes would make the game impossible to finish.\n");
		System.out.print("Please indicate the map size (minimum " + MazeBuilder.MIN_REC_SIDE + ") : ");
		int map_side = s.nextInt();

		while(map_side < MazeBuilder.MIN_REC_SIDE)
		{
			System.out.print("\nGiven value is less than " + MazeBuilder.MIN_REC_SIDE + ".\nPlease insert new value : ");
			map_side = s.nextInt();
		}
		
		return new Game(map_side);
	}

	private void drawMap(GameData gameData)
	{
		int[] coords = new int[8];
		Arrays.fill(coords, -1);
		char[] chars = new char[4];
		Arrays.fill(chars, ' ');
		
		
		coords[0] = gameData.getHero().getX();
		coords[1] = gameData.getHero().getY();
		if(gameData.getHero().isArmed())
			chars[0] = heroi_armado_char;
		else
			chars[0] = heroi_char;
		
		int i = 1;
		
		// TODO: Suporte multi-dragões
		if(gameData.getSword().getX() == gameData.getDragons()[0].getX() && gameData.getSword().getY() == gameData.getDragons()[0].getY())
		{
			coords[2*i] = gameData.getSword().getX();
			coords[2*i+1] = gameData.getSword().getY();
			
			if(!gameData.getHero().isArmed())
				chars[i] = dragao_espada_char;
			else
				chars[i] = dragao_char;
			i++;
		}
		else
		{
			if(!gameData.getHero().isArmed())
			{
				coords[2*i] = gameData.getSword().getX();
				coords[2*i + 1] = gameData.getSword().getY();
				chars[i] = espada_char;
				i++;
			}
			
			if(gameData.getDragons()[0].isAlive())
			{
				coords[2*i] = gameData.getDragons()[0].getX();
				coords[2*i + 1] = gameData.getDragons()[0].getY();
				chars[i] = dragao_char;
				i++;
			}
		}
		coords[2 * i] = gameData.getMap().getExit().getX();
		coords[2 * i] = gameData.getMap().getExit().getY();
		if (gameData.getMap().getExit().isVisible())
			chars[i] = exit_char;
		else
			chars[i] = wall_char;
		drawMatrix(gameData.getMap().getMatrix(), coords, chars);
	}
	
	public void drawMatrix(char[][] matrix, int[] coords, char[] chars)
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
						printed = true;
						System.out.print(chars[coord]);
						break;
					}
				}
				
				if(!printed)
				{
					if (j == coords[6] && i == coords[7])
						System.out.print(chars[3]);
					else
						System.out.print(matrix[i][j]);						
				}
				
				printed = false;
				System.out.print(' ');
			}
			System.out.print('\n');
		}
	}
}
