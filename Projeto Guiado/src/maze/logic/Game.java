package maze.logic;
import java.util.Random;

public class Game {
	

	////////////////////////////////
	////////   Attributes   ////////
	////////////////////////////////
	
	private static final int ELEM_DIST_FACTOR = 2;			// Fator usado para determinar a dist. mínima entre elementos
	
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
		{
			sword.setDropped(false);
			hero.setArmed(true);
		}

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