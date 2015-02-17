package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.models.EmptyActor;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.scene2d.ui.Text;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetLoader;

public class HighScoresScreen extends AbstractScreen implements IScreen{

	TableModel tableMenu;
	float dipRatioWidth = AppSettings.WORLD_WIDTH/2.5f;
	float dipRatioHeight = 50 * AppSettings.getWorldSizeRatio();
	float padding = 2.0f * AppSettings.getWorldSizeRatio();
	ButtonGame btnHome;
	EmptyActor player;

	public HighScoresScreen(Game game, String screenName) {
		super(game, screenName);
		setUpScreenElements();
		setUpInfoPanel();
		setUpActors();
		setUpMenu();
	}

	@Override
	public void setUpScreenElements() {
		setBackgroundTexture(AssetLoader.background_home);
		setBackButtonActive(true);
	}

	@Override
	public void setUpInfoPanel() {

		// Home Button
		btnHome = MenuCreator.createCustomGameButton(null, AssetLoader.button_overlay_left, AssetLoader.button_overlay_left, 64, 64 ,true);
		btnHome.setPosition(AppSettings.SCREEN_W - btnHome.getWidth(), AppSettings.SCREEN_H-btnHome.getHeight());
		btnHome.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				setAwayMenu();

				getGame().setScreen(new MenuScreen(getGame(), "MainMenu"));

			}
		});

		getStage().addActor(btnHome);
	}

	private void setUpActors() {
		player = new EmptyActor(40, 80, true);
		player.setPosition(100 * AppSettings.getWorldPositionXRatio()- player.getWidth() / 2.0f, 100 * AppSettings.getWorldPositionYRatio() - player.getHeight() / 2.0f);
		getStage().addActor(player);

		player.setAnimation(AssetLoader.pummaWalk, true, true);

		ButtonGame text = MenuCreator.createCustomGameButton(AssetLoader.smallFont, AssetLoader.holder, AssetLoader.holder, dipRatioWidth/1.3f, dipRatioHeight*1.2f, true);
		text.setPosition(player.getX(),player.getY()+player.getHeight());
		text.setText("High Scores ", true);
		text.setTextPosXY(30, 50);

		getStage().addActor(text);
	}

	@Override
	public void setUpMenu() {
		// #03.2 Test
		// Menu table with sliding animation
		// // #######################################
		tableMenu = new TableModel(AssetLoader.background_menu_berge, 528*AppSettings.getWorldPositionXRatio() , 454*AppSettings.getWorldPositionYRatio());
		tableMenu.setPosition(AppSettings.SCREEN_W - 1.1f*tableMenu.getWidth(), AppSettings.SCREEN_H + tableMenu.getHeight());
		tableMenu.addAction(Actions.moveTo(AppSettings.SCREEN_W - 1.1f*tableMenu.getWidth(), AppSettings.SCREEN_H - 1.1f*tableMenu.getHeight(), 0.5f));

		for(int i=0; i<5;i++){
			addScoreButton(i);
		}
		getStage().addActor(tableMenu);
	}

	public void setAwayMenu() {
		tableMenu.addAction(Actions.moveTo(AppSettings.SCREEN_W - 1.1f*tableMenu.getWidth(), AppSettings.SCREEN_H + tableMenu.getHeight(), 0.5f));
	}

	private void addScoreButton(int i){

		Text scoreText = new Text(AssetLoader.smallFont, dipRatioWidth,dipRatioHeight, true);

		scoreText.setText((i+1)+") "+"  "+GameData.getHighScores()[i]);
		scoreText.setOrigin(0, 0);

		tableMenu.add(scoreText).size(dipRatioWidth, dipRatioHeight).pad(padding);
		tableMenu.row();
		}

	@Override
	public void keyBackPressed() {
		super.keyBackPressed();
		getGame().setScreen(
				new MenuScreen(getGame(), "MenuScreen"));
	}
}
