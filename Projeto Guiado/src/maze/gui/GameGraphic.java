package maze.gui;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import maze.logic.*;

@SuppressWarnings("serial")
public class GameGraphic extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	private static BufferedImage hero;
	private static BufferedImage hero_shielded;
	private static BufferedImage hero_armed;
	private static BufferedImage hero_armed_shielded;
	private static BufferedImage dragon;
	private static BufferedImage dragon_sleeping;
	private static BufferedImage sword;
	private static BufferedImage wall;
	private static BufferedImage pavement;
	private static BufferedImage shield;
	private static BufferedImage dart;
	private int border = 10;
	
	private JLabel lblDarts;
	private JButton btnNewGame;

	Game game;

	public GameGraphic() throws IOException {		
		game = null;

		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
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
				System.exit(0);
			}
		});
		
		lblDarts = new JLabel("Darts : 0");
		lblDarts.setToolTipText("Shoot darts at the dragons with W, A, S and D");
		add(lblDarts);
		
		btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game = new Game(15, 3, Dragon.Dragon_mode.DGN_RAND_SLP);
				repaint();
				requestFocus();
			}
		});
		add(btnNewGame);
		add(btnCloseGame);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		if(game != null)
			showGame(game.getGameData(), g);
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
			switch(arg0.getKeyCode())
			{
			case KeyEvent.VK_UP:
				ret = game.turn(Game.command.MOVE, Game.Direction.UP);
				break;
			case KeyEvent.VK_DOWN:
				ret = game.turn(Game.command.MOVE, Game.Direction.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				ret = game.turn(Game.command.MOVE, Game.Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				ret = game.turn(Game.command.MOVE, Game.Direction.RIGHT);
				break;
			case KeyEvent.VK_W:
				ret = game.turn(Game.command.FIRE, Game.Direction.UP);
				break;
			case KeyEvent.VK_A:
				ret = game.turn(Game.command.FIRE, Game.Direction.LEFT);
				break;
			case KeyEvent.VK_S:
				ret = game.turn(Game.command.FIRE, Game.Direction.DOWN);
				break;
			case KeyEvent.VK_D:
				ret = game.turn(Game.command.FIRE, Game.Direction.RIGHT);
				break;
			}
		}
		
		switch(ret)
		{
		case LOSE:
			JOptionPane.showMessageDialog(null, "Ups... A dragon killed you...\nTry picking up a sword next time!");
			resetGame();
			break;
		case LOSE_FIRE:
			JOptionPane.showMessageDialog(null, "Ups... You were killed by dragon fire...\nTry protecting yourself with a shield next time!");
			resetGame();
			break;
		case SHIELDED:
			JOptionPane.showMessageDialog(null, "You are now protected against dragon fire!");
			break;
		case WIN:
			JOptionPane.showMessageDialog(null, "CONGRATULATIONS! You escaped the maze!\nCome back and try again!");
			resetGame();
			break;
		}
		
		repaint();
	}
	
	private void showImageCell(Graphics g, BufferedImage img, int x, int y)
	{
		int xBorder = border;
		int yBorder = border+btnNewGame.getY() + btnNewGame.getHeight();
		
		int minY = btnNewGame.getY() + btnNewGame.getHeight();
		
		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/game.getGameData().getMap().getSide();
		
		g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
				yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
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
				if(map.isExit(x, y) && exit.isVisible())
				{
					showImageCell(g, pavement, x, y);
				}
				else if(map.isWall(x,  y))
				{
					showImageCell(g, wall, x, y);
				}
				else
				{
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



	// Unimplemented ////////////////////////////////////////////////////////////////////////


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

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

}
