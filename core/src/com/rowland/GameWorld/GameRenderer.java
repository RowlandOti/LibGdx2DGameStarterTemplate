package com.rowland.GameWorld;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.magnetideas.parallax.ParallaxBackground;
import com.rowland.GameObjects.Yoyo;
import com.rowland.Screens.GameScreen;

public class GameRenderer {

	public OrthogonalTiledMapRenderer renderer;
	public SpriteBatch batch;
	private GameWorld world;
	private ShaderProgram blurShader;
	private FrameBuffer blurTarget;
	private TextureRegion fboRegion;
	public static final int FBO_SIZE = 1024;
	
	public GameRenderer(GameWorld world) 
	{
        this.world = world;
        renderer = new OrthogonalTiledMapRenderer(world.map, 1/32f);
        batch = (SpriteBatch) renderer.getBatch();
    }
	
	public void render(int[] array) 
	{
		if(GameScreen.state == GameScreen.State.GAME_RUNNING)
		renderer.render(array);
    }
	
	public void renderPlayer(float deltaTime)
	{
		if(GameScreen.state == GameScreen.State.GAME_RUNNING || GameScreen.state == GameScreen.State.GAME_LEVEL_END){
			
		// Based on the Player state, get the animation frame
		if(world.getYoyo().visible)
		{
			TextureRegion frame = null;
			switch (world.getYoyo().getState())
		{
			case 0:
				frame = GameScreen.pummaStill.getKeyFrame(world.getYoyo().stateTime);
				break;
	
			case 1:
				frame = GameScreen.pummaWalk.getKeyFrame(world.getYoyo().stateTime);
				break;
				
			case 2:
				frame = GameScreen.pummaJump.getKeyFrame(world.getYoyo().stateTime);
				break;
			
		}

		// draw the Player depending on the current velocity
		// on the x-axis, draw the Player facing either right or left
		
		batch.begin();
		if (world.getYoyo().facesRight)
		{
			batch.draw(frame, world.getYoyo().position.x, world.getYoyo().position.y, Yoyo.width, Yoyo.height);
		}
		else
		{
			batch.draw(frame, world.getYoyo().position.x + Yoyo.width, world.getYoyo().position.y, -Yoyo.width, Yoyo.height);
		}
		batch.end();
	   }
  }
}	
	
	public void renderBackground(ParallaxBackground parallaxBackground, OrthographicCamera camera) 
	{
		batch.begin();
		parallaxBackground.draw(camera, batch);
		batch.end();
	}
	
	public void renderForeground(ParallaxBackground parallaxForeground, OrthographicCamera camera) 
	{
		batch.begin();
		parallaxForeground.draw(camera, batch);
		batch.end();
	}
}
