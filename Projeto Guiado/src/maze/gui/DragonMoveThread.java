package maze.gui;

import java.util.Random;

import maze.logic.Game;

public class DragonMoveThread extends Thread implements Runnable {

	private Game game;
	private int drag_index;
	private GameGraphic caller;
	int start_delay = 0;

	private boolean run = true;

	public DragonMoveThread(Game game, int index, GameGraphic caller)
	{
		this.game = game;
		drag_index = index;
		this.caller = caller;
	}

	public DragonMoveThread(Game game, int index, GameGraphic caller, int delay)
	{
		this.game = game;
		drag_index = index;
		this.caller = caller;
		this.start_delay = delay;
	}

	public void run()
	{
		Random rand = new Random();
		int timer;
		try{
			sleep(start_delay);
		} catch(Exception ex) {}

		while(caller.isGameRunning() && run)
		{
			try {
				game.turnDragao(drag_index);
				Game.event battle = game.combateDragao();
				caller.actOnEvent(battle);

				timer = rand.nextInt(GameGraphic.dragon_timer_max - GameGraphic.dragon_timer_min) + GameGraphic.dragon_timer_min;

				sleep(timer);
			} catch(Exception ex) {}
		}
	}

	public void end()
	{
		run = false;
	}


}
