package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.scene2d.models.SmartActor;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Screens.MenuScreen;

import java.util.ArrayList;
import java.util.Random;

public class MainMenuScreenEnvironment {

	private MenuScreen mainMenuScreen;

	public MainMenuScreenEnvironment(final MenuScreen mainMenuScreen)
	{
		this.mainMenuScreen = mainMenuScreen;

	}

	public void setUpGameName()
	{
		//mainMenuScreen.gameName = new EmptyActorLight(420, 280,true);
		mainMenuScreen.gameName = new EmptyActorLight(350, 234,true);
		mainMenuScreen.gameName.setTextureRegion(MenuScreen.img_obj_text_junglegamemenu, true);
		mainMenuScreen.gameName.setOrigin(mainMenuScreen.gameName.getWidth() / 2,mainMenuScreen.gameName.getHeight() / 2);
		mainMenuScreen.gameName.setPosition(AppSettings.SCREEN_W / 2- mainMenuScreen.gameName.getWidth() / 2,AppSettings.SCREEN_H+ mainMenuScreen.gameName.getHeight());

		mainMenuScreen.getStage().addActor(mainMenuScreen.gameName);
	}

	public void setUpMounatins()
	{
		mainMenuScreen.mountains = new EmptyActorLight(AppSettings.SCREEN_W, 250 * AppSettings.getWorldSizeRatio(),false);
		mainMenuScreen.mountains.setTextureRegion(MenuScreen.img_obj_mountains, true);
		mainMenuScreen.mountains.setPosition(0, 0);

		mainMenuScreen.getStage().addActor(mainMenuScreen.mountains);
	}

	public void sendInGameName()
	{
		mainMenuScreen.gameName.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - mainMenuScreen.gameName.getWidth() / 2, AppSettings.SCREEN_H- mainMenuScreen.gameName.getHeight(), 0.5f));

	}

	public void sendAwayGameName()
	{
		mainMenuScreen.gameName.addAction(Actions.moveTo(AppSettings.SCREEN_W / 2 - mainMenuScreen.gameName.getWidth() / 2, AppSettings.SCREEN_H+ mainMenuScreen.gameName.getHeight(), 0.5f));
	}

	public void setUpBackgroundBalloons()
	{
		mainMenuScreen.backgroundBalloons = new ArrayList<SmartActor>();

		Random rnd = new Random();

		for (int i = 0; i < 30; i++)
		{
			float sizeWH = rnd.nextInt(60) + 40;
            SmartActor currentBalloon = new SmartActor(sizeWH, sizeWH, rnd,true);

			currentBalloon.setTextureRegion(MenuScreen.img_obj_circle, true);
			currentBalloon.setOrigin(currentBalloon.getWidth() / 2,currentBalloon.getHeight() / 2);
			currentBalloon.startActionMoveFreely(15,(int) AppSettings.SCREEN_W, (int) AppSettings.SCREEN_H, 6);
			currentBalloon.startActionScale(10, 2, 2, 3, true);
			currentBalloon.startActionFadeInOut(10, 3, true);

			mainMenuScreen.backgroundBalloons.add(currentBalloon);
			mainMenuScreen.getStage().addActor(currentBalloon);
		}
	}

	public void setUpFlowers() {
		mainMenuScreen.flowers = new ArrayList<SmartActor>();

		Random rnd = new Random();

		int size = 15;
		for (int i = 0; i < size; i++)
		{
			float sizeRatio = rnd.nextInt(90) + 10;
			float wid = 140 * sizeRatio / 100;
			float hei = 180 * sizeRatio / 100;
			// Position X nicely along the screen (Natural looking)
			float posX = i * (AppSettings.SCREEN_W / size);
			//
            SmartActor currentFlower = new SmartActor(wid, hei, rnd, true);
			// Increase varitaion with 2 different flowers as textures
			if (i % 2 == 0)
			{
				currentFlower.setTextureRegion(MenuScreen.img_obj_flower_1_, true);
			}
			else
			{
				currentFlower.setTextureRegion(MenuScreen.img_obj_flower_2_, true);
			}
			currentFlower.setOrigin(currentFlower.getWidth() / 2.0f,currentFlower.getOriginY());
			currentFlower.startActionScale(10, 1.3f, 1.3f, 3, true);
			currentFlower.setPosition(posX, 0);

			mainMenuScreen.flowers.add(currentFlower);
			mainMenuScreen.getStage().addActor(currentFlower);
		}
	}
}
