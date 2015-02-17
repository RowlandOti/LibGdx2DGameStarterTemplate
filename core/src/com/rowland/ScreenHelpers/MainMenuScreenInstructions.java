package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.MenuScreen;

public class MainMenuScreenInstructions {

	public void setUpInstructions(final MenuScreen jungleMainMenuScreen)
	{
		jungleMainMenuScreen.instructions = new EmptyActorLight(AppSettings.SCREEN_W, AppSettings.SCREEN_H, false);
		jungleMainMenuScreen.instructions.setTextureRegion(jungleMainMenuScreen.img_obj_rectangle, true);
		jungleMainMenuScreen.instructions.setPosition(0 - AppSettings.SCREEN_W,0);

		//
		jungleMainMenuScreen.getStage().addActor(jungleMainMenuScreen.instructions);
	}

	//
	public void sendInInstructions(final MenuScreen jungleMainMenuScreen)
	{
		float widthAsX = jungleMainMenuScreen.btnSwipeForMenu.getWidth();
		jungleMainMenuScreen.instructions.addAction(Actions.moveTo(0 - widthAsX, 0, 0.5f));
	}

	public void sendAwayInstructions(final MenuScreen jungleMainMenuScreen)
	{
		jungleMainMenuScreen.instructions.addAction(Actions.moveTo(0 - AppSettings.SCREEN_W, 0, 0.5f));
	}
}
