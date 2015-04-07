package maze.gui;


import java.awt.*;

import javax.swing.*;

public class GameFrame extends JFrame {

	static String title = "Maze Escape";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameFrame frame = new GameFrame();
					frame.setVisible(true);

					JPanel panel = new GameGraphic(frame);

					frame.getContentPane().add(panel);
					frame.setVisible(true);
					panel.requestFocus();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameFrame() {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 890, 926);
	}

}
