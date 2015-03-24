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
	
	Game game;
	
	public GamePanel() throws IOException {		
		game = new Game(15, 3, Dragon.Dragon_mode.DGN_RAND_SLP);
		
		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
		}
		catch(IOException e)
		{
			System.out.println("Error");
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
		g.drawImage(wall, 50, 50, 200, 200, 0, 0, wall.getWidth(), wall.getHeight(), null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	
	
	public void showGame(GameData gameData, Graphics g)
	{
		int side = gameData.getMap().getSide();
		char[][] matrix = new char[side][side];
		matrix = AlphanumericInterface.placeMaze(matrix, gameData.getMap());
		matrix = AlphanumericInterface.placeEntities(matrix, gameData.getHero(), gameData.getSword(), gameData.getDragons(), gameData.getDarts(), gameData.getShield());
		drawMatrix(matrix, g);
		System.out.print("\n Available darts : " + gameData.getHero().getDarts() + "\n\n");
	}

	public void drawMatrix(char[][] matrix, Graphics g)
	{
		for (int y = 0; y < matrix.length; y++)
		{
			for (int x = 0; x < matrix.length; x++)
			{
				g.drawRect(10*x, 10*y, 5, 5);
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
