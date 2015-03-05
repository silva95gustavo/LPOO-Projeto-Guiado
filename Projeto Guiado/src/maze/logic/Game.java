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
	
	public Game(int side, Dragon.Dragon_mode dragon_mode)
	{
		int minElemDist = (int) (side/ELEM_DIST_FACTOR);
		minElemDist = minElemDist*minElemDist;
		map = new MazeBuilder(side).build();
		generateMapElements(minElemDist);
		
		for(int i = 0; i < dragons.length; i++)
			dragons[i].setMode(dragon_mode);
	}
	
	public Game(int side,int dragon_number, Dragon.Dragon_mode dragon_mode)
	{
		int minElemDist = (int) (side/ELEM_DIST_FACTOR);
		minElemDist = minElemDist*minElemDist;
		map = new MazeBuilder(side).build();
		
		dragons = new Dragon[dragon_number];
		
		for(int i = 0; i < dragons.length; i++)
		{
			Dragon dragon = new Dragon(0, 0, dragon_mode);
			dragons[i] = dragon;
		}
		
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
			turnDragoes();
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
	
	private void turnDragao(int index)
	{
		switch(dragons[index].getMode()){
		case DGN_STILL:
			return;
		case DGN_RAND:
			moverDragaoRandom(index);
			return;
		case DGN_RAND_SLP:
			if(dragons[index].isSleeping())
			{
				if(probability(8))
					dragons[index].setSleeping(false);
				else
					return;
			}
			else
			{
				if(probability(8))
				{
					dragons[index].setSleeping(true);
					return;
				}
			}

			moverDragaoRandom(index);
			return;
		}	
	}
	
	private void turnDragoes()
	{
		
		for(int i = 0; i < dragons.length; i++)
		{
			turnDragao(i);
		}
	}

	private void moverDragaoRandom(int index) {

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
				posicaoValida = moverDragao(dragons[index].getX(), dragons[index].getY() - 1, index);
				break;
			case 2: // baixo
				posicaoValida = moverDragao(dragons[index].getX(), dragons[index].getY() + 1, index);
				break;
			case 3: // esquerda
				posicaoValida = moverDragao(dragons[index].getX() - 1, dragons[index].getY(), index);
				break;
			case 4: // direita
				posicaoValida = moverDragao(dragons[index].getX() + 1, dragons[index].getY(), index);
				break;
			}
		} while (!posicaoValida);
	}
	
	private boolean moverDragao(int x, int y, int index) {
		if (!map.isWall(x, y))
		{
			dragons[index].setPosition(x, y);
			return true;
		}
		return false;
	}

	private boolean moverHeroi(int x, int y) {
		if(!map.isWall(x, y) || (map.isExit(x,  y) && allDragonsDead()))
		{
			hero.setPosition(x, y);
			return true;
		}
		return false;
	}

	private boolean allDragonsDead()
	{
		for(int i = 0; i < dragons.length; i++)
		{
			if(dragons[i].isAlive())
				return false;
		}
		return true;
	}
	
	private boolean combateDragao()
	{
		for(int i = 0; i < dragons.length; i++)
		{
			if(dragons[i].isAlive() && Maze.areAdjacent(hero.getX(), hero.getY(), dragons[i].getX(), dragons[i].getY()))
			{
				if(hero.isArmed())
				{
					// Dragão morreu
					dragons[i].setAlive(false);
					map.setExitVisible(true);
				}
				else if (!dragons[i].isSleeping())
					return false;
			}
		}
		
		return true;
	}

	private void generateMapElements(int min_dist)
	{
		generatePosHeroi();
		generatePosEspada(min_dist);
		generatePosDragoes(min_dist);
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

	private void generatePosDragoes(int min_dist) {
		Random rand = new Random();

		int randX, randY;
		
		for(int i = 0; i < dragons.length; i++)
		{
			do
			{
				randX = rand.nextInt(map.getSide()-2) + 1;
				randY= rand.nextInt(map.getSide()-2) + 1;			
			} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, hero.getX(), hero.getY()) < min_dist);

			dragons[i].setCoords(randX,  randY);
		}
		
	}

	private boolean probability(int val)
	{
		Random rand = new Random();
		int r = rand.nextInt(101);
		if(r <= val)
			return true;
		
		return false;
	}
}