/**
 *
 */
package com.rowland.tests.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.magnetideas.loaders.MeshObject;
import com.magnetideas.loaders.ShaderLoader;
import com.magnetideas.parallax.ParallaxBackground;
import com.magnetideas.parallax.TextureRegionParallaxLayer;
import com.magnetideas.parallax.Utils;
import com.magnetideas.parallax.Utils.WH;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.rowland.GameWorld.GameWorld;

/**
 * @author Rowland
 *
 */
public class ScrollScreenTest extends AbstractScreen {

	private SpriteBatch batch;
	private OrthographicCamera worldCamera;

	private TextureAtlas atlas;
	private ParallaxBackground parallaxBackground, parallaxForeground;
	private ShaderProgram parallaxShader;
	private MeshObject mesh0 ,mesh1, mesh2, mesh3;
	private Array<Float> vertexArray;
	private short[] indicesArray;

	private final float worldWidth = 40;
	private float worldHeight;

	private Color clearColor = new Color(0Xbeaf7bff);
	private AtlasRegion skyRegion;
	private AtlasRegion downtownNairobiRegion;
	private AtlasRegion uptownNairobiRegion;
	private AtlasRegion ghettoFenceRegion;

	public ScrollScreenTest(Game game, String screenName)
	{
		super(game, screenName);

	}

	public void initilize()
	{
		mesh0 = new MeshObject();
        mesh1 = new MeshObject();
        mesh2 = new MeshObject();
        mesh3 = new MeshObject();

		worldHeight = Utils.calculateOtherDimension(WH.width, worldWidth, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch = new SpriteBatch();
	    worldCamera = new OrthographicCamera();
	    worldCamera.setToOrtho(false,32f,18f);
	    worldCamera.update();

	    createLayers();
	    mesh0.createFullScreenQuad(skyRegion);
	    mesh1.createFullScreenQuad(downtownNairobiRegion);
	    mesh2.createFullScreenQuad(uptownNairobiRegion);
	    mesh3.createFullScreenQuad(ghettoFenceRegion);

	    createShader();

	}

	private void createLayers()
	{
		atlas = new TextureAtlas("data/game_screen/game_atlas.pack");
		Array<AtlasRegion> regions = atlas.getRegions();

    	skyRegion = atlas.findRegion("sky");
    	TextureRegionParallaxLayer skyLayer = new TextureRegionParallaxLayer(skyRegion, GameWorld.DEFAULT_VIEWPORT_WIDTH, new Vector2(.3f,.3f), WH.width);
    	skyRegion.getTexture().bind(3);

    	downtownNairobiRegion = atlas.findRegion("background_downtown");
    	TextureRegionParallaxLayer downtownNairobiLayer = new TextureRegionParallaxLayer(downtownNairobiRegion, GameWorld.DEFAULT_VIEWPORT_WIDTH, new Vector2(.6f,.6f), WH.width);
    	//downtownNairobiRegion.getTexture().bind(2);

    	uptownNairobiRegion = atlas.findRegion("nairobi_city_uptown");
    	TextureRegionParallaxLayer uptownNairobiLayer = new TextureRegionParallaxLayer(uptownNairobiRegion, GameWorld.DEFAULT_VIEWPORT_WIDTH, new Vector2(.75f,.75f), WH.width);
    	//uptownNairobiRegion.getTexture().bind(1);

    	ghettoFenceRegion = atlas.findRegion("ghettofence");
    	TextureRegionParallaxLayer ghettofenceLayer = new TextureRegionParallaxLayer(ghettoFenceRegion, GameWorld.DEFAULT_VIEWPORT_WIDTH, new Vector2(1.3f,1.3f), WH.width);
    	//ghettoFenceRegion.getTexture().bind(0);

		parallaxBackground = new ParallaxBackground();
		parallaxForeground = new ParallaxBackground();
		parallaxBackground.addLayers(skyLayer, downtownNairobiLayer,uptownNairobiLayer);
		parallaxForeground.addLayers(ghettofenceLayer);

	}

	protected void createShader()
	{
		ShaderLoader.pedantic = false;
		ShaderLoader.BASEPATH = "data/shaders/";

		parallaxShader =  ShaderLoader.fromFile("ParallaxVertexShader", "ParallaxFragmentShader");
	}


	boolean inputOn = false;
	private class MyInputProcessor extends InputAdapter
	{
		@Override
		public boolean keyDown(int keycode) {
			if(keycode==Keys.ESCAPE)
			{
				Gdx.app.exit();
				return true;
			}
			if(keycode==Keys.LEFT||keycode==Keys.RIGHT||keycode==Keys.UP||keycode==Keys.DOWN||keycode==Keys.CONTROL_RIGHT||keycode==Keys.ALT_RIGHT)
			{
				inputOn = true;
			}
			return false;
		}
		@Override
		public boolean keyUp(int keycode)
		{
		    inputOn = false;
		    return false;
		}

	}

	@Override
	public void render(float delta) {

		//super.render(delta);

		if(inputOn)
			processInput();
		//Gdx.gl.glClearColor(clearColor.r,clearColor.g,clearColor.b, clearColor.a);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		float travelDistance = worldCamera.position.x / 100;

		parallaxShader.begin();
		//parallaxShader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
		parallaxShader.setUniformMatrix("u_worldView", batch.getProjectionMatrix());
		parallaxShader.setUniformf("travelDistance", travelDistance);
		parallaxShader.setUniformi("u_texture0", 0);
		parallaxShader.setUniformi("u_texture1", 1);
		parallaxShader.setUniformi("u_texture2", 2);
		parallaxShader.setUniformi("u_texture3", 3);
		mesh0.fullscreenQuadMesh.render(parallaxShader, GL20.GL_TRIANGLE_FAN);
		mesh1.fullscreenQuadMesh.render(parallaxShader, GL20.GL_TRIANGLE_FAN);
		mesh2.fullscreenQuadMesh.render(parallaxShader, GL20.GL_TRIANGLE_FAN);
		mesh3.fullscreenQuadMesh.render(parallaxShader, GL20.GL_TRIANGLE_FAN);
		parallaxShader.end();

		//batch.begin();
		//parallaxBackground.draw(worldCamera, batch);
		//parallaxForeground.draw(worldCamera, batch);
		//batch.end();
	}

	private final float deltaDimen = .5f;

	private void processInput()
	{
		if(Gdx.input.isKeyPressed(Keys.LEFT)&&(worldCamera.position.x-worldCamera.viewportWidth*.5f>0))
			worldCamera.position.sub(deltaDimen, 0, 0);
		else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			worldCamera.position.add(deltaDimen, 0, 0);
		else if(Gdx.input.isKeyPressed(Keys.DOWN)&&(worldCamera.position.y-worldCamera.viewportHeight*.5f>0))
			worldCamera.position.sub(0, deltaDimen, 0);
		else if(Gdx.input.isKeyPressed(Keys.UP))
			worldCamera.position.add(0, deltaDimen, 0);
		else if(Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))
			worldCamera.zoom*=1.1f;
		else if(Gdx.input.isKeyPressed(Keys.ALT_RIGHT))
			worldCamera.zoom/=1.1f;

		worldCamera.update();
		batch.setProjectionMatrix(worldCamera.combined);

	}


	@Override
	public void dispose()
	{
		super.dispose();
		batch.dispose();
		atlas.dispose();
		Gdx.gl20.glDisable(GL20.GL_TEXTURE_2D);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
}
