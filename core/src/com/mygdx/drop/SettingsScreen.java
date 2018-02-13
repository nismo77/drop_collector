package com.mygdx.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	private FileHandle hsFile;
	private Texture splash;
	private int camWid, camHei;
	private Stage stage;
	private Table table;
	private TextButton backBtn;
	private TextButton clearLeadBtn;

	private Label titleLabel;
	private Label clearLabel;
	private Dialog clearedDlg;
	private float btnWid = Gdx.graphics.getWidth() * 0.35f;

	private boolean fileMissing = false;

	public SettingsScreen(final Drop gam) {
		game = gam;

		try {
			hsFile = Gdx.files.local("hs.hs");
			System.out.printf("[INFO] Got the hs file");
		} catch (Exception e) {
			System.out.println("[ERROR] Can't get hs file (missing?)");
			fileMissing = true;
		}
		camWid = Gdx.graphics.getWidth();
		camHei = Gdx.graphics.getHeight();
		clearedDlg = new Dialog("Scores cleared..", game.skin);
		clearLeadBtn = new TextButton("Clear", game.skin,"small");
		backBtn = new TextButton("Back", game.skin,"small");
		titleLabel = new Label("Settings", game.skin, "big");
		clearLabel = new Label("Scores", game.skin,"big");

		stage = new Stage(new ScreenViewport());
		table = new Table();
		table.setWidth(stage.getWidth());
		table.setPosition(0, camHei);
		table.align(Align.top | Align.center);

		table.add(titleLabel).padBottom(100).colspan(2);
		table.row();
		table.add(clearLabel).width(btnWid).padBottom(50);
		table.add(clearLeadBtn).width(btnWid).padBottom(50);

		stage.addActor(table);
		stage.addActor(backBtn);

		//table.setDebug(true);
		Gdx.input.setInputProcessor(stage);

		backBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MainMenuScreen(game));
			}
		});

		clearLeadBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!fileMissing) {
					hsFile.delete();
					clearedDlg.show(stage);
					Timer.schedule(new Task() {

						@Override
						public void run() {
							clearedDlg.hide();
							game.prefs.clear();
						}
					}, 1);
				}
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
		game.batch.draw(splash, 0, 0);
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