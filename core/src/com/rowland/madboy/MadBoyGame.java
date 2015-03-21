package com.rowland.madboy;

//import com.badlogic.gdx.Game;
import com.moribitotech.mtx.game.AbstractGame;
import com.moribitotech.mtx.managers.SettingsManager;
//import com.moribitotech.mtx.SettingsManager;
import com.moribitotech.mtx.settings.AppSettings;
import com.moribitotech.mtx.settings.MtxLogger;
import com.rowland.GameData.GameData;
import com.rowland.Helpers.AssetCentral;
import com.rowland.Screens.LoadingScreen;

public class MadBoyGame extends AbstractGame {

	private AssetCentral manager;

	public MadBoyGame()
	{
		//THIS IS NOT IMPLEMENTED TO BE CALLED BY DESKTOP LAUNCHER
	}

	@Override
	public void create()
	{
		super.create();
	}

	public AssetCentral getManager()
	{
        return manager;
    }

	@Override
	public void resume()
	{
       super.resume();
	}
	@Override
	public void pause()
	{
		super.pause();
		getScreen().pause();
	}
	@Override
	public void dispose()
	{
		super.dispose();

		manager.dispose();
	}

	@Override
    public void setUpAppSettings()
	{
        AppSettings.setUp();

		// If the Application is launched for the first time, create preferences
		// to store game data such as Array of top 5 Highscores, timer mode and
		// sound settings
		if (SettingsManager.isFirstLaunchDone())
		{
			SettingsManager.setFirstLaunchDone(true);
			MtxLogger.log(true, true, "LAUNCH", "THIS IS FIRST LAUNCH");
			GameData.createPrefs();
			GameData.saveLevelInfo();
			GameData.saveStarsEarnedInfo();
		}
		else
		{
			MtxLogger.log(true, true, "LAUNCH", "THIS IS NOT FIRST LAUNCH");
			if (GameData.prefs == null)
				GameData.createPrefs();

			GameData.addToUnLockedLevel(1);
		}
	}

    @Override
    public void setUpAssets()
    {
		// Load assets before setting the screen
		// Assets file path should be parametrized within a config file
		manager = new AssetCentral("data/json/assets.json");
    }

    @Override
    public void setUpLoadingScreen()
    {
		setScreen(new LoadingScreen(this, "Loading Screen",LoadingScreen.TYPE_UI_MENU));
    }
}
