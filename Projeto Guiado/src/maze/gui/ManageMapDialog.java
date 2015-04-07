package maze.gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

import maze.logic.Game;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ManageMapDialog extends JDialog implements MouseListener, MouseMotionListener {

	private JComboBox<String> comboBox;
	private String[] files;
	
	public static String mapName;

	public ManageMapDialog() {
		setTitle("Manage Maps");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 282, 160);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 0, 444, 1);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
		}

		JLabel lblMap = new JLabel("Map:");
		lblMap.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMap.setBounds(38, 11, 44, 24);
		getContentPane().add(lblMap);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(92, 13, 131, 20);

		JButton btnLoadMap = new JButton("Load Map");
		btnLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int index = comboBox.getSelectedIndex();

				if(index == 0)
				{
					JOptionPane.showMessageDialog(null, "Invalid Selection", "Error", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					mapName = files[index];
					setVisible(false);
				}
			}
		});
		btnLoadMap.setBounds(37, 81, 89, 23);
		getContentPane().add(btnLoadMap);

		JButton btnDeleteMap = new JButton("Delete Map");
		btnDeleteMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index = comboBox.getSelectedIndex();

				if(index == 0)
				{
					JOptionPane.showMessageDialog(null, "Invalid Selection", "Error", JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					String name = files[index];
					int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete map " + name + "?", "Delete Game", JOptionPane.YES_NO_OPTION);
					
					if(n == JOptionPane.YES_OPTION)
					{
						File f = new File("./maps/" + name + Game.mapFileExtension);
						
						if(f.delete())
						{
							JOptionPane.showMessageDialog(null, "Map Deleted!", "Delete Map", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Error deleting file", "Error", JOptionPane.ERROR_MESSAGE);
						}
						updateMaps();
					}
				}
			}
		});
		btnDeleteMap.setBounds(136, 81, 89, 23);
		getContentPane().add(btnDeleteMap);
	}
	
	private void updateMaps()
	{
		File folder = new File("./maps");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> maps = new ArrayList<String>();
		maps.add("None");
		
		String extension = "";

		for (int i = 0; i < listOfFiles.length; i++) {
			
			String fileName = listOfFiles[i].getName();
			int index_ext = fileName.lastIndexOf('.');
			if(i>0) extension = fileName.substring(index_ext);
			
			if (listOfFiles[i].isFile() && extension.equals(Game.mapFileExtension))
			{
				fileName = fileName.substring(0, index_ext);
				maps.add(fileName);
			}
		}

		files = maps.toArray(new String[maps.size()]);

		comboBox.setModel(new DefaultComboBoxModel<String>(files));
		getContentPane().remove(comboBox);
		getContentPane().add(comboBox);
	}

	public void display() {
		
		updateMaps();

		setVisible(true);
	}

	public void mouseDragged(MouseEvent arg0) {
		//updateMaps();		
	}
	
	public void mouseMoved(MouseEvent arg0) {
		//updateMaps();
	}

	public void mouseClicked(MouseEvent arg0) {
		updateMaps();
	}

	public void mouseEntered(MouseEvent arg0) {
		updateMaps();
	}
	public void mouseExited(MouseEvent arg0) {
		updateMaps();
	}

	public void mousePressed(MouseEvent arg0) {
		updateMaps();
	}

	public void mouseReleased(MouseEvent arg0) {
		updateMaps();
	}
}
