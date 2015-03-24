package maze.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameScreen extends JPanel {

	/**
	 * Create the panel.
	 */
	public GameScreen() {

	}

	public static void main(String[] args) {
		
		JFrame f = new JFrame("O Gustavo é gay");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		f.setPreferredSize(new Dimension(500,500));
		
		JPanel panel = new GameScreen();
		
		f.getContentPane().add(panel);
		f.pack();
		f.setVisible(true);
		
		panel.requestFocus(); // para receber eventos do teclado       
	}

}
