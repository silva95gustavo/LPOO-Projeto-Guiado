package maze.logic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game {

	/**
	 * Turn events.
	 */
	public static enum event {
		/**
		 * No event to be reported.
		 */
		NONE,

		/**
		 * Game won.
		 */
		WIN,

		/**
		 * The hero grabbed a shield.
		 */
		SHIELDED,

		/**
		 * Game lost because a dragon attacked the hero.
		 */
		LOSE,

		/**
		 * Game lost because the hero burned in a dragon's fire.
		 */
		LOSE_FIRE
	};

	/**
	 * Action commands
	 *
	 */
	public static enum command {
		/**
		 * Move hero.
		 */
		MOVE,

		/**
		 * Fire dart.
		 */
		FIRE
	};

	/**
	 * {@link command.MOVE} direction
	 */
	public enum Direction {
		/**
		 * Move up
		 */
		UP,

		/**
		 * Move down
		 */
		DOWN,

		/**
		 * Move left
		 */
		LEFT,

		/**
		 * Move right
		 */
		RIGHT
	};

	public static final String mapFileExtension = ".map";
	public static final String gameFileExtension = ".game";

	////////////////////////////////
	////////   Attributes   ////////
	////////////////////////////////

	/**
	 * Factor used to calculate the minimum distance between elements (used in random map generation).
	 */
	private static final int ELEM_DIST_FACTOR = 2;

	/**
	 * Factor used to calculated the maximum number of darts to be created (used in random map generation).
	 */
	public static final int MAX_DARTS_FACTOR = 4;

	private Maze map;						// Represents the game map

	private Hero hero;
	private Sword sword;
	private Dragon[] dragons;
	private Dart[] darts;
	private Shield shield;

	////////////////////////////////
	////////   Functions   /////////
	////////////////////////////////

	/**
	 * Creates a game with the following settings:
	 * Dragon number: 1
	 * Dragon mode: {@link Dragon.Dragon_mode#DGN_STILL}
	 * Map generation: random
	 * Hero, sword and dragons position: random
	 * Shield and darts position: random
	 * 
	 * @param side size of the map to be generated
	 */
	public Game(int side)
	{
		this(side, Dragon.Dragon_mode.DGN_STILL);
	}

	/**
	 * Creates a game with custom settings
	 * 
	 * @param data
	 */
	public Game(GameData data)
	{
		map = data.getMap();
		hero = data.getHero();
		sword = data.getSword();
		dragons = data.getDragons();
		shield = data.getShield();
		darts = data.getDarts();
	}

	/**
	 * Creates a game with the following settings:
	 * Dragon number: 1
	 * Map generation: random
	 * Hero, sword and dragons position: random
	 * Shield and darts position: random
	 * 
	 * @param side size of the map to be generated
	 * @param dragon_mode
	 */
	public Game(int side, Dragon.Dragon_mode dragon_mode)
	{
		this(side, 1, dragon_mode);
	}

	/**
	 * Creates a game with the following settings:
	 * Map generation: random
	 * Hero, sword and dragons position: random
	 * Shield and darts position: random
	 * 
	 * @param side size of the map to be generated
	 * @param dragon_number
	 * @param dragon_mode
	 */
	public Game(int side, int dragon_number, Dragon.Dragon_mode dragon_mode)
	{
		this(side, dragon_number, dragon_mode, true, false);
	}

	/**
	 * Creates a game with the following settings:
	 * Map generation: default map
	 * Hero, sword and dragons position: default positions
	 * @param dragon_mode
	 * @param defaultGame if false a shield and darts will be generated, otherwise they won't be created
	 */
	public Game(Dragon.Dragon_mode dragon_mode, boolean defaultGame)
	{
		this(DefaultMaze.defaultMatrix.length, 1, dragon_mode, false, defaultGame);
	}

	/**
	 * Creates a game with custom settings
	 * @param side size of the map to be generated (this field will be ignored if the random param is set to false)
	 * @param dragon_number
	 * @param dragon_mode
	 * @param random
	 * @param defaultGame if false a shield and darts will be generated, otherwise they won't be created (this field will be ignored if the random param is set to true)
	 */
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

			if(defaultGame)
			{
				darts = new Dart[0];
				dragons[0].setFireAbility(false);
			}
			else
			{
				generatePosDarts(minElemDist);
				generatePosShield(minElemDist);
			}
		}
	}

	/**
	 * Returns a {@link GameData} class with all information about the game
	 * @return {@link GameData}
	 */
	public GameData getGameData()
	{
		return new GameData(map, hero, sword, dragons, darts, shield);
	}

	/**
	 * Makes a moving/firing turn and updates dragons positions
	 * @param cmd {@link command}
	 * @param direction {@link Direction}
	 * @return {@link event} class representing the result of the turn
	 */
	public event turn(command cmd, Direction direction)
	{
		if (cmd == command.MOVE)
		{
			switch (direction)
			{
			case LEFT:
				moverHeroi(hero.getX()-1, hero.getY());
				break;
			case UP:
				moverHeroi(hero.getX(), hero.getY() - 1);
				break;
			case RIGHT:
				moverHeroi(hero.getX()+1, hero.getY());
				break;
			case DOWN:
				moverHeroi(hero.getX(), hero.getY() + 1);
				break;
			}
		}
		else if (cmd == command.FIRE)
		{
			fireDart(direction);
		}

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
		if (shield != null)
			if(hero.getX() == shield.getX() && hero.getY() == shield.getY() && shield.isDropped())
			{
				caughtShield = true;
				shield.setDropped(false);
				hero.catchShield();
			}

		for(int i = 0; i < darts.length; i++)
		{
			if(hero.getX() == darts[i].getX() && hero.getY() == darts[i].getY() && darts[i].isDropped())
			{
				darts[i].setDropped(false);
				hero.catchDart();
			}
		}

		event battle = combateDragao();

		if(battle == event.NONE)
		{
			turnDragoes();
		}
		else
		{
			return battle;
		}

		battle = combateDragao();

		if(battle == event.NONE)
		{
			if(allDragonsDead())
				map.setExitVisible(true);
			return caughtShield ? event.SHIELDED : event.NONE;
		}

		return battle;
	}

	/**
	 * Makes a moving/firing turn, but only for the hero
	 * @param cmd {@link command}
	 * @param direction {@link Direction}
	 * @return {@link event} class representing the result of the turn
	 */
	public event turnHero(command cmd, Direction direction)
	{
		if (cmd == command.MOVE)
		{
			switch (direction)
			{
			case LEFT:
				moverHeroi(hero.getX()-1, hero.getY());
				break;
			case UP:
				moverHeroi(hero.getX(), hero.getY() - 1);
				break;
			case RIGHT:
				moverHeroi(hero.getX()+1, hero.getY());
				break;
			case DOWN:
				moverHeroi(hero.getX(), hero.getY() + 1);
				break;
			}
		}
		else if (cmd == command.FIRE)
		{
			fireDart(direction);
		}

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
			if(hero.getX() == darts[i].getX() && hero.getY() == darts[i].getY() && darts[i].isDropped())
			{
				darts[i].setDropped(false);
				hero.catchDart();
			}
		}

		event battle = combateDragao();

		if(battle == event.NONE)
		{
			if(allDragonsDead())
				map.setExitVisible(true);
			return caughtShield ? event.SHIELDED : event.NONE;
		}

		return battle;
	}

	/**
	 * Saves the game to the file /data/game
	 */
	public void save()
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./data/game")));
			oos.writeObject(getGameData());
			oos.close();
			JOptionPane.showMessageDialog(null, "Game saved!");
		}
		catch(Exception exc)
		{	
			JOptionPane.showMessageDialog(null, "Error on opening or writing to output file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Loads a game from the file /data/game
	 * @return The loaded game in case of success or null otherwise.
	 */
	public static Game load()
	{
		return loadFromFile("./data/game");
	}
	
	/**
	 * Loads a game from the file given
	 * @return The loaded game in case of success or null otherwise.
	 */
	public static Game loadFromFile(String filename)
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filename)));
			GameData data = (GameData) ois.readObject();
			ois.close();

			return new Game(data);
		}
		catch(Exception exc)
		{	
			exc.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	public int numDragoes()
	{
		return dragons.length;
	}
	
	public int numDartsHero()
	{
		return hero.getDarts();
	}
	
	public void turnDragao(int index)
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
		case DGN_SLP:
			return;
		}	
	}

	public void turnDragoes()
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

	/**
	 * Forces a dragon to move to a given position
	 * @param x x coordinate of the position to move the dragon to
	 * @param y y coordinate of the position to move the dragon to
	 * @param index number of the dragon to be moved
	 * @return
	 */
	public boolean moverDragao(int x, int y, int index) {
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

	public event combateDragao()
	{
		for(int i = 0; i < dragons.length; i++)
		{

			if(dragons[i].isAlive())
			{
				if(Maze.areAdjacent(hero.getX(), hero.getY(), dragons[i].getX(), dragons[i].getY()))
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
						hero.setAlive(false);
						return event.LOSE;
					}
				}

				if(dragonFire(dragons[i]))
				{
					hero.setAlive(false);
					return event.LOSE_FIRE;
				}
			}


		}

		return event.NONE;
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
		int n_darts = map.getSide()/MAX_DARTS_FACTOR;
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
