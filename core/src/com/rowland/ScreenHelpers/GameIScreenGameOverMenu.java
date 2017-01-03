package com.rowland.ScreenHelpers;

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

public class GameIScreenGameOverMenu implements IScreenAbstractMenu {

    private GameScreen gameScreen;
    private TableModel gameOverMenuTable;

    private ButtonGame infoButton;
    private ButtonGame scoreButton;
    private ButtonGame okButton;
    private ButtonGame cancelButton;

    float buttonWidth = 174 * AppSettings.getWorldSizeRatio();
    float buttonHeight = 74 * AppSettings.getWorldSizeRatio();
    float dipRatioWidth = 80 * AppSettings.getWorldSizeRatio();
    float dipRatioHeight = 80 * AppSettings.getWorldSizeRatio();
    float padding = 10.0f * AppSettings.getWorldSizeRatio();


    public GameIScreenGameOverMenu(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }


    @Override
    public void setUpMenu() {
        //Set Up means create and add the required Actors/UI Widgets to the Screen

        gameOverMenuTable = new TableModel(GameScreen.background_game_over, AppSettings.SCREEN_W, AppSettings.WORLD_HEIGHT);
        gameOverMenuTable.setPosition(0, -gameOverMenuTable.getHeight());

        gameScreen.getStage().addActor(gameOverMenuTable);

        infoButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, GameScreen.button_green, GameScreen.button_green, dipRatioWidth / 2, dipRatioHeight, true);
        infoButton.setTextPosXY(100 * AppSettings.getWorldSizeRatio(), 60 * AppSettings.getWorldSizeRatio());

        scoreButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, GameScreen.button_green, GameScreen.button_green, dipRatioWidth, dipRatioHeight, true);
        scoreButton.setTextPosXY(100 * AppSettings.getWorldSizeRatio(), 60 * AppSettings.getWorldSizeRatio());


        okButton = MenuCreator.createCustomGameButton(null, GameScreen.button_overlay_right, GameScreen.button_overlay_right);
        okButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                sendAwayMenu();
                gameScreen.restartGame();
                gameScreen.resetGame();
                GameScreen.state = GameScreen.State.GAME_RUNNING;
                gameScreen.resetGame();
            }
        });

        cancelButton = MenuCreator.createCustomGameButton(null, GameScreen.button_overlay_left, GameScreen.button_overlay_left);
        cancelButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameScreen.resetGame();
                sendAwayMenu();
                gameScreen.getGame().setScreen(new LoadingScreen(gameScreen.getGame(), "Menu Screen", LoadingScreen.TYPE_UI_MENU));


            }
        });

        gameOverMenuTable.add(infoButton).left().size(infoButton.getWidth(), infoButton.getHeight()).pad(padding);
        gameOverMenuTable.row();

        gameOverMenuTable.add(scoreButton).left().size(scoreButton.getWidth(), scoreButton.getHeight()).pad(padding);
        gameOverMenuTable.row();

        gameOverMenuTable.add(cancelButton).size(buttonWidth, buttonHeight).pad(padding).left();
        gameOverMenuTable.add(okButton).size(buttonWidth, buttonHeight).pad(padding).right();

    }

    @Override
    public void sendInMenu() {
        GameScreen.gameOverCounterForAds++;
        infoButton.setText("" + GameScreen.gameoverinfo, true);
        scoreButton.setText("" + GameScreen.scoreString, true);
        gameOverMenuTable.addAction(Actions.moveTo(0, 0, 0.5f));
    }

    @Override
    public void sendAwayMenu() {
        gameOverMenuTable.addAction(Actions.moveTo(0, -gameOverMenuTable.getHeight(), 0.5f));
    }
}
