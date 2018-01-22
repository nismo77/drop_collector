package com.mygdx.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Drop extends Game {

	Texture imgDrop, imgBucket, imgSnowFlake, imgWatch, imgMeadow, imgFlash;
	
	Sound sndDrop, sndWin, sndFlake, sndBell;
	Music mscThunder;
	

	SpriteBatch batch;
	BitmapFont font;
	public Music music;
	public Skin skin;
	
	public int result = 0 , highScore;
	byte[] hsbyte;

	FileHandle file;
	boolean isHSAv;

	public void create() {
		
		try {
			skin = new Skin(Gdx.files.internal("skin/glassy/glassy-ui.json"));
			System.out.println("Skin loaded successfully");
		}catch(Exception ex) {
			System.out.println("Can't read skin file "+ex);
		}
		hsbyte = new byte[1];

		file = Gdx.files.local("highscore.hs");
		if (!(isHSAv = file.exists())) {
			System.out.println("Cannot open highscore.hs file! Creating a file...");
			file = Gdx.files.local("highscore.hs");
			file.writeBytes(new byte[] { 23 }, false);
		} else {
			// if file exists, read the highscore
			System.out.println("Got the highscore file: " + file.path());
			hsbyte = file.readBytes();
			System.out.println("HS is: "+hsbyte[0]);
			highScore = hsbyte[0];
		}
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();			
		
	}

}