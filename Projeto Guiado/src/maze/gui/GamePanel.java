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

	//BufferedImage hero;
	BufferedImage wall;
	BufferedImage grass;
	
	Game game;
	
	public GamePanel() throws IOException {		
		game = new Game(15, 3, Dragon.Dragon_mode.DGN_RAND_SLP);
		
		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
			grass = ImageIO.read(new File("./res/pavement.jpg"));
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
