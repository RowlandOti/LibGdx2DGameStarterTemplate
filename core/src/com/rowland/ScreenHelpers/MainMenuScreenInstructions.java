package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.MenuScreen;

public class MainMenuScreenInstructions {

	private MenuScreen mainMenuScreen;

	public MainMenuScreenInstructions(final MenuScreen mainMenuScreen)
	{
		this.mainMenuScreen = mainMenuScreen;

	}

	public void setUpInstructions()
	{
		mainMenuScreen.instructions = new EmptyActorLight(AppSettings.SCREEN_W, AppSettings.SCREEN_H, false);
		mainMenuScreen.instructions.setTextureRegion(MenuScreen.img_obj_rectangle, true);
		mainMenuScreen.instructions.setPosition(0 - AppSettings.SCREEN_W,0);

		//
		mainMenuScreen.getStage().addActor(mainMenuScreen.instructions);
	}

	//
	public void sendInInstructions()
	{
		float widthAsX = mainMenuScreen.btnSwipeForMenu.getWidth();
		mainMenuScreen.instructions.addAction(Actions.moveTo(0 - widthAsX, 0, 0.5f));
	}

	public void sendAwayInstructions()
	{
		mainMenuScreen.instructions.addAction(Actions.moveTo(0 - AppSettings.SCREEN_W, 0, 0.5f));
	}
}
