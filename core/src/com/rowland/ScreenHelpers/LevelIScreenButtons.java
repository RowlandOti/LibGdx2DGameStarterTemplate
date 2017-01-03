package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.moribitotech.mtx.scene2d.ui.ButtonLevel;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.Screens.LoadingScreen;
import com.rowland.UI.PagedScrollPane;

/**
 * @author Rowland
 */
public class LevelIScreenButtons implements IScreenAbstractMenu {

    private PagedScrollPane scrollPane;
    private LevelSelectScreen levelSelectScreen;

    private int selectedLevel;

    public LevelIScreenButtons(final LevelSelectScreen levelSelectScreen) {
        this.levelSelectScreen = levelSelectScreen;
    }

    /**
     * Creates a button to represent the level
     *
     * @param level
     * @return The button to use for the level
     */
    public ButtonLevel getLevelButton(int level) {
        //3. Set lock condition (get from database if it is locked or not and lock it)
        // use if/else here to lock or not
        if (!GameData.prefs.getBoolean("level" + level)) {
            //1. Create level button
            final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, LevelSelectScreen.button_level_grey, LevelSelectScreen.button_level_grey);

            //final int selectedLevel =i;
            //2. Set level number
            levelButton.setLockActive(true);
            levelButton.setScale(AppSettings.getWorldSizeRatio(), AppSettings.getWorldSizeRatio());
            //levelButton.setLevelNumber(level , AssetLoader.whiteFont);
            //levelButton.setTextureExternal(LevelSelectScreen.lock_closed, true);
            levelButton.setTextureExternalPosXY((17.5f * AppSettings.getWorldPositionXRatio()), (15.0f * AppSettings.getWorldPositionYRatio()));
            levelButton.setTextureExternalSize(80 * AppSettings.getWorldSizeRatio(), 80 * AppSettings.getWorldSizeRatio());

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
                    //infoButton.setText(message, true);
                }
            });

            return levelButton;
        }

        if (GameData.prefs.getBoolean("level" + level)) {

            //1. Create level button
            final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, LevelSelectScreen.button_level_green, LevelSelectScreen.button_level_green);

            //final int selectedLevel =i;
            //2. Set level number
            levelButton.setLevelNumber(level, AssetLoader.whiteFont);
            levelButton.setLevelStars(LevelSelectScreen.star_empty, LevelSelectScreen.star_normal, 3, GameData.getStarsEarned()[level]);
            levelButton.setLevelStarSizeRatio(5 * AppSettings.getWorldSizeRatio());
            levelButton.setLevelStarPosYStart(-40 * AppSettings.getWorldSizeRatio());

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
                    //infoButton.setText(message, true);
                }
            });

            return levelButton;
        }
        return null;

        //4. Set stars or any other achievements (get from database or text files here)
        // I just made a random number of earned stars
    }

    @Override
    public void setUpMenu() {
        levelSelectScreen.getStage().setDebugAll(true);

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = new TextureRegionDrawable(LevelSelectScreen.background_level);
        //style.hScrollKnob = new TextureRegionDrawable(LevelSelectScreen.nav_left);
        //style.hScroll = new TextureRegionDrawable(LevelSelectScreen.nav_right);

        scrollPane = new PagedScrollPane();
        scrollPane.setSize(AppSettings.SCREEN_W - 100f, AppSettings.SCREEN_H - 140f);
        scrollPane.setPosition(AppSettings.SCREEN_W/2, AppSettings.SCREEN_H + scrollPane.getHeight()/2);
        scrollPane.setStyle(style);
        scrollPane.setFlingTime(0.5f);
        scrollPane.setPageSpacing(100f);
        scrollPane.setScrollingDisabled(false, true);

        int c = 1;
        int numberOfLevels = GameData.NUMBER_OF_LEVELS;

        float pad = 20 * AppSettings.getWorldSizeRatio();
        float button_size = 75f * AppSettings.getWorldSizeRatio();

        for (int l = 0; l < 2; l++) {

            Table levels = new TableModel();
            levels.pad(pad, pad, pad, pad);

            for (int rows = 0; rows < 3; rows++) {
                levels.row();

                for (int columns = 0; columns < 4; columns++) {
                    c = c++;

                    if (numberOfLevels + 1 != c) {
                        Cell cell = levels.add(getLevelButton(c++)).size(button_size, button_size).padTop(pad);

                        if (columns % 4 == 0) {
                            cell.padLeft(pad * 3);
                        } else if (columns % 4 == 3) {
                            cell.padRight(pad * 3);
                        } else {
                            if (columns == 1) {
                                cell.padRight(pad * 5).padLeft(pad * 5);
                            } else {
                                cell.padRight(pad * 5);
                            }
                        }

                    } else {
                        break;
                    }
                }
            }
            scrollPane.addPage(levels);
        }

        levelSelectScreen.getStage().addActor(scrollPane);
    }

    @Override
    public void sendInMenu() {
        scrollPane.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - scrollPane.getWidth() / 2, AppSettings.SCREEN_H / 2 - scrollPane.getHeight() / 2, 0.5f));
    }

    @Override
    public void sendAwayMenu() {
        scrollPane.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2, AppSettings.SCREEN_H + scrollPane.getHeight() / 2, 0.5f));
    }
}
