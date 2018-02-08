package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScreen implements Screen {
	
	final Drop game;
	
	private Sound winSound;
	private OrthographicCamera camera;
	private Texture splash;
    private int camWid, camHei;
    private Stage stage;
    private Table table;
    
    
	
	
	public EndScreen(final Drop game) {		
		this.game = game;
		camera = new OrthographicCamera();
		splash = new Texture(Gdx.files.internal("splash_mainmenu.jpg"));
		camWid = splash.getWidth();
		camHei = splash.getHeight();
		
		stage = new Stage(new ScreenViewport());
		table = new Table();
		
		final TextButton yesButton = new TextButton("Yes",game.skin);
		final TextButton noButton =  new TextButton("No" ,game.skin);
		final TextButton againButton =  new TextButton("Again?" ,game.skin);

	    final Label againLabel = new Label("Play again?",game.skin, "big");
	    
	   
	    table.setWidth(stage.getWidth());
	    table.align(Align.top|Align.center);
	    table.setPosition(0, (Gdx.graphics.getHeight()/2)+yesButton.getHeight());
//	    table.setDebug(true);


	    
//	    table.padTop();
	    table.add(againLabel).colspan(2).padBottom(camHei*0.05f);
	    table.row();
	    table.add(yesButton).center();
	    table.add(noButton).center();
	    
	    stage.addActor(table);
	    
	    Gdx.input.setInputProcessor(stage);
	    
	    yesButton.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		game.setScreen(new GameScreen(game));
	    	}
	    });
	    
	    noButton.addListener(new ClickListener() {
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		Gdx.app.exit();
	    	}
	    });

		camera.setToOrtho(false, camWid, camHei);
		winSound = Gdx.audio.newSound( (Gdx.files.internal("wygrana.wav") ) );
	}

	@Override
	public void show() {
		game.music.stop();
		winSound.play();
		

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(splash, 0,0);
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		
//		game.batch.begin();		
//		game.batch.draw(splash, 0, 0);
//		game.font.getData().setScale(0.3f, 0.3f);
//		game.font.draw(game.batch, "Your result is: "+game.result, camWid/2, camHei/2);
//		game.font.draw(game.batch, "HIGHSCORE: "+game.highScore, camWid/2, camHei/2-80);
//		game.font.draw(game.batch, "Tap to play again... ", camWid/2, camHei/2-200);
//
//		game.batch.end();
		
		
//		float delay = 1; // seconds
//		Timer.schedule(new Task() {
//			@Override
//			public void run() {
//				if(Gdx.input.justTouched()) {
//					
//					game.setScreen(new GameScreen(game));
//				}				
//			}			
//		}, delay);	
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.dispose();
	}
}
