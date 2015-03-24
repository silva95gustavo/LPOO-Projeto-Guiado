package maze.test;

import static org.junit.Assert.assertEquals;
import maze.logic.DefaultMaze;
import maze.logic.Dragon;
import maze.logic.Game;
import maze.logic.GameData;

import org.junit.Test;

public class TestMapGeneration {

	@Test
	public void testMultipleDragons() {
		Game tGame = new Game(20, 5, Dragon.Dragon_mode.DGN_RAND_SLP);

		GameData data = tGame.getGameData();

		assertEquals(5.0, data.getDragons().length, 0.0);
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[0].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[1].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[2].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[3].getMode());
		assertEquals(Dragon.Dragon_mode.DGN_RAND_SLP, data.getDragons()[4].getMode());

	}
	
	@Test
	public void testMapSize() {
		Game tGame = new Game(Dragon.Dragon_mode.DGN_RAND_SLP, true);
		GameData data = tGame.getGameData();
		
		assertEquals(data.getMap().getSide(), DefaultMaze.defaultMatrix.length);
		
		tGame = new Game(18);
		data = tGame.getGameData();
		assertEquals(data.getMap().getSide(), 18);
	}
}
