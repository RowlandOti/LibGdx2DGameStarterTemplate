package com.rowland.ScreenHelpers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.scene2d.ui.ButtonLevel;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LevelSelectScreen;
import com.rowland.Screens.LoadingScreen;
import com.rowland.UI.MyMenuCreator;
import com.rowland.UI.PagedScrollPane;

/**
 * @author Rowland
 *
 */
public class LevelScreenButtons {

	private int selectedLevel;
	private float pad = 50*AppSettings.getWorldSizeRatio();
	private float button_size = 120f*AppSettings.getWorldSizeRatio();
	private LevelSelectScreen levelSelectScreen;

	public LevelScreenButtons(final LevelSelectScreen levelSelectScreen)
	{
		this.levelSelectScreen = levelSelectScreen;
	}

	public void setUpLevelScreenButtons()
	{
		levelSelectScreen.container = (TableModel) MyMenuCreator.createTable(null);
		levelSelectScreen.container.setSize(AppSettings.SCREEN_W/1.0f, AppSettings.SCREEN_H);
		levelSelectScreen.container.setPosition(-999, AppSettings.SCREEN_H - levelSelectScreen.container.getHeight());

		levelSelectScreen.container.addAction(Actions.moveTo(0, AppSettings.SCREEN_H - levelSelectScreen.container.getHeight(), 0.7f));
		levelSelectScreen.container.setFillParent(true);
		levelSelectScreen.getStage().addActor(levelSelectScreen.container);


		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25f);
		scroll.setScrollingDisabled(false, true);

		int c = 1;
		int numberOfLevels = GameData.NUMBER_OF_LEVELS;

		for (int l = 0; l < 2; l++)
		{

		    Table levels = MyMenuCreator.createTable(null);
		    levels.pad(pad, pad, pad, pad);
		    levels.setSize(AppSettings.SCREEN_W, AppSettings.SCREEN_H);

		    for (int y = 0; y < 2; y++)
		    {
		        levels.row();

		        for (int x = 0; x < 4; x++)
		        {
		        	    c= c++;

		        	    if(numberOfLevels + 1 != c)
		        	    {

		        		levels.add(getLevelButton(c++)).size(button_size,button_size).pad(pad);

		        	    }
		        	    else
		        	    {
		        	    	break;
		        	    }

		        }
		    }

		    scroll.addPage(levels);

		}
		levelSelectScreen.container.add(scroll).expand().fill();

	}

	/**
     * Creates a button to represent the level
     *
     * @param level
     * @return The button to use for the level
     */
    public ButtonLevel getLevelButton(int level)
    {
		//3. Set lock condition (get from database if it is locked or not and lock it)
		// use if/else here to lock or not

		if(!GameData.prefs.getBoolean("level"+level))
		{
			//1. Create level button
		final ButtonLevel   levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, LevelSelectScreen.button_level_grey, LevelSelectScreen.button_level_grey);

		//final int selectedLevel =i;
		//2. Set level number
		    levelButton.setLockActive(true);
		    levelButton.setScale(AppSettings.getWorldSizeRatio(), AppSettings.getWorldSizeRatio());
			//levelButton.setLevelNumber(level , AssetLoader.whiteFont);
			levelButton.setTextureExternal(LevelSelectScreen.lock_closed, true);
			levelButton.setTextureExternalPosXY((17.5f*AppSettings.getWorldPositionXRatio()), (15.0f*AppSettings.getWorldPositionYRatio()));
			levelButton.setTextureExternalSize(80*AppSettings.getWorldSizeRatio(), 80*AppSettings.getWorldSizeRatio());

			addListener(levelButton);

			return levelButton;
		}


		if(GameData.prefs.getBoolean("level"+level))
		{

			//1. Create level button
			final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, LevelSelectScreen.button_level_green, LevelSelectScreen.button_level_green);

			//final int selectedLevel =i;
			//2. Set level number
			levelButton.setLevelNumber(level , AssetLoader.whiteFont);
			levelButton.setLevelStars(LevelSelectScreen.star_empty, LevelSelectScreen.star_normal, 3, GameData.getStarsEarned()[level]);
			levelButton.setLevelStarSizeRatio(5*AppSettings.getWorldSizeRatio());
			levelButton.setLevelStarPosYStart(-40*AppSettings.getWorldSizeRatio());

			addListener(levelButton);

			return levelButton;
		}
		return null;

		//4. Set stars or any other achievements (get from database or text files here)
		// I just made a random number of earned stars

	}

   //My level select listener
	private void addListener(final ButtonLevel levelButton)
	{
		//5. Add  listener
		//Add button listener to go to a level (gamescreen)

		levelButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				super.touchUp(event, x, y, pointer, button);
				selectedLevel = levelButton.getLevelNumber();

				if(GameData.getLevelInfo()[selectedLevel])
				{
					GameScreen.currentlevel = selectedLevel;
					levelSelectScreen.getMyGame().setScreen(new LoadingScreen(levelSelectScreen.getMyGame(), "Instruction Screen", LoadingScreen.TYPE_UI_INSTRUCTION));
				}
				else
				{

				}

				//infoButton.setText(message, true);
			}
		});
	}


}
