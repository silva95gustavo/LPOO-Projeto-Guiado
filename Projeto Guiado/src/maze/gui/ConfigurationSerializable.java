package maze.gui;

import java.io.Serializable;

import maze.logic.Dragon;

@SuppressWarnings("serial")
class ConfigurationSerializable implements Serializable {
	
	public int side;
	public int dragonNumber;
	public int dragModeValue;
	
	public int cmdUP;
	public int cmdDOWN;
	public int cmdLEFT;
	public int cmdRIGHT;
	
	public int dartUP;
	public int dartDOWN;
	public int dartLEFT;
	public int dartRIGHT;
	
	public ConfigurationSerializable(Configuration config)
	{
		side = config.side;
		dragonNumber = config.dragonNumber;
		cmdUP = config.cmdUP;
		cmdDOWN = config.cmdDOWN;
		cmdLEFT = config.cmdLEFT;
		cmdRIGHT = config.cmdRIGHT;
		dartUP = config.dartUP;
		dartDOWN = config.dartDOWN;
		dartLEFT = config.dartLEFT;
		dartRIGHT = config.dartRIGHT;
		
		switch(config.dragonMode)
		{
		case DGN_STILL:
			dragModeValue = 0;
			break;
		case DGN_RAND:
			dragModeValue = 1;
			break;
		case DGN_RAND_SLP:
			dragModeValue = 2;
			break;
		case DGN_SLP:
			dragModeValue = 3;
			break;
		}
	}
	
	public Configuration toConfig()
	{
		Configuration config = new Configuration();
		
		config.side = side;
		config.dragonNumber = dragonNumber;
		config.cmdUP = cmdUP;
		config.cmdDOWN = cmdDOWN;
		config.cmdLEFT = cmdLEFT;
		config.cmdRIGHT = cmdRIGHT;
		config.dartUP = dartUP;
		config.dartDOWN = dartDOWN;
		config.dartLEFT = dartLEFT;
		config.dartRIGHT = dartRIGHT;
		
		switch(dragModeValue)
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
		
		return config;
	}
}
