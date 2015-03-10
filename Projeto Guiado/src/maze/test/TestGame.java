package maze.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	TestGameMechanics.class,
	TestMapGeneration.class
})

public class TestGame {
}
