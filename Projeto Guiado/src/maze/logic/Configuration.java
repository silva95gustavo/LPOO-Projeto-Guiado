package maze.logic;

import java.awt.event.KeyEvent;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Configuration implements Serializable {
	
	// Default configuration
	
	public int side = 15;
	public int dragonNumber = 3;
	public Dragon.Dragon_mode dragonMode = Dragon.Dragon_mode.DGN_RAND_SLP;
	
	public int cmdUP = KeyEvent.VK_UP;
	public int cmdDOWN = KeyEvent.VK_DOWN;
	public int cmdLEFT = KeyEvent.VK_LEFT;
	public int cmdRIGHT = KeyEvent.VK_RIGHT;
	
	public int dartUP = KeyEvent.VK_W;
	public int dartDOWN = KeyEvent.VK_S;
	public int dartLEFT = KeyEvent.VK_A;
	public int dartRIGHT = KeyEvent.VK_D;
	
	public Configuration() {}

}
