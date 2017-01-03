package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.mtx.interfaces.IScreen;
import com.rowland.ScreenHelpers.LevelIScreenButtons;
import com.rowland.ScreenHelpers.LevelScreenEnvironment;

/**
 * @author Rowland
 */
public class LevelSelectScreen extends MyAbstractScreen implements IScreen {

    public static TextureRegion background_level, star_empty, star_normal, button_level_grey, button_level_green, nav_left, nav_right;

    LevelIScreenButtons levelScreenButtons;
    LevelScreenEnvironment levelScreenEnvironment;

    public LevelSelectScreen(Game game, String screenName) {
        super(game, screenName);

        initScreenAssets();
        setUpScreenElements();
        setUpMenu();
    }

    //Initialise screen assets
    private void initScreenAssets() {
        //Getting previously loaded atlas
        atlas = getMyGame().getManager().get(LoadingScreen.UI_LEVEL_ATLAS, TextureAtlas.class);
        atlas_base = getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

        //Allocate Texture regions from the atlas
        background_level = atlas_base.findRegion("level");
        // Knob
        nav_left = atlas_base.findRegion("nav_left");
        nav_right = atlas_base.findRegion("nav_right");
        //
        button_level_green = atlas_base.findRegion("button_level_green");
        button_level_grey = atlas_base.findRegion("button_level_locked");

        star_empty = atlas.findRegion("star_empty");
        star_normal = atlas.findRegion("star_normal");
    }

    @Override
    public void setUpScreenElements() {
        setBackButtonActive(true);
        levelScreenButtons = new LevelIScreenButtons(this);
        levelScreenEnvironment = new LevelScreenEnvironment(this);
    }

    @Override
    public void setUpMenu() {
        // Main menu (Order is important) - They can be animated hence the importance of order
        levelScreenEnvironment.setUpLevelScreenEnvironment();
        levelScreenButtons.setUpMenu();
    }

    @Override
    public void setUpInfoPanel() {

    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();
        getGame().setScreen(new LoadingScreen(getMyGame(), "MenuScreen", LoadingScreen.TYPE_UI_MENU));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void resume() {
        super.resume();
        levelScreenButtons.sendInMenu();
    }
}
