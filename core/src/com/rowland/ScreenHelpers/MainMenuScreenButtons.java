package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.madboy.buttons.JungleGameButton;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.LoadingScreen;
import com.rowland.Screens.MenuScreen;
import com.rowland.Screens.PreGameScreen;

import java.util.Random;

public class MainMenuScreenButtons {

	private MenuScreen mainMenuScreen;

	public MainMenuScreenButtons(final MenuScreen mainMenuScreen)
	{
		this.mainMenuScreen = mainMenuScreen;
	}

	public void setUpMainMenuButtons()
	{

		// Menu Table (Play, Scores, Settings)
		// #################################################################
		mainMenuScreen.menuTable = new TableModel(null,AppSettings.SCREEN_W, AppSettings.SCREEN_H);
		mainMenuScreen.menuTable.setPosition(0,-120 * AppSettings.getWorldPositionYRatio());

		//
		// Btn Values
		// #################################################################
		Random rnd = new Random();
		float btnWidth = 270f ;
		float btnHeight = 90f ;

		//
		// Btn Play
		// #################################################################
		mainMenuScreen.btnPlay = new JungleGameButton(btnWidth,btnHeight, rnd, true);
		mainMenuScreen.btnPlay.setTextureRegion(MenuScreen.img_obj_btn_play,true);
		mainMenuScreen.btnPlay.setOrigin(mainMenuScreen.btnPlay.getWidth() / 2.0f,mainMenuScreen.btnPlay.getHeight() / 2.0f);
		mainMenuScreen.btnPlay.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y,int pointer, int button)
			{
				super.touchDown(event, x, y, pointer, button);

				mainMenuScreen.btnPlay.clearActions();
				EffectCreator.create_SC_SHK_BTN(mainMenuScreen.btnPlay, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
				mainMenuScreen.getMyGame().setScreen(new LoadingScreen(mainMenuScreen.getMyGame(), "Loading Screen", LoadingScreen.TYPE_UI_LEVEL));
			}

		});

		//
		// Btn Scores
		// #################################################################
		mainMenuScreen.btnScores = new JungleGameButton(btnWidth,btnHeight, rnd, true);
		mainMenuScreen.btnScores.setTextureRegion(MenuScreen.img_obj_btn_scores, true);
		mainMenuScreen.btnScores.setOrigin(mainMenuScreen.btnScores.getWidth() / 2.0f,mainMenuScreen.btnScores.getHeight() / 2.0f);
		mainMenuScreen.btnScores.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y,int pointer, int button)
			{
				super.touchDown(event, x, y, pointer, button);

				mainMenuScreen.btnScores.clearActions();
				EffectCreator.create_SC_SHK_BTN(mainMenuScreen.btnScores,1.3f, 1.3f, 5f, 0, 0.05f, null, false);
				//mainMenuScreen.getMyGame().setScreen(new LoadingScreen(mainMenuScreen.getMyGame(), "Highscore Screen", LoadingScreen.TYPE_UI_HIGHSCORE));
			}
		});

		//
		// Btn Settings
		// #################################################################
		mainMenuScreen.btnSettings = new JungleGameButton(btnWidth, btnHeight, rnd, true);
		mainMenuScreen.btnSettings.setTextureRegion(MenuScreen.img_obj_btn_credit, true);
		mainMenuScreen.btnSettings.setOrigin(mainMenuScreen.btnSettings.getWidth() / 2.0f,mainMenuScreen.btnSettings.getHeight() / 2.0f);
		mainMenuScreen.btnSettings.addListener(new ActorGestureListener() {
					@Override
					public void touchDown(InputEvent event, float x, float y, int pointer, int button)
					{
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSettings.clearActions();
						EffectCreator.create_SC_SHK_BTN(mainMenuScreen.btnSettings, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);
						//mainMenuScreen.getMyGame().setScreen(new LoadingScreen(mainMenuScreen.getMyGame(), "Settings Screen", LoadingScreen.TYPE_UI_HIGHSCORE));
					}
				});

		//
		// Scale them to "0", we will send them in after splash completed
		// #################################################################
		mainMenuScreen.btnPlay.setScale(0f);
		mainMenuScreen.btnScores.setScale(0f);
		mainMenuScreen.btnSettings.setScale(0f);

		//
		// Add
		// #################################################################
		mainMenuScreen.menuTable.add(mainMenuScreen.btnPlay).pad(6);
		mainMenuScreen.menuTable.row();
		mainMenuScreen.menuTable.add(mainMenuScreen.btnScores).pad(6);
		mainMenuScreen.menuTable.row();
		mainMenuScreen.menuTable.add(mainMenuScreen.btnSettings).pad(6);
		//
		mainMenuScreen.getStage().addActor(mainMenuScreen.menuTable);

	}

	public void setUpSocialButtons()
	{
		//
		// Btn Values
		// #################################################################
		Random rnd = new Random();
		//float btnWidth = 80f;
		//float btnHeight = 130f;
		float btnWidth = 90f;
		float btnHeight = 146.25f;

		float initialPosX = 30 * AppSettings.getWorldPositionXRatio();
		float initialposY = AppSettings.SCREEN_H;
		final float touchAnimationDuration = 0.05f;

		//
		// Btn Facebook
		// #################################################################
		mainMenuScreen.btnSocialFacebook = new JungleGameButton(btnWidth, btnHeight, rnd, true);
		mainMenuScreen.btnSocialFacebook.setTextureRegion(MenuScreen.img_obj_social_facebook, true);
		mainMenuScreen.btnSocialFacebook.setOrigin(mainMenuScreen.btnSocialFacebook.getWidth() / 2.0f, mainMenuScreen.btnSocialFacebook.getHeight() / 2.0f);
		mainMenuScreen.btnSocialFacebook.addListener(new ActorGestureListener() {

					@Override
					public void touchDown(InputEvent event, float x, float y, int pointer, int button)
					{
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialFacebook.addAction(Actions.moveTo(mainMenuScreen.btnSocialFacebook.getX(),AppSettings.SCREEN_H - mainMenuScreen.btnSocialFacebook.getHeight(),touchAnimationDuration));
					}

					@Override
					public void touchUp(InputEvent event, float x, float y,int pointer, int button)
					{
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialFacebook.addAction(Actions.moveTo(mainMenuScreen.btnSocialFacebook.getX(),AppSettings.SCREEN_H - mainMenuScreen.btnSocialFacebook.getHeight() / 1.25f,touchAnimationDuration));
					}
				});

		//
		// Btn Twitter
		// #################################################################
		mainMenuScreen.btnSocialTwitter = new JungleGameButton(btnWidth,btnHeight, rnd, true);
		mainMenuScreen.btnSocialTwitter.setTextureRegion(MenuScreen.img_obj_social_twitter, true);
		mainMenuScreen.btnSocialTwitter.setOrigin(mainMenuScreen.btnSocialTwitter.getWidth() / 2.0f,mainMenuScreen.btnSocialTwitter.getHeight() / 2.0f);
		mainMenuScreen.btnSocialTwitter.addListener(new ActorGestureListener() {
					@Override
					public void touchDown(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialTwitter.addAction(Actions.moveTo(mainMenuScreen.btnSocialTwitter.getX(),AppSettings.SCREEN_H- mainMenuScreen.btnSocialTwitter.getHeight(),touchAnimationDuration));
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button)
					{
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialTwitter.addAction(Actions.moveTo(mainMenuScreen.btnSocialTwitter.getX(),AppSettings.SCREEN_H- mainMenuScreen.btnSocialTwitter.getHeight() / 1.25f,touchAnimationDuration));
					}
				});

		//
		// Btn Google+
		// #################################################################
		mainMenuScreen.btnSocialGoogle = new JungleGameButton(btnWidth, btnHeight, rnd, true);
		mainMenuScreen.btnSocialGoogle.setTextureRegion(MenuScreen.img_obj_social_google, true);
		mainMenuScreen.btnSocialGoogle.setOrigin(mainMenuScreen.btnSocialGoogle.getWidth() / 2.0f,mainMenuScreen.btnSocialGoogle.getHeight() / 2.0f);
		mainMenuScreen.btnSocialGoogle.addListener(new ActorGestureListener() {
					@Override
					public void touchDown(InputEvent event, float x, float y, int pointer, int button)
					{
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialGoogle.addAction(Actions.moveTo(mainMenuScreen.btnSocialGoogle.getX(),AppSettings.SCREEN_H- mainMenuScreen.btnSocialGoogle.getHeight(),touchAnimationDuration));
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchDown(event, x, y, pointer, button);

						mainMenuScreen.btnSocialGoogle.addAction(Actions.moveTo(mainMenuScreen.btnSocialGoogle.getX(),AppSettings.SCREEN_H- mainMenuScreen.btnSocialGoogle.getHeight() / 1.25f,touchAnimationDuration));
					}
				});

		//
		// Construct Position
		// #################################################################
		mainMenuScreen.btnSocialFacebook.setPosition(initialPosX,initialposY);
		mainMenuScreen.btnSocialTwitter.setPosition(mainMenuScreen.btnSocialFacebook.getX()+ mainMenuScreen.btnSocialFacebook.getWidth(),initialposY);
		mainMenuScreen.btnSocialGoogle.setPosition(mainMenuScreen.btnSocialTwitter.getX()+ mainMenuScreen.btnSocialTwitter.getWidth(),initialposY);

		//
		// Add
		// #################################################################
		mainMenuScreen.getStage().addActor(mainMenuScreen.btnSocialFacebook);
		mainMenuScreen.getStage().addActor(mainMenuScreen.btnSocialTwitter);
		mainMenuScreen.getStage().addActor(mainMenuScreen.btnSocialGoogle);
	}

	public void sendInMainMenuButtons()
	{
		EffectCreator.create_SC_BTO(mainMenuScreen.btnPlay, 1.3f, 1.3f,0.4f, null, false);
		EffectCreator.create_SC_BTO(mainMenuScreen.btnScores, 1.3f, 1.3f,0.6f, null, false);
		EffectCreator.create_SC_BTO(mainMenuScreen.btnSettings, 1.3f,1.3f, 0.8f, null, false);

		mainMenuScreen.btnPlay.setTouchable(Touchable.enabled);
		mainMenuScreen.btnScores.setTouchable(Touchable.enabled);
		mainMenuScreen.btnSettings.setTouchable(Touchable.enabled);
	}

	public void sendAwayMainMenuButtons()
	{
		EffectCreator.create_SC(mainMenuScreen.btnPlay, 0f, 0f, 0.4f,null, false);
		EffectCreator.create_SC(mainMenuScreen.btnScores, 0f, 0f, 0.6f,null, false);
		EffectCreator.create_SC(mainMenuScreen.btnSettings, 0f, 0f, 0.8f,null, false);

		mainMenuScreen.btnPlay.setTouchable(Touchable.disabled);
		mainMenuScreen.btnScores.setTouchable(Touchable.disabled);
		mainMenuScreen.btnSettings.setTouchable(Touchable.disabled);
	}

	public void sendInSocialButtons() {
		//
		float duration = 0.6f;
		//
		mainMenuScreen.btnSocialFacebook.addAction(Actions.moveTo(mainMenuScreen.btnSocialFacebook.getX(),AppSettings.SCREEN_H- (mainMenuScreen.btnSocialFacebook.getHeight() / 1.25f), duration));
		mainMenuScreen.btnSocialTwitter.addAction(Actions.moveTo(mainMenuScreen.btnSocialTwitter.getX(),AppSettings.SCREEN_H- (mainMenuScreen.btnSocialTwitter.getHeight() / 1.25f), duration + 0.2f));
		mainMenuScreen.btnSocialGoogle.addAction(Actions.moveTo(mainMenuScreen.btnSocialGoogle.getX(),AppSettings.SCREEN_H- (mainMenuScreen.btnSocialGoogle.getHeight() / 1.25f), duration + 0.4f));

		mainMenuScreen.btnSocialFacebook.setTouchable(Touchable.enabled);
		mainMenuScreen.btnSocialGoogle.setTouchable(Touchable.enabled);
		mainMenuScreen.btnSocialTwitter.setTouchable(Touchable.enabled);

	}

	public void sendAwaySocialButtons() {

		float duration = 0.6f;

		mainMenuScreen.btnSocialFacebook.addAction(Actions.moveTo(mainMenuScreen.btnSocialFacebook.getX(),AppSettings.SCREEN_H, duration));
		mainMenuScreen.btnSocialTwitter.addAction(Actions.moveTo(mainMenuScreen.btnSocialTwitter.getX(),AppSettings.SCREEN_H, duration + 0.2f));
		mainMenuScreen.btnSocialGoogle.addAction(Actions.moveTo(mainMenuScreen.btnSocialGoogle.getX(),AppSettings.SCREEN_H, duration + 0.4f));

		mainMenuScreen.btnSocialFacebook.setTouchable(Touchable.disabled);
		mainMenuScreen.btnSocialGoogle.setTouchable(Touchable.disabled);
		mainMenuScreen.btnSocialTwitter.setTouchable(Touchable.disabled);
	}

	public void setUpSwipeButtons()
	{
		Random rnd = new Random();
		//float btnWidth = 220f / 1.2f;
		//float btnHeight = 140f / 1.2f;
		float btnWidth = 240f / 1.2f;
		float btnHeight = 152.73f / 1.2f;

		//
		// Btn Start
		// #################################################################
		mainMenuScreen.btnSwipeForMenu = new JungleGameButton(btnWidth,btnHeight, rnd, true);
		mainMenuScreen.btnSwipeForMenu.setTextureRegion(MenuScreen.img_obj_swipe_down_menu, true);
		mainMenuScreen.btnSwipeForMenu.setOrigin(mainMenuScreen.btnSwipeForMenu.getWidth(), 0);
		mainMenuScreen.btnSwipeForMenu.setPosition(AppSettings.SCREEN_W - mainMenuScreen.btnSwipeForMenu.getWidth(), 0);

		//
		// Btn Start
		// #################################################################
		mainMenuScreen.btnSwipeForInstructions = new JungleGameButton(btnWidth, btnHeight, rnd, true);
		mainMenuScreen.btnSwipeForInstructions.setTextureRegion(MenuScreen.img_obj_swipe_up_instructions, true);
		mainMenuScreen.btnSwipeForInstructions.setOrigin(mainMenuScreen.btnSwipeForInstructions.getWidth(), 0);
		mainMenuScreen.btnSwipeForInstructions.setPosition(AppSettings.SCREEN_W- mainMenuScreen.btnSwipeForInstructions.getWidth(), 0);

		//
		// Add
		// #################################################################
		mainMenuScreen.btnSwipeForMenu.setScale(0f);
		mainMenuScreen.btnSwipeForInstructions.setScale(0f);

		//
		// Add
		// #################################################################
		mainMenuScreen.getStage().addActor(mainMenuScreen.btnSwipeForMenu);
		mainMenuScreen.getStage().addActor(mainMenuScreen.btnSwipeForInstructions);
	}

	public void sendInSwipeForMenu()
	{
		EffectCreator.create_SC(mainMenuScreen.btnSwipeForMenu, 1f, 1f,0.5f, null, false);
	}

	public void sendAwaySwipeForMenu()
	{
		EffectCreator.create_SC(mainMenuScreen.btnSwipeForMenu, 0f, 0f,0.5f, null, false);
	}

	public void sendInSwipeForInstruction()
	{
		EffectCreator.create_SC(mainMenuScreen.btnSwipeForInstructions,1f, 1f, 0.5f, null, false);
	}

	public void sendAwaySwipeForInstructions()
	{
		EffectCreator.create_SC(mainMenuScreen.btnSwipeForInstructions,0f, 0f, 0.5f, null, false);
	}
}
