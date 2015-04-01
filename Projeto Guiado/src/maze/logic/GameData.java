package maze.logic;

import java.io.Serializable;

public class GameData implements Serializable
 {
	private Maze map;
	private Hero hero;
	private Sword sword;
	private Dragon[] dragons;
	private Dart[] darts;
	private Shield shield;
	
	public GameData(Maze map, Hero hero, Sword sword, Dragon[] dragons, Dart[] darts, Shield shield) {
		this.map = map;
		this.hero = hero;
		this.sword = sword;
		this.dragons = dragons;
		this.setDarts(darts);
		this.shield = shield;
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

	public Dart[] getDarts() {
		return darts;
	}

	public void setDarts(Dart[] darts) {
		this.darts = darts;
	}
	
	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}
}