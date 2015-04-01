package maze.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import maze.logic.Dart;
import maze.logic.Dragon;
import maze.logic.Exit;
import maze.logic.Game;
import maze.logic.GameData;
import maze.logic.Hero;
import maze.logic.Maze;
import maze.logic.MazeBuilder;
import maze.logic.Shield;
import maze.logic.Sword;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class DrawMapWindow extends JPanel implements MouseListener, MouseMotionListener, PropertyChangeListener {

	private JFormattedTextField mapSizeField;
	
	private static final int MAX_SIDE = 25;
	
	char[][] matrix;
	
	private static BufferedImage hero;
	private static BufferedImage dragon;
	private static BufferedImage sword;
	private static BufferedImage wall;
	private static BufferedImage pavement;
	private static BufferedImage shield;
	private static BufferedImage dart;
	
	private int border = 10;
	
	private JFrame frame;
	
	private JButton btnSet;
	private JComboBox<String> pieceBox;
	
	private String[] pieces = new String[] {"Wall", "Hero", "Dragon", "Shield", "Sword", "Dart"};
	private char[] pieces_char = new char[] {'x', 'h', 'd', 's', 'e', 'a'};
	private JButton btnDone;
	
	public void start()
	{
		frame = new JFrame();
		frame.setVisible(true);

		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 616, 676);
		frame.setTitle("Draw maze");
		frame.setMinimumSize(new Dimension(516, 576));
		this.setBounds(100,  100, 516, 576);
		frame.getContentPane().add(this);
		requestFocus();
	}
	
	public DrawMapWindow() {
		setLayout(null);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
			dragon = ImageIO.read(new File("./res/dragon.png"));
			hero = ImageIO.read(new File("./res/hero.png"));
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
		
		mapSizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
		mapSizeField.setHorizontalAlignment(SwingConstants.LEFT);
		mapSizeField.setColumns(2);
		mapSizeField.setBounds(59, 12, 35, 20);
		mapSizeField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		mapSizeField.addPropertyChangeListener("value", this);
		mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
		add(mapSizeField);
		
		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSet.setBounds(99, 11, 70, 23);
		add(btnSet);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblSize.setBounds(0, 15, 70, 14);
		add(lblSize);
		
		pieceBox = new JComboBox();
		pieceBox.setModel(new DefaultComboBoxModel(pieces));
		pieceBox.setBounds(179, 12, 95, 20);
		add(pieceBox);
		
		JButton btnNeedHelp = new JButton("Need help?");
		btnNeedHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Just select the piece in the list on the top and" + '\n' + "left-click to place it. Use the right click to remove" + '\n' + "any piece. Mark the exit by leaving a blank cell on a margin" + '\n' + '\n' + "Rules:" + '\n' + "Max 1 hero, 1 sword, 5 dragons, 1 shield, "+ mapSide()/Game.MAX_DARTS_FACTOR + " darts.", "Help", JOptionPane.QUESTION_MESSAGE);
				frame.setAlwaysOnTop(true);
			}
		});
		btnNeedHelp.setBounds(284, 11, 118, 23);
		add(btnNeedHelp);
		
		btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//COMPLETE
			}
		});
		btnDone.setBounds(403, 11, 89, 23);
		add(btnDone);
		
		matrix = new char[MAX_SIDE][MAX_SIDE];
		for(int i = 0; i < MAX_SIDE; i++)
		{
			for(int j = 0; j < MAX_SIDE; j++)
			{
				matrix[i][j] = ' ';
			}
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		
		for(int i = 0; i < mapSide(); i++)
		{
			for(int j = 0; j < mapSide(); j++)
			{
				showImageCell(g, pavement, j, i);

				if(matrix[i][j] == 'x')
					showImageCell(g, wall, j, i);
				else if(matrix[i][j] == 'd')
					showImageCell(g, dragon, j, i);
				else if(matrix[i][j] == 's')
					showImageCell(g, shield, j, i);
				else if(matrix[i][j] == 'e')
					showImageCell(g, sword, j, i);
				else if(matrix[i][j] == 'a')
					showImageCell(g, dart, j, i);
				else if(matrix[i][j] == 'h')
					showImageCell(g, hero, j, i);
			}
		}
		
		System.out.println(frame.getWidth());
		System.out.println(frame.getHeight());
	}
	
	private void showImageCell(Graphics g, BufferedImage img, int x, int y)
	{
		int xBorder = border;
		int yBorder = border+btnSet.getY() + btnSet.getHeight();

		int minY = btnSet.getY() + btnSet.getHeight();

		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/mapSide();

		g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
				yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	private int mapSide()
	{
		return ((Number)mapSizeField.getValue()).intValue();
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		Number x;
		
		if (source == mapSizeField && mapSizeField != null)
		{
			x = ((Number)mapSizeField.getValue());

			if(x != null && x.intValue() < MazeBuilder.MIN_REC_SIDE)
			{
				mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
				frame.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Minimum maze size is " + MazeBuilder.MIN_REC_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
				frame.setAlwaysOnTop(true);
			}
			else if(x != null && x.intValue() > MAX_SIDE)
			{
				mapSizeField.setValue(MAX_SIDE);
				frame.setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Maximum maze size is " + MAX_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
				frame.setAlwaysOnTop(true);
			}
			else if(x == null)
			{
				mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
			}
		}

		repaint();
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
	
		int xBorder = border;
		int yBorder = border+btnSet.getY() + btnSet.getHeight();

		int minY = btnSet.getY() + btnSet.getHeight();

		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/mapSide();
		
		if(e.getX() <= xBorder || e.getY() <= yBorder || e.getX() >= xBorder+mapSide()*cellSide || e.getY() >= yBorder+mapSide()*cellSide)
			return;
		
		double y = (e.getY() - yBorder)/(cellSide+1);
		double x = (e.getX() - xBorder)/(cellSide+1);
		
		if(e.getButton() == MouseEvent.BUTTON3)
			matrix[(int) Math.round(y)][(int) Math.round(x)] = ' ';
		else
			insertChar((int) Math.round(x), (int) Math.round(y), pieces_char[pieceBox.getSelectedIndex()]);
		
		repaint();
	}
	
	private void insertChar(int x, int y, char c)
	{
		switch(c)
		{
		case 'h':
			deleteCharCount('h', 0);
			matrix[y][x] = c;
			break;
		case 'd':
			deleteCharCount('d', 5);
			matrix[y][x] = c;
			break;
		case 's':
			deleteCharCount('s', 0);
			matrix[y][x] = c;
			break;
		case 'e':
			deleteCharCount('e', 0);
			matrix[y][x] = c;
			break;
		case 'x':
			matrix[y][x] = c;
			break;
		case 'a':
			deleteCharCount('a', Game.MAX_DARTS_FACTOR);
			matrix[y][x] = c;
			break;
		}
	}
	
	private void deleteCharCount(char c, int maxCount)
	{
		int count = 0;
		for(int i = 0; i < mapSide(); i++)
		{
			for(int j = 0; j < mapSide(); j++)
			{
				if(matrix[i][j] == c)
				{
					count++;
					if(count >= maxCount)
					{
						matrix[i][j] = ' ';
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
