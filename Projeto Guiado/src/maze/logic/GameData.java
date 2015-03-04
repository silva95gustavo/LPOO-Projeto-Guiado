package maze.logic;

public class GameData {
	private Maze map;
	private Hero hero;
	private Sword sword;
	private Dragon[] dragons;
	
	public GameData(Maze map, Hero hero, Sword sword, Dragon[] dragons) {
		this.map = map;
		this.hero = hero;
		this.sword = sword;
		this.dragons = dragons;
	}
	
	public Maze getMap() {
		return map;
	}

	public void setMap(Maze map) {
		this.map = map;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public Sword getSword() {
		return sword;
	}

	public void setSword(Sword sword) {
		this.sword = sword;
	}

	public Dragon[] getDragons() {
		return dragons;
	}

	public void setDragons(Dragon[] dragons) {
		this.dragons = dragons;
	}
}