package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.Screens.GameScreen;


public class GameIScreenGameReadyMenu implements IScreenAbstractMenu {

    private TableModel gameReadyMenuTable;
    private ButtonGame okButton, cancelButton;
    private GameScreen gameScreen;

    public GameIScreenGameReadyMenu(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void setUpMenu() {

        gameReadyMenuTable = new TableModel(GameScreen.background_game_over, AppSettings.SCREEN_W / 1.3f, AppSettings.WORLD_HEIGHT / 1.3f);
        gameReadyMenuTable.setPosition(100 * AppSettings.getWorldPositionXRatio(), -AppSettings.SCREEN_H);

        cancelButton = MenuCreator.createCustomGameButton(null, GameScreen.button_overlay_left, GameScreen.button_overlay_left);
        cancelButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameScreen.getGame().setScreen(new LevelSelectScreen(gameScreen.getGame(), "LevelSelect Screen"));
            }
        });

        okButton = MenuCreator.createCustomGameButton(null, GameScreen.button_overlay_right, GameScreen.button_overlay_right);
        okButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                sendAwayMenu();

                gameScreen.state = GameScreen.State.GAME_RUNNING;
                gameScreen.removeBackgroundTexture();
                gameScreen.resetGame();
                gameScreen.setUpTheWorld();
                gameScreen.resume();

            }
        });


        float dipRatioWidth = 80 * AppSettings.getWorldSizeRatio();
        float dipRatioHeight = 80 * AppSettings.getWorldSizeRatio();


        gameReadyMenuTable.add(cancelButton).align(Align.left).size(dipRatioWidth, dipRatioHeight).pad(150f);
        gameReadyMenuTable.add(okButton).align(Align.right).size(dipRatioWidth, dipRatioHeight).pad(150f);

    }

    //Send in menu
    @Override
    public void sendInMenu() {
        gameScreen.getStage().addActor(gameReadyMenuTable);
        gameReadyMenuTable.addAction(Actions.moveTo(100 * AppSettings.getWorldPositionXRatio(), 60 * AppSettings.getWorldPositionYRatio(), 0.5f));
    }

    //Send away menu
    @Override
    public void sendAwayMenu() {
        gameReadyMenuTable.addAction(Actions.moveTo(100 * AppSettings.getWorldPositionXRatio(), -gameReadyMenuTable.getHeight(), 0.5f));
    }

}
