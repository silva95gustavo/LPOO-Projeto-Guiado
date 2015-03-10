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
		
		tGame.turn("d");

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(2.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn("d");

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(3.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn("d");

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(4.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn("s");

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
		
		tGame.turn("w");

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(1.0, x, 0.0);
		assertEquals(1.0, y, 0.0);

		tGame.turn("d");
		tGame.turn("s");

		data = tGame.getGameData();
		x = data.getHero().getX();
		y = data.getHero().getY();

		assertEquals(2.0, x, 0.0);
		assertEquals(1.0, y, 0.0);
	}
	
	@Test
	public void testCatchSword() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");

		GameData data = tGame.getGameData();
		
		assertTrue(data.getHero().isArmed());
		
	}

	@Test
	public void testBeingKilled() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");	
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("w");

		GameData data = tGame.getGameData();
		
		assertFalse(data.getHero().isAlive());
		
	}
	
	@Test
	public void testKillDragon() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");

		GameData data = tGame.getGameData();
		
		assertFalse(data.getDragons()[0].isAlive());
		
	}
	
	@Test
	public void testExit() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		Game.event jogada;
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("s");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		jogada = tGame.turn("d");
		
		assertEquals(jogada, Game.event.WIN);
		
	}
	
	@Test
	public void testNotExit() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		Game.event jogada;
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		jogada = tGame.turn("d");
		
		assertEquals(jogada, Game.event.NONE);
		
		tGame = new Game(Dragon.Dragon_mode.DGN_STILL, true);
		
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("a");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("s");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("s");
		tGame.turn("d");
		tGame.turn("d");
		tGame.turn("w");
		tGame.turn("w");
		tGame.turn("w");
		jogada = tGame.turn("d");
		
		assertEquals(jogada, Game.event.NONE);
	}
}
