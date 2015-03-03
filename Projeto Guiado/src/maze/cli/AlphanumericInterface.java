package maze.cli;

import java.util.Arrays;
import java.util.Scanner;

import maze.logic.Jogo;
import maze.logic.Labirinto;

public class AlphanumericInterface {
	private Scanner s;
	public void start()
	{
		s = new Scanner(System.in);
		Jogo jogo = createGame();
		
		do{
			jogo.drawMap();
		} while(jogo.turn(s.next()));
		s.close();
	}

	private Jogo createGame()
	{
		System.out.print("For this game, you can choose the dimmensions of the game map. The minimum size is 8,\nsince smaller sizes would make the game impossible to finish.\n");
		System.out.print("Please indicate the map size (minimum " + Labirinto.MIN_REC_SIDE + ") : ");
		int map_side = s.nextInt();

		while(map_side < Labirinto.MIN_REC_SIDE)
		{
			System.out.print("\nGiven value is less than " + Labirinto.MIN_REC_SIDE + ".\nPlease insert new value : ");
			map_side = s.nextInt();
		}
		
		return new Jogo(map_side);
	}

}
