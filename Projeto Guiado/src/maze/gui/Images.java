package maze.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	public static BufferedImage hero;
	public static BufferedImage hero_shielded;
	public static BufferedImage hero_armed;
	public static BufferedImage hero_armed_shielded;
	public static BufferedImage dragon;
	public static BufferedImage dragon_sleeping;
	public static BufferedImage sword;
	public static BufferedImage wall;
	public static BufferedImage pavement_wall;
	public static BufferedImage pavement;
	public static BufferedImage shield;
	public static BufferedImage dart;
	public static BufferedImage exit_open;
	public static BufferedImage exit_closed;
	public static BufferedImage game_paused;
	
	public static void load()
	{
		try
		{
			wall = ImageIO.read(new File("./res/wall.jpg"));
			pavement_wall = ImageIO.read(new File("./res/pavement_wall.jpg"));
			dragon = ImageIO.read(new File("./res/dragon.png"));
			dragon_sleeping = ImageIO.read(new File("./res/dragon_sleeping.png"));
			hero = ImageIO.read(new File("./res/hero.png"));
			hero_shielded = ImageIO.read(new File("./res/hero_shielded.png"));
			hero_armed = ImageIO.read(new File("./res/hero_armed.png"));
			hero_armed_shielded = ImageIO.read(new File("./res/hero_armed_shielded.png"));
			sword = ImageIO.read(new File("./res/sword.png"));
			pavement = ImageIO.read(new File("./res/pavement.jpg"));
			shield = ImageIO.read(new File("./res/shield.png"));
			dart = ImageIO.read(new File("./res/dart.png"));
			exit_open = ImageIO.read(new File("./res/exit_open.png"));
			exit_closed = ImageIO.read(new File("./res/exit_closed.png"));
			game_paused = ImageIO.read(new File("./res/game_paused.jpg"));
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
}
