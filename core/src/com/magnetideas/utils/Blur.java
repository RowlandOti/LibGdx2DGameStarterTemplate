/**
 *
 */
package com.magnetideas.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * @author Rowland
 *
 */
public class Blur {

	//It's expensive to blur a 1080x1920 image, especially on Android! Usually, when doing a Gaussian blur, you can downsample the screen to half or a quarter of the width and height without much quality loss. That also lets you get away with a smaller sampling radius to achieve the same appearance.

	private static final int FB_SIZE = 200;
	public SpriteBatch batch;
	protected ShaderProgram mShaderA;
	protected ShaderProgram mShaderB;
	protected Mesh mMeshA;
	protected Mesh mMeshB;
	//RenderSurface  blurTargetB;
	//RenderSurface  blurTargetC;
	FrameBuffer  blurTargetB;
	FrameBuffer  blurTargetC;
	float m_fboScaler = 1;
	Texture texture;
	public void init() {
	    texture = new Texture(
	            Gdx.files.internal("mainMenuBack.png"));
	    texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	    //blurTargetB = new RenderSurface(Format.RGBA4444, 1080, 1920, true);
	    //blurTargetC = new RenderSurface(Format.RGBA4444, 1080, 1920, true);
	    blurTargetB = new FrameBuffer(Format.RGB565, (int)(Gdx.graphics.getWidth() * m_fboScaler), (int)(Gdx.graphics.getHeight() * m_fboScaler), false);
	    blurTargetC = new FrameBuffer(Format.RGBA4444, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	    if (mMeshA != null)
	        mMeshA.dispose();
	    mMeshA = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position, 2,
	            ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
	            Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE
	                    + "0"));

	    mMeshA.setVertices(new float[] { -1f, -1f, 0, 1, 1f, -1f, 1, 1, 1f, 1f,
	            1, 0, -1f, 1f, 0, 0 });

	    if (mMeshB != null)
	        mMeshB.dispose();
	    mMeshB = new Mesh(true, 4, 4, new VertexAttribute(Usage.Position, 2,
	            ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
	            Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE
	                    + "1"));
	    mMeshB.setVertices(new float[] { -1f, -1f, 0, 1, 1f, -1f, 1, 1, 1f, 1f,
	            1, 0, -1f, 1f, 0, 0 });

	}

	public void BlurRenderer() {
	    mShaderA = createXBlurShader();
	    //mShaderB = createYBlurShader();
	    init();
	    batch=new SpriteBatch();
	}


	public void render() {
	    drawAToB();
	    drawBToC();
	    drawCToSceen();
	}



	public void drawAToB() {
	    blurTargetB.begin();

	    batch.setShader(mShaderA);

	    batch.begin();

	    batch.draw(texture, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	    batch.flush();
	    blurTargetB.end();
	}

	public void drawBToC() {
	    blurTargetC.begin();

	    batch.draw( blurTargetB.getColorBufferTexture(), 0, 0);
	    batch.flush();
	    blurTargetC.end();
	}

	public void dispose() {
	    mMeshA.dispose();
	    mMeshB.dispose();
	}
	public void drawCToSceen() {
	    batch.draw( blurTargetC.getColorBufferTexture(), 0, 0);
	    batch.end();
	}

	public ShaderProgram createXBlurShader() {
	    String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
	        + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
	        + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
	        + "uniform float uBlurBufferSize;\n" // 1 / Size of the blur
	        + "uniform mat4 u_projTrans;\n" //
	        + "varying vec4 v_color;\n" //
	        + "varying vec2 v_texCoords;\n" //
	        + "varying vec2 vBlurTexCoords[5];\n" // output texture
	        + "\n" //
	        + "void main()\n" //
	        + "{\n" //
	        + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
	        + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
	            + " vBlurTexCoords[0] = v_texCoords + vec2(-2.0 * uBlurBufferSize, 0.0);\n"
	            + " vBlurTexCoords[1] = v_texCoords + vec2(-1.0 * uBlurBufferSize, 0.0);\n"
	            + " vBlurTexCoords[2] = v_texCoords;\n"
	            + " vBlurTexCoords[3] = v_texCoords + vec2( 1.0 * uBlurBufferSize, 0.0);\n"
	            + " vBlurTexCoords[4] = v_texCoords + vec2( 2.0 * uBlurBufferSize, 0.0);\n"
	        + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
	        + "}\n";
	    String fragmentShader = "#ifdef GL_ES\n" //
	        + "#define LOWP lowp\n" //
	        + "precision mediump float;\n" //
	        + "#else\n" //
	        + "#define LOWP \n" //
	        + "#endif\n" //
	        + "varying LOWP vec4 v_color;\n" //
	        + "varying vec2 v_texCoords;\n" //
	        + "varying vec2 vBlurTexCoords[5];\n" // input texture coords
	        + "uniform sampler2D u_texture;\n" //
	        + "void main()\n"//
	        + "{\n" //
	            + "  vec4 sum = vec4(0.0);\n"
	            + "  sum += texture2D(u_texture, vBlurTexCoords[0]) * 0.164074;\n"
	            + "  sum += texture2D(u_texture, vBlurTexCoords[1]) * 0.216901;\n"
	            + "  sum += texture2D(u_texture, vBlurTexCoords[2]) * 0.23805;\n"
	            + "  sum += texture2D(u_texture, vBlurTexCoords[3]) * 0.216901;\n"
	            + "  sum += texture2D(u_texture, vBlurTexCoords[4]) * 0.164074;\n"
	            + "  gl_FragColor = sum;\n"
	        + "}";
	    ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
	    if (shader.isCompiled() == false) {
	        Gdx.app.log("ERROR", shader.getLog());
	    }
	    return shader;
	}
}
