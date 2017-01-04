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

    private Table controlElementsTable;
    private SmartButton navLeftButton, navRightButton;

    public LevelScreenEnvironment(final LevelSelectScreen levelSelectScreen) {
        this.levelSelectScreen = levelSelectScreen;
    }

    // Initialise specific assets if any
    public void initScreenAssets() {

    }


    public void setUpEnvironment() {
        controlElementsTable = new Table();
        // Change in size will affect the dimensions of the Table
        controlElementsTable.setSize(AppSettings.SCREEN_W - 220f, AppSettings.SCREEN_H - 90f);
        controlElementsTable.setPosition(AppSettings.SCREEN_W / 2 - controlElementsTable.getWidth() / 2, AppSettings.SCREEN_H + controlElementsTable.getHeight() / 2);

        float levelSelectMenuTableX = controlElementsTable.getX();
        float levelSelectMenuTableY = controlElementsTable.getY();

        Random rnd = new Random();
        navLeftButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        navLeftButton.setTextureRegion(levelSelectScreen.nav_left, true);
        navLeftButton.setOrigin(navLeftButton.getWidth() / 2.0f, navLeftButton.getHeight() / 2.0f);
        navLeftButton.setPosition(levelSelectMenuTableX, levelSelectMenuTableY + controlElementsTable.getHeight() / 2);
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

        controlElementsTable.add(navLeftButton).expandX().left().width(levelSelectScreen.getButtonSize()).height(levelSelectScreen.getButtonSize());

        //navRightButton = MenuCreator.createCustomGameButton(null, LevelSelectScreen.nav_right, LevelSelectScreen.nav_right);
        navRightButton = new SmartButton(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize(), rnd, true);
        navRightButton.setTextureRegion(levelSelectScreen.nav_right, true);
        navRightButton.setOrigin(navRightButton.getWidth() / 2.0f, navRightButton.getHeight() / 2.0f);
        navRightButton.setPosition(levelSelectMenuTableX + controlElementsTable.getWidth(), levelSelectMenuTableY + controlElementsTable.getHeight() / 2);
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

        controlElementsTable.add(navRightButton).expandX().right().width(levelSelectScreen.getButtonSize()).height(levelSelectScreen.getButtonSize());

        // Scale them to "0", we will send them in with ScrollPane
        // #################################################################
        navLeftButton.setScale(0f);
        navRightButton.setScale(0f);
    }

    public void sendInEnvironment() {
        //1. Add Tables to Stage
        levelSelectScreen.getStage().addActor(controlElementsTable);
        //2. Send the screen in with some effects
        EffectCreator.create_SC_BTO(navLeftButton, 1.3f, 1.3f, 0.4f, null, false);
        EffectCreator.create_SC_BTO(navRightButton, 1.3f, 1.3f, 0.7f, null, false);
        controlElementsTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - controlElementsTable.getWidth() / 2, AppSettings.SCREEN_H / 2 - controlElementsTable.getHeight() / 2, 0.3f));
    }

    public void sendAwayEnvironment() {
        //1. Send the screen away with some effects
        EffectCreator.create_SC(navLeftButton, 0f, 0f, 0.4f, null, false);
        EffectCreator.create_SC(navRightButton, 0f, 0f, 0.7f, null, false);
        controlElementsTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - controlElementsTable.getWidth() / 2, AppSettings.SCREEN_H + controlElementsTable.getHeight() / 2, 0.4f));
    }
}
