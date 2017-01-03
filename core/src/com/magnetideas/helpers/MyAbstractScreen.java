package com.magnetideas.helpers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.moribitotech.mtx.settings.MtxLogger;
import com.rowland.madboy.MadBoyGame;


/**
 * @author Rowland
 */
public abstract class MyAbstractScreen extends AbstractScreen {

    private MadBoyGame myGame;
    protected TextureAtlas atlas;
    protected TextureAtlas atlas_base;
    private Image imgbg;

    public MyAbstractScreen(Game game, String screenName) {
        super(game, screenName);
        this.myGame = (MadBoyGame) game;
        //You can customise your clear color by calling this method in your Screen class, in the constructor
        setOpenGLClearColor(72.0f / 255f, 53.0f / 255f, 83.0f / 255f, 14.0f / 255f);
    }

    /**
     * Set stage background. Sets the image (Adds to stage as image)
     *
     * @param textureBackground
     */
    @Override
    public void setBackgroundTexture(TextureRegion textureBackground) {
        Drawable tBg = new TextureRegionDrawable(textureBackground);
        imgbg = new Image(tBg, Scaling.stretch);
        imgbg.setFillParent(true);
        getStage().addActor(imgbg);

        MtxLogger.log(logActive, true, logTag, "SCREEN BG IMAGE SET: " + getScreenName());
    }

    /**
     * Remove the background texture
     */
    public void removeBackgroundTexture() {
        getStage().getRoot().removeActor(imgbg);
        imgbg = null;
    }

    public TextureRegion getScreenTexture() {
        Pixmap bgPix = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextureRegion backgroundBlur = new TextureRegion(new Texture(bgPix));
        backgroundBlur.flip(false, true);

        return backgroundBlur;
    }

    public MadBoyGame getMyGame() {
        return myGame;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        resume();
    }
}
