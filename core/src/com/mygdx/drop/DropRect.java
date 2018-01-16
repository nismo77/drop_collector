package com.mygdx.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class DropRect extends Rectangle {

	Texture img;
	DropRect(int scrWidth, int scrHeight, Texture img) {
		this.img = img;
		this.x = MathUtils.random(0, scrWidth - 64);
		this.y = scrHeight + 64;
		this.width = 64;
		this.height = 64;
	}
}