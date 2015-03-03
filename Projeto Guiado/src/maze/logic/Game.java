package maze.logic;
import java.util.Arrays;
import java.util.Random;

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

	private Maze map;						// Represents the game map

	private Hero hero;
	private Sword sword;
	private Dragon[] dragons = new Dragon[1];

	////////////////////////////////
	////////   Functions   /////////
	////////////////////////////////
	
	public Game(int side)
	{
		int minElemDist = (int) (side/ELEM_DIST_FACTOR);
		minElemDist = minElemDist*minElemDist;
		map = new MazeBuilder(side).build();
		generateMapElements(minElemDist);
	}
	
	public GameData getGameData()
	{
		return new GameData(map, hero, sword, dragons);
	}
	
	public void drawMap()
	{
		int[] coords = new int[6];
		Arrays.fill(coords, -1);
		char[] chars = new char[3];
		Arrays.fill(chars, ' ');
		
		
		coords[0] = hero.getX();
		coords[1] = hero.getY();
		if(hero.isArmed())
			chars[0] = heroi_armado_char;
		else
			chars[0] = heroi_char;
		
		int i = 1;
		
		// TODO: Suporte multi-dragões
		if(sword.getX() == dragons[0].getX() && sword.getY() == dragons[0].getY())
		{
			coords[2*i] = sword.getX();
			coords[2*i+1] = sword.getY();
			
			if(!hero.isArmed())
				chars[i] = dragao_espada_char;
			else
				chars[i] = dragao_char;
			i++;
		}
		else
		{
			if(!hero.isArmed())
			{
				coords[2*i] = sword.getX();
				coords[2*i + 1] = sword.getY();
				chars[i] = espada_char;
				i++;
			}
			
			if(dragons[0].isAlive())
			{
				coords[2*i] = dragons[0].getX();
				coords[2*i + 1] = dragons[0].getY();
				chars[i] = dragao_char;
				i++;
			}
		}
		map.drawMatrix(coords, chars);
	}

	public boolean turn(String key)
	{
		if(key.toUpperCase().equals("A"))
			moverHeroi(hero.getX()-1, hero.getY());
		else if(key.toUpperCase().equals("W"))
			moverHeroi(hero.getX(), hero.getY() - 1);
		else if(key.toUpperCase().equals("S"))
			moverHeroi(hero.getX(), hero.getY() + 1);
		else if(key.toUpperCase().equals("D"))
			moverHeroi(hero.getX()+1, hero.getY());
		
		if(map.isExit(hero.getX(), hero.getY())) 
		{
			System.out.print("\n\n Congratulations! You escaped the maze!\n\n");
			return false;
		}

		if(hero.getX() == sword.getX() && hero.getY() == sword.getY())
			hero.setArmed(true);

		if(combateDragao())
		{
			if(dragons[0].isAlive())
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

	private void turnDragao()
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
				posicaoValida = moverDragao(dragons[0].getX(), dragons[0].getY() - 1);
				break;
			case 2: // baixo
				posicaoValida = moverDragao(dragons[0].getX(), dragons[0].getY() + 1);
				break;
			case 3: // esquerda
				posicaoValida = moverDragao(dragons[0].getX() - 1, dragons[0].getY());
				break;
			case 4: // direita
				posicaoValida = moverDragao(dragons[0].getX() + 1, dragons[0].getY());
				break;
			}
		} while (!posicaoValida);
	}

	private boolean moverDragao(int x, int y) {
		if (!map.isWall(x, y))
		{
			dragons[0].setPosition(x, y);
			return true;
		}
		return false;
	}

	private boolean moverHeroi(int x, int y) {
		if(!map.isWall(x, y) || (map.isExit(x,  y) && !dragons[0].isAlive()))
		{
			hero.setPosition(x, y);
			return true;
		}
		return false;
	}

	private boolean combateDragao()
	{
		if(dragons[0].isAlive() && Maze.areAdjacent(hero.getX(), hero.getY(), dragons[0].getX(), dragons[0].getY()))
		{
			if(hero.isArmed())
			{
				// Dragão morreu
				dragons[0].setAlive(false);
				map.setExitVisible(true);
			}
			else
				return false;
		}
		return true;
	}

	private void generateMapElements(int min_dist)
	{
		generatePosHeroi();
		generatePosEspada(min_dist);
		generatePosDragao(min_dist);
	}

	private void generatePosHeroi() {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY = rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || randX >= map.getSide() || randY >= map.getSide());
		
		hero = new Hero(randX, randY);
	}

	private void generatePosEspada(int min_dist) {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY= rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, hero.getX(), hero.getY()) < min_dist);

		sword = new Sword(randX, randY);
	}

	private void generatePosDragao(int min_dist) {
		Random rand = new Random();

		int randX, randY;

		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY= rand.nextInt(map.getSide()-2) + 1;			
		} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, hero.getX(), hero.getY()) < min_dist);

		dragons[0] = new Dragon(randX, randY);
	}

}