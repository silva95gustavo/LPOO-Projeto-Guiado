package maze.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import maze.cli.AlphanumericInterface;
import maze.logic.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	BufferedImage hero;
	BufferedImage hero_shielded;
	BufferedImage hero_armed;
	BufferedImage hero_armed_shielded;
	BufferedImage dragon;
	BufferedImage sword;
	BufferedImage wall;
	BufferedImage pavement;

	Game game;

	public GamePanel() throws IOException {		
		game = new Game(15, 3, Dragon.Dragon_mode.DGN_RAND_SLP);

		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
			pavement = ImageIO.read(new File("./res/pavement.jpg"));
			dragon = ImageIO.read(new File("./res/dragon.png"));
			hero = ImageIO.read(new File("./res/hero.png"));
			hero_shielded = ImageIO.read(new File("./res/hero_shielded.png"));
			hero_armed = ImageIO.read(new File("./res/hero_armed.png"));
			hero_armed_shielded = ImageIO.read(new File("./res/hero_armed_shielded.png"));
			sword = ImageIO.read(new File("./res/sword.png"));
		}
		catch(IOException e)
		{

			System.exit(1);
		}


		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		showGame(game.getGameData(), g);
		//g.drawImage(hero, 10, 10, 200, 200, 0, 0, wall.getWidth(), wall.getHeight(), null);
		if(game.getGameData().getHero().isArmed())
			System.out.println("ARMED");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_UP:
			game.turn(Game.command.MOVE, Game.Direction.UP);
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			game.turn(Game.command.MOVE, Game.Direction.DOWN);
			repaint();
			break;
		case KeyEvent.VK_LEFT:
			game.turn(Game.command.MOVE, Game.Direction.LEFT);
			repaint();
			break;
		case KeyEvent.VK_RIGHT:
			game.turn(Game.command.MOVE, Game.Direction.RIGHT);
			repaint();
			break;
		case KeyEvent.VK_W:
			game.turn(Game.command.FIRE, Game.Direction.UP);
			repaint();
			break;
		case KeyEvent.VK_A:
			game.turn(Game.command.FIRE, Game.Direction.LEFT);
			repaint();
			break;
		case KeyEvent.VK_S:
			game.turn(Game.command.FIRE, Game.Direction.DOWN);
			repaint();
			break;
		case KeyEvent.VK_D:
			game.turn(Game.command.FIRE, Game.Direction.RIGHT);
			repaint();
			break;
		}
	}



	public void showGame(GameData gameData, Graphics g)
	{
		Maze map = gameData.getMap();

		Hero hero = gameData.getHero();
		Dart darts[] = gameData.getDarts();
		Shield shield = gameData.getShield();
		Dragon[] dragons = gameData.getDragons();
		Sword sword = gameData.getSword();

		int maxSide = Math.min(this.getHeight(), this.getWidth());

		int border = 10;
		int cellSide = (maxSide-(2*border))/map.getSide();
		
		//g.drawImage(pavement, 0, 0, this.getWidth()-1, this.getHeight()-1, 0, 0, wall.getWidth(), wall.getHeight(), null);

		for (int y = 0; y < map.getSide(); y++)
		{
			for (int x = 0; x < map.getSide(); x++)
			{	
				if(map.isWall(x,  y))
				{
					g.drawImage(wall, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
				}
				else
				{
					//g.drawImage(pavement, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
				}

				if(x == hero.getX() && y == hero.getY())
				{
					if(hero.isArmed())
					{
						if(hero.isShielded())
							g.drawImage(this.hero_armed_shielded, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
						else
							g.drawImage(this.hero_armed, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
					}
					else
					{
						if(hero.isShielded())
							g.drawImage(this.hero_shielded, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
						else
						{
							g.drawImage(this.hero, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
							//System.out.println("hero");
						}
					}
				}
				
				for(int i = 0; i < darts.length; i++)
				{
					if(x == darts[i].getX() && y == darts[i].getY() && darts[i].isDropped())
					{
						//g.drawImage(this.dart, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
					}
				}
				
				if(x == shield.getX() && y == shield.getY() && shield.isDropped())
				{
					//g.drawImage(this.shield, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
				}
				
				if(x == sword.getX() && y == sword.getY() && sword.isDropped())
				{
					g.drawImage(this.sword, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
				}
				
				for(int i = 0; i < dragons.length; i++)
				{
					if(dragons[i].isAlive() && x == dragons[i].getX() && y == dragons[i].getY())
					{
						if(dragons[i].isSleeping())
							g.drawImage(this.pavement, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
						else
							g.drawImage(this.dragon, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
					}
				}

				//g.drawImage(wall, 50, 50, 200, 200, 0, 0, wall.getWidth(), wall.getHeight(), null);
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
