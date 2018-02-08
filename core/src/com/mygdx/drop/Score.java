package com.mygdx.drop;

import java.util.ArrayList;

public class Score {
	
	private ArrayList<Player> scores = new ArrayList<Player>();
	
	public void setScore(Player player) {
		scores.add(player);
	}
	public Score(Player player) {
		scores.add(player);
	}
	
	public Score() {
		
	}
}
