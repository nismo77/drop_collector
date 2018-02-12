package com.mygdx.drop;

public class Player implements Comparable<Player> {

	private String name;
	private int score;
	
	public Player(String name, int score) {
		this.setName(name);
		this.setScore(score);
	}
	
	public Player() {
		this.setName("Unnamed Player");
		this.setScore(0);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Player arg0) {
		
		int compareScore = ((Player)arg0).getScore();
		
		return this.score - compareScore;
	}
	
}
