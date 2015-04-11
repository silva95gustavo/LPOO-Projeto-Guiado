package maze.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import maze.logic.Dragon;
import maze.logic.Game;
import maze.logic.MazeBuilder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingConstants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;

import java.text.Format;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.sun.glass.events.KeyEvent;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class ConfigDialog extends JDialog implements PropertyChangeListener, KeyListener  {

	private final JPanel contentPanel = new JPanel();
	
	private static final int width = 387;
	private static final int height = 300;
	
	private boolean keyListen = false;
	private int typedKey;

	private Configuration config;
	
	private JFormattedTextField mazeSizeField;
	private JFormattedTextField dragNoField;
	private JComboBox<String> dragModeBox;
	private JLabel lblSettings;
	private JLabel lblNumberOfDragons;
	private JLabel lblNumberOfDragons_1;
	private JLabel lblDragonsMode;
	private JLabel lblemptyBoxesWill;
	private JLabel lblUp;
	private JLabel lblDown;
	private JLabel lblLeft;
	private JLabel lblRight;
	private JLabel lblFireDartRight;
	private JLabel lblFireDartLeft;
	private JLabel lblFireDartUp;
	private JLabel lblFireDartDown;
	private JButton btnDeleteGame;
	private JPanel buttonPane;
	private JButton okButton;
	private JButton btnDefault;
	private JButton cancelButton;
	
	private JButton btnUpCmd;
	private int upCmd;
	private JButton btnDownCmd;
	private int downCmd;
	private JButton btnLeftCmd;
	private int leftCmd;
	private JButton btnRightCmd;
	private int rightCmd;
	private JButton btnUpDart;
	private int upDart;
	private JButton btnDownDart;
	private int downDart;
	private JButton btnLeftDart;
	private int leftDart;
	private JButton btnRightDart;
	private int rightDart;
	private JButton cmdButtons[];
	private int values[];

	public void display(Configuration config) {

		this.config = config;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		mazeSizeField.setValue(config.side);
		dragNoField.setValue(config.dragonNumber);

		switch(config.dragonMode)
		{
		case DGN_RAND:
			dragModeBox.setSelectedIndex(1);
			break;
		case DGN_RAND_SLP:
			dragModeBox.setSelectedIndex(2);
			break;
		case DGN_SLP:
			dragModeBox.setSelectedIndex(3);
			break;
		case DGN_STILL:
			dragModeBox.setSelectedIndex(0);
			break;
		}
		
		btnUpCmd.setText(getKeyChar(config.cmdUP));
		btnDownCmd.setText(getKeyChar(config.cmdDOWN));
		btnLeftCmd.setText(getKeyChar(config.cmdLEFT));
		btnRightCmd.setText(getKeyChar(config.cmdRIGHT));
		btnUpDart.setText(getKeyChar(config.dartUP));
		btnDownDart.setText(getKeyChar(config.dartDOWN));
		btnLeftDart.setText(getKeyChar(config.dartLEFT));
		btnRightDart.setText(getKeyChar(config.dartRIGHT));
		upCmd = config.cmdUP;
		downCmd = config.cmdDOWN;
		leftCmd = config.cmdLEFT;
		rightCmd = config.cmdRIGHT;
		upDart = config.dartUP;
		downDart = config.dartDOWN;
		leftDart = config.dartLEFT;
		rightDart = config.dartRIGHT;

		updateArrays();
		setVisible(true);
	}

	public ConfigDialog() {

		setResizable(false);
		setAlwaysOnTop(true);
		setModal(true);
		setType(Type.POPUP);
		setTitle("Game Configuration");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int windowX = dim.width/2-width/2;
		int windowY = dim.height/2-height/2;
		setBounds(windowX, windowY, 417, 300);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		initializeElements();
	}
	
	private void initializeElements()
	{
		lblSettings = new JLabel("Settings:");
		lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSettings.setBounds(0, 11, 411, 14);
		contentPanel.add(lblSettings);

		lblNumberOfDragons = new JLabel("Maze size:");
		lblNumberOfDragons.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumberOfDragons.setBounds(0, 79, 86, 14);
		contentPanel.add(lblNumberOfDragons);

		mazeSizeField = new JFormattedTextField(NumberFormat.getNumberInstance());
		mazeSizeField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		mazeSizeField.setColumns(2);
		mazeSizeField.setBounds(85, 76, 72, 20);
		mazeSizeField.addPropertyChangeListener("value", this);
		contentPanel.add(mazeSizeField);

		dragNoField = new JFormattedTextField((Format) null);
		dragNoField.setFocusLostBehavior(JFormattedTextField.COMMIT);
		dragNoField.setColumns(2);
		dragNoField.setBounds(85, 104, 72, 20);
		dragNoField.addPropertyChangeListener("value", this);
		contentPanel.add(dragNoField);

		lblNumberOfDragons_1 = new JLabel("Dragon n\u00BA:");
		lblNumberOfDragons_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumberOfDragons_1.setBounds(0, 107, 86, 14);
		contentPanel.add(lblNumberOfDragons_1);

		lblDragonsMode = new JLabel("Dragons mode:");
		lblDragonsMode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDragonsMode.setBounds(48, 145, 92, 14);
		contentPanel.add(lblDragonsMode);

		lblemptyBoxesWill = new JLabel("(empty boxes will be set to default)");
		lblemptyBoxesWill.setHorizontalAlignment(SwingConstants.CENTER);
		lblemptyBoxesWill.setBounds(0, 29, 411, 14);
		contentPanel.add(lblemptyBoxesWill);

		dragModeBox = new JComboBox<String>();
		dragModeBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Not moving", "Moving randomly", "Moving and sleeping", "Always sleeping"}));
		dragModeBox.setBounds(34, 160, 123, 20);
		contentPanel.add(dragModeBox);

		lblUp = new JLabel("Up");
		lblUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblUp.setBounds(193, 60, 86, 14);
		contentPanel.add(lblUp);

		lblDown = new JLabel("Down");
		lblDown.setHorizontalAlignment(SwingConstants.CENTER);
		lblDown.setBounds(193, 79, 86, 14);
		contentPanel.add(lblDown);

		lblRight = new JLabel("Right");
		lblRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblRight.setBounds(193, 112, 86, 20);
		contentPanel.add(lblRight);

		lblLeft = new JLabel("Left");
		lblLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeft.setBounds(193, 97, 86, 14);
		contentPanel.add(lblLeft);

		lblFireDartRight = new JLabel("Fire Dart Right");
		lblFireDartRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartRight.setBounds(181, 204, 98, 20);
		contentPanel.add(lblFireDartRight);

		lblFireDartLeft = new JLabel("Fire Dart Left");
		lblFireDartLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartLeft.setBounds(181, 185, 98, 17);
		contentPanel.add(lblFireDartLeft);

		lblFireDartDown = new JLabel("Fire Dart Down");
		lblFireDartDown.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartDown.setBounds(181, 166, 98, 14);
		contentPanel.add(lblFireDartDown);

		lblFireDartUp = new JLabel("Fire Dart Up");
		lblFireDartUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartUp.setBounds(181, 145, 98, 14);
		contentPanel.add(lblFireDartUp);
		
		btnDeleteGame = new JButton("Delete a game");
		btnDeleteGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File folder = new File(GameGraphic.gamesDir);
				File[] listOfFiles = folder.listFiles();

				ArrayList<String> games = new ArrayList<String>();

				String extension = "";

				for (int i = 0; i < listOfFiles.length; i++) {

					String fileName = listOfFiles[i].getName();
					int index_ext = fileName.lastIndexOf('.');
					if(index_ext>0) extension = fileName.substring(index_ext);

					if (listOfFiles[i].isFile() && extension.equals(Game.gameFileExtension))
					{
						fileName = fileName.substring(0, index_ext);
						games.add(fileName);
					}
				}

				if(games.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "There are no saved games.", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String files[] = new String[0];

				files = games.toArray(new String[games.size()]);

				String s = (String)JOptionPane.showInputDialog(null, "Please choose a game to delete:", "Delete Game", JOptionPane.PLAIN_MESSAGE, null, files, "files[0]");

				if ((s != null) && (s.length() > 0)) {

					String fileName = GameGraphic.gamesDir + "/" + s + Game.gameFileExtension;
					
					int n = JOptionPane.showConfirmDialog(null, "Are you sure you wish to delete " + s + " ?", "Confirm action", JOptionPane.YES_NO_OPTION);
					
					if(n == JOptionPane.YES_OPTION)
					{
						File deleteFile = new File(fileName);
						
						if(!deleteFile.delete())
						{
							JOptionPane.showMessageDialog(null, "An error has occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
				}
				
				repaint();
				requestFocus();
			}
		});
		btnDeleteGame.setBounds(34, 193, 123, 24);
		contentPanel.add(btnDeleteGame);
		
		btnLeftDart = new JButton("Up Arrow");
		btnLeftDart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leftDart = changeButton(btnLeftDart, leftDart);
			}
		});
		btnLeftDart.setBounds(277, 185, 122, 17);
		contentPanel.add(btnLeftDart);
		
		btnRightDart = new JButton("Up Arrow");
		btnRightDart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rightDart = changeButton(btnRightDart, rightDart);
			}
		});
		btnRightDart.setBounds(277, 206, 122, 17);
		contentPanel.add(btnRightDart);
		
		btnLeftCmd = new JButton("Up Arrow");
		btnLeftCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leftCmd = changeButton(btnLeftCmd, leftCmd);
			}
		});
		btnLeftCmd.setBounds(277, 97, 122, 17);
		contentPanel.add(btnLeftCmd);
		
		btnRightCmd = new JButton("Up Arrow");
		btnRightCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rightCmd = changeButton(btnRightCmd, rightCmd);
			}
		});
		btnRightCmd.setBounds(277, 118, 122, 17);
		contentPanel.add(btnRightCmd);
		
		btnDownDart = new JButton("Up Arrow");
		btnDownDart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downDart = changeButton(btnDownDart, downDart);
			}
		});
		btnDownDart.setBounds(277, 166, 122, 17);
		contentPanel.add(btnDownDart);
		
		btnUpDart = new JButton("Up Arrow");
		btnUpDart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upDart = changeButton(btnUpDart, upDart);
			}
		});
		btnUpDart.setBounds(277, 145, 122, 17);
		contentPanel.add(btnUpDart);
		
		btnDownCmd = new JButton("Up Arrow");
		btnDownCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downCmd = changeButton(btnDownCmd, downCmd);
			}
		});
		btnDownCmd.setBounds(277, 78, 122, 17);
		contentPanel.add(btnDownCmd);
		
		btnUpCmd = new JButton("Up Arrow");
		btnUpCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				upCmd = changeButton(btnUpCmd, upCmd);
			}
		});
		btnUpCmd.setBounds(277, 57, 122, 17);
		contentPanel.add(btnUpCmd);


		buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Configuration def_config = new Configuration();

				if(!mazeSizeField.getText().isEmpty())
					config.side = ((Number)mazeSizeField.getValue()).intValue();
				else
					config.side = def_config.side;

				if(!dragNoField.getText().isEmpty())
					config.dragonNumber = ((Number)dragNoField.getValue()).intValue();
				else
					config.dragonNumber = def_config.dragonNumber;

				switch(dragModeBox.getSelectedIndex())
				{
				case 0:
					config.dragonMode = Dragon.Dragon_mode.DGN_STILL;
					break;
				case 1:
					config.dragonMode = Dragon.Dragon_mode.DGN_RAND;
					break;
				case 2:
					config.dragonMode = Dragon.Dragon_mode.DGN_RAND_SLP;
					break;
				case 3:
					config.dragonMode = Dragon.Dragon_mode.DGN_SLP;
					break;
				}
				
				config.cmdUP = upCmd;
				config.cmdDOWN = downCmd;
				config.cmdLEFT = leftCmd;
				config.cmdRIGHT = rightCmd;
				config.dartUP = upDart;
				config.dartDOWN = downDart;
				config.dartLEFT = leftDart;
				config.dartRIGHT = rightDart;

				setVisible(false);
			}
		});

		btnDefault = new JButton("Reset");
		btnDefault.setHorizontalAlignment(SwingConstants.LEFT);
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration def_config = new Configuration();

				mazeSizeField.setValue(def_config.side);
				dragNoField.setValue(def_config.dragonNumber);

				switch(def_config.dragonMode)
				{
				case DGN_RAND:
					dragModeBox.setSelectedIndex(1);
					break;
				case DGN_RAND_SLP:
					dragModeBox.setSelectedIndex(2);
					break;
				case DGN_SLP:
					dragModeBox.setSelectedIndex(3);
					break;
				case DGN_STILL:
					dragModeBox.setSelectedIndex(0);
					break;
				}
				
				upCmd = def_config.cmdUP;
				btnUpCmd.setText(getKeyChar(upCmd));
				downCmd = def_config.cmdDOWN;
				btnDownCmd.setText(getKeyChar(downCmd));
				leftCmd = def_config.cmdLEFT;
				btnLeftCmd.setText(getKeyChar(leftCmd));
				rightCmd = def_config.cmdRIGHT;
				btnRightCmd.setText(getKeyChar(rightCmd));
				
				upDart = def_config.dartUP;
				btnUpDart.setText(getKeyChar(upDart));
				downDart = def_config.dartDOWN;
				btnDownDart.setText(getKeyChar(downDart));
				leftDart = def_config.dartLEFT;
				btnLeftDart.setText(getKeyChar(leftDart));
				rightDart = def_config.dartRIGHT;
				btnRightDart.setText(getKeyChar(rightDart));
			}
		});
		buttonPane.add(btnDefault);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);


		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		updateArrays();
	}
	
	private int changeButton(JButton button, int currCmd)
	{
		updateArrays();

		disableAllElements();
		
		KeySetDialog d = new KeySetDialog();
		int value = KeySetDialog.keyPressed;
		
		if(value != -1)
		{
			for(int i = 0; i < cmdButtons.length; i++)
			{
				if(cmdButtons[i] != button && values[i] == value)
				{
					values[i] = currCmd;
					cmdButtons[i].setText(getKeyChar(values[i]));
				}
			}
			
			currCmd = value;
			button.setText(getKeyChar(value));
		}
		
		enableAllElements();
		
		return currCmd;
	}
	
	private void updateArrays()
	{
		cmdButtons = new JButton[] {btnUpCmd, btnDownCmd, btnLeftCmd, btnRightCmd, btnUpDart, btnDownDart, btnLeftDart, btnRightDart};
		values = new int[] {upCmd, downCmd, leftCmd, rightCmd, upDart, downDart, leftDart, rightDart};
	}
	
	private void disableAllElements()
	{
		mazeSizeField.setEnabled(false);
		dragNoField.setEnabled(false);
		dragModeBox.setEnabled(false);
		lblSettings.setEnabled(false);
		lblNumberOfDragons.setEnabled(false);
		lblNumberOfDragons_1.setEnabled(false);
		lblDragonsMode.setEnabled(false);
		lblemptyBoxesWill.setEnabled(false);
		lblUp.setEnabled(false);
		lblDown.setEnabled(false);
		lblLeft.setEnabled(false);
		lblRight.setEnabled(false);
		lblFireDartRight.setEnabled(false);
		lblFireDartLeft.setEnabled(false);
		lblFireDartUp.setEnabled(false);
		lblFireDartDown.setEnabled(false);
		btnDeleteGame.setEnabled(false);
		buttonPane.setEnabled(false);
		okButton.setEnabled(false);
		btnDefault.setEnabled(false);
		btnDefault.setEnabled(false);
		cancelButton.setEnabled(false);

		btnUpCmd.setEnabled(false);
		btnDownCmd.setEnabled(false);
		btnLeftCmd.setEnabled(false);
		btnRightCmd.setEnabled(false);
		btnUpDart.setEnabled(false);
		btnDownDart.setEnabled(false);
		btnLeftDart.setEnabled(false);
		btnRightDart.setEnabled(false);
	}
	
	private void enableAllElements()
	{
		mazeSizeField.setEnabled(true);
		dragNoField.setEnabled(true);
		dragModeBox.setEnabled(true);
		lblSettings.setEnabled(true);
		lblNumberOfDragons.setEnabled(true);
		lblNumberOfDragons_1.setEnabled(true);
		lblDragonsMode.setEnabled(true);
		lblemptyBoxesWill.setEnabled(true);
		lblUp.setEnabled(true);
		lblDown.setEnabled(true);
		lblLeft.setEnabled(true);
		lblRight.setEnabled(true);
		lblFireDartRight.setEnabled(true);
		lblFireDartLeft.setEnabled(true);
		lblFireDartUp.setEnabled(true);
		lblFireDartDown.setEnabled(true);
		btnDeleteGame.setEnabled(true);
		buttonPane.setEnabled(true);
		okButton.setEnabled(true);
		btnDefault.setEnabled(true);
		btnDefault.setEnabled(true);
		cancelButton.setEnabled(true);

		btnUpCmd.setEnabled(true);
		btnDownCmd.setEnabled(true);
		btnLeftCmd.setEnabled(true);
		btnRightCmd.setEnabled(true);
		btnUpDart.setEnabled(true);
		btnDownDart.setEnabled(true);
		btnLeftDart.setEnabled(true);
		btnRightDart.setEnabled(true);
	}

	private void enableKeyListen()
	{
		addKeyListener(this);
	}
	
	private void disableKeyListen()
	{
		removeKeyListener(this);
	}
	
	private String getKeyChar(int keyCode)
	{
		switch(keyCode)
		{
		case KeyEvent.VK_UP:
			return "Up Arrow";
		case KeyEvent.VK_DOWN:
			return "Down Arrow";
		case KeyEvent.VK_LEFT:
			return "Left Arrow";
		case KeyEvent.VK_RIGHT:
			return "Right Arrow";
		case KeyEvent.VK_TAB:
			return "Tab";
		case KeyEvent.VK_SHIFT:
			return "Shift";
		case KeyEvent.VK_CAPS_LOCK:
			return "Caps Lock";
		case KeyEvent.VK_PLUS:
			return "+";
		case KeyEvent.VK_MINUS:
			return "-";
		case KeyEvent.VK_PERIOD:
			return ".";
		case KeyEvent.VK_ENTER:
			return "Enter";
		}
		
		return "" + (char)keyCode;
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		Number x = 0;

		if (source == mazeSizeField && mazeSizeField != null)
		{
			x = ((Number)mazeSizeField.getValue());

			if(x != null && x.intValue() < MazeBuilder.MIN_REC_SIDE)
			{
				mazeSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
				JOptionPane.showMessageDialog(null, "Minimum maze size is " + MazeBuilder.MIN_REC_SIDE, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x == null)
			{
				mazeSizeField.setValue(MazeBuilder.MIN_REC_SIDE);
			}
		}
		else if(source == dragNoField && dragNoField != null)
		{
			x = ((Number)dragNoField.getValue());

			if(x != null && x.intValue() == 0)
			{
				dragNoField.setValue(MazeBuilder.MIN_DRAGS);
				JOptionPane.showMessageDialog(null, "Minimum dragon mumber is " + MazeBuilder.MIN_DRAGS, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x != null && x.intValue() < MazeBuilder.MIN_DRAGS)
			{
				dragNoField.setValue(MazeBuilder.MIN_DRAGS);
				JOptionPane.showMessageDialog(null, "Minimum dragon mumber is " + MazeBuilder.MIN_DRAGS, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x != null && x.intValue() > MazeBuilder.MAX_DRAGS)
			{
				dragNoField.setValue(MazeBuilder.MAX_DRAGS);
				JOptionPane.showMessageDialog(null, "Minimum dragon mumber is " + MazeBuilder.MAX_DRAGS, "Warning", JOptionPane.WARNING_MESSAGE);
			}
			else if(x == null)
			{
				dragNoField.setValue(MazeBuilder.MIN_DRAGS);
			}
		}

		repaint();
	}

	
	public void keyPressed(java.awt.event.KeyEvent arg0) {

		if(keyListen)
		{
			typedKey = arg0.getKeyCode();
			keyListen = false;
		}
	}

	
	public void keyReleased(java.awt.event.KeyEvent arg0) {}

	
	public void keyTyped(java.awt.event.KeyEvent arg0) {}
}
