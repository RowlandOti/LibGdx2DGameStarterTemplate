package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LoadingScreen;

public class GameScreenGamePauseMenu extends GameScreenAbstractMenu {

    private TableModel pauseMenuTable;
    private ButtonGame mainMenuButton, resumeButton, quitButton;
    private GameScreen gameScreen;

    public GameScreenGamePauseMenu(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void setUpMenu() {
        pauseMenuTable = new TableModel(null, AppSettings.WORLD_WIDTH, AppSettings.WORLD_HEIGHT);
        pauseMenuTable.setPosition(0, AppSettings.WORLD_HEIGHT + pauseMenuTable.getHeight());

        //MAIN MENU BUTTON ON THE RIGHT SIDE
        mainMenuButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, GameScreen.button_menu_up, GameScreen.button_menu_down);
        mainMenuButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                sendAwayMenu();
                gameScreen.getGame().setScreen(new LoadingScreen(gameScreen.getGame(), "Menu Screen", LoadingScreen.TYPE_UI_MENU));
            }
        });

        resumeButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, GameScreen.button_resume_up, GameScreen.button_resume_down);
        resumeButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sendAwayMenu();
                gameScreen.removeBackgroundTexture();
                gameScreen.resume();
            }
        });


        quitButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, GameScreen.button_quit_up, GameScreen.button_quit_down);
        quitButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });

        float dipRatioWidth = 174 * AppSettings.getWorldSizeRatio();
        float dipRatioHeight = 74 * AppSettings.getWorldSizeRatio();
        float dipPadding = 10.0f * AppSettings.getWorldSizeRatio();

        // #######################################
        //tableMenu.add(shootingActor).size(dipRatioWidth, dipRatioHeight).pad(dipPadding);
        pauseMenuTable.add(mainMenuButton).size(dipRatioWidth, dipRatioHeight).pad(dipPadding);
        pauseMenuTable.row();
        pauseMenuTable.add(resumeButton).size(dipRatioWidth, dipRatioHeight).pad(dipPadding);
        pauseMenuTable.row();
        pauseMenuTable.add(quitButton).size(dipRatioWidth, dipRatioHeight).pad(dipPadding);

    }

    @Override
    public void sendInMenu() {
        gameScreen.getStage().addActor(pauseMenuTable);
        pauseMenuTable.addAction(Actions.moveTo(0, AppSettings.WORLD_HEIGHT - pauseMenuTable.getHeight(), 0.5f));
    }

    @Override
    public void sendAwayMenu() {
        pauseMenuTable.addAction(Actions.moveTo(0, AppSettings.WORLD_HEIGHT + pauseMenuTable.getHeight(), 0.5f));
    }

}
