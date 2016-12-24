package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.rowland.ScreenHelpers.LevelScreenButtons;
import com.rowland.ScreenHelpers.LevelScreenEnvironment;

/**
 * @author Rowland
 */
public class LevelSelectScreen extends MyAbstractScreen implements IScreen {

    public TableModel container;


    public static TextureRegion background_menu_berge, star_empty, star_normal, lock_closed, button_level_grey, button_level_green;

    LevelScreenButtons levelScreenButtons;
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
        background_menu_berge = atlas_base.findRegion("background_menu_berge");
        star_empty = atlas.findRegion("star_empty");
        star_normal = atlas.findRegion("star_normal");
        lock_closed = atlas.findRegion("lock_closed");
        button_level_grey = atlas.findRegion("button_level_grey");
        button_level_green = atlas.findRegion("button_level_green");
    }

    @Override
    public void setUpScreenElements() {
        setBackButtonActive(true);
        levelScreenButtons = new LevelScreenButtons(this);
        levelScreenEnvironment = new LevelScreenEnvironment(this);
    }

    @Override
    public void setUpMenu() {
        // Main menu (Order is important)
        // #################################################################
        // They can be animated hence the importance of order
        levelScreenEnvironment.setUpLevelScreenEnvironment();
        levelScreenButtons.setUpLevelScreenButtons();
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
}
