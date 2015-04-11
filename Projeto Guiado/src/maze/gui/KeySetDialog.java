package maze.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class KeySetDialog extends JDialog implements KeyListener {
	
	public static int keyPressed;

	public KeySetDialog() {
		setResizable(false);
		setType(Type.POPUP);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int windowX = dim.width/2-288/2;
		int windowY = dim.height/2-288/2;
		setBounds(windowX, windowY, 288, 67);
		
		getContentPane().setLayout(new BorderLayout());
		{
			JLabel lblPressAnyKey = new JLabel("Press any key to set it as the command key");
			lblPressAnyKey.setHorizontalAlignment(SwingConstants.CENTER);
			getContentPane().add(lblPressAnyKey, BorderLayout.CENTER);
		}
		
		setAlwaysOnTop(true);
		addKeyListener(this);
		requestFocus();
		keyPressed = -1;
	}

	public void keyPressed(KeyEvent arg0) {
		keyPressed = arg0.getKeyCode();
		setVisible(false);
		this.dispose();
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}
}
