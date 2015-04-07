package maze.gui;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
<<<<<<< HEAD
=======
import java.awt.image.AffineTransformOp;
>>>>>>> refs/remotes/origin/master
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import maze.logic.*;

@SuppressWarnings("serial")
public class GameGraphic extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	private GameFrame frame;

	private static final int default_width = 880;
	private static final int default_width_minimized = 370;
	private static final int default_height = 940;
	private static final int default_height_minimized = 100;

	private static BufferedImage hero;
	private static BufferedImage hero_shielded;
	private static BufferedImage hero_armed;
	private static BufferedImage hero_armed_shielded;
	private static BufferedImage dragon;
	private static BufferedImage dragon_sleeping;
	private static BufferedImage sword;
	private static BufferedImage wall;
	private static BufferedImage pavement_wall;
	private static BufferedImage pavement;
	private static BufferedImage shield;
	private static BufferedImage dart;
	private static BufferedImage exit_open;
	private static BufferedImage exit_closed;
	private int border = 10;

	private Game.Direction hero_direction = Game.Direction.UP;

	private JLabel lblDarts;
	private JButton btnNewGame;
	private JButton btnLoadGame;
	private JButton btnSaveGame;
	private JButton btnDrawMaze;

	private Configuration config = new Configuration();

	Game game;

	public GameGraphic(GameFrame frame) throws IOException {		
		game = null;
		this.frame = frame;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int windowX = dim.width/2-default_width_minimized/2;
		int windowY = dim.height/2-default_height_minimized/2;
		frame.setBounds(windowX, windowY, default_width_minimized, default_height_minimized);

		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
			pavement_wall = ImageIO.read(new File("./res/pavement_wall.jpg"));
			dragon = ImageIO.read(new File("./res/dragon.png"));
			dragon_sleeping = ImageIO.read(new File("./res/dragon_sleeping.png"));
			hero = ImageIO.read(new File("./res/hero.png"));
			hero_shielded = ImageIO.read(new File("./res/hero_shielded.png"));
			hero_armed = ImageIO.read(new File("./res/hero_armed.png"));
			hero_armed_shielded = ImageIO.read(new File("./res/hero_armed_shielded.png"));
			sword = ImageIO.read(new File("./res/sword.png"));
			pavement = ImageIO.read(new File("./res/pavement.jpg"));
			shield = ImageIO.read(new File("./res/shield.png"));
			dart = ImageIO.read(new File("./res/dart.png"));
			exit_open = ImageIO.read(new File("./res/exit_open.png"));
			exit_closed = ImageIO.read(new File("./res/exit_closed.png"));
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);

		JButton btnCloseGame = new JButton("Close Game");
		btnCloseGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the game?", "Close Game", JOptionPane.YES_NO_OPTION);

				if(n == 0)
				{
					saveSettings();
					System.exit(0);
				}
			}
		});

		JButton btnConfig = new JButton("Settings");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String[] options = {"Settings", "Manage map"};

				int n = JOptionPane.showOptionDialog(null,
						"What would you like to do?",
						"Settings",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);

				if(n == 0)
				{
					ConfigDialog sett = new ConfigDialog();
					sett.display(config);
					saveSettings();	
				}
				else if (n == 1)
				{
					ManageMapDialog mapDialog = new ManageMapDialog();
					mapDialog.display();
					String file = ManageMapDialog.mapName;
					if(file != null)
					{
						file = ManageMapDialog.mapName.toString();
						ManageMapDialog.mapName = null;
						loadGameFromMap(file + Game.mapFileExtension);
					}
				}
			}
		});

		lblDarts = new JLabel("Darts : 0");
		lblDarts.setToolTipText("Shoot darts at the dragons with W, A, S and D");

		btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				int n = 0;

				if(game != null)
					n = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);

				/*
				 * The line above was made so that the program only asks the user's confirmation to create a new game
				 * when there is a game ongoing. If there are no current games, it creates a new one without asking.
				 */

				if(n == 0)
				{
					game = new Game(config.side, config.dragonNumber, config.dragonMode);
					repaint();
					requestFocus();
				}
			}
		});

		btnLoadGame = new JButton("Load Saved Game");
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to load the previous game? Doing this will finish the current game.", "Load Game", JOptionPane.YES_NO_OPTION);

				if(n == 0)
				{
					try
					{
						game = Game.load();
					}
					catch(Exception exc)
					{	
						n = JOptionPane.showConfirmDialog(null, "An error occurred loading the game. Do you wish to start a new game with the current settings? (accepting will finish the current game)", "Load Game", JOptionPane.YES_NO_OPTION);

						if(n == 0)
						{
							game = new Game(config.side, config.dragonNumber, config.dragonMode);
							repaint();
						}
					}
					repaint();
				}
				requestFocus();
			}
		});

		btnSaveGame = new JButton("Save Game");
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.save();
				requestFocus();
			}
		});

		btnSaveGame.setEnabled(false);

		btnDrawMaze = new JButton("Draw Maze");
		btnDrawMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DrawMapWindow win = new DrawMapWindow();
				win.start();
			}
		});

		loadSettings();

		add(btnNewGame);
		add(btnConfig);
		add(btnCloseGame);
		add(lblDarts);
		add(btnSaveGame);
		add(btnLoadGame);
		add(btnDrawMaze);
	}

	private void loadGameFromMap(String file)
	{
		char[][] matrix;
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("./maps/" + file)));
			matrix = (char[][])ois.readObject();
			ois.close();
		}
		catch(Exception exc) {
			JOptionPane.showMessageDialog(null, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Hero hero = new Hero(0, 0);
		ArrayList<Dragon> dragList = new ArrayList<Dragon>();
		Dragon[] dragons = new Dragon[0];
		Sword sword = new Sword(0, 0);
		Exit exit = new Exit(0, 0);
		Maze map;
		ArrayList<Dart> dartList = new ArrayList<Dart>();
		Dart[] darts;
		Shield shield = new Shield(0, 0);

		for(int lin = 1; lin < matrix.length-1; lin++)
		{
			for(int col = 1; col < matrix.length-1; col++)
			{				
				switch(matrix[lin][col])
				{
				case 'h':
					hero = new Hero(col, lin);
					break;
				case 'e':
					sword = new Sword(col, lin);
					break;
				case 'd':
					dragList.add(new Dragon(col, lin, config.dragonMode));
					break;
				case 's':
					shield = new Shield(col, lin);
					break;
				case 'a':
					dartList.add(new Dart(col, lin));
					break;
				}

				if(matrix[lin][col] != Maze.wallChar)
					matrix[lin][col] = ' ';
			}
		}

		int side = matrix.length;

		for(int i = 0; i < side; i++)
		{			
			if(matrix[i][0] == ' ')
			{
				exit = new Exit(0, i);
				matrix[i][0] = 'X';
				break;
			}

			if(matrix[i][side-1] == ' ')
			{
				exit = new Exit(side-1, i);
				matrix[i][side-1] = 'X';
				break;
			}

			if(matrix[0][i] == ' ')
			{
				exit = new Exit(i, 0);
				matrix[0][i] = 'X';
				break;
			}

			if(matrix[side-1][i] == ' ')
			{
				exit = new Exit(i, side-1);
				matrix[side-1][i] = 'X';
				break;
			}
		}

		map = new Maze(matrix, exit);
		dragons = dragList.toArray(new Dragon[dragList.size()]);
		darts = dartList.toArray(new Dart[dartList.size()]);

		GameData data = new GameData(map, hero, sword, dragons, darts, shield);
		game = new Game(data);
		repaint();
		requestFocus();
	}

	public void saveSettings()
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("./data/settings")));
			ConfigurationSerializable conf = new ConfigurationSerializable(config);
			oos.writeObject(conf);
			oos.close();
		}
		catch(Exception exc) {}
	}

	public void loadSettings()
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("./data/settings")));
			ConfigurationSerializable conf = (ConfigurationSerializable) ois.readObject();
			ois.close();

			this.config = conf.toConfig();
		}
		catch(Exception exc) {}
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.setColor(Color.BLACK);
		if(game != null)
		{
			lblDarts.setVisible(true);
			if(!frame.isResizable())
			{
				frame.setResizable(true);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int windowX = dim.width/2-default_width/2;
				int windowY = dim.height/2-default_height/2;
				frame.setBounds(windowX, windowY, default_width, default_height);

			}
			btnSaveGame.setEnabled(true);
			showGame(game.getGameData(), g);
		}
		else
		{
			lblDarts.setVisible(false);
			if(frame.isResizable())
			{
				frame.setResizable(false);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int windowX = dim.width/2-default_width_minimized/2;
				int windowY = dim.height/2-default_height_minimized/2;
				frame.setBounds(windowX, windowY, default_width_minimized, default_height_minimized);
			}
			btnSaveGame.setEnabled(false);
		}
	}

	private void resetGame()
	{
		game = null;
		lblDarts.setText("Darts : 0");
	}

	public void keyPressed(KeyEvent arg0) {

		Game.event ret = Game.event.NONE;

		if(game != null)
		{
			if(arg0.getKeyCode() == config.cmdUP)
			{
				ret = game.turn(Game.command.MOVE, Game.Direction.UP);
				hero_direction = Game.Direction.UP;
			}
			else if(arg0.getKeyCode() == config.cmdDOWN)
			{
				ret = game.turn(Game.command.MOVE, Game.Direction.DOWN);
				hero_direction = Game.Direction.DOWN;
			}
			else if(arg0.getKeyCode() == config.cmdLEFT)
			{
				ret = game.turn(Game.command.MOVE, Game.Direction.LEFT);
				hero_direction = Game.Direction.LEFT;
			}
			else if(arg0.getKeyCode() == config.cmdRIGHT)
			{
				ret = game.turn(Game.command.MOVE, Game.Direction.RIGHT);
				hero_direction = Game.Direction.RIGHT;
			}
			else if(arg0.getKeyCode() == config.dartUP)
				ret = game.turn(Game.command.FIRE, Game.Direction.UP);
			else if(arg0.getKeyCode() == config.dartDOWN)
				ret = game.turn(Game.command.FIRE, Game.Direction.DOWN);
			else if(arg0.getKeyCode() == config.dartRIGHT)
				ret = game.turn(Game.command.FIRE, Game.Direction.RIGHT);
			else if(arg0.getKeyCode() == config.dartLEFT)
				ret = game.turn(Game.command.FIRE, Game.Direction.LEFT);
		}

		switch(ret)
		{
		case LOSE:
			JOptionPane.showMessageDialog(null, "Ups... A dragon killed you...\nTry picking up a sword next time!", "Game over", JOptionPane.INFORMATION_MESSAGE);
			resetGame();
			break;
		case LOSE_FIRE:
			JOptionPane.showMessageDialog(null, "Ups... You were killed by dragon fire...\nTry protecting yourself with a shield next time!", "Game over", JOptionPane.INFORMATION_MESSAGE);
			resetGame();
			break;
		case WIN:
			JOptionPane.showMessageDialog(null, "CONGRATULATIONS! You escaped the maze!\nCome back and try again!", "Game won!", JOptionPane.PLAIN_MESSAGE);;
			resetGame();
			break;
		default:
			break;
		}

		repaint();
	}

	private void showExitCellRot(Graphics g, int x, int y, int cellSide, BufferedImage img, int xCoord, int yCoord)
	{
		int rotation_factor = 0;

		if(xCoord == 0)
			rotation_factor = 1;
		else if(yCoord != 0)
		{
			if(yCoord>xCoord)
				rotation_factor = 2;
			else
				rotation_factor = 3;
		}

		int imgW = img.getWidth();
		int imgH = img.getHeight();

		AffineTransform at = new AffineTransform();
		at.translate(x+cellSide/2, y+cellSide/2);
		at.rotate(rotation_factor*Math.PI/2);
		at.scale((double)cellSide/imgW, (double)cellSide/imgH);
		at.translate(-imgW/2, -imgH/2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
	}

	private void showHeroCell(Graphics g, int x, int y, int cellSide, BufferedImage img)
	{
		int rotation_factor = 0;

		switch(hero_direction)
		{
		case RIGHT:
			rotation_factor = 1;
			break;
		case DOWN:
			rotation_factor = 2;
			break;
		case LEFT:
			rotation_factor = 3;
			break;
		default:			// just to prevent warnings
			break;
		}

		int heroW = img.getWidth();
		int heroH = img.getHeight();

		AffineTransform at = new AffineTransform();
		at.translate(x+cellSide/2, y+cellSide/2);
		at.rotate(rotation_factor*Math.PI/2);
		at.scale((double)cellSide/heroW, (double)cellSide/heroH);
		at.translate(-heroW/2, -heroH/2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
	}

	private void showImageCell(Graphics g, BufferedImage img, int x, int y)
	{
		int xBorder = border;
		int yBorder = border+btnDrawMaze.getY() + btnDrawMaze.getHeight();

		int minY = btnDrawMaze.getY() + btnDrawMaze.getHeight();

		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/game.getGameData().getMap().getSide();

<<<<<<< HEAD
		if(img == hero || img == hero_armed || img == hero_shielded || img == hero_armed_shielded)
			showHeroCell(g, xBorder+x*cellSide, yBorder+y*cellSide, cellSide, img);
		else if(img == exit_open || img == exit_closed)
			showExitCellRot(g, xBorder+x*cellSide, yBorder+y*cellSide, cellSide, img, x, y);
		else
			g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
					yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
=======
		/*///////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
		
		// The required drawing location
		int drawLocationX = 300;
		int drawLocationY = 300;

		// Rotation information

		double rotationRequired = Math.toRadians(90);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		img.getGraphics().drawImage(op.filter(img, null), drawLocationX, drawLocationY, null);
		
		////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////*/

		g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
				yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
>>>>>>> refs/remotes/origin/master
	}

	public void showGame(GameData gameData, Graphics g)
	{
		Maze map = gameData.getMap();

		Hero hero = gameData.getHero();
		Dart darts[] = gameData.getDarts();
		Shield shield = gameData.getShield();
		Dragon[] dragons = gameData.getDragons();
		Sword sword = gameData.getSword();
		Exit exit = map.getExit();

		lblDarts.setText("Darts : " + hero.getDarts());

		for (int y = 0; y < map.getSide(); y++)
		{
			for (int x = 0; x < map.getSide(); x++)
			{
				if(map.isExit(x, y))
				{
					if(y>0 && map.isWall(x, y-1))
						showImageCell(g, pavement_wall, x, y);
					else
						showImageCell(g, pavement, x, y);

					if (exit.isVisible())
						showImageCell(g, exit_open, x, y);
					else
						showImageCell(g, exit_closed, x, y);
				}
				else if(map.isWall(x,  y))
				{
					showImageCell(g, wall, x, y);
				}
				else
				{
					if (y > 0 && map.isWall(x, y - 1) && !(map.isExit(x, y - 1)))
						showImageCell(g, pavement_wall, x, y);
					else
						showImageCell(g, pavement, x, y);
				}

				if(x == hero.getX() && y == hero.getY())
				{
					if(hero.isArmed())
					{
						if(hero.isShielded())
							showImageCell(g, hero_armed_shielded, x, y);
						else
							showImageCell(g, hero_armed, x, y);
					}
					else
					{
						if(hero.isShielded())
							showImageCell(g, hero_shielded, x, y);
						else
						{
							showImageCell(g, GameGraphic.hero, x, y);
						}
					}
				}

				for(int i = 0; i < darts.length; i++)
				{
					if(x == darts[i].getX() && y == darts[i].getY() && darts[i].isDropped())
					{
						showImageCell(g, GameGraphic.dart, x, y);
					}
				}

				if(x == shield.getX() && y == shield.getY() && shield.isDropped())
				{
					showImageCell(g, GameGraphic.shield, x, y);
				}

				if(x == sword.getX() && y == sword.getY() && sword.isDropped())
				{
					showImageCell(g, GameGraphic.sword, x, y);
				}

				for(int i = 0; i < dragons.length; i++)
				{
					if(dragons[i].isAlive() && x == dragons[i].getX() && y == dragons[i].getY())
					{
						if(dragons[i].isSleeping())
							showImageCell(g, GameGraphic.dragon_sleeping, x, y);
						else
							showImageCell(g, GameGraphic.dragon, x, y);
					}
				}
			}
		}
	}

<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/master
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

<<<<<<< HEAD
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
=======

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

>>>>>>> refs/remotes/origin/master

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
