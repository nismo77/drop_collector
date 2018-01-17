package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
	
        final Drop game;
        OrthographicCamera camera;
        private Texture splash;
        private int camWid, camHei;
        private Stage stage;
        private Table table;
        private Skin skin;

	public MainMenuScreen(final Drop gam) {
		game = gam;		

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

		stage = new Stage(new ScreenViewport());
		table = new Table();
		table.setWidth(stage.getWidth());
		table.align(Align.center|Align.top);
		table.setPosition(0, Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(stage);

		
		final TextButton startButton = new TextButton("Start", skin);
		final TextButton quitButton =  new TextButton("Quit" , skin);
		
		table.padTop(50);
		table.row();
		table.add(startButton).padBottom(50);
		table.row();
		table.add(quitButton);
		stage.addActor(table);		
		
		splash = new Texture(Gdx.files.internal("splash_mainmenu.jpg"));
		camWid = splash.getWidth();
		camHei = splash.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, camWid, camHei);

		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new EndScreen(game));
				Gdx.app.log("Clicked", "CLICKED_TAG");
			}
		});
		
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();				
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());	
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.getData().setScale(.4f, .4f);
		game.font.setColor(Color.GOLD);
		
		game.batch.draw(splash, 0,0);
		game.batch.end();
		stage.draw();
	
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}