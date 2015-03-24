package maze.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javafx.scene.input.KeyCode;

import javax.swing.*;

import maze.logic.Game;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	//BufferedImage hero;
	//BufferedImage wall;
	
	Game game;
	
	int xi, yi;
	
	public GamePanel() {
		xi = 10;
		yi = 10;

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}
				
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLUE);
		g.fillRect(xi, yi, 50, 50);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_LEFT:
			xi--;
			repaint();
			break;
		case KeyEvent.VK_RIGHT:
			xi++;
			repaint();
			break;
		case KeyEvent.VK_UP:
			yi--;
			repaint();
			break;
		case KeyEvent.VK_DOWN:
			yi++;
			repaint();
			break;
		}
	}

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
