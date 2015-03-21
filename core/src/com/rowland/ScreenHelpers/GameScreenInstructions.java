package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.PreGameScreen;

/**
 * @author Rowland
 *
 */
public class GameScreenInstructions {

	private PreGameScreen preGameScreen;

	public GameScreenInstructions(PreGameScreen preGameScreen)
	{
      this.preGameScreen = preGameScreen;
	}

	public void setUpInstructions()
	{
		preGameScreen.instructions = new EmptyActorLight(AppSettings.SCREEN_W, AppSettings.SCREEN_H, false);
		preGameScreen.instructions.setTextureRegion(PreGameScreen.img_obj_rectangle, true);
		preGameScreen.instructions.setPosition(0 - AppSettings.SCREEN_W,0);

		preGameScreen.getStage().addActor(preGameScreen.instructions);
	}

	//
	public void sendInInstructions()
	{
		float widthAsX = preGameScreen.btnSwipeForMenu.getWidth();
		preGameScreen.instructions.addAction(Actions.moveTo(0 - widthAsX, 0, 0.5f));
	}

	public void sendAwayInstructions()
	{
		preGameScreen.instructions.addAction(Actions.moveTo(0 - AppSettings.SCREEN_W, 0, 0.5f));
	}

	public void sendInMenu()
	{
		float widthAsX = preGameScreen.btnSwipeForMenu.getWidth();
		preGameScreen.instructions.addAction(Actions.moveTo(0 - widthAsX, 0, 0.5f));

	}


}
