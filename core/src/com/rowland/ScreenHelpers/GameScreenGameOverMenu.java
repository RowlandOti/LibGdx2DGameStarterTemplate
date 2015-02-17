package com.rowland.ScreenHelpers;

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
import com.rowland.Screens.MenuScreen;

public class GameScreenGameOverMenu extends GameScreenAbstractMenu{

	private TableModel gameOverMenuTable;
	private ButtonGame infoButton,scoreButton;

	private ButtonGame okButton, cancelButton;

	float buttonWidth = 174 * AppSettings.getWorldSizeRatio();
	float buttonHeight = 74 * AppSettings.getWorldSizeRatio();
	float dipRatioWidth = 80 * AppSettings.getWorldSizeRatio();
	float dipRatioHeight = 80 * AppSettings.getWorldSizeRatio();
	float padding = 10.0f * AppSettings.getWorldSizeRatio();

	private TextureRegion holder, button_overlay_left, button_overlay_right, background_menu_berge;

	public GameScreenGameOverMenu(TextureRegion[] overMenu)
	{
		this.holder = overMenu[0];
		this.button_overlay_left = overMenu[1];
		this.button_overlay_right = overMenu[2];
		this.background_menu_berge = overMenu[3];
	}

	@Override
	public void setUpMenu(final GameScreen gameScreen) {
		//Set Up means create and add the required Actors/UI Widgets to the Screen

		gameOverMenuTable = new TableModel(background_menu_berge, AppSettings.SCREEN_W, AppSettings.WORLD_HEIGHT);
		gameOverMenuTable.setPosition(0, -gameOverMenuTable.getHeight());

		gameScreen.getStage().addActor(gameOverMenuTable);

		infoButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, holder, holder, dipRatioWidth/2, dipRatioHeight, true);

		infoButton.setTextPosXY(100*AppSettings.getWorldSizeRatio(), 60*AppSettings.getWorldSizeRatio());

		scoreButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, holder, holder, dipRatioWidth, dipRatioHeight, true);

		scoreButton.setTextPosXY(100*AppSettings.getWorldSizeRatio(), 60*AppSettings.getWorldSizeRatio());


		okButton = MenuCreator.createCustomGameButton(null, holder, holder);
		okButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				sendAwayMenu(gameScreen);
				GameScreen.state = GameScreen.State.GAME_READY;
				gameScreen.resetGame();
			}
		});

		cancelButton = MenuCreator.createCustomGameButton(null, holder, holder);
		cancelButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				gameScreen.resetGame();
				sendAwayMenu(gameScreen);
				gameScreen.getGame().setScreen(new MenuScreen(gameScreen.getGame(), "MainMenuScreen"));


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
	public void sendInMenu(GameScreen gameScreen) {
		// TODO Auto-generated method stub
		GameScreen.gameOverCounterForAds++;
		infoButton.setText(""+GameScreen.gameoverinfo, true);
		scoreButton.setText(""+GameScreen.scoreString, true);
		gameOverMenuTable.addAction(Actions.moveTo(0, 0, 0.5f));
	}

	@Override
	public void sendAwayMenu(GameScreen gameScreen) {
		// TODO Auto-generated method stub

		gameOverMenuTable.addAction(Actions.moveTo(0,- gameOverMenuTable.getHeight(), 0.5f));
	}

}
