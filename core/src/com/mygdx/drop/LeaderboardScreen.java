package com.mygdx.drop;

import java.util.Comparator;
import java.util.Iterator;

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
import com.badlogic.gdx.utils.Array;
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

		back = new TextButton("Back", game.skin, "small");
		titleLabel = new Label("Scoreboard", game.skin, "big-black");
		table.setWidth(stage.getWidth());
		table.align(Align.top | Align.center);

		table.setPosition(0, camHei);
//		table.setDebug(true);

		Gdx.input.setInputProcessor(stage);


		table.add(titleLabel).padBottom(50).colspan(2); // TODO: Change this to flexbile value
		table.row();
		
		scoreFile = Gdx.files.local("hs.hs");
		String tmp="";
		if (scoreFile.exists()) {
			System.out.println("Hs file loaded successfully in scoreboardScreen");
			tmp = scoreFile.readString();
		} else {
			System.out.println("Could not load the " + scoreFile.name());
		}

		if (tmp.isEmpty()) {
			table.add(new Label("No scores yet...", game.skin, "big-black")).colspan(2);
		} else {
			String[] lines = tmp.split("\\r?\\n");
			Array<String[]> scores = new Array<String[]>();
			Array<Player> players = new Array<Player>();

			for (String l : lines) {
				scores.add(l.split(":"));
			}

			Iterator<String[]> iter = scores.iterator();

			while (iter.hasNext()) {
				String[] s = iter.next();
				players.add(new Player(s[0], Integer.parseInt(s[1])));
			}

			players.sort(new Comparator<Player>() {

				@Override
				public int compare(Player p1, Player p2) {
					return p2.compareTo(p1);
				}

			});

			int ps = players.size;
			int k = 0;
			if (ps > 10)
				ps = 10;

			for (Player p : players) {
				if (k == ps)
					break;
				table.add(new Label(p.getName(), game.skin, "big-black"));
				table.add(new Label(Integer.toString(p.getScore()), game.skin, "big-black"));
				table.row();
				k++;
			}
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
