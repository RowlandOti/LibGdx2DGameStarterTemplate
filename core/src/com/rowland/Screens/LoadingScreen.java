package com.rowland.Screens;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.madboy.buttons.JungleGameButton;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.scene2d.models.SmartActor;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.UI.MadBoyGameButton;

/**
 * @author Rowland
 */
public class LoadingScreen extends MyAbstractScreen {

    private static final String TAG = "ASSETLOAD";
    public static final int TYPE_UI_MENU = 0;
    public static final int TYPE_UI_LEVEL = 1;
    public static final int TYPE_UI_HIGHSCORE = 2;
    public static final int TYPE_UI_CREDIT = 3;
    public static final int TYPE_UI_INSTRUCTION = 4;
    public static final int TYPE_GAME = 5;

    public static final String BASE_ATLAS = "data/base/base_atlas.pack";
    public static final String UI_MENU_ATLAS = "data/ui_menu/menu_atlas.pack";
    public static final String UI_LEVEL_ATLAS = "data/ui_level/level_atlas.pack";
    public static final String UI_HIGHSCORE_ATLAS = "data/ui_highscore/highscore_atlas.pack";
    public static final String UI_CREDIT_ATLAS = "data/ui_credit/credit_atlas.pack";
    public static final String UI_INSTRUCTION = "data/ui_instruction/instruction_atlas.pack";
    public static final String GAME_ATLAS = "data/game_screen/game_screen_atlas.pack";

    private int type;
    private Texture background_loading, logo, progressBarImg, progressBarBaseImg;
    private SpriteBatch batch;
    private boolean loaded = false;
    private Vector2 logoPos, pbPos;
    public TableModel menuTable;
    public EmptyActorLight btnLogo;

    public LoadingScreen(Game game, String screenName, int type) {
        super(game, screenName);
        this.type = type;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load assets needed for loading screen
        getMyGame().getManager().loadGroup("loading_screen");
        getMyGame().getManager().loadGroup("base");
        getMyGame().getManager().finishLoading(); //Blocks until all resources are loaded into memory

        Gdx.app.log(TAG, "Assets loaded");

        // Get Assets
        background_loading = getMyGame().getManager().get("data/loading_screen/background_loading.png");
        logo = getMyGame().getManager().get("data/loading_screen/logo.png");

        //progressBarImg = getMyGame().getManager().get("data/loading_screen/progress_bar.png");
        //progressBarBaseImg = getMyGame().getManager().get("data/loading_screen/progress_bar_base.png");

        // Get logo position
        //logoPos = new Vector2();
        // Centre the log in the screen
        //logoPos.set(Gdx.graphics.getWidth()/2 - logo.getWidth()/2, Gdx.graphics.getHeight()/2 - logo.getHeight()/2);

        // ProgressBar position
        //pbPos = new Vector2();
        //pbPos.set(logoPos.x, logoPos.y - (logo.getHeight()));

        //Depending on screen type load appropriate assets
        switch (type) {
            case TYPE_UI_MENU:
                if (getMyGame().getManager().isLoaded(GAME_ATLAS, TextureAtlas.class))
                    getMyGame().getManager().unloadGroup("ui_game");
                getMyGame().getManager().loadGroup("ui_menu");
                break;
            case TYPE_UI_LEVEL:
                getMyGame().getManager().loadGroup("ui_level");
                break;
            case TYPE_UI_HIGHSCORE:
                getMyGame().getManager().loadGroup("ui_highscore");
                break;
            case TYPE_UI_CREDIT:
                getMyGame().getManager().loadGroup("ui_credit");
                break;
            case TYPE_UI_INSTRUCTION:
                getMyGame().getManager().loadGroup("ui_instruction");
                break;
            case TYPE_GAME:
                getMyGame().getManager().unloadGroup("ui_menu");
                getMyGame().getManager().unloadGroup("ui_level");
                getMyGame().getManager().unloadGroup("ui_instruction");
                getMyGame().getManager().loadGroup("ui_game");
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render background image
        //batch.begin();
        //batch.updateHUD(background_loading, 0, 0);
        //batch.end();

        // Check if async load is done
        if (!loaded) {
            // Render Logo and Loading Bar
            batch.begin();
            batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() / 2 - logo.getHeight() / 2);
            //batch.updateHUD(progressBarBaseImg, pbPos.x, pbPos.y);
            //batch.updateHUD(progressBarImg, pbPos.x, pbPos.y,progressBarImg.getWidth() * getMyGame().getManager().getProgress(),progressBarImg.getHeight());
            batch.end();


            if (getMyGame().getManager().update()) {
                loaded = true;

                switch (type) {
                    case TYPE_UI_MENU:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(getMyGame(), "MainMenu Screen"));
                        break;
                    case TYPE_UI_LEVEL:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelSelectScreen(getMyGame(), "LevelSelect Screen"));
                        break;
                    case TYPE_UI_HIGHSCORE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new HighScoresScreen(getMyGame(), "Highscore Screen"));
                        break;
                    case TYPE_UI_CREDIT:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new CreditsScreen(getMyGame(), "Credit Screen"));
                        break;
                    case TYPE_UI_INSTRUCTION:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new PreGameScreen(getMyGame(), "Tutorial Screen"));
                        break;
                    case TYPE_GAME:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(getMyGame(), "Game Screen"));
                        break;
                }
            }
        } else {

        }

    }

    @Override
    public void hide() {
        getMyGame().getManager().unloadGroup("loading_screen");
    }


}
