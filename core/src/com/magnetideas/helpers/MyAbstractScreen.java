package com.magnetideas.helpers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.magnetideas.loaders.ShaderLoader;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.moribitotech.mtx.settings.MtxLogger;
import com.rowland.madboy.MadBoyGame;

/**
 * @author Rowland
 *
 */
public abstract class MyAbstractScreen extends AbstractScreen
{
	private MadBoyGame myGame;
	protected TextureAtlas   atlas;
	protected TextureAtlas  atlas_base;
	private Texture blurTex;
	private Image imgbg;

	public MyAbstractScreen(Game game, String screenName)
	{
		super(game, screenName);
		this.myGame = (MadBoyGame) game;
		setOpenGLClearColor(72.0f/255f, 53.0f/255f, 83.0f/255f, 14.0f/255f);
	}

	public void setBackgroundTexture(TextureRegion textureBackground)
	{
			Drawable tBg = new TextureRegionDrawable(textureBackground);
			imgbg = new Image(tBg, Scaling.stretch);
			imgbg.setFillParent(true);
			getStage().addActor(imgbg);

			MtxLogger.log(logActive, true, logTag, "SCREEN BG IMAGE SET: " + getScreenName());

	}

	public void removeBackgroundTexture()
	{
		getStage().getRoot().removeActor(imgbg);
		imgbg = null;
	}
	public Texture applyBlurEffect(FileHandle file)
	{
		//load original pixmap
		Pixmap orig = new Pixmap(file);

		//Blur the original pixmap with a radius of 4 px
		//The blur is applied over 2 iterations for better quality
		//We specify "disposePixmap=true" to destroy the original pixmap
		Pixmap blurred = ScreenBlurUtils.blur(orig, 2, 2, true);

		//we then create a GL texture with the blurred pixmap
		blurTex = new Texture(blurred);

		//dispose our blurred data now that it resides on the GPU
		blurred.dispose();

		return blurTex;
	}

	public MadBoyGame getMyGame() {
		return myGame;
	}

}
