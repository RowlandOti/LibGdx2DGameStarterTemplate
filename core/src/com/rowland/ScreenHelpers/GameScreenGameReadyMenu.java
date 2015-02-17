package com.rowland.ScreenHelpers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LevelSelectScreen;


public class GameScreenGameReadyMenu extends GameScreenAbstractMenu{

	private TableModel gameReadyMenuTable;
	private ButtonGame infoButton, numGemsButton,numEggsButton;
	private ButtonGame missionText;
	private ButtonGame  okButton,cancelButton;
	private TextureRegion holder, button_overlay_left, button_overlay_right, background_menu_berge;

	public GameScreenGameReadyMenu(TextureRegion[] readyMenu)
	{
		this.holder = readyMenu[0];
		this.button_overlay_left = readyMenu[1];
		this.button_overlay_right = readyMenu[2];
		this.background_menu_berge = readyMenu[3];
	}

	@Override
	public void setUpMenu(final GameScreen gameScreen)
	{

			gameReadyMenuTable = new TableModel(background_menu_berge, AppSettings.SCREEN_W/1.3f , AppSettings.WORLD_HEIGHT/1.3f);
			gameReadyMenuTable.setPosition(100*AppSettings.getWorldPositionXRatio(), -AppSettings.SCREEN_H);

				infoButton = MenuCreator.createCustomGameButton(AssetLoader.bigFont, holder, holder);
				infoButton.setTextPosXY(20*AppSettings.getWorldPositionXRatio(), 38*AppSettings.getWorldPositionYRatio());

				missionText = MenuCreator.createCustomGameButton(AssetLoader.smallFont, holder, holder);
				missionText.setTextPosXY(20*AppSettings.getWorldPositionXRatio(), 40*AppSettings.getWorldPositionYRatio());


				cancelButton = MenuCreator.createCustomGameButton(null, button_overlay_left, button_overlay_left);
				cancelButton.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						//gameScreen.setBackgroundTexture(textureBackground);
						gameScreen.getGame().setScreen(new LevelSelectScreen(gameScreen.getGame(), "LevelSelect Screen"));
					}
				});

				okButton = MenuCreator.createCustomGameButton(null, button_overlay_right, button_overlay_right);
				okButton.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						sendAwayMenu(gameScreen);

						GameScreen.state = GameScreen.State.GAME_RUNNING;
						gameScreen.removeBackgroundTexture();
						gameScreen.resetGame();
						gameScreen.setUpTheWorld();
						gameScreen.toggleGestureProcessor(true);

					}
				});


				float dipRatioWidth = 80 * AppSettings.getWorldSizeRatio();
				float dipRatioHeight = 80 * AppSettings.getWorldSizeRatio();
				float padding = 10.0f * AppSettings.getWorldSizeRatio();

				gameReadyMenuTable.defaults().align(Align.bottom);
				gameReadyMenuTable.add(infoButton).size(3.0f*dipRatioWidth, dipRatioHeight/1.5f).pad(padding).center();
				gameReadyMenuTable.row();

				gameReadyMenuTable.add(missionText).size(5.5f*dipRatioWidth, dipRatioHeight/1.5f);
				gameReadyMenuTable.row();

				gameReadyMenuTable.add(numGemsButton).size(80/2*AppSettings.getWorldSizeRatio(), 74/2*AppSettings.getWorldSizeRatio()).pad(padding);
				gameReadyMenuTable.row();
				gameReadyMenuTable.add(numEggsButton).size(80/2*AppSettings.getWorldSizeRatio(), 67/2*AppSettings.getWorldSizeRatio());
				gameReadyMenuTable.row();

				gameReadyMenuTable.add(cancelButton).align(Align.left).size(dipRatioWidth, dipRatioHeight);
				gameReadyMenuTable.add(okButton).align(Align.right).size(dipRatioWidth, dipRatioHeight);

	}

	@Override
	public void sendInMenu(final GameScreen gameScreen)
	{
		// TODO Auto-generated method stub
		missionText.setText("Collect at least ..", true);

		gameScreen.getStage().addActor(gameReadyMenuTable);

		infoButton.setText("LEVEL : "+GameScreen.currentlevel, true);
		gameReadyMenuTable.addAction(Actions.moveTo(100*AppSettings.getWorldPositionXRatio(), 60*AppSettings.getWorldPositionYRatio(), 0.5f));
	}

	@Override
	public void sendAwayMenu(final GameScreen gameScreen)
	{
		// TODO Auto-generated method stub
		gameReadyMenuTable.addAction(Actions.moveTo(100*AppSettings.getWorldPositionXRatio(),- gameReadyMenuTable.getHeight(), 0.5f));
	}

}
