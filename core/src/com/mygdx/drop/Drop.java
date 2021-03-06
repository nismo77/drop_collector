package com.mygdx.drop;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
	
	public String[] names;
	public String[] scores;
	

	SpriteBatch batch;
	BitmapFont font;
	public Music music;
	public Skin skin;
	public Preferences prefs;
	public int result = 0 , highScore = 0 ;
	byte[] hsbyte;

	FileHandle file;
	boolean isHSAv;

	public void create() {
		
		prefs = Gdx.app.getPreferences("dropcollector.prefs");
		highScore = prefs.getInteger("highscore");
		if(highScore == 0) {
			prefs.putInteger("highscore", 0);
		}
		try {
			skin = new Skin(Gdx.files.internal("skin/glassy/glassy-ui.json"));
			System.out.println("Skin loaded successfully");
		}catch(Exception ex) {
			System.out.println("Can't read skin file "+ex);
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