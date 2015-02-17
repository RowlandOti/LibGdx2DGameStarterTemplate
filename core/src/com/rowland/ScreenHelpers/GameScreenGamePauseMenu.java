package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.rowland.Screens.MenuScreen;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class GameScreenGamePauseMenu extends GameScreenAbstractMenu{

	private TableModel pauseMenuTable;
	private ButtonGame mainMenuButton,resumeButton, quitButton;
	private TextureRegion button_menu_up, button_menu_down,button_resume_up, button_resume_down,button_quit_up, button_quit_down;

	public GameScreenGamePauseMenu(TextureRegion[] pauseMenu)
	{
		this.button_menu_up = pauseMenu[4];
		this.button_menu_down = pauseMenu[5];
		this.button_resume_up = pauseMenu[6];
		this.button_resume_down = pauseMenu[7];
		this.button_quit_up = pauseMenu[8];
		this.button_quit_down = pauseMenu[9];
	}

	@Override
	public void setUpMenu(final GameScreen gameScreen)
	{
		pauseMenuTable = new TableModel(null, AppSettings.WORLD_WIDTH , AppSettings.WORLD_HEIGHT);
		pauseMenuTable.setPosition(0 , AppSettings.WORLD_HEIGHT + pauseMenuTable.getHeight());

		//MAIN MENU BUTTON ON THE RIGHT SIDE
		mainMenuButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, button_menu_up, button_menu_down);
		mainMenuButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				sendAwayMenu(gameScreen);
				gameScreen.getGame().setScreen(new LoadingScreen(gameScreen.getGame(), "Menu Screen", LoadingScreen.TYPE_UI_MENU));
			}
		});

		resumeButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, button_resume_up, button_resume_down);
		resumeButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				sendAwayMenu(gameScreen);
				GameScreen.state = GameScreen.State.GAME_RUNNING;
				gameScreen.removeBackgroundTexture();
		        gameScreen.toggleGestureProcessor(true);
			}
		});


		quitButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, button_quit_up, button_quit_down);
		quitButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				super.touchUp(event, x, y, pointer, button);

				Gdx.app.exit();
			}
		});

		float dipRatioWidth = 174 * AppSettings.getWorldSizeRatio();
		float dipRatioHeight = 74* AppSettings.getWorldSizeRatio();
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
	public void sendInMenu(GameScreen gameScreen)
	{
		gameScreen.getStage().addActor(pauseMenuTable);
    	pauseMenuTable.addAction(Actions.moveTo(0, AppSettings.WORLD_HEIGHT- pauseMenuTable.getHeight(), 0.5f));
	}

	@Override
	public void sendAwayMenu(GameScreen gameScreen)
	{
		pauseMenuTable.addAction(Actions.moveTo(0 , AppSettings.WORLD_HEIGHT + pauseMenuTable.getHeight(), 0.5f));
	}

}
