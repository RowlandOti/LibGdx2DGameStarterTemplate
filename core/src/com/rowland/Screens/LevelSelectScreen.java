package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.mtx.scene2d.ui.ButtonLevel;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetLoader;
import com.rowland.UI.MyMenuCreator;
import com.rowland.UI.PagedScrollPane;

public class LevelSelectScreen extends MyAbstractScreen {

	Table container;

	private int selectedLevel;
	private float pad = 20*AppSettings.getWorldSizeRatio();
	private float button_size = 90*AppSettings.getWorldSizeRatio();

	private TextureRegion background_home, background_menu_berge, star_empty, star_normal, lock_closed,button_level_grey, button_level_green;

	public LevelSelectScreen(Game game, String screenName)
	{
		super(game, screenName);

		initScreenAssets();
		setUpScreenElements();
		setUpLevelsScreen();
	}

	private void initScreenAssets()
	{
		//Getting previously loaded atlas
        atlas = getMyGame().getManager().get(LoadingScreen.UI_LEVEL_ATLAS, TextureAtlas.class);
        atlas_base = getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

		//background_home = atlas_base.findRegion("background_home");
		background_menu_berge = atlas_base.findRegion("background_menu_berge");
		star_empty = atlas.findRegion("star_empty");
		star_normal = atlas.findRegion("star_normal");
		lock_closed = atlas.findRegion("lock_closed");
		button_level_grey = atlas.findRegion("button_level_grey");
		button_level_green = atlas.findRegion("button_level_green");
	}

	public void setUpScreenElements()
	{
		//setBackgroundTexture(background_home);
		setBackButtonActive(true);
	}

	private void setUpLevelsScreen()
	{
		NinePatch patch = new NinePatch(background_menu_berge);
		NinePatchDrawable patchDraw = new NinePatchDrawable(patch);

		container = MyMenuCreator.createTable(null);
		container.setSize(AppSettings.SCREEN_W/1.5f, AppSettings.SCREEN_H);
		container.setPosition(-999, AppSettings.SCREEN_H - container.getHeight());
		container.setBackground(patchDraw);
		container.addAction(Actions.moveTo(0, AppSettings.SCREEN_H - container.getHeight(), 0.7f));
		container.setFillParent(true);
		getStage().addActor(container);


		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		scroll.setScrollingDisabled(false, true);

		int c = 1;
		int numberOfLevels = GameData.NUMBER_OF_LEVELS;

		for (int l = 0; l < 2; l++)
		{

		    Table levels = MyMenuCreator.createTable(null);
		    levels.pad(pad, pad, pad, pad);
		    levels.setSize(AppSettings.SCREEN_W, AppSettings.SCREEN_H);

		    for (int y = 0; y < 3; y++)
		    {
		        levels.row();

		        for (int x = 0; x < 4; x++)
		        {
		        	    c= c++;

		        	    if(numberOfLevels + 1 != c)
		        	    {

		        		levels.add(getLevelButton(c++)).size(button_size,button_size).pad(pad, pad, pad, pad);

		        	    }
		        	    else
		        	    {
		        	    	break;
		        	    }

		        }
		    }

		    scroll.addPage(levels);

		}
		container.add(scroll).expand().fill();
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
		final ButtonLevel   levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, button_level_grey, button_level_grey);

		//final int selectedLevel =i;
		//2. Set level number
		    levelButton.setLockActive(true);
		    levelButton.setScale(AppSettings.getWorldSizeRatio(), AppSettings.getWorldSizeRatio());
			//levelButton.setLevelNumber(level , AssetLoader.whiteFont);
			levelButton.setTextureExternal(lock_closed, true);
			levelButton.setTextureExternalPosXY((35*AppSettings.getWorldPositionXRatio())/2, (30*AppSettings.getWorldPositionYRatio())/2);
			levelButton.setTextureExternalSize(60*AppSettings.getWorldSizeRatio(), 60*AppSettings.getWorldSizeRatio());

			addListener(levelButton);

			return levelButton;
		}


		if(GameData.prefs.getBoolean("level"+level))
		{

			//1. Create level button
			final ButtonLevel levelButton = MenuCreator.createCustomLevelButton(AssetLoader.whiteFont, button_level_green, button_level_green);

		//final int selectedLevel =i;
		//2. Set level number

			levelButton.setLevelNumber(level , AssetLoader.whiteFont);
			levelButton.setLevelStars(star_empty, star_normal, 3, GameData.getStarsEarned()[level]);
			levelButton.setLevelStarSizeRatio(4*AppSettings.getWorldSizeRatio());
			levelButton.setLevelStarPosYStart(-40*AppSettings.getWorldPositionXRatio());

			addListener(levelButton);

			return levelButton;
		}
		return null;

		//4. Set stars or any other achievements (get from database or text files here)
		// I just made a random number of earned stars

	}


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
							getMyGame().setScreen(new LoadingScreen(getMyGame(), "Loading Screen", LoadingScreen.TYPE_GAME));
						}
						else
						{

						}

						//infoButton.setText(message, true);
					}
				});
	}

	@Override
	public void keyBackPressed()
	{
		super.keyBackPressed();
		getGame().setScreen(new MenuScreen(getGame(), "MenuScreen"));
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

	}
}
