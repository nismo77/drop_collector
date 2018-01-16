package com.mygdx.drop;

import com.badlogic.gdx.math.Rectangle;

public class BucketRect extends Rectangle {

	BucketRect(int scrWidth, int scrHeight) {
		this.x = scrWidth / 2 - 64 / 2;
		this.y = 0;
		this.width = 64;
		this.height = 64;
	}

}