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
	final String VERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE
			+ ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n" + "void main() {\n" + "	vColor = "
			+ ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "	vTexCoord = "
			+ ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
			+ "	gl_Position = u_projTrans * "
			+ ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";
	final String FRAG = "#ifdef GL_ES\n"
			+ "#define LOWP lowp\n"
			+ "precision mediump float;\n"
			+ "#else\n"
			+ "#define LOWP \n"
			+ "#endif\n"
			+ "varying LOWP vec4 vColor;\n"
			+ "varying vec2 vTexCoord;\n"
			+ "\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform float resolution;\n"
			+ "uniform float radius;\n"
			+ "uniform vec2 dir;\n"
			+ "\n"
			+ "void main() {\n"
			+ "	vec4 sum = vec4(0.0);\n"
			+ "	vec2 tc = vTexCoord;\n"
			+ "	float blur = radius/resolution; \n"
			+ " \n"
			+ " float hstep = dir.x;\n"
			+ " float vstep = dir.y;\n"
			+ " \n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;\n"
			+ "	\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;\n"
			+ "	\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;\n"
			+ "	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;\n"
			+ "\n" + "	gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + "}";

	@Override
	public void create()
	{
		tex = new Texture(Gdx.files.internal("data/loading_screen/logo.png"));
		tex2 = new Texture(Gdx.files.internal("data/loading_screen/progress_bar.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tex2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		// important since we aren't using some uniforms and attributes that
		// SpriteBatch expects
		ShaderProgram.pedantic = false;
		blurShader = new ShaderProgram(VERT, FRAG);
		if (!blurShader.isCompiled())
		{
			System.err.println(blurShader.getLog());
			System.exit(0);
		}
		if (blurShader.getLog().length() != 0)
			System.out.println(blurShader.getLog());

		// setup uniforms for our shader
		blurShader.begin();
		blurShader.setUniformf("dir", 0f, 0f);
		blurShader.setUniformf("resolution", FBO_SIZE);
		blurShader.setUniformf("radius", 1f);
		blurShader.end();
		blurTargetA = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE,false);
		blurTargetB = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
		fboRegion.flip(false, true);
		batch = new SpriteBatch();
		fps = new BitmapFont();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);
	}

	@Override
	public void resize(int width, int height)
	{
		resizeBatch(width, height);
	}

	void resizeBatch(int width, int height) {
		cam.setToOrtho(false, width, height);
		batch.setProjectionMatrix(cam.combined);
	}

	void renderEntities(SpriteBatch batch) {
		batch.draw(tex, 0, 0);
		batch.draw(tex2, tex.getWidth() + 5, 30);
	}

	@Override
	public void render() {
		// Start rendering to an offscreen color buffer
		blurTargetA.begin();
		// Clear the offscreen buffer with an opaque background
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// before rendering, ensure we are using the default shader
		batch.setShader(null);
		// resize the batch projection matrix before drawing with it
		resizeBatch(FBO_SIZE, FBO_SIZE);
		// now we can start drawing...
		batch.begin();
		// draw our scene here
		renderEntities(batch);
		// finish rendering to the offscreen buffer
		batch.flush();
		// finish rendering to the offscreen buffer
		blurTargetA.end();
		// now let's start blurring the offscreen image
		batch.setShader(blurShader);
		// since we never called batch.end(), we should still be drawing
		// which means are blurShader should now be in use
		// ensure the direction is along the X-axis only
		blurShader.setUniformf("dir", 1f, 0f);
		// update blur amount based on touch input
		float mouseXAmt = Gdx.input.getX() / (float) Gdx.graphics.getWidth();
		blurShader.setUniformf("radius", mouseXAmt * MAX_BLUR);
		// our first blur pass goes to target B
		blurTargetB.begin();
		// we want to render FBO target A into target B
		fboRegion.setTexture(blurTargetA.getColorBufferTexture());
		// draw the scene to target B with a horizontal blur effect
		batch.draw(fboRegion, 0, 0);
		// flush the batch before ending the FBO
		batch.flush();
		// finish rendering target B
		blurTargetB.end();
		// now we can render to the screen using the vertical blur shader
		// update our projection matrix with the screen size
		resizeBatch(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// update the blur only along Y-axis
		blurShader.setUniformf("dir", 0f, 1f);
		// update the Y-axis blur radius
		float mouseYAmt = Gdx.input.getY() / (float) Gdx.graphics.getHeight();
		blurShader.setUniformf("radius", mouseYAmt * MAX_BLUR);
		// draw target B to the screen with a vertical blur effect
		fboRegion.setTexture(blurTargetB.getColorBufferTexture());
		batch.draw(fboRegion, 0, 0);
		// reset to default shader without blurs
		batch.setShader(null);
		// draw FPS
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
	public void dispose() {
		batch.dispose();
		blurShader.dispose();
		tex.dispose();
		tex2.dispose();
	}
}
