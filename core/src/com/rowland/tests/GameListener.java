package com.rowland.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.magnetideas.loaders.ShaderLoader;
import com.magnetideas.parallax.ParallaxBackground;
import com.magnetideas.parallax.TextureRegionParallaxLayer;
import com.magnetideas.parallax.Utils;
import com.magnetideas.parallax.Utils.WH;
/**
 * Usage example of {@link ParallaxBackground}
 * @author Rahul Verma
 *
 */
public class GameListener extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera worldCamera;

	private TextureAtlas atlas;
	private TextureRegion mountainsRegionA,mountainsRegionB,cloudsRegion, buildingsRegionA,buildingsRegionB,buildingsRegionC;
	private ParallaxBackground parallaxBackground;
	private ShaderProgram parallaxShader;
	private Mesh fullscreenQuadMesh;

	private final float worldWidth = 40;
	private float worldHeight;

	private Color clearColor = new Color(0Xbeaf7bff);

	@Override
	public void create ()
	{

		worldHeight = Utils.calculateOtherDimension(WH.width, worldWidth, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch = new SpriteBatch();
	    worldCamera = new OrthographicCamera();
	    worldCamera.setToOrtho(false,worldWidth,worldHeight);
	    worldCamera.update();

	    //createLayers();
	    createFullScreenQuad();

	    Gdx.input.setInputProcessor(new MyInputProcessor());
	}


	private void createLayers()
	{
		atlas = new TextureAtlas("data/test/main_atlas.atlas");


		mountainsRegionA = new TextureRegion(atlas.findRegion("mountains_a"));
		TextureRegionParallaxLayer mountainsLayerA = new TextureRegionParallaxLayer(mountainsRegionA, worldWidth, new Vector2(.3f,.3f), WH.width);
		mountainsRegionA.getTexture().bind(5);

		mountainsRegionB = new TextureRegion(atlas.findRegion("mountains_b"));
		TextureRegionParallaxLayer mountainsLayerB = new TextureRegionParallaxLayer(mountainsRegionB, worldWidth*.7275f, new Vector2(.6f,.6f), WH.width);
        mountainsLayerB.setPadLeft(.2725f*worldWidth);
        mountainsRegionB.getTexture().bind(4);

		cloudsRegion = new TextureRegion(atlas.findRegion("clouds"));
		TextureRegionParallaxLayer cloudsLayer = new TextureRegionParallaxLayer(cloudsRegion, worldWidth, new Vector2(.6f,.6f), WH.width);
		cloudsLayer.setPadBottom(worldHeight*.467f);
		cloudsRegion.getTexture().bind(3);

		buildingsRegionA = new TextureRegion(atlas.findRegion("buildings_a"));
		TextureRegionParallaxLayer buildingsLayerA = new TextureRegionParallaxLayer(buildingsRegionA, worldWidth, new Vector2(.75f,.75f), WH.width);
		buildingsRegionA.getTexture().bind(2);

		buildingsRegionB = new TextureRegion(atlas.findRegion("buildings_b"));
		TextureRegionParallaxLayer buildingsLayerB = new TextureRegionParallaxLayer(buildingsRegionB, worldWidth*.8575f, new Vector2(1,1), WH.width);
       	buildingsLayerB.setPadLeft(.07125f*worldWidth);
		buildingsLayerB.setPadRight(buildingsLayerB.getPadLeft());
		buildingsRegionB.getTexture().bind(1);

		buildingsRegionC = new TextureRegion(atlas.findRegion("buildings_c"));
		TextureRegionParallaxLayer buildingsLayerC = new TextureRegionParallaxLayer(buildingsRegionC, worldWidth, new Vector2(1.3f,1.3f), WH.width);
		buildingsRegionC.getTexture().bind(0);

		parallaxBackground = new ParallaxBackground();
    	parallaxBackground.addLayers(mountainsLayerA,mountainsLayerB,cloudsLayer,buildingsLayerA,buildingsLayerB,buildingsLayerC);

	}

	protected void createShader()
	{
		ShaderLoader.pedantic = false;
		ShaderLoader.BASEPATH = "data/test/shaders/";

		parallaxShader =  ShaderLoader.fromFile("ParallaxVertexShader", "ParallaxFragmentShader");
	}

	public void createFullScreenQuad()
	{

	      float[] verts = new float[36];
	      int i = 0;

	      verts[i++] = -1f; // x1
	      verts[i++] = 1f; // y1
	      verts[i++] = 0;  // z1
	      verts[i++] = 0f; // u1
	      verts[i++] = 0f; // v1
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;
	      //Color.toFloatBits(255, 0, 0, 255); //--Research Method

	      verts[i++] = -1f; // x2
	      verts[i++] = 1f; // y2
	      verts[i++] = 0;  // z2
	      verts[i++] = 0f; // u2
	      verts[i++] = 0f; // v2
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;


	      verts[i++] = -1f; // x3
	      verts[i++] = 1f; // y3
	      verts[i++] = 0;  // z3
	      verts[i++] = 0f; // u3
	      verts[i++] = 0f; // v3
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;


	      verts[i++] = 1f; // x4
	      verts[i++] = 1f; // y4
	      verts[i++] = 0;  // z4
	      verts[i++] = 0f; // u4
	      verts[i++] = 0f; // v4
	      verts[i++] = clearColor.r;
	      verts[i++] = clearColor.g;
	      verts[i++] = clearColor.b;
	      verts[i++] = clearColor.a;

	      // static mesh with 4 vertices and no indices
	      fullscreenQuadMesh = new Mesh(false, 4, 0,
	    		  new VertexAttribute( Usage.Position, 3, "a_position"),
	    		  new VertexAttribute( Usage.TextureCoordinates, 2, "a_texCoords"),
	              new VertexAttribute( Usage.ColorUnpacked, 4, "a_color")
	      );
	      fullscreenQuadMesh.setVertices(verts);

/*
	      fullscreenQuadMesh.setVertices(new float[]
	    		  {
	    		  -1f, -1f, 0, buildingsRegionC.getU(), buildingsRegionC.getV2(), // bottom left
	              1f, -1f, 0, buildingsRegionC.getU2(), buildingsRegionC.getV2(), // bottom right
	              1f, 1f, 0, buildingsRegionC.getU2(), buildingsRegionC.getV(),  // top right
	              -1f, 1f, 0, buildingsRegionC.getU(), buildingsRegionC.getV()   // top left
	              });

	      fullscreenQuadMesh.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });*/

	      createShader();
	      createLayers();
	   }


	@Override
	public void render ()
	{
		if(inputOn)
			processInput();
		//Gdx.gl.glClearColor(clearColor.r,clearColor.g,clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		float travelDistance = worldCamera.position.x / 100;

		parallaxShader.begin();
		//parallaxShader.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
		//parallaxShader.setUniformMatrix("u_worldView", batch.getProjectionMatrix());
		parallaxShader.setUniformf("travelDistance", travelDistance);
		parallaxShader.setUniformi("u_texture0", 0);
		parallaxShader.setUniformi("u_texture1", 1);
		parallaxShader.setUniformi("u_texture2", 2);
		parallaxShader.setUniformi("u_texture3", 3);
		fullscreenQuadMesh.render(parallaxShader, GL20.GL_TRIANGLE_FAN);
		parallaxShader.end();

		//batch.begin();
		//parallaxBackground.draw(worldCamera, batch);
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
	}


	boolean inputOn = false;
	private class MyInputProcessor extends InputAdapter{
		@Override
		public boolean keyDown(int keycode) {
			if(keycode==Keys.ESCAPE){
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
		public boolean keyUp(int keycode) {
		    inputOn = false;
		    return false;
		}


	}




}
