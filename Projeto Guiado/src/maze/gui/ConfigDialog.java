package maze.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import maze.logic.Dragon;
import maze.logic.MazeBuilder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;

import java.text.Format;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.sun.glass.events.KeyEvent;

@SuppressWarnings("serial")
public class ConfigDialog extends JDialog implements PropertyChangeListener  {

	private final JPanel contentPanel = new JPanel();

	private Configuration config;
	private JFormattedTextField mazeSizeField;
	private JFormattedTextField dragNoField;
	private JComboBox<String> dragModeBox;
	private JComboBox<String> upCmdBox;
	private JComboBox<String> downCmdBox;
	private JComboBox<String> rightCmdBox;
	private JComboBox<String> leftCmdBox;
	private JComboBox<String> upDCmdBox;
	private JComboBox<String> downDCmdBox;
	private JComboBox<String> rightDCmdBox;
	private JComboBox<String> leftDCmdBox;

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
		
		upCmdBox.setSelectedIndex(getIndexFromKey(config.cmdUP));
		downCmdBox.setSelectedIndex(getIndexFromKey(config.cmdDOWN));
		leftCmdBox.setSelectedIndex(getIndexFromKey(config.cmdLEFT));
		rightCmdBox.setSelectedIndex(getIndexFromKey(config.cmdRIGHT));
		upDCmdBox.setSelectedIndex(getIndexFromKey(config.dartUP));
		downDCmdBox.setSelectedIndex(getIndexFromKey(config.dartDOWN));
		leftDCmdBox.setSelectedIndex(getIndexFromKey(config.dartLEFT));
		rightDCmdBox.setSelectedIndex(getIndexFromKey(config.dartRIGHT));

		setVisible(true);
	}

	private int getKeyFromIndex(int index)
	{	
		switch(index)
		{
		case 0:
			return KeyEvent.VK_UP;
		case 1:
			return KeyEvent.VK_DOWN;
		case 2:
			return KeyEvent.VK_LEFT;
		case 3:
			return KeyEvent.VK_RIGHT;
		}
		

		if(index > 3)
		{
			if(index < 30)
				return 61+index;
			else if (index <= 39)
				return 18+index;
		}
		
		return -1;
	}
	
	private int getIndexFromKey(int key)
	{
		switch(key)
		{
		case KeyEvent.VK_UP:
			return 0;
		case KeyEvent.VK_DOWN:
			return 1;
		case KeyEvent.VK_LEFT:
			return 2;
		case KeyEvent.VK_RIGHT:
			return 3;
		}
		
		if(key >= 65 && key <= 90)
			return key - 61;
		else if(key >= 48 && key <= 57)
			return key-18;
		
		return -1;
	}

	public ConfigDialog() {

		setResizable(false);
		setAlwaysOnTop(true);
		setModal(true);
		setType(Type.POPUP);
		setTitle("Game Configuration");
		setBounds(100, 100, 387, 300);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblSettings = new JLabel("Settings:");
		lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSettings.setBounds(0, 11, 351, 14);
		contentPanel.add(lblSettings);

		JLabel lblNumberOfDragons = new JLabel("Maze size:");
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

		JLabel lblNumberOfDragons_1 = new JLabel("Dragon n\u00BA:");
		lblNumberOfDragons_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumberOfDragons_1.setBounds(0, 107, 86, 14);
		contentPanel.add(lblNumberOfDragons_1);

		JLabel lblDragonsMode = new JLabel("Dragons mode:");
		lblDragonsMode.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDragonsMode.setBounds(48, 145, 92, 14);
		contentPanel.add(lblDragonsMode);

		JLabel lblemptyBoxesWill = new JLabel("(empty boxes will be set to default)");
		lblemptyBoxesWill.setHorizontalAlignment(SwingConstants.CENTER);
		lblemptyBoxesWill.setBounds(0, 29, 351, 14);
		contentPanel.add(lblemptyBoxesWill);

		dragModeBox = new JComboBox<String>();
		dragModeBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Not moving", "Moving randomly", "Moving and sleeping", "Always sleeping"}));
		dragModeBox.setBounds(34, 160, 123, 20);
		contentPanel.add(dragModeBox);

		JLabel lblUp = new JLabel("Up");
		lblUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblUp.setBounds(193, 70, 86, 14);
		contentPanel.add(lblUp);

		JLabel lblDown = new JLabel("Down");
		lblDown.setHorizontalAlignment(SwingConstants.CENTER);
		lblDown.setBounds(193, 87, 86, 14);
		contentPanel.add(lblDown);

		JLabel lblRight = new JLabel("Right");
		lblRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblRight.setBounds(193, 120, 86, 20);
		contentPanel.add(lblRight);

		JLabel lblLeft = new JLabel("Left");
		lblLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeft.setBounds(193, 104, 86, 17);
		contentPanel.add(lblLeft);

		String[] cmdOptions = new String[] {"Up Arrow", "Down Arrow", "Left Arrow", "Right Arrow", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

		upCmdBox = new JComboBox<String>();
		upCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		upCmdBox.setBounds(279, 69, 92, 17);
		contentPanel.add(upCmdBox);

		downCmdBox = new JComboBox<String>();
		downCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		downCmdBox.setBounds(279, 86, 92, 17);
		contentPanel.add(downCmdBox);

		leftCmdBox = new JComboBox<String>();
		leftCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		leftCmdBox.setBounds(279, 103, 92, 17);
		contentPanel.add(leftCmdBox);

		rightCmdBox = new JComboBox<String>();
		rightCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		rightCmdBox.setBounds(279, 120, 92, 17);
		contentPanel.add(rightCmdBox);

		rightDCmdBox = new JComboBox<String>();
		rightDCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		rightDCmdBox.setBounds(279, 195, 92, 17);
		contentPanel.add(rightDCmdBox);

		leftDCmdBox = new JComboBox<String>();
		leftDCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		leftDCmdBox.setBounds(279, 178, 92, 17);
		contentPanel.add(leftDCmdBox);

		downDCmdBox = new JComboBox<String>();
		downDCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		downDCmdBox.setBounds(279, 161, 92, 17);
		contentPanel.add(downDCmdBox);

		upDCmdBox = new JComboBox<String>();
		upDCmdBox.setModel(new DefaultComboBoxModel<String>(cmdOptions));
		upDCmdBox.setBounds(279, 144, 92, 17);
		contentPanel.add(upDCmdBox);

		JLabel lblFireDartRight = new JLabel("Fire Dart Right");
		lblFireDartRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartRight.setBounds(193, 195, 86, 20);
		contentPanel.add(lblFireDartRight);

		JLabel lblFireDartLeft = new JLabel("Fire Dart Left");
		lblFireDartLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartLeft.setBounds(193, 179, 86, 17);
		contentPanel.add(lblFireDartLeft);

		JLabel lblFireDartDown = new JLabel("Fire Dart Down");
		lblFireDartDown.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartDown.setBounds(193, 162, 86, 14);
		contentPanel.add(lblFireDartDown);

		JLabel lblFireDartUp = new JLabel("Fire Dart Up");
		lblFireDartUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblFireDartUp.setBounds(193, 145, 86, 14);
		contentPanel.add(lblFireDartUp);


		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
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
				
				config.cmdUP = getKeyFromIndex(upCmdBox.getSelectedIndex());
				config.cmdDOWN = getKeyFromIndex(downCmdBox.getSelectedIndex());
				config.cmdLEFT = getKeyFromIndex(leftCmdBox.getSelectedIndex());
				config.cmdRIGHT = getKeyFromIndex(rightCmdBox.getSelectedIndex());
				config.dartUP = getKeyFromIndex(upDCmdBox.getSelectedIndex());
				config.dartDOWN = getKeyFromIndex(downDCmdBox.getSelectedIndex());
				config.dartLEFT = getKeyFromIndex(leftDCmdBox.getSelectedIndex());
				config.dartRIGHT = getKeyFromIndex(rightDCmdBox.getSelectedIndex());

				setVisible(false);
			}
		});

		JButton btnDefault = new JButton("Reset");
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
				
				upCmdBox.setSelectedIndex(getIndexFromKey(def_config.cmdUP));
				downCmdBox.setSelectedIndex(getIndexFromKey(def_config.cmdDOWN));
				leftCmdBox.setSelectedIndex(getIndexFromKey(def_config.cmdLEFT));
				rightCmdBox.setSelectedIndex(getIndexFromKey(def_config.cmdRIGHT));
				upDCmdBox.setSelectedIndex(getIndexFromKey(def_config.dartUP));
				downDCmdBox.setSelectedIndex(getIndexFromKey(def_config.dartDOWN));
				leftDCmdBox.setSelectedIndex(getIndexFromKey(def_config.dartLEFT));
				rightDCmdBox.setSelectedIndex(getIndexFromKey(def_config.dartRIGHT));
			}
		});
		buttonPane.add(btnDefault);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);


		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);




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
}
