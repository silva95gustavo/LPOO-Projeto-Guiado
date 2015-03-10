package maze.test;

import static org.junit.Assert.*;

import org.junit.Test;

import maze.logic.*;

public class TestGameMechanics {

	@Test
	public void testMoveHero() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		GameData data = tGame.getGameData();
		int x, y;
		x = data.getHero().getX();
		y = data.getHero().getY();
		
		assertEquals(1.0, x, 0.0);
		assertEquals(1.0, y, 0.0);
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(2.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(3.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(4.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(4.0, x, 0.0);
		assertEquals(2.0, y, 0.0);
	}
	
	@Test
	public void testMoveHeroWall() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		GameData data = tGame.getGameData();
		int x, y;
		
		tGame.turn(Game.command.MOVE, Game.Direction.UP);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(1.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(2.0, x, 0.0);
		assertEquals(1.0, y, 0.0);
	}
	
	@Test
	public void testCatchSword() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);

		GameData data = tGame.getGameData();
		
		assertTrue(data.getHero().isArmed());
		
	}

	@Test
	public void testBeingKilled() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);	
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);

		GameData data = tGame.getGameData();
		
		assertFalse(data.getHero().isAlive());
		
	}
	
	@Test
	public void testKillDragon() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);

		GameData data = tGame.getGameData();
		
		assertFalse(data.getDragons()[0].isAlive());
		
	}
	
	@Test
	public void testExit() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		Game.event jogada;
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		jogada = tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		
		assertEquals(jogada, Game.event.WIN);
		
	}
	
	@Test
	public void testNotExit() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		Game.event jogada;
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		jogada = tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		
		assertEquals(jogada, Game.event.NONE);
		
		tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.LEFT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.DOWN);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		tGame.turn(Game.command.MOVE, Game.Direction.UP);
		jogada = tGame.turn(Game.command.MOVE, Game.Direction.RIGHT);
		
		assertEquals(jogada, Game.event.NONE);
	}
}
