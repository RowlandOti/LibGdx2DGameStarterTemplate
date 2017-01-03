package com.rowland.tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.magnetideas.loaders.ShaderLoader;

/**
 * A port of ShaderLesson5 from lwjgl-basics to LibGDX:
 * https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson5
 *
 * @author davedes
 */
public class ShaderBlurTest implements ApplicationListener {

	Texture tex, tex2;
	SpriteBatch batch;
	OrthographicCamera cam;
	ShaderProgram blurShader;
	FrameBuffer blurTargetA, blurTargetB;
	TextureRegion fboRegion;
	public static final int FBO_SIZE = 1024;
	public static final float MAX_BLUR = 2f;
	BitmapFont fps;

	@Override
	public void create()
	{


	}


	@Override
	public void render() {

		tex = new Texture(Gdx.files.internal("data/loading_screen/logo.png"));
		tex2 = new Texture(Gdx.files.internal("data/loading_screen/progress_bar.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tex2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// Important since we aren't using some uniforms and attributes that SpriteBatch expects
		ShaderProgram.pedantic = false;

		if (blurShader == null)
			blurShader = ShaderLoader.fromFile("UniBlurVertexShader", "UniBlurFragmentShader");

		// setup uniforms for our shader
		blurShader.begin();
		blurShader.setUniformf("dir", 1f, 0f);
		blurShader.setUniformf("resolution", FBO_SIZE);
		blurShader.setUniformf("radius", 1f);
		blurShader.end();
		blurTargetA = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE,false);
		blurTargetB = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
		fboRegion.flip(false, true);
		batch = new SpriteBatch();
		fps = new BitmapFont();
		// Start rendering to an offscreen color buffer
		blurTargetA.begin();
		// Clear the offscreen buffer with an opaque background
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// before rendering, ensure we are using the default shader
		batch.setShader(null);
		// now we can start drawing...
		batch.begin();
		// updateHUD our scene here
		batch.draw(tex, 0, 0);
		batch.draw(tex2, tex.getWidth() + 5, 30);
		// finish rendering to the offscreen buffer
		batch.flush();
		// finish rendering to the offscreen buffer
		blurTargetA.end();
		// now let's start blurring the offscreen image
		batch.setShader(blurShader);
		// since we never called batch.end(), we should still be drawing
		// which means are blurShader should now be in use
		// ensure the direction is along the X-axis only
		//blurShader.setUniformf("dir", 1f, 0f);
		// update blur amount based on touch input
		//float mouseXAmt = Gdx.input.getX() / (float) Gdx.graphics.getWidth();
		//blurShader.setUniformf("radius", mouseXAmt * MAX_BLUR);
		// our first blur pass goes to target B
		blurTargetB.begin();
		// we want to render FBO target A into target B
		fboRegion.setTexture(blurTargetA.getColorBufferTexture());
		// updateHUD the scene to target B with a horizontal blur effect
		batch.draw(fboRegion, 0, 0);
		// flush the batch before ending the FBO
		batch.flush();
		// finish rendering target B
		blurTargetB.end();
		// now we can render to the screen using the vertical blur shader
		// update the blur only along Y-axis
		//blurShader.setUniformf("dir", 0f, 1f);
		// update the Y-axis blur radius
		//float mouseYAmt = Gdx.input.getY() / (float) Gdx.graphics.getHeight();
		//blurShader.setUniformf("radius", mouseYAmt * MAX_BLUR);
		// updateHUD target B to the screen with a vertical blur effect
		fboRegion.setTexture(blurTargetB.getColorBufferTexture());
		batch.draw(fboRegion, 0, 0);
		// reset to default shader without blurs
		batch.setShader(null);
		// updateHUD FPS
		fps.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), 5, Gdx.graphics.getHeight() - 5);
		// finally, end the batch since we have reached the end of the frame
		batch.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {
		batch.dispose();
		blurShader.dispose();
		tex.dispose();
		tex2.dispose();
	}
}
