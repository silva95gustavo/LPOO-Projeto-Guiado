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
			pavement = ImageIO.read(new File("./res/grass.jpg"));
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
		//g.drawImage(wall, 10, 10, 20, 200, 0, 0, wall.getWidth(), wall.getHeight(), null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		//System.out.println(this.getWidth());
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
					g.drawImage(grass, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
				}

				if(x == hero.getX() && y == hero.getY())
				{
					if(hero.isArmed())
					{
						if(hero.isShielded())
						{
							g.drawImage(this.hero_armed_shielded, border+x*cellSide, border+y*cellSide, border+x*cellSide+cellSide, border+y*cellSide+cellSide, 0, 0, wall.getWidth(), wall.getHeight(), null);
						}
					}
				}



				/*
				 // Hero
		if (hero.isArmed())
			matrix[hero.getY()][hero.getX()] = armed_hero_char;
		else
			matrix[hero.getY()][hero.getX()] = hero_char;

		// Darts
		for(int i = 0; i < darts.length; i++)
		{
			if(darts[i].isDropped())
				matrix[darts[i].getY()][darts[i].getX()] = dart_char;
		}

		if(shield.isDropped())
			matrix[shield.getY()][shield.getX()] = shield_shar;

		// Dragons
		for (int i = 0; i < dragons.length; i++)
		{
			if (dragons[i].isAlive())
			{
				if (dragons[i].isSleeping())
					matrix[dragons[i].getY()][dragons[i].getX()] = dragon_sleeping_char;
				else
					matrix[dragons[i].getY()][dragons[i].getX()] = dragon_char;
			}
		}

		// Sword
		if (sword.isDropped())
		{
			if (matrix[sword.getY()][sword.getX()] == dragon_char)
				matrix[sword.getY()][sword.getX()] = dragon_sword_char;
			else if (matrix[sword.getY()][sword.getX()] == dragon_sleeping_char)
				matrix[sword.getY()][sword.getX()] = dragon_sleeping_sword_char;
			else
				matrix[sword.getY()][sword.getX()] = sword_char;
		}
		return matrix;
				 */

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
