package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.rowland.Helpers.AssetLoader;

public class CongratsScreen extends AbstractScreen implements IScreen {

    public CongratsScreen(Game game, String screenName) {
        super(game, screenName);
        //
        setUpScreens();
        setUpInfoPanel();
        setUpActors();
        setUpMenu();
    }

    @Override
    public void setUpScreens() {
        setBackgroundTexture(AssetLoader.bg);
        setBackButtonActive(true);
    }

    @Override
    public void setUpInfoPanel() {

    }

    private void setUpActors() {

    }

    @Override
    public void setUpMenu() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }

    @Override
    public void keyBackPressed() {
        super.keyBackPressed();
    }
}
