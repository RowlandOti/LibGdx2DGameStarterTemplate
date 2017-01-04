package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.ButtonLevel;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.Screens.LoadingScreen;
import com.rowland.Screens.PreGameScreen;
import com.rowland.UI.PagedScrollPane;
import com.rowland.UI.SmartButton;

import java.util.Random;

/**
 * @author Rowland
 */
public class LevelScreenButtons implements IScreenAbstractMenu {

    private LevelSelectScreen levelSelectScreen;
    private Table levelSelectMenuTable;
    private PagedScrollPane scrollPane;

    private int selectedLevel;

    public LevelScreenButtons(final LevelSelectScreen levelSelectScreen) {
        this.levelSelectScreen = levelSelectScreen;
    }

    // Initialise specific assets if any
    public void initScreenAssets() {

    }

    @Override
    public void setUpMenu() {

        scrollPane = new PagedScrollPane();
        // For accurate change in size of ScrollPane use paddings with respect to parent Table
        scrollPane.setSize(AppSettings.SCREEN_W - 260f, AppSettings.SCREEN_H - 90f);
        scrollPane.setPosition(AppSettings.SCREEN_W / 2, AppSettings.SCREEN_H + scrollPane.getHeight() / 2);
        scrollPane.setFlingTime(0.5f);
        scrollPane.setPageSpacing(20f);
        scrollPane.setScrollingDisabled(false, true);

        int c = 1;
        int numberOfLevels = GameData.NUMBER_OF_LEVELS;

        float padLevels = 20 * AppSettings.getWorldSizeRatio();
        float padScrollPane = 40 * AppSettings.getWorldSizeRatio();
//        float getButtonSize() = 75f * AppSettings.getWorldSizeRatio();

        for (int l = 0; l < 2; l++) {

            Table levels = new TableModel();
            levels.padTop(padLevels).padBottom(padLevels);

            if (l == 0) {
                levels.padLeft(padLevels).padRight(padLevels);
            } else {
                levels.padRight(padLevels);
            }

            for (int rows = 0; rows < 3; rows++) {
                levels.row();

                for (int columns = 0; columns < 4; columns++) {
                    c = c++;

                    if (numberOfLevels + 1 != c) {
                        Cell cell = levels.add(getLevelButton(c++)).size(levelSelectScreen.getButtonSize(), levelSelectScreen.getButtonSize()).padTop(padLevels);

                        if (columns % 4 == 0) {
                            cell.padLeft(padLevels * 3);
                        } else if (columns % 4 == 3) {
                            cell.padRight(padLevels * 3);
                        } else {
                            if (columns == 1) {
                                cell.padRight(padLevels * 4).padLeft(padLevels * 4);
                            } else {
                                cell.padRight(padLevels * 4);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            scrollPane.addPage(levels);
        }

        levelSelectMenuTable = new Table();
        // Change in size will affect the dimensions of the Table
        levelSelectMenuTable.setSize(AppSettings.SCREEN_W - 260f, AppSettings.SCREEN_H - 90f);
        levelSelectMenuTable.setPosition(AppSettings.SCREEN_W / 2 - levelSelectMenuTable.getWidth() / 2, AppSettings.SCREEN_H + levelSelectMenuTable.getHeight() / 2);
        levelSelectMenuTable.setBackground(new TextureRegionDrawable(levelSelectScreen.background_level));
        // Needed to contain the ScrollPane within left/right bounds of Table
        levelSelectMenuTable.add(scrollPane).padLeft(padScrollPane).padRight(padScrollPane);


    }

    @Override
    public void sendInMenu() {
        //1. Add Tables to Stage
        levelSelectScreen.getStage().addActor(levelSelectMenuTable);
        //2. Send the screen in with some effects
        levelSelectMenuTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - levelSelectMenuTable.getWidth() / 2, AppSettings.SCREEN_H / 2 - levelSelectMenuTable.getHeight() / 2, 0.3f));
    }

    @Override
    public void sendAwayMenu() {
        //1. Send the screen away with some effects
        levelSelectMenuTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - levelSelectMenuTable.getWidth() / 2, AppSettings.SCREEN_H + levelSelectMenuTable.getHeight() / 2, 0.2f));
    }

    /**
     * Creates a button to represent the level
     *
     * @param level
     * @return The button to use for the level
     */
    public ButtonLevel getLevelButton(int level) {

        if (GameData.prefs.getBoolean("level" + level)) {

            //1. Create level button
            final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, levelSelectScreen.button_level_green, levelSelectScreen.button_level_green);
            //2. Set level number
            levelButton.setLevelNumber(level, AssetLoader.whiteFont);
            //3. Set stars or any other achievements (get from database)
            levelButton.setLevelStars(levelSelectScreen.star_empty, levelSelectScreen.star_golden, 3, GameData.getStarsEarned()[level]);
            levelButton.setLevelStarScaleFactor(2.0f);
            levelButton.setLevelStarPosYStart(-30);

            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    selectedLevel = levelButton.getLevelNumber();

                    if (GameData.getLevelInfo()[selectedLevel]) {
                        sendAwayMenu();
                        GameScreen.currentlevel = selectedLevel;
                        levelSelectScreen.getMyGame().setScreen(new LoadingScreen(levelSelectScreen.getMyGame(), "Instruction Screen", LoadingScreen.TYPE_UI_INSTRUCTION));
                    } else {
                    }
                }
            });

            return levelButton;
        } else {
            //1. Create level button
            final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, levelSelectScreen.button_level_grey, levelSelectScreen.button_level_grey);
            //2. Set level number
            levelButton.setLockActive(true);
            levelButton.setLevelNumber(level, AssetLoader.whiteFont);

            levelButton.addListener(new ActorGestureListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    selectedLevel = levelButton.getLevelNumber();
                    // Inform user of the locked status
                    //infoButton.setText(message, true);
                }
            });
            return levelButton;
        }
    }

    public PagedScrollPane getScrollPane() {
        return scrollPane;
    }
}
