package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.ScreenHelpers.LevelScreenButtons;
import com.rowland.ScreenHelpers.LevelScreenEnvironment;

/**
 * @author Rowland
 */
public class LevelSelectScreen extends MyAbstractScreen implements IScreen {

    public TextureRegion background_level, star_empty, star_normal, star_golden, button_level_grey, button_level_green, nav_left, nav_right;
    public TextureRegion ctrl_home, ctrl_controller, ctrl_update, ctrl_cart, ctrl_play;

    private LevelScreenButtons levelScreenButtons;
    private LevelScreenEnvironment levelScreenEnvironment;

    private float button_size = 75f * AppSettings.getWorldSizeRatio();

    public LevelSelectScreen(Game game, String screenName) {
        super(game, screenName);
        getStage().setDebugAll(true);
        setUpScreens();
        initScreenAssets();
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
        // Level Button
        button_level_green = atlas_base.findRegion("button_level_green");
        button_level_grey = atlas_base.findRegion("button_level_locked");
        // Level decoration elements
        star_golden = atlas_base.findRegion("star_golden");
        star_empty = atlas.findRegion("star_empty");
        star_normal = atlas.findRegion("star_normal");
        // Control Buttons
        ctrl_home = atlas_base.findRegion("button_home_green");
        ctrl_controller = atlas_base.findRegion("button_controller_green");
        ctrl_update = atlas_base.findRegion("button_update_green");
        ctrl_cart = atlas_base.findRegion("button_cart_green");
        ctrl_play = atlas_base.findRegion("button_play_orange");

        levelScreenButtons.initScreenAssets();
        levelScreenEnvironment.initScreenAssets();
    }

    @Override
    public void setUpScreens() {
        setBackButtonActive(true);
        levelScreenButtons = new LevelScreenButtons(this);
        levelScreenEnvironment = new LevelScreenEnvironment(this);
    }

    @Override
    public void setUpMenu() {
        // Main menu (Order is important) - They can be animated hence the importance of order
        levelScreenEnvironment.setUpEnvironment();
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
        levelScreenEnvironment.sendInEnvironment();
    }

    public LevelScreenButtons getLevelScreenButtons() {
        return levelScreenButtons;
    }

    public LevelScreenEnvironment getLevelScreenEnvironment() {
        return levelScreenEnvironment;
    }

    public float getButtonSize() {
        return button_size;
    }
}
