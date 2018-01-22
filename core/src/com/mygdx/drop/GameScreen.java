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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

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

	public GameScreen(final Drop game) {
		this.game = game;

		game.result = 0;
		gameTime = 30.0f;
		dropSpeedFactor = 0.3f;
		dropSpawnTime = 400;

		droplets = new Array<DropRect>();

		// game.font = new Bitmapgame.font();
		game.font.setColor(Color.MAGENTA);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// viewport = new FitViewport(800,600,camera);

		game.imgDrop = new Texture("droplet.png");
		game.imgBucket = new Texture("bucket.png");
		game.imgSnowFlake = new Texture("snowflake.png");
		game.imgWatch = new Texture("watch.png");
		game.imgMeadow = new Texture("meadow.jpg"); // 1200x800
		game.imgFlash = new Texture("thunderflash.png");

		game.sndDrop = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		game.sndFlake = Gdx.audio.newSound(Gdx.files.internal("flakehit.wav"));
		game.sndBell = Gdx.audio.newSound(Gdx.files.internal("bell.wav"));
		game.mscThunder = Gdx.audio.newMusic(Gdx.files.internal("thunder.wav"));

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
		game.imgBucket.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		game.font.draw(game.batch, "Drops Collected: " + game.result, 0, camHei - game.font.getScaleY());
		game.font.draw(game.batch, "Time: " + (int) gameTime, camWid - camWid * 0.3f, camHei - game.font.getScaleY());
		game.batch.draw(game.imgBucket, bucket.x, bucket.y);
		for (DropRect raindrop : droplets) {
			raindrop.img.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			game.batch.draw(raindrop.img, raindrop.x, raindrop.y);
		}
		game.batch.end();

		// check if we need to create a new raindrop
		if (TimeUtils.millis() - lastTimeDrop > 5000)
			spawnDrop();

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
			if (dr.overlaps(bucket)) {
				if (dr.img == game.imgDrop) {
					game.sndDrop.play();
					game.result++;
				} else if (dr.img == game.imgSnowFlake) {
					game.sndFlake.play();
					game.result += 2;
				} else if (dr.img == game.imgWatch) {
					game.sndBell.play();
					gameTime = gameTime + 3 * sekunda;
				}
				// storm happens here
				if (game.result >= 50) {
					storm = true;
					flash = true;
					if (flashTime >= 2)
						flash = false;
					if (stormTime >= 15) {
						storm = false;
					}
					if (storm) {
						game.mscThunder.play();
						dropSpeedFactor = 0.5f;
						dropSpawnTime = 200;
					} else {
						dropSpeedFactor = 0.3f;
						dropSpawnTime = 400;
					}
				}
				iter.remove();
			}
		}
		if (sekunda > 1) {
			gameTime -= sekunda;
			if (storm) {
				stormTime++;
				flashTime++;
				System.out.printf("stormTime: %d\n", stormTime);
				System.out.printf("flashTime: %d\n", flashTime);
			}
			sekunda = 0;
		}

		// check game end
		if (gameTime <= 0) {
			gameEnd = true;
		}

		if (gameTime <= 0) {
			// check highscore
			if (game.result > game.highScore) {
				game.highScore = game.result;
				game.hsbyte[0] = (byte) game.highScore;
				game.file.writeBytes(game.hsbyte, false);
			}

			game.setScreen(new EndScreen(this.game));
		}
		sekunda += Gdx.graphics.getDeltaTime();
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