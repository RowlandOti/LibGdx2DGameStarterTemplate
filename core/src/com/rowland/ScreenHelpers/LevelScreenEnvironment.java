package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.UI.PagedScrollPane;
import com.rowland.UI.SmartButton;

import java.util.Random;

/**
 * @author Rowland
 */
public class LevelScreenEnvironment {

    private LevelSelectScreen levelSelectScreen;

    private Table navTable, controlTable;
    private SmartButton navLeftButton, navRightButton;
    private SmartButton ctrlHomeButton, ctrlControllerButton, ctrlUpdateButton, ctrlCartButton, ctrlPlayButton;

    public LevelScreenEnvironment(final LevelSelectScreen levelSelectScreen) {
        this.levelSelectScreen = levelSelectScreen;
    }

    // Initialise specific assets if any
    public void initScreenAssets() {

    }


    public void setUpEnvironment() {
        navTable = new Table();
        // Change in size will affect the dimensions of the Table
        navTable.setSize(AppSettings.SCREEN_W - 220f, AppSettings.SCREEN_H - 90f);
        navTable.setPosition(AppSettings.SCREEN_W / 2 - navTable.getWidth() / 2, AppSettings.SCREEN_H + navTable.getHeight() / 2);

        float navTableX = navTable.getX();
        float navTableY = navTable.getY();

        Random rnd = new Random();
        navLeftButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        navLeftButton.setTextureRegion(levelSelectScreen.nav_left, true);
        navLeftButton.setOrigin(navLeftButton.getWidth() / 2.0f, navLeftButton.getHeight() / 2.0f);
        navLeftButton.setPosition(navTableX, navTableY + navTable.getHeight() / 2);
        navLeftButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("LevelNavLeft", "Hit");
                navLeftButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(navLeftButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Needs Fix - Preferably custom PagedScrollPane
                PagedScrollPane scrollPane = levelSelectScreen.getLevelScreenButtons().getScrollPane();
                scrollPane.scrollTo(x, y, scrollPane.getWidth(), scrollPane.getHeight());
            }
        });

        navTable.add(navLeftButton).expandX().left().size(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize());

        //navRightButton = MenuCreator.createCustomGameButton(null, LevelSelectScreen.nav_right, LevelSelectScreen.nav_right);
        navRightButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        navRightButton.setTextureRegion(levelSelectScreen.nav_right, true);
        navRightButton.setOrigin(navRightButton.getWidth() / 2.0f, navRightButton.getHeight() / 2.0f);
        navRightButton.setPosition(navTableX + navTable.getWidth(), navTableY + navTable.getHeight() / 2);
        navRightButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("LevelNavRight", "Hit");
                navRightButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(navRightButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Needs Fix - Preferably custom PagedScrollPane
                PagedScrollPane scrollPane = levelSelectScreen.getLevelScreenButtons().getScrollPane();
                scrollPane.scrollTo(x, y, scrollPane.getWidth(), scrollPane.getHeight());
            }
        });

        navTable.add(navRightButton).expandX().right().size(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize()).expandY();

        // Scale them to "0", we will send them in with ScrollPane
        // #################################################################
        navLeftButton.setScale(0f);
        navRightButton.setScale(0f);

        controlTable = new Table();
        // Change in size will affect the dimensions of the Table
        controlTable.setSize(AppSettings.SCREEN_W - 260f, AppSettings.SCREEN_H - 70f);
        controlTable.setPosition(AppSettings.SCREEN_W / 2 - navTable.getWidth() / 2, AppSettings.SCREEN_H + navTable.getHeight() / 2);

        float controlTableX = controlTable.getX();
        float controlTableY = controlTable.getY();


        ctrlHomeButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        ctrlHomeButton.setTextureRegion(levelSelectScreen.ctrl_home, true);
        ctrlHomeButton.setOrigin(ctrlHomeButton.getWidth() / 2.0f, ctrlHomeButton.getHeight() / 2.0f);
        ctrlHomeButton.setPosition(controlTableX, controlTableY);
        ctrlHomeButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("ctrlHome", "Hit");
                ctrlHomeButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(ctrlHomeButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Do something
            }
        });


        ctrlControllerButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        ctrlControllerButton.setTextureRegion(levelSelectScreen.ctrl_controller, true);
        ctrlControllerButton.setOrigin(ctrlControllerButton.getWidth() / 2.0f, ctrlControllerButton.getHeight() / 2.0f);
        ctrlControllerButton.setPosition(controlTableX, controlTableY);
        ctrlControllerButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("ctrlController", "Hit");
                ctrlControllerButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(ctrlControllerButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Do something
            }
        });

        ctrlUpdateButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        ctrlUpdateButton.setTextureRegion(levelSelectScreen.ctrl_update, true);
        ctrlUpdateButton.setOrigin(ctrlUpdateButton.getWidth() / 2.0f, ctrlUpdateButton.getHeight() / 2.0f);
        ctrlUpdateButton.setPosition(controlTableX, controlTableY);
        ctrlUpdateButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("ctrlCart", "Hit");
                ctrlUpdateButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(ctrlUpdateButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Do something
            }
        });

        ctrlCartButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        ctrlCartButton.setTextureRegion(levelSelectScreen.ctrl_cart, true);
        ctrlCartButton.setOrigin(ctrlCartButton.getWidth() / 2.0f, ctrlCartButton.getHeight() / 2.0f);
        ctrlCartButton.setPosition(controlTableX, controlTableY);
        ctrlCartButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("ctrlCart", "Hit");
                ctrlCartButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(ctrlCartButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Do something
            }
        });

        ctrlPlayButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        ctrlPlayButton.setTextureRegion(levelSelectScreen.ctrl_play, true);
        ctrlPlayButton.setOrigin(ctrlPlayButton.getWidth() / 2.0f, ctrlPlayButton.getHeight() / 2.0f);
        ctrlPlayButton.setPosition(controlTableX, controlTableY);
        ctrlPlayButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("ctrlPlay", "Hit");
                ctrlPlayButton.clearActions();
                EffectCreator.create_SC_SHK_BTN(ctrlPlayButton, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
                // Do something
            }
        });

        controlTable.bottom();
        controlTable.add(ctrlHomeButton).expandX().size(levelSelectScreen.getButtonSize() * 1.2f, levelSelectScreen.getButtonSize() * 1.2f);
        controlTable.add(ctrlControllerButton).expandX().size(levelSelectScreen.getButtonSize() * 1.2f, levelSelectScreen.getButtonSize() * 1.2f);
        controlTable.add(ctrlUpdateButton).expandX().size(levelSelectScreen.getButtonSize() * 1.2f, levelSelectScreen.getButtonSize() * 1.2f);
        controlTable.add(ctrlCartButton).expandX().size(levelSelectScreen.getButtonSize() * 1.2f, levelSelectScreen.getButtonSize() * 1.2f);
        controlTable.add(ctrlPlayButton).expandX().size(levelSelectScreen.getButtonSize() * 1.2f, levelSelectScreen.getButtonSize() * 1.2f);
    }

    public void sendInEnvironment() {
        //1. Add Tables to Stage
        levelSelectScreen.getStage().addActor(navTable);
        levelSelectScreen.getStage().addActor(controlTable);
        //2. Send the screen in with some effects
        EffectCreator.create_SC_BTO(navLeftButton, 1.3f, 1.3f, 0.4f, null, false);
        EffectCreator.create_SC_BTO(navRightButton, 1.3f, 1.3f, 0.7f, null, false);
        navTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - navTable.getWidth() / 2, AppSettings.SCREEN_H / 2 - navTable.getHeight() / 2, 0.3f));
        controlTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - controlTable.getWidth() / 2, AppSettings.SCREEN_H / 2 - controlTable.getHeight() / 2, 0.3f));
    }

    public void sendAwayEnvironment() {
        //1. Send the screen away with some effects
        EffectCreator.create_SC(navLeftButton, 0f, 0f, 0.4f, null, false);
        EffectCreator.create_SC(navRightButton, 0f, 0f, 0.7f, null, false);
        navTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - navTable.getWidth() / 2, AppSettings.SCREEN_H + navTable.getHeight() / 2, 0.4f));
        controlTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - controlTable.getWidth() / 2, AppSettings.SCREEN_H + controlTable.getHeight() / 2, 0.4f));
    }
}
