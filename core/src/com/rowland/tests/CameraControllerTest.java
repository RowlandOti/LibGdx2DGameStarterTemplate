package com.rowland.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Helpers.InputHandler;

/**
 * @author Rowland
 *
 */
public class CameraControllerTest extends ApplicationAdapter {
	
	private OrthographicCamera camera;
	private CameraController camController;
	private Sprite space;
	private Sprite player;
	private InputHandler handler;
	private InputMultiplexer plex;
	private SpriteBatch batch;

	@Override
	public void create() 
	{
		space = new Sprite(AssetLoader.getSpriteAtlas().findRegion("blue_grass"));
		player = new Sprite(AssetLoader.getSpriteAtlas().findRegion("lock_closed"));
		player.setPosition(space.getWidth()/2, space.getHeight()/2);
		player.setSize(player.getWidth()*3, player.getHeight()*3);
		
		
		batch = new SpriteBatch();
		camera = new OrthographicCamera(AppSettings.SCREEN_W, AppSettings.SCREEN_H);
		camera.setToOrtho(false, 960, 540);
		
		plex = new InputMultiplexer();
		camController = new CameraController(camera);
	    handler = new InputHandler();
	    /* Input Controllers */
	    plex.addProcessor(camController);
	    plex.addProcessor(handler);
	    Gdx.input.setInputProcessor(plex);
		camera.update();
	}

	@Override
	public void render() 
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		space.draw(batch);
		player.draw(batch);
		batch.end();
		
		handler.update();
		camController.update();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
	}
	
	
}
