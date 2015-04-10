package maze.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import maze.logic.Dart;
import maze.logic.Dragon;
import maze.logic.Exit;
import maze.logic.Game.Direction;
import maze.logic.GameData;
import maze.logic.Hero;
import maze.logic.Maze;
import maze.logic.Shield;
import maze.logic.Sword;

public class MapDrawer {

	Maze m;

	MapDrawer(Maze m)
	{
		this.m = m;
	}
	
	public void draw(GameData gameData, Direction hero_direction, Graphics g, int x, int y, int width, int length)
	{
		Hero hero = gameData.getHero();
		Dart darts[] = gameData.getDarts();
		Shield shield = gameData.getShield();
		Dragon[] dragons = gameData.getDragons();
		Sword sword = gameData.getSword();
		Exit exit = m.getExit();

		for (int j = 0; j < m.getSide(); j++)
		{
			for (int i = 0; i < m.getSide(); i++)
			{
				if(m.isExit(i, j))
				{
					if(j>0 && m.isWall(i, j-1))
						showImageCell(g, Images.pavement_wall, x, y, width, length, i, j);
					else
						showImageCell(g, Images.pavement, x, y, width, length, i, j);

					if (exit.isVisible())
						showExitCellRot(g, Images.exit_open, x, y, width, length, i, j);
					else
						showExitCellRot(g, Images.exit_closed, x, y, width, length, i, j);
				}
				else if(m.isWall(i,  j))
				{
					showImageCell(g, Images.wall, x, y, width, length, i, j);
				}
				else
				{
					if (j > 0 && m.isWall(i, j - 1) && !(m.isExit(i, j - 1)))
						showImageCell(g, Images.pavement_wall, x, y, width, length, i, j);
					else
						showImageCell(g, Images.pavement, x, y, width, length, i, j);
				}

				if(i == hero.getX() && j == hero.getY())
				{
					if(hero.isArmed())
					{
						if(hero.isShielded())
							showHeroCell(hero_direction, g, Images.hero_armed_shielded, x, y, width, length, i, j);
						else
							showHeroCell(hero_direction, g, Images.hero_armed, x, y, width, length, i, j);
					}
					else
					{
						if(hero.isShielded())
							showHeroCell(hero_direction, g, Images.hero_shielded, x, y, width, length, i, j);
						else
						{
							showHeroCell(hero_direction, g, Images.hero, x, y, width, length, i, j);
						}
					}
				}

				for(int k = 0; k < darts.length; k++)
				{
					if(i == darts[k].getX() && j == darts[k].getY() && darts[k].isDropped())
					{
						showImageCell(g, Images.dart, x, y, width, length, i, j);
					}
				}

				if(i == shield.getX() && j == shield.getY() && shield.isDropped())
				{
					showImageCell(g, Images.shield, x, y, width, length, i, j);
				}

				if(i == sword.getX() && j == sword.getY() && sword.isDropped())
				{
					showImageCell(g, Images.sword, x, y, width, length, i, j);
				}

				for(int k = 0; k < dragons.length; k++)
				{
					if(dragons[k].isAlive() && i == dragons[k].getX() && j == dragons[k].getY())
					{
						if(dragons[k].isSleeping())
							showImageCell(g, Images.dragon_sleeping, x, y, width, length, i, j);
						else
							showImageCell(g, Images.dragon, x, y, width, length, i, j);
					}
				}
			}
		}
	}

	private void showImageCell(Graphics g, BufferedImage img, int x, int y, int canvasWidth, int canvasHeight, int cellX, int cellY)
	{
		int min = Math.min(canvasWidth, canvasHeight);
		int cellSide = min / m.getSide();
		x += (canvasWidth - min) / 2;
		y += (canvasHeight - min) / 2;
		
		int cellStartX = x + cellX * cellSide;
		int cellStartY = y + cellY * cellSide;

		g.drawImage(img, cellStartX, cellStartY, cellStartX + cellSide, cellStartY + cellSide, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	private void showExitCellRot(Graphics g, BufferedImage img, int x, int y, int canvasWidth, int canvasHeight, int cellX, int cellY)
	{
		int min = Math.min(canvasWidth, canvasHeight);
		int cellSide = min / m.getSide();
		x += (canvasWidth - min) / 2;
		y += (canvasHeight - min) / 2;
		
		int cellStartX = x + cellX * cellSide;
		int cellStartY = y + cellY * cellSide;
		
		int rotation_factor = 0;

		if(cellX == 0)
			rotation_factor = 1;
		else if(cellY != 0)
		{
			if(cellY > cellX)
				rotation_factor = 2;
			else
				rotation_factor = 3;
		}

		int imgW = img.getWidth();
		int imgH = img.getHeight();

		AffineTransform at = new AffineTransform();
		at.translate(cellStartX + cellSide / 2, cellStartY + cellSide / 2);
		at.rotate(rotation_factor*Math.PI/2);
		at.scale((double)cellSide / imgW, (double)cellSide / imgH);
		at.translate(-imgW / 2, -imgH / 2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
	}

	private void showHeroCell(Direction hero_direction, Graphics g, BufferedImage img, int x, int y, int canvasWidth, int canvasHeight, int cellX, int cellY)
	{
		int min = Math.min(canvasWidth, canvasHeight);
		int cellSide = min / m.getSide();
		x += (canvasWidth - min) / 2;
		y += (canvasHeight - min) / 2;
		
		int cellStartX = x + cellX * cellSide;
		int cellStartY = y + cellY * cellSide;
		
		int rotation_factor = 0;

		switch(hero_direction)
		{
		case RIGHT:
			rotation_factor = 1;
			break;
		case DOWN:
			rotation_factor = 2;
			break;
		case LEFT:
			rotation_factor = 3;
			break;
		default:			// just to prevent warnings
			break;
		}

		int heroW = img.getWidth();
		int heroH = img.getHeight();

		AffineTransform at = new AffineTransform();
		at.translate(cellStartX + cellSide / 2, cellStartY + cellSide / 2);
		at.rotate(rotation_factor*Math.PI/2);
		at.scale((double)cellSide / heroW, (double)cellSide / heroH);
		at.translate(-heroW / 2, -heroH / 2);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
	}
}