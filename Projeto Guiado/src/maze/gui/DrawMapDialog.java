package maze.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Window.Type;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;

import maze.logic.Game;
import maze.logic.MazeBuilder;

public class DrawMapDialog extends JDialog implements MouseListener, MouseMotionListener, PropertyChangeListener {

	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField mapSizeField;
	private JComboBox pieceBox;
	private JButton btnSet;

	private static final int MAX_SIDE = 25;
	private char[][] matrix;

	private static BufferedImage hero;
	private static BufferedImage dragon;
	private static BufferedImage sword;
	private static BufferedImage wall;
	private static BufferedImage pavement;
	private static BufferedImage shield;
	private static BufferedImage dart;

	private int border = 10;

	private String[] pieces = new String[] {"Wall", "Hero", "Dragon", "Shield", "Sword", "Dart"};
	private char[] pieces_char = new char[] {'x', 'h', 'd', 's', 'e', 'a'};

	public DrawMapDialog() {
		setTitle("Draw Map");
		setBounds(100,  100, 516, 576);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setType(Type.POPUP);

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

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(10, 15, 23, 14);
		contentPanel.add(lblSize);
		lblSize.setHorizontalAlignment(SwingConstants.LEFT);


		JButton btnSaveMap = new JButton("Save Map");
		btnSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSaveMap.setBounds(357, 11, 79, 23);
		contentPanel.add(btnSaveMap);


		JButton okButton = new JButton("Close");
		okButton.setBounds(441, 11, 59, 23);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		okButton.setActionCommand("OK");
		contentPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);


		mapSizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
		mapSizeField.setBounds(39, 12, 59, 20);
		mapSizeField.setColumns(2);
		mapSizeField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		mapSizeField.addPropertyChangeListener("value", this);
		mapSizeField.setHorizontalAlignment(SwingConstants.LEFT);
		mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
		contentPanel.add(mapSizeField);

		btnSet = new JButton("Set");
		btnSet.setBounds(108, 11, 49, 23);
		contentPanel.add(btnSet);

		pieceBox = new JComboBox();
		pieceBox.setModel(new DefaultComboBoxModel(pieces));
		pieceBox.setBounds(167, 12, 101, 20);
		contentPanel.add(pieceBox);

		matrix = new char[MAX_SIDE][MAX_SIDE];
		for(int i = 0; i < MAX_SIDE; i++)
		{
			for(int j = 0; j < MAX_SIDE; j++)
			{
				matrix[i][j] = ' ';
			}
		}
	}

	public void start()
	{
		this.setVisible(true);
		repaint();
		requestFocus();
	}

	public void paint(Graphics g) {
		paintComponents(g);

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
	}

	private int mapSide()
	{
		return ((Number)mapSizeField.getValue()).intValue();
	}

	private void showImageCell(Graphics g, BufferedImage img, int x, int y)
	{
		int xBorder = border;
		int yBorder = border+btnSet.getY() + btnSet.getHeight()+20;

		int minY = btnSet.getY() + btnSet.getHeight();

		int maxSide = Math.min(contentPanel.getHeight() - minY , contentPanel.getWidth());
		int cellSide = (maxSide-(2*border))/mapSide();

		g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
				yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
	}


	public void propertyChange(PropertyChangeEvent arg0) {

		Object source = arg0.getSource();
		Number x;

		if (source == mapSizeField && mapSizeField != null)
		{
			x = ((Number)mapSizeField.getValue());

			if(x != null && x.intValue() < MazeBuilder.MIN_REC_SIDE)
			{
				mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
				setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Minimum maze size is " + MazeBuilder.MIN_REC_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
				setAlwaysOnTop(true);
			}
			else if(x != null && x.intValue() > MAX_SIDE)
			{
				mapSizeField.setValue(MAX_SIDE);
				setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(null, "Maximum maze size is " + MAX_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
				setAlwaysOnTop(true);
			}
			else if(x == null)
			{
				mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
			}
		}

		repaint();
		requestFocus();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
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

	@Override
	public void mouseReleased(MouseEvent e) {
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
}
