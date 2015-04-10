package maze.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import maze.logic.Game;
import maze.logic.MazeBuilder;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class DrawMapWindow extends JPanel implements MouseListener, MouseMotionListener, PropertyChangeListener {

	private JFormattedTextField mapSizeField;
	private boolean leftBtnPressed = false;
	private boolean rightBtnPressed = false;

	public static enum pathImpossibility {
		/**
		 * No impossibility found
		 */
		NONE,

		/**
		 * There is no valid path to all the dragons, so it is impossible to kill them all
		 */
		DRAGONS,

		/**
		 * There is no valid path to the sword, so it is impossible to kill all the dragons due to lack of darts
		 */
		SWORD,

		/**
		 * There is no valid path to the shield, so it is impossible to kill all the dragons without them killing the hero with fire
		 */
		SHIELD,

		/**
		 * There is no valid path to the exit, so the hero can't escape the maze
		 */
		EXIT
	};

	private static final int default_width = 516;
	private static final int default_height = 576;

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
	private JButton btnNeedHelp;
	private JComboBox<String> pieceBox;

	private String[] pieces = new String[] {"Wall", "Hero", "Dragon", "Shield", "Sword", "Dart"};
	private char[] pieces_char = new char[] {'X', 'h', 'd', 's', 'e', 'a'};
	private JButton btnDone;

	private int heroCol = -1;
	private int heroLin = -1;
	private int dragonCount = 0;
	private int numDarts = 0;
	private int numDrags = 0;

	public void start()
	{
		frame = new JFrame();
		frame.setVisible(true);

		frame.setVisible(true);
		frame.setBounds(100, 100, 610, 676);
		frame.setTitle("Draw maze");
		frame.setMinimumSize(new Dimension(516, 576));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int windowX = dim.width/2-default_width/2;
		int windowY = dim.height/2-default_height/2;
		frame.setBounds(windowX, windowY, default_width, default_height);

		frame.getContentPane().add(this);
		requestFocus();
	}

	public DrawMapWindow() {

		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		try
		{
			initializeImages();
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}

		initializeWindowElements();

		matrix = new char[MAX_SIDE][MAX_SIDE];
		for(int i = 0; i < MAX_SIDE; i++)
		{
			for(int j = 0; j < MAX_SIDE; j++)
			{
				matrix[i][j] = ' ';
			}
		}

	}

	private void initializeImages() throws IOException
	{
		wall = ImageIO.read(new File("./res/wall.jpg"));
		dragon = ImageIO.read(new File("./res/dragon.png"));
		hero = ImageIO.read(new File("./res/hero.png"));
		sword = ImageIO.read(new File("./res/sword.png"));
		pavement = ImageIO.read(new File("./res/pavement.jpg"));
		shield = ImageIO.read(new File("./res/shield.png"));
		dart = ImageIO.read(new File("./res/dart.png"));
	}

	private void initializeWindowElements()
	{
		mapSizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
		mapSizeField.setHorizontalAlignment(SwingConstants.LEFT);
		mapSizeField.setColumns(3);
		mapSizeField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		mapSizeField.addPropertyChangeListener("value", this);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblSize = new JLabel("Size:");
		add(lblSize);
		lblSize.setHorizontalAlignment(SwingConstants.CENTER);
		mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
		add(mapSizeField);

		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		add(btnSet);

		pieceBox = new JComboBox<String>();
		pieceBox.setModel(new DefaultComboBoxModel<String>(pieces));
		add(pieceBox);

		btnNeedHelp = new JButton("Need help?");
		btnNeedHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Just select the piece in the list on the top and" + '\n' + "left-click to place it. Use the right click to remove" + '\n' + "any piece. Mark the exit by leaving a blank cell on a margin" + '\n' + '\n' + "Rules:" + '\n' + "The maze must have 1 hero, 1 sword, 1-5 dragons, 1 shield, 0-"+ mapSide()/Game.MAX_DARTS_FACTOR + " darts (depends on map size)." + '\n' + "The hero can't be next to any dragon(s)." + '\n' + "There can only be linear paths on the maze.", "Help", JOptionPane.QUESTION_MESSAGE);
			}
		});
		add(btnNeedHelp);

		btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(!isValidMap())
				{
					JOptionPane.showMessageDialog(null, "This map is invalid." + '\n' + '\n' + "Check the help section to see the rules.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else
				{
					pathImpossibility result = isPossibleMap();
					int n = 0;
					boolean ask = false;

					switch(result)
					{
					case DRAGONS:
						n = JOptionPane.showConfirmDialog(null, "This map is impossible to solve, since there is no valid path" + '\n' + "to all the dragons. Are you sure you wish to save it?", "Draw Map", JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.NO_OPTION)
							return;
						break;
					case EXIT:
						n = JOptionPane.showConfirmDialog(null, "This map is impossible to solve, since there is no valid path" + '\n' + "to the exit. Are you sure you wish to save it?", "Draw Map", JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.NO_OPTION)
							return;
						break;
					case SHIELD:
						n = JOptionPane.showConfirmDialog(null, "This map is impossible to solve, since there is no valid path" + '\n' + "to the shield, and because the hero can't get enough darts," + '\n' + "he will die by fire. Are you sure you wish to save it?", "Draw Map", JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.NO_OPTION)
							return;
						break;
					case SWORD:
						n = JOptionPane.showConfirmDialog(null, "This map is impossible to solve, since there is no valid path" + '\n' + "to the sword, and because the hero can't get enough darts," + '\n' + "he will not be able to kill all the dragons." + '\n' + "Are you sure you wish to save it?", "Draw Map", JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.NO_OPTION)
							return;
						break;
					default:
						ask = true;
						break;
					}

					if(ask)
						n = JOptionPane.showConfirmDialog(null, "Do you wish to save this map?", "Draw Map", JOptionPane.YES_NO_OPTION);
					else
						n = JOptionPane.YES_OPTION;

					if(n == JOptionPane.YES_OPTION)
					{
						String file = new String();						

						while(file.isEmpty())
						{
							file = JOptionPane.showInputDialog(null, "Map name:", "Save map", JOptionPane.PLAIN_MESSAGE);

							if(file == null)
							{
								JOptionPane.showMessageDialog(null, "Map saving cancelled", "Warning", JOptionPane.WARNING_MESSAGE);
								return;
							}

							file = file + Game.mapFileExtension;
							File f = new File("./maps/" + file);

							if(f.exists() && !f.isDirectory())
							{
								JOptionPane.showMessageDialog(null, "Map name already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
								file = "";
							}
							else
							{
								try
								{
									int side = mapSide();
									char[][] map = new char[side][side];

									for(int i = 0; i < side; i++)
									{
										for(int j = 0; j < side; j++)
										{
											map[i][j] = matrix[i][j];
										}
									}

									ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
									oos.writeObject(map);
									oos.close();
								}
								catch(Exception exc) {
									JOptionPane.showMessageDialog(null, "Error opening file. Please try a different name.", "Warning", JOptionPane.WARNING_MESSAGE);
									file = "";
								}
							}

						}

						JOptionPane.showMessageDialog(null, "Map saved", "Message", JOptionPane.PLAIN_MESSAGE);
					}
					else
						return;
				}

				frame.setVisible(false);
				frame.dispose();
			}
		});
		add(btnDone);
	}

	private boolean isValidMap()
	{		
		int side = mapSide();

		heroCol = -1;
		heroLin = -1;
		dragonCount = 0;
		numDarts = 0;
		numDrags = 0;

		// Check walls and exit
		{			
			int countExit = 0;
			if(matrix[0][0] != 'X' || matrix[0][side-1] != 'X' || matrix[side-1][0] != 'X' || matrix[side-1][side-1]!='X')
				return false;

			for(int i = 1; i < side-1; i++)
			{
				if(matrix[0][i] == ' ')
					countExit++;
				else if(matrix[0][i] != 'X')
					return false;

				if(matrix[side-1][i] == ' ')
					countExit++;
				else if(matrix[side-1][i] != 'X')
					return false;

				if(matrix[i][0] == ' ')
					countExit++;
				else if(matrix[i][0] != 'X')
					return false;

				if(matrix[i][side-1] == ' ')
					countExit++;
				else if(matrix[i][side-1] != 'X')
					return false;
			}

			if(countExit != 1)
				return false;
		}

		// Check map elements
		{
			int heroCount = 0;
			dragonCount = 0;
			int swordCount = 0;
			int shieldCount = 0;

			for(int i = 0; i < side; i++)
			{
				for(int j = 0; j < side; j++)
				{
					if(matrix[i][j] == 'h')
					{
						heroLin = i;
						heroCol = j;
						heroCount++;

						if((i < side-1 && matrix[i+1][j] == 'd') || (i > 0 && matrix[i-1][j] == 'd') || (j < side-1 && matrix[i][j+1] == 'd') || (j > 0 && matrix[i][j-1] == 'd'))
							return false;

						if(i == 0 || i == side-1 || j == 0 || j == side-1)
							return false;
					}

					if(matrix[i][j] == 'd')
					{
						dragonCount++;

						if(i == 0 || i == side-1 || j == 0 || j == side-1)
							return false;
					}

					if(matrix[i][j] == 's')
					{
						shieldCount++;

						if(i == 0 || i == side-1 || j == 0 || j == side-1)
							return false;
					}

					if(matrix[i][j] == 'e')
					{
						swordCount++;

						if(i == 0 || i == side-1 || j == 0 || j == side-1)
							return false;
					}
				}
			}

			if(heroCount!=1 || dragonCount<1 || shieldCount!=1 || swordCount!=1)
				return false;
		}

		// Check linear paths
		for(int i = 1; i < side-2; i++)
		{
			for(int j = 1; j < side-2; j++)
			{
				if(matrix[i][j] != 'X' && matrix[i+1][j] != 'X' && matrix[i][j+1] != 'X' && matrix[i+1][j+1] != 'X')
					return false;
			}
		}

		return true;
	}

	private pathImpossibility isPossibleMap()
	{
		// Check path to exit
		int side = mapSide();
		boolean visited[][] = new boolean[side][side];
		resetVisitedMatrix(visited);
		boolean exit = checkHeroPathExit(side, visited, heroCol, heroLin);
		if(!exit) return pathImpossibility.EXIT;

		resetVisitedMatrix(visited);
		boolean sword = checkHeroPathSword(side, visited, heroCol, heroLin);
		resetVisitedMatrix(visited);
		boolean shield = checkHeroPathShield(side, visited, heroCol, heroLin);
		resetVisitedMatrix(visited);
		checkHeroPathDragons(side, visited, heroCol, heroLin);
		resetVisitedMatrix(visited);
		checkHeroPathDarts(side, visited, heroCol, heroLin);

		if(numDrags != dragonCount) return pathImpossibility.DRAGONS;

		if(numDarts == dragonCount) return pathImpossibility.NONE;

		if(!sword) return pathImpossibility.SWORD;

		if(!shield) return pathImpossibility.SHIELD;		

		return pathImpossibility.NONE;
	}
	
	private void resetVisitedMatrix(boolean visited[][])
	{
		for(int i = 0; i < visited.length; i++)
		{
			for(int j = 0; j < visited.length; j++)
				visited[i][j] = false;
		}
	}

	private boolean checkHeroPathExit(int side, boolean visited[][], int heroCol, int heroLin)
	{
		if(heroCol < 0 || heroCol >= side || heroLin < 0 || heroLin >= side)
			return false;

		if(visited[heroLin][heroCol])
			return false;

		visited[heroLin][heroCol] = true;

		if(matrix[heroLin][heroCol] == 'X')
			return false;

		if(isExit(heroCol, heroLin))
			return true;

		if(checkHeroPathExit(side, visited, heroCol+1, heroLin))
			return true;
		if(checkHeroPathExit(side, visited, heroCol-1, heroLin))
			return true;
		if(checkHeroPathExit(side, visited, heroCol, heroLin+1))
			return true;
		if(checkHeroPathExit(side, visited, heroCol, heroLin-1))
			return true;

		return false;
	}

	private boolean checkHeroPathShield(int side, boolean visited[][], int heroCol, int heroLin)
	{
		if(heroCol < 0 || heroCol >= side || heroLin < 0 || heroLin >= side)
			return false;

		if(visited[heroLin][heroCol])
			return false;

		visited[heroLin][heroCol] = true;

		if(matrix[heroLin][heroCol] == 'X')
			return false;

		if(isShield(heroCol, heroLin))
			return true;

		if(checkHeroPathShield(side, visited, heroCol+1, heroLin))
			return true;
		if(checkHeroPathShield(side, visited, heroCol-1, heroLin))
			return true;
		if(checkHeroPathShield(side, visited, heroCol, heroLin+1))
			return true;
		if(checkHeroPathShield(side, visited, heroCol, heroLin-1))
			return true;

		return false;
	}

	private boolean checkHeroPathSword(int side, boolean visited[][], int heroCol, int heroLin)
	{
		if(heroCol < 0 || heroCol >= side || heroLin < 0 || heroLin >= side)
			return false;

		if(visited[heroLin][heroCol])
			return false;

		visited[heroLin][heroCol] = true;

		if(matrix[heroLin][heroCol] == 'X')
			return false;

		if(isSword(heroCol, heroLin))
			return true;

		if(checkHeroPathSword(side, visited, heroCol+1, heroLin))
			return true;
		if(checkHeroPathSword(side, visited, heroCol-1, heroLin))
			return true;
		if(checkHeroPathSword(side, visited, heroCol, heroLin+1))
			return true;
		if(checkHeroPathSword(side, visited, heroCol, heroLin-1))
			return true;

		return false;
	}

	// Always returns false, but puts in numDarts the number of darts with a path to the hero
	private boolean checkHeroPathDarts(int side, boolean visited[][], int heroCol, int heroLin)
	{
		if(heroCol < 0 || heroCol >= side || heroLin < 0 || heroLin >= side)
			return false;

		if(visited[heroLin][heroCol])
			return false;

		visited[heroLin][heroCol] = true;

		if(matrix[heroLin][heroCol] == 'X')
			return false;

		if(isDart(heroCol, heroLin)) numDarts++;

		if(checkHeroPathDarts(side, visited, heroCol+1, heroLin))
			return true;
		if(checkHeroPathDarts(side, visited, heroCol-1, heroLin))
			return true;
		if(checkHeroPathDarts(side, visited, heroCol, heroLin+1))
			return true;
		if(checkHeroPathDarts(side, visited, heroCol, heroLin-1))
			return true;

		return false;
	}

	// Always returns false, but puts in numDrags the number of dragons with a path to the hero
	private boolean checkHeroPathDragons(int side, boolean visited[][], int heroCol, int heroLin)
	{
		if(heroCol < 0 || heroCol >= side || heroLin < 0 || heroLin >= side)
			return false;

		if(visited[heroLin][heroCol])
			return false;

		visited[heroLin][heroCol] = true;

		if(matrix[heroLin][heroCol] == 'X')
			return false;

		if(isDragon(heroCol, heroLin)) numDrags++;

		if(checkHeroPathDragons(side, visited, heroCol+1, heroLin))
			return true;
		if(checkHeroPathDragons(side, visited, heroCol-1, heroLin))
			return true;
		if(checkHeroPathDragons(side, visited, heroCol, heroLin+1))
			return true;
		if(checkHeroPathDragons(side, visited, heroCol, heroLin-1))
			return true;

		return false;
	}

	private void updateDrawMouse(MouseEvent e)
	{
		int xBorder = border;
		int yBorder = border+btnSet.getY() + btnSet.getHeight();

		int minY = btnSet.getY() + btnSet.getHeight();

		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/mapSide();

		if(e.getX() <= xBorder || e.getY() <= yBorder || e.getX() >= xBorder+mapSide()*cellSide || e.getY() >= yBorder+mapSide()*cellSide)
			return;

		double y = (e.getY() - yBorder)/(cellSide+1);
		double x = (e.getX() - xBorder)/(cellSide+1);

		if(rightBtnPressed)
			matrix[(int) Math.round(y)][(int) Math.round(x)] = ' ';
		else if(leftBtnPressed)
			insertChar((int) Math.round(x), (int) Math.round(y), pieces_char[pieceBox.getSelectedIndex()]);

		repaint();
	}

	private boolean isExit(int col, int lin)
	{
		int side = mapSide();

		if(col < 0 || col >= side || lin < 0 || lin >= side)
			return false;

		if(matrix[lin][col] == ' ' && (col == 0 || col == side-1 || lin == 0 || lin == side-1))
			return true;

		return false;
	}

	private boolean isShield(int col, int lin)
	{
		int side = mapSide();

		if(col < 0 || col >= side || lin < 0 || lin >= side)
			return false;

		if(matrix[lin][col] == 's')
			return true;

		return false;
	}

	private boolean isSword(int col, int lin)
	{
		int side = mapSide();

		if(col < 0 || col >= side || lin < 0 || lin >= side)
			return false;

		if(matrix[lin][col] == 'e')
			return true;

		return false;
	}

	private boolean isDart(int col, int lin)
	{
		int side = mapSide();

		if(col < 0 || col >= side || lin < 0 || lin >= side)
			return false;

		if(matrix[lin][col] == 'a')
			return true;

		return false;
	}

	private boolean isDragon(int col, int lin)
	{
		int side = mapSide();

		if(col < 0 || col >= side || lin < 0 || lin >= side)
			return false;

		if(matrix[lin][col] == 'd')
			return true;

		return false;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);

		for(int i = 0; i < mapSide(); i++)
		{
			for(int j = 0; j < mapSide(); j++)
			{
				showImageCell(g, pavement, j, i);

				if(matrix[i][j] == 'X')
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

	private void showImageCell(Graphics g, BufferedImage img, int x, int y)
	{
		int minHeight = btnSet.getY() + btnSet.getHeight();

		int gridWidth = this.getWidth()-2*border;
		int gridHeight = this.getHeight() - minHeight - 2*border;
		int minSize = Math.min(gridWidth, gridHeight);
		int mazeSize = mapSide();
		int cellSize = (int)minSize/mazeSize;

		int xBorder = (int)((this.getWidth()-cellSize*mazeSize)/2);
		int yBorder = (int)((this.getHeight()-minHeight-cellSize*mazeSize)/2);

		g.drawImage(img, xBorder+x*cellSize, minHeight+yBorder+y*cellSize, xBorder+x*cellSize + cellSize,
				minHeight+yBorder+y*cellSize+cellSize, 0, 0, img.getWidth(), img.getHeight(), null);


		/*int xBorder = border;
		int yBorder = border+btnSet.getY() + btnSet.getHeight();

		int minY = btnSet.getY() + btnSet.getHeight();

		int maxSide = Math.min(this.getHeight() - minY , this.getWidth());
		int cellSide = (maxSide-(2*border))/mapSide();

		g.drawImage(img, xBorder+x*cellSide, yBorder+y*cellSide, xBorder+x*cellSide+cellSide,
				yBorder+y*cellSide+cellSide, 0, 0, img.getWidth(), img.getHeight(), null);*/
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
				JOptionPane.showMessageDialog(null, "Minimum maze size is " + MazeBuilder.MIN_REC_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x != null && x.intValue() > MAX_SIDE)
			{
				mapSizeField.setValue(MAX_SIDE);
				JOptionPane.showMessageDialog(null, "Maximum maze size is " + MAX_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x == null)
			{
				mapSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
			}
		}

		repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		updateDrawMouse(arg0);
	}


	public void mouseMoved(MouseEvent arg0) {
		updateDrawMouse(arg0);
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftBtnPressed = true;

		if(e.getButton() == MouseEvent.BUTTON3)
			rightBtnPressed = true;

		updateDrawMouse(e);

		if(e.getButton() == MouseEvent.BUTTON1)
			leftBtnPressed = false;

		if(e.getButton() == MouseEvent.BUTTON3)
			rightBtnPressed = false;
	}


	public void mouseEntered(MouseEvent e) {		
		updateDrawMouse(e);
	}

	public void mouseExited(MouseEvent e) {
		updateDrawMouse(e);
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftBtnPressed = true;

		if(e.getButton() == MouseEvent.BUTTON3)
			rightBtnPressed = true;

		updateDrawMouse(e);	
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
		case 'X':
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

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			leftBtnPressed = false;

		if(e.getButton() == MouseEvent.BUTTON3)
			rightBtnPressed = false;

		updateDrawMouse(e);
	}
}
