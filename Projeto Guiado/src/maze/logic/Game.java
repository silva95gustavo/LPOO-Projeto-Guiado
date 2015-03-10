package maze.logic;
import java.util.Random;

import maze.logic.Dragon.Dragon_mode;

public class Game {
	public static enum event { NONE, WIN, SHIELDED, LOSE, LOSE_FIRE};

	////////////////////////////////
	////////   Attributes   ////////
	////////////////////////////////

	private static final int ELEM_DIST_FACTOR = 2;			// Fator usado para determinar a dist. mínima entre elementos
	
	private Maze map;						// Represents the game map

	private Hero hero;
	private Sword sword;
	private Dragon[] dragons;
	private Dart[] darts;
	private enum Direction {UP, DOWN, LEFT, RIGHT};
	private Shield shield;

	////////////////////////////////
	////////   Functions   /////////
	////////////////////////////////
	
	public Game(int side)
	{
		this(side, Dragon.Dragon_mode.DGN_STILL);
	}

	public Game(int side, Dragon.Dragon_mode dragon_mode)
	{
		this(side, 1, dragon_mode);
	}

	public Game(int side, int dragon_number, Dragon.Dragon_mode dragon_mode)
	{
		this(side, dragon_number, dragon_mode, true, false);
	}
	
	public Game(Dragon.Dragon_mode dragon_mode, boolean defaultGame)
	{
		this(DefaultMaze.defaultMatrix.length, 1, dragon_mode, false, defaultGame);
	}
	
	public Game(int side,int dragon_number, Dragon.Dragon_mode dragon_mode, boolean random, boolean defaultGame)
	{
		int minElemDist = (int) (side/ELEM_DIST_FACTOR);
		minElemDist = minElemDist*minElemDist;
		
		if (random)
		{
			map = new MazeBuilder(side).build();
	
			dragons = new Dragon[dragon_number];
	
			for(int i = 0; i < dragons.length; i++)
			{
				Dragon dragon = new Dragon(0, 0, dragon_mode);
				dragons[i] = dragon;
			}
	
			generateMapElements(minElemDist);
		}
		else
		{
			map = new DefaultMaze();
			hero = new Hero(1, 1);
			dragons = new Dragon[1];
			dragons[0] = new Dragon(1, 3, dragon_mode);
			sword = new Sword(1, 8);
			generatePosDarts(minElemDist);
			generatePosShield(minElemDist);
			
			if(defaultGame)
			{
				shield.setDropped(false);
				darts = new Dart[0];
				dragons[0].setFireAbility(false);
			}
		}
	}

	public GameData getGameData()
	{
		return new GameData(map, hero, sword, dragons, darts, shield);
	}

	public event turn(String key)
	{
		if(key.toUpperCase().equals("A"))
			moverHeroi(hero.getX()-1, hero.getY());
		else if(key.toUpperCase().equals("W"))
			moverHeroi(hero.getX(), hero.getY() - 1);
		else if(key.toUpperCase().equals("S"))
			moverHeroi(hero.getX(), hero.getY() + 1);
		else if(key.toUpperCase().equals("D"))
			moverHeroi(hero.getX()+1, hero.getY());
		else if(key.toUpperCase().equals("I"))
			fireDart(Direction.UP);
		else if(key.toUpperCase().equals("J"))
			fireDart(Direction.LEFT);
		else if(key.toUpperCase().equals("K"))
			fireDart(Direction.DOWN);
		else if(key.toUpperCase().equals("L"))
			fireDart(Direction.RIGHT);

		if(map.isExit(hero.getX(), hero.getY())) 
		{
			return event.WIN;
		}

		if(hero.getX() == sword.getX() && hero.getY() == sword.getY())
		{
			sword.setDropped(false);
			hero.setArmed(true);
		}
		boolean caughtShield = false;
		if(hero.getX() == shield.getX() && hero.getY() == shield.getY() && shield.isDropped())
		{
			caughtShield = true;
			shield.setDropped(false);
			hero.catchShield();
		}

		for(int i = 0; i < darts.length; i++)
		{
			if(hero.getX() == darts[i].getX() && hero.getY() == darts[i].getY())
			{
				darts[i].setDropped(false);
				hero.catchDart();
			}
		}

		if(combateDragao())
		{
			turnDragoes();
		}
		else
		{
			return event.LOSE;
		}

		if(combateDragao())
		{
			if(allDragonsDead())
				map.setExitVisible(true);
			return caughtShield ? event.SHIELDED : event.NONE;
		}
		
		return event.LOSE;
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

	private void fireDart(Direction dir)
	{
		if(!hero.hasDarts())
			return;
		hero.fireDart();

		switch(dir)
		{
		case UP:
			for(int i = hero.getY(); i > 0; i--)
			{
				if(map.isWall(hero.getX(), i))
					return;
				for(int d = 0; d < dragons.length; d++)
				{
					if(dragons[d].getX() == hero.getX() && dragons[d].getY() == i)
					{
						dragons[d].setAlive(false);
						return;
					}
				}
			}
			break;
		case DOWN:
			for(int i = hero.getY(); i < map.getSide(); i++)
			{
				if(map.isWall(hero.getX(), i))
					return;
				for(int d = 0; d < dragons.length; d++)
				{
					if(dragons[d].getX() == hero.getX() && dragons[d].getY() == i)
					{
						dragons[d].setAlive(false);
						return;
					}
				}
			}
			break;
		case LEFT:
			for(int i = hero.getX(); i > 0; i--)
			{
				if(map.isWall(i, hero.getY()))
					return;
				for(int d = 0; d < dragons.length; d++)
				{
					if(dragons[d].getX() == i && dragons[d].getY() == hero.getY())
					{
						dragons[d].setAlive(false);
						return;
					}
				}
			}
			break;
		case RIGHT:
			for(int i = hero.getX(); i < map.getSide(); i++)
			{
				if(map.isWall(i, hero.getY()))
					return;
				for(int d = 0; d < dragons.length; d++)
				{
					if(dragons[d].getX() == i && dragons[d].getY() == hero.getY())
					{
						dragons[d].setAlive(false);
						return;
					}
				}
			}
			break;
		}
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
					if(allDragonsDead())
						map.setExitVisible(true);
				}
				else if (!dragons[i].isSleeping())
				{
					return false;
				}
			}
			
			if(dragonFire(dragons[i]))
			{
				return false;
			}
		}

		return true;
	}

	private boolean dragonFire(Dragon dragon)
	{
		if(!dragon.isAlive() || dragon.isSleeping() || hero.isShielded() || !dragon.canFire())
			return false;
		
		int pos, cells = 3;
		
		if(dragon.getX() == hero.getX())
		{
			pos = dragon.getY();
			while(cells > 0)
			{
				cells--;
				
				if(dragon.getY() > hero.getY())
					pos--;
				else
					pos++;
				
				if(hero.getY() == pos)
					return true;
				
				if(map.isWall(dragon.getX(), pos))
					return false;
			}
		}
		else if(dragon.getY() == hero.getY())
		{
			pos = dragon.getX();
			while(cells > 0)
			{
				cells--;
				
				if(dragon.getX() > hero.getX())
					pos--;
				else
					pos++;
				
				if(hero.getX() == pos)
					return true;
				
				if(map.isWall(pos, dragon.getY()))
					return false;
			}
		}
		
		return false;
	}
	
	private void generateMapElements(int min_dist)
	{
		generatePosHeroi();
		generatePosEspada(min_dist);
		generatePosDragoes(min_dist);
		generatePosDarts(min_dist);
		generatePosShield(min_dist);
	}

	private void generatePosShield(int min_dist)
	{
		Random rand = new Random();
		int randX, randY;
		
		do
		{
			randX = rand.nextInt(map.getSide()-2) + 1;
			randY= rand.nextInt(map.getSide()-2) + 1;
		} while(!map.isEmptyCell(randX, randY) || Maze.coordDistSquare(randX, randY, hero.getX(), hero.getY()) < min_dist);

		shield = new Shield(randX, randY);
	}

	private void generatePosDarts(int min_dist) {
		Random rand = new Random();

		int randX, randY;
		int n_darts = map.getSide()/4;
		darts = new Dart[n_darts];

		for(int i = 0; i < n_darts; i++)
		{
			do
			{
				randX = rand.nextInt(map.getSide()-2) + 1;
				randY= rand.nextInt(map.getSide()-2) + 1;	
			} while(map.isWall(randX, randY) || Maze.coordDistSquare(randX, randY, hero.getX(), hero.getY()) < min_dist);

			Dart dart = new Dart(randX, randY);
			darts[i] = dart;
		}

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
