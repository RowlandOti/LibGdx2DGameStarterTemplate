package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LoadingScreen;

public class GameIScreenGamePauseMenu implements IScreenAbstractMenu {

    private Table pauseMenuTable;
    private TextButton mainMenuButton, resumeButton, quitButton;
    private GameScreen gameScreen;

    final String menuText = "Menu";
    final String resumeText = "Resume";
    final String quitText = "Quit";

    public GameIScreenGamePauseMenu(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void setUpMenu() {
        pauseMenuTable = new Table();
        pauseMenuTable.setSize(AppSettings.SCREEN_H / 2, AppSettings.SCREEN_W / 2);
        pauseMenuTable.setBackground(new TextureRegionDrawable(GameScreen.background_game_pause));
        pauseMenuTable.setPosition(AppSettings.SCREEN_W / 2, AppSettings.SCREEN_H + pauseMenuTable.getHeight() / 2);

        float dipRatioWidth = 174 * AppSettings.getWorldSizeRatio();
        float dipRatioHeight = 74 * AppSettings.getWorldSizeRatio();
        float dipPadding = 10.0f * AppSettings.getWorldSizeRatio();

        float fontScale = 1.3f * AppSettings.getWorldSizeRatio();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        gameScreen.getGameFont().setScale(fontScale);
        buttonStyle.font = gameScreen.getGameFont();
        buttonStyle.up = new TextureRegionDrawable(gameScreen.button_green);
        buttonStyle.down = new TextureRegionDrawable(gameScreen.button_orange);


        //MAIN MENU BUTTON ON THE RIGHT SIDE
        mainMenuButton = new TextButton(menuText, buttonStyle);
        mainMenuButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                sendAwayMenu();
                gameScreen.getGame().setScreen(new LoadingScreen(gameScreen.getGame(), "Menu Screen", LoadingScreen.TYPE_UI_MENU));
            }
        });

        resumeButton = new TextButton(resumeText, buttonStyle);
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


        quitButton = new TextButton(quitText, buttonStyle);
        quitButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });

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
        pauseMenuTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - pauseMenuTable.getWidth() / 2, AppSettings.SCREEN_H / 2 - pauseMenuTable.getHeight() / 2, 0.5f));
    }

    @Override
    public void sendAwayMenu() {
        pauseMenuTable.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2, AppSettings.SCREEN_H + pauseMenuTable.getHeight() / 2, 0.5f));
    }
}
