package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class MainMenuScreen implements Screen {
	
        final Drop game;
        OrthographicCamera camera;
        private Texture splash;
        private int camWid, camHei;
        private Stage stage;
        private Table table;
		private  TextButton startBtn;
		private TextButton quitBtn;
		private TextButton creditsBtn;
		private TextButton scoreBtn;
		private TextButton settingsBtn;
		private Label titleLabel;
		private float btnWid = Gdx.graphics.getWidth()*0.35f;



	public MainMenuScreen(final Drop gam) {
		game = gam;		

		startBtn = new TextButton("Start",game.skin);
		quitBtn = new TextButton("Quit",game.skin);
		creditsBtn = new TextButton("Credits",game.skin);
		scoreBtn = new TextButton("Score board",game.skin);
		settingsBtn = new TextButton("Settings", game.skin);
		titleLabel = new Label("Drop collector",game.skin, "big");

		
		stage = new Stage(new ScreenViewport());
		table = new Table();
//		table.setDebug(true);
		table.setWidth(stage.getWidth());
		table.align(Align.center|Align.center);
		table.setPosition(0, Gdx.graphics.getHeight()/2);

		table.add(titleLabel).padBottom(50);
		table.row();
		table.add(startBtn).width(btnWid).padBottom(10);
		table.row();
		table.add(settingsBtn).width(btnWid).padBottom(10);
		table.row();
		table.add(scoreBtn).width(btnWid).padBottom(10);
		table.row();
		table.add(creditsBtn).width(btnWid).padBottom(10);
		table.row();
		table.add(quitBtn).width(btnWid).padBottom(10);
		
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);
		
		
		settingsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SettingsScreen(game));
			}
		});
		
		startBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
			}
		});
		
		quitBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		scoreBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new LeaderboardScreen(game));
			}
		});
		
		creditsBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new CreditsScreen(game));
			}
		});	
		
		splash = new Texture(Gdx.files.internal("splash_mainmenu.jpg"));
		camWid = splash.getWidth();
		camHei = splash.getHeight();		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, camWid, camHei);	
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