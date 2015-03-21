package com.rowland.ScreenHelpers;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.Screens.LoadingScreen;
import com.rowland.Screens.PreGameScreen;
import com.rowland.UI.MadBoyGameButton;

/**
 * @author Rowland
 *
 */
public class PreGameScreenButtons {

	private PreGameScreen preGameScreen;

	public PreGameScreenButtons(final PreGameScreen preGameScreen)
	{
		this.preGameScreen = preGameScreen;
	}

	public void setUpMenuButtons()
	{

		// Menu Table (Play, Scores, Settings)
		// #################################################################
		preGameScreen.menuTable = new TableModel(null, AppSettings.SCREEN_W/1.3f , AppSettings.WORLD_HEIGHT/1.3f);
		preGameScreen.menuTable.setPosition(100*AppSettings.getWorldPositionXRatio(), -AppSettings.SCREEN_H);

		//
		// Btn Values
		// #################################################################
		Random rnd = new Random();
		float btnWidth = 270f;
		float btnHeight = 90f;

		//
		// Btn Okay
		// #################################################################
		preGameScreen.btnOk = new MadBoyGameButton(btnWidth,btnHeight, rnd, true);
		preGameScreen.btnOk.setTextureRegion(PreGameScreen.button_overlay_right,true);
		preGameScreen.btnOk.setOrigin(preGameScreen.btnOk.getWidth() / 2.0f,preGameScreen.btnOk.getHeight() / 2.0f);
		preGameScreen.btnOk.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y,int pointer, int button)
			{
				super.touchDown(event, x, y, pointer, button);

				preGameScreen.btnOk.clearActions();
				EffectCreator.create_SC_SHK_BTN(preGameScreen.btnOk, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);

				preGameScreen.getMyGame().setScreen(new LoadingScreen(preGameScreen.getMyGame(), "Game Screen", LoadingScreen.TYPE_GAME));
			}

		});

		//
		// Btn Cancel
		// #################################################################
		preGameScreen.btnCancel = new MadBoyGameButton(btnWidth,btnHeight, rnd, true);
		preGameScreen.btnCancel.setTextureRegion(PreGameScreen.button_overlay_left,true);
		preGameScreen.btnCancel.setOrigin(preGameScreen.btnCancel.getWidth() / 2.0f,preGameScreen.btnCancel.getHeight() / 2.0f);
		preGameScreen.btnCancel.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y,int pointer, int button)
			{
				super.touchDown(event, x, y, pointer, button);

				preGameScreen.btnCancel.clearActions();
				EffectCreator.create_SC_SHK_BTN(preGameScreen.btnCancel, 1.3f, 1.3f, 5f, 0, 0.05f, null, false);

				preGameScreen.getGame().setScreen(new LevelSelectScreen(preGameScreen.getGame(), "LevelSelect Screen"));
			}

		});

		//
		// Scale them to "0", we will send them in after splash completed
		// #################################################################
		preGameScreen.btnOk.setScale(0f);
		preGameScreen.btnCancel.setScale(0f);


		float dipRatioWidth = 80 * AppSettings.getWorldSizeRatio();
		float dipRatioHeight = 80 * AppSettings.getWorldSizeRatio();
		float pad = 150f * AppSettings.getWorldSizeRatio();

		//
		// Add
		// #################################################################
		preGameScreen.menuTable.add(preGameScreen.btnCancel).align(Align.left).size(dipRatioWidth, dipRatioHeight).pad(pad);
		preGameScreen.menuTable.add(preGameScreen.btnOk).align(Align.right).size(dipRatioWidth, dipRatioHeight).pad(pad);

		// Add to stage
		preGameScreen.getStage().addActor(preGameScreen.menuTable);

	}

	public void setUpSwipeButtons()
	{
		Random rnd = new Random();

		float btnWidth = (240f / 1.2f);
		float btnHeight = (152.73f / 1.2f);

		// Btn Swipe Down Menu
		// #################################################################
		preGameScreen.btnSwipeForMenu = new MadBoyGameButton(btnWidth,btnHeight, rnd, true);
		preGameScreen.btnSwipeForMenu.setTextureRegion(PreGameScreen.img_obj_swipe_down_menu, true);
		preGameScreen.btnSwipeForMenu.setOrigin(preGameScreen.btnSwipeForMenu.getWidth(), 0);
		preGameScreen.btnSwipeForMenu.setPosition(AppSettings.SCREEN_W - preGameScreen.btnSwipeForMenu.getWidth(), 0);

		// Btn Swipe Up menu
		// #################################################################
		preGameScreen.btnSwipeForInstructions = new MadBoyGameButton(btnWidth, btnHeight, rnd, true);
		preGameScreen.btnSwipeForInstructions.setTextureRegion(PreGameScreen.img_obj_swipe_up_instructions, true);
		preGameScreen.btnSwipeForInstructions.setOrigin(preGameScreen.btnSwipeForInstructions.getWidth(), 0);
		preGameScreen.btnSwipeForInstructions.setPosition(AppSettings.SCREEN_W- preGameScreen.btnSwipeForInstructions.getWidth(), 0);

		// Add
		// #################################################################
		preGameScreen.btnSwipeForMenu.setScale(0f);
		preGameScreen.btnSwipeForInstructions.setScale(0f);

		// Add
		// #################################################################
		preGameScreen.getStage().addActor(preGameScreen.btnSwipeForMenu);
		preGameScreen.getStage().addActor(preGameScreen.btnSwipeForInstructions);
	}
    //Our swipable Instructions
	public void sendInSwipeForMenu()
	{
		EffectCreator.create_SC(preGameScreen.btnSwipeForMenu, 1f, 1f,0.5f, null, false);
	}

	public void sendAwaySwipeForMenu()
	{
		EffectCreator.create_SC(preGameScreen.btnSwipeForMenu, 0f, 0f,0.5f, null, false);
	}

	public void sendInSwipeForInstruction()
	{
		EffectCreator.create_SC(preGameScreen.btnSwipeForInstructions,1f, 1f, 0.5f, null, false);
	}

	public void sendAwaySwipeForInstructions()
	{
		EffectCreator.create_SC(preGameScreen.btnSwipeForInstructions,0f, 0f, 0.5f, null, false);
	}

	//Our menu buttons
	public void sendInMenuButtons()
	{
		preGameScreen.getStage().addActor(preGameScreen.menuTable);

		EffectCreator.create_SC_BTO(preGameScreen.btnOk, 1.3f, 1.3f,0.4f, null, false);
		EffectCreator.create_SC_BTO(preGameScreen.btnCancel, 1.3f, 1.3f,0.7f, null, false);

		preGameScreen.menuTable.addAction(Actions.moveTo(100*AppSettings.getWorldPositionXRatio(), 60*AppSettings.getWorldPositionYRatio(), 0.5f));


		preGameScreen.btnOk.setTouchable(Touchable.enabled);
		preGameScreen.btnCancel.setTouchable(Touchable.enabled);

	}

	public void sendAwayMenuButtons()
	{
		EffectCreator.create_SC(preGameScreen.btnOk, 0f, 0f, 0.4f,null, false);
		EffectCreator.create_SC(preGameScreen.btnCancel, 0f, 0f, 0.7f,null, false);

		preGameScreen.menuTable.addAction(Actions.moveTo(100*AppSettings.getWorldPositionXRatio(),- preGameScreen.menuTable.getHeight(), 0.5f));

		preGameScreen.btnOk.setTouchable(Touchable.disabled);
		preGameScreen.btnCancel.setTouchable(Touchable.disabled);
	}

}
