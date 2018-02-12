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
		private float btnWid = Gdx.graphics.getWidth()*0.35f;
		
		private boolean fileMissing = false;



	public SettingsScreen(final Drop gam) {
		game = gam;		

		try {
			hsFile = Gdx.files.local("hs.hs");
			System.out.printf("[INFO] Got the hs file");
		}catch(Exception e) {
			System.out.println("[ERROR] Can't get hs file (missing?)");
			fileMissing = true;
		}
		clearedDlg = new Dialog("Scoreboard cleared...",game.skin);
		clearLeadBtn = new TextButton("Clear",game.skin);
		backBtn = new TextButton("Back",game.skin);
		titleLabel = new Label("Settings",game.skin, "big");
		clearLabel = new Label("Clear scoreboard", game.skin, "big-black");

		
		stage = new Stage(new ScreenViewport());
		table = new Table();
//		table.setDebug(true);
		table.setWidth(stage.getWidth());
		table.align(Align.center|Align.center);
		table.setPosition(0, Gdx.graphics.getHeight()/2);

		table.add(titleLabel).padBottom(100);
		table.row();
		table.add(clearLabel).width(btnWid).padBottom(50);
		table.add(clearLeadBtn).width(btnWid).padBottom(50);
	
		
		stage.addActor(table);
		stage.addActor(backBtn);

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
				if(!fileMissing) {
					hsFile.delete();
					clearedDlg.show(stage);
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