package maze.logic;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {
	

	////////////////////////////////
	////////   Attributes   ////////
	////////////////////////////////
	
	private static final int ELEM_DIST_FACTOR = 2;			// Fator usado para determinar a dist. mínima entre elementos

	private static final char dragao_char = 'D';			// Símbolo representativo do dragão
	private static final char espada_char = 'E';			// Símbolo da espada
	private static final char dragao_espada_char = 'F';		// Símbolo a representar quando dragão e espada estão coincidentes
	private static final char heroi_char = 'H';				// Símbolo do herói sem espada
	private static final char heroi_armado_char = 'A';		// Símbolo do herói com espada

	private static int heroi_x, heroi_y;			// Coordenadas do herói
	private static int dragao_x, dragao_y;			// Coordenadas do dragão
	private static int espada_x, espada_y;			// Coordenadas da espada
	private static boolean heroi_armado;			// True se o heroi já está armado
	private static boolean dragao;					// True se o dragão está vivo

	private static Maze map;					// Represents the game map


	////////////////////////////////
	////////   Functions   /////////
	////////////////////////////////
	
	public Game(int side)
	{
		heroi_armado = false;
		dragao = true;

		int minElemDist = (int) (side/ELEM_DIST_FACTOR);
		minElemDist = minElemDist*minElemDist;
		map = new MazeBuilder(side).build();
		generateMapElements(minElemDist);
	}

	public void drawMap()
	{
		int[] coords = new int[6];
		Arrays.fill(coords, -1);
		char[] chars = new char[3];
		Arrays.fill(chars, ' ');
		
		
		coords[0] = heroi_x;
		coords[1] = heroi_y;
		if(heroi_armado)
			chars[0] = heroi_armado_char;
		else
			chars[0] = heroi_char;
		
		int i = 1;
		

		if(espada_x == dragao_x && espada_y == dragao_y)
		{
			coords[2*i] = espada_x;
			coords[2*i+1] = espada_y;
			
			if(!heroi_armado)
				chars[i] = dragao_espada_char;
			else
				chars[i] = dragao_char;
			i++;
		}
		else
		{
			if(!heroi_armado)
			{
				coords[2*i] = espada_x;
				coords[2*i + 1] = espada_y;
				chars[i] = espada_char;
				i++;
			}
			
			if(dragao)
			{
				coords[2*i] = dragao_x;
				coords[2*i + 1] = dragao_y;
				chars[i] = dragao_char;
				i++;
			}
		}
		map.drawMatrix(coords, chars);
	}

	public boolean turn(String key)
	{
		if(key.toUpperCase().equals("A"))
			moverHeroi(heroi_x-1, heroi_y);
		else if(key.toUpperCase().equals("W"))
			moverHeroi(heroi_x, heroi_y - 1);
		else if(key.toUpperCase().equals("S"))
			moverHeroi(heroi_x, heroi_y + 1);
		else if(key.toUpperCase().equals("D"))
			moverHeroi(heroi_x+1, heroi_y);
		
		if(map.isExit(heroi_x, heroi_y)) 
		{
			System.out.print("\n\n Congratulations! You escaped the maze!\n\n");
			return false;
		}

		if(heroi_x == espada_x && heroi_y == espada_y)
			heroi_armado = true;

		if(combateDragao())
		{
			if(dragao)
				turnDragao();
		}
		else
		{
			System.out.print("\n\n Bad luck! The dragon killed you... Take revenge next time!\n\n");
			return false;
		}

		if(combateDragao())
		{
			return true;
		}

		System.out.print("\n\n Bad luck! The dragon killed you... Take revenge next time!\n\n");
		return false;
	}

	private static void turnDragao()
	{
		Random x = new Random();
		boolean posicaoValida = false;
		do
		{
			switch(x.nextInt(5))
			{
			case 0:
				posicaoValida = true;
				break;
			case 1: // cima
				posicaoValida = moverDragao(dragao_x, dragao_y - 1);
				break;
			case 2: // baixo
				posicaoValida = moverDragao(dragao_x, dragao_y + 1);
				break;
			case 3: // esquerda
				posicaoValida = moverDragao(dragao_x - 1, dragao_y);
				break;
			case 4: // direita
				posicaoValida = moverDragao(dragao_x + 1, dragao_y);
				break;
			}
		} while (!posicaoValida);
	}

	private static boolean moverDragao(int x, int y) {
		if (!map.isWall(x, y))
		{
			dragao_x = x;
			dragao_y = y;
			return true;
		}
		return false;
	}

	private static boolean moverHeroi(int x, int y) {
		if(!map.isWall(x, y) || (map.isExit(x,  y) && !dragao))
		{
			heroi_x=x;
			heroi_y=y;
			return true;
		}
		return false;
	}

	private static boolean combateDragao()
	{
		if(Maze.areAdjacent(heroi_x, heroi_y, dragao_x, dragao_y) && dragao)
		{
			if(heroi_armado)
			{
				// Dragão morreu
				dragao = false;
				map.setExitVisible(true);
			}
			else
				return false;
		}
		return true;
	}

	private static void generateMapElements(int min_dist)
	{
		generatePosHeroi();
		generatePosEspada(min_dist);
		generatePosDragao(min_dist);
	}

	private static void generatePosHeroi() {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY = rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || randX >= map.getSide() || randY >= map.getSide());

		heroi_x = randX;
		heroi_y = randY;
	}

	private static void generatePosEspada(int min_dist) {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY= rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, heroi_x, heroi_y) < min_dist);

		espada_x = randX;
		espada_y = randY;
	}

	private static void generatePosDragao(int min_dist) {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY= rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, heroi_x, heroi_y) < min_dist);

		dragao_x = randX;
		dragao_y = randY;
	}

}