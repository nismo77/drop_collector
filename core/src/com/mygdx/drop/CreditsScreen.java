package com.mygdx.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CreditsScreen implements Screen {

	final Drop game;

	// public Bitmapgame.font game.font;
	private Array<DropRect> droplets;
	Iterator<DropRect> iter, deiter;
 
	int dropSpawnTime, stormTime, flashTime;
	float sekunda, gameTime, drop_amount;
	long lastTimeDrop;
	boolean gameEnd = false;
	boolean flash = false;
	boolean storm = false;
	float dropSpeed, dropSpeedFactor, sflakeSpeed;

	int scrWid = Gdx.graphics.getWidth(), scrHei = Gdx.graphics.getHeight();
	int camWid = Gdx.graphics.getWidth(), camHei = Gdx.graphics.getHeight();

	private BucketRect bucket;
	private OrthographicCamera camera;
	
	private Table table, dialogTable;
	private Stage stage;
	private Window creditWindow;
	private TextButton backBtn;
	private Label gfxLabel, sfxLabel, codeLabel, poweredLabel;

	public CreditsScreen(final Drop game) {
		this.game = game;

		creditWindow = new Window("Credits", game.skin);
		backBtn = new TextButton("Back", game.skin, "small");
		
		gfxLabel = new Label("Gfx: opengameart.org",game.skin, "black");
		sfxLabel = new Label("Sfx: opengameart.org",game.skin, "black");
		codeLabel = new Label("Code: http://nism0.lunarii.org",game.skin, "black");
		poweredLabel = new Label("Powered by: LibGdx",game.skin,"black");
		
		stage = new Stage(new ScreenViewport());
		table = new Table();
		dialogTable = new Table();
		

		table.setWidth(stage.getWidth());
		table.setPosition(0, Gdx.graphics.getHeight());
		table.align(Align.top|Align.center).padTop(20);
	
		dialogTable.align(Align.center|Align.center);
		
		dialogTable.add(gfxLabel).left();
		dialogTable.row();
		dialogTable.add(sfxLabel).left();
		dialogTable.row();
		dialogTable.add(codeLabel).left();
		dialogTable.row();
		dialogTable.add(poweredLabel).left();
		dialogTable.row();
		
		creditWindow.add(dialogTable);
		table.add(creditWindow).width(Gdx.graphics.getWidth()/2);
		
		stage.addActor(backBtn);
		stage.addActor(table);	
		
		backBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.music.stop();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		Gdx.input.setInputProcessor(stage);
		

		dropSpeedFactor = 0.3f;
		dropSpawnTime = 400;

		droplets = new Array<DropRect>();

		// game.font = new Bitmapgame.font();
		game.font.setColor(Color.MAGENTA);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// viewport = new FitViewport(800,600,camera);

		game.imgDrop = new Texture("droplet.png");
		game.imgSnowFlake = new Texture("snowflake.png");
		game.imgWatch = new Texture("watch.png");
		game.imgMeadow = new Texture("meadow.jpg"); // 1200x800


		game.music.setLooping(true);
		game.music.play();

		bucket = new BucketRect(camWid, camHei);
		spawnDrop();
	}

	@Override
	public void show() {
		game.music.play();
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		scrWid = Gdx.graphics.getWidth();
		scrHei = Gdx.graphics.getHeight();

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		camera.setToOrtho(false, camWid, camHei);
		stage.act(Gdx.graphics.getDeltaTime());


		if (scrWid > 1200) {
			camWid = 1200;
		} else
			camWid = Gdx.graphics.getWidth();

		if (scrHei > 800) {
			camHei = 800;
		} else
			camHei = Gdx.graphics.getHeight();

		camera.setToOrtho(false, camWid, camHei);

		game.batch.begin();
		game.batch.draw(game.imgMeadow, 0, 0);
		if (flash) {
			if (sekunda > 0.9000f) {
				game.batch.draw(game.imgMeadow, 0, 0);
				game.batch.draw(game.imgFlash, 0, 0);
			}

		}
		game.font.getData().setScale(camWid / 5000.0f, camHei / 5000.0f);

		for (DropRect raindrop : droplets) {
			raindrop.img.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			game.batch.draw(raindrop.img, raindrop.x, raindrop.y);
		}
		game.batch.end();

		// check if we need to create a new raindrop
		if (TimeUtils.millis() - lastTimeDrop > 5000)
			spawnDrop();

		stage.draw();
		logic();
		
		

	}

	@Override
	public void resize(int width, int height) {

		if (width > 1200) {
			camWid = 1200;
		} else
			camWid = Gdx.graphics.getWidth();

		if (height > 800) {
			camHei = 800;
		} else
			camHei = Gdx.graphics.getHeight();

		camera.setToOrtho(false, camWid, camHei);
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
		game.sndDrop.dispose();
		game.sndWin.dispose();
		game.sndFlake.dispose();
		game.sndBell.dispose();
		game.mscThunder.dispose();
		game.imgBucket.dispose();
		game.imgDrop.dispose();
		game.imgSnowFlake.dispose();
		game.imgWatch.dispose();
		game.imgMeadow.dispose();
		game.imgFlash.dispose();
	}

	public void logic() {

		dropSpeed = dropSpeedFactor * 500;

		Vector3 touchPos = new Vector3();
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touchPos);

		if (Gdx.input.justTouched()) {
			if (Gdx.input.getX() < 0)
				bucket.x = 0;
			else if (Gdx.input.getX() > camWid)
				bucket.x = camWid - 64;
			else
				bucket.x = touchPos.x - 64 / 2;
		}
		if (TimeUtils.millis() > lastTimeDrop + dropSpawnTime)
			spawnDrop();

		iter = droplets.iterator();
		while (iter.hasNext()) {
			DropRect dr = iter.next();
			dr.y -= dropSpeed * Gdx.graphics.getDeltaTime();
			if (dr.y < 0) {
				iter.remove();
			}
		}	
	}

	public void spawnDrop() {

		int rnd = MathUtils.random(0, 8);
		if (rnd == 4) {
			DropRect drop = new DropRect(camWid, camHei, game.imgSnowFlake);
			droplets.add(drop);

		} else if (rnd == 3) {
			DropRect drop = new DropRect(camWid, camHei, game.imgWatch);
			droplets.add(drop);
		} else {
			DropRect drop = new DropRect(camWid, camHei, game.imgDrop);
			droplets.add(drop);
		}

		lastTimeDrop = TimeUtils.millis();

	}

	public void clearDrops(Array<DropRect> drops) {

		deiter = drops.iterator();
		while (deiter.hasNext()) {
			DropRect rd = deiter.next();
			deiter.remove();

		}

	}

}