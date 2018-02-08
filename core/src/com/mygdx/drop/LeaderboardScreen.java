package com.mygdx.drop;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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

public class LeaderboardScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	private Texture splash;
	private int camWid, camHei;
	private Stage stage;
	private Table table;
	private Label titleLabel;
	private FileHandle scoreFile;		
	private TextButton back;


	public LeaderboardScreen(final Drop gam) {
		game = gam;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camWid = Gdx.graphics.getWidth();
		camHei = Gdx.graphics.getHeight();

		splash = new Texture(Gdx.files.internal("meadow.jpg"));
		stage = new Stage(new ScreenViewport());
		table = new Table();
		
		back = new TextButton("Back",game.skin, "small");
		titleLabel = new Label("Scoreboard", game.skin, "big-black");
		table.setWidth(stage.getWidth());
		table.align(Align.center|Align.center);

		table.setPosition(0, camHei/2);
		table.setDebug(true);
		
		Gdx.input.setInputProcessor(stage);	

		table.add(titleLabel).padBottom(50).colspan(2); // TODO: Change this to flexbile value
		table.row();
		table.add(new Label("Name",game.skin,"black")).width(150).padBottom(20).width(50);
		table.add(new Label("Score",game.skin,"black")).width(150).padBottom(20).width(50);
		table.row(); // <-- TODO: Change this to flexible value
		
		scoreFile = Gdx.files.local("hs.hs");
		if(scoreFile.exists()) {
			System.out.println("Hs file loaded successfully in scoreboardScreen");
		}
		else {
			System.out.println("Could not load the "+scoreFile.name());
		}
		
		String tmp = scoreFile.readString();
		String[] lines = tmp.split("\\r?\\n");	
	
		ArrayList<String[]> scores = new ArrayList<String[]>();
		for(String line : lines) {
			scores.add(line.split(":"));
		}
		for(String[] s : scores) {
			for(String l : s) {
				table.add(new Label(l,game.skin,"black")).padBottom(10).maxWidth(camWid*0.3f);
			}
			table.row();
		}		
		
	
		stage.addActor(table);
		stage.addActor(back);

		
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
	}
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		camera.setToOrtho(false, camWid, camHei);

		stage.act(Gdx.graphics.getDeltaTime());

		game.batch.begin();
		game.batch.draw(splash, 0, 0);
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