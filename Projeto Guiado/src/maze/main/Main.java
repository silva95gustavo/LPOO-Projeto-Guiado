package maze.main;

import java.util.Scanner;

import maze.cli.AlphanumericInterface;
import maze.gui.GameFrame;

public class Main {
	
	public static void main(String[] args) {
		
		System.out.println("     Welcome to the Maze Game!");
		System.out.print("  Please select a game interface (1 - Console, 2 - Graphic): ");
		
		Scanner s = new Scanner(System.in);
		
		int value = 0;
		while((value = s.nextInt()) != 1 && value != 2)
		{
			System.out.print("Please insert a valid value: ");
		}
		
		if(value == 1)
		{
			AlphanumericInterface cli = new AlphanumericInterface();
			cli.start();
		}
		else if (value == 2)
		{
			GameFrame.start(new String[0]);	
		}
		
		s.close();
	}
}
