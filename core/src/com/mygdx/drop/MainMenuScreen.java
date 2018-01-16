package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {
        final Drop game;
        OrthographicCamera camera;
        private Texture splash;
        private int camWid, camHei;

	public MainMenuScreen(final Drop gam) {
		game = gam;
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
		game.font.getData().setScale(.4f, .4f);
		game.font.setColor(Color.GOLD);
		
		game.batch.draw(splash, 0,0);
		game.font.draw(game.batch, "Welcome to Drop! ", camWid/2-180, camHei-camHei*0.1f);
		game.font.draw(game.batch, "Tap anywhere to begin!", camWid/2-280, camHei/2);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
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
	}
}