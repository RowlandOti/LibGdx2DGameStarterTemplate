package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.magnetideas.helpers.MyAbstractScreen;
import com.rowland.madboy.MadBoyGame;

/**
 * @author Rowland
 *
 */
public class LoadingScreen extends MyAbstractScreen {
	
	private static final String TAG = "ASSETLOAD";
    public static final int TYPE_UI_MENU = 0;
    public static final int TYPE_UI_LEVEL = 1;
    public static final int TYPE_UI_HIGHSCORE = 2;
    public static final int TYPE_UI_CREDIT = 3;
    public static final int TYPE_UI_TUTORIAL = 4;
    public static final int TYPE_GAME = 5;
    
    public static final  String BASE_ATLAS ="data/base/base_atlas.pack";
    public static final  String UI_MENU_ATLAS ="data/ui_menu/menu_atlas.pack";
    public static final  String UI_LEVEL_ATLAS ="data/ui_level/level_atlas.pack";
    public static final  String UI_HIGHSCORE_ATLAS ="data/ui_highscore/highscore_atlas.pack";
    public static final  String UI_CREDIT_ATLAS ="data/ui_credit/credit_atlas.pack";
    public static final  String UI_TUTORIAL_ATLAS ="data/ui_tutorial/tutorial_atlas.pack";
    public static final  String GAME_ATLAS ="data/game_screen/game_atlas.pack";
 
    private int type;
    private Texture background_loading, logo, progressBarImg, progressBarBaseImg;
    private SpriteBatch batch;
	private boolean loaded = false;
	private Vector2 logoPos, pbPos;
 
    public LoadingScreen(Game game, String screenName, int type) 
    {
        super(game, screenName);
        this.type = type;
    }
 
    @Override
    public void show() 
    {
        batch = new SpriteBatch();
 
    	// Load assets needed for loading screen
        getMyGame().getManager().loadGroup("loading_screen");
        getMyGame().getManager().loadGroup("base");
        getMyGame().getManager().finishLoading(); //Blocks until all resources are loaded into memory
        
        Gdx.app.log(TAG, "Assets loaded"); 

        // Get Assets
        background_loading = getMyGame().getManager().get("data/loading_screen/background_loading.png");
        logo = getMyGame().getManager().get("data/loading_screen/logo.png");
        progressBarImg = getMyGame().getManager().get("data/loading_screen/progress_bar.png");
        progressBarBaseImg = getMyGame().getManager().get("data/loading_screen/progress_bar_base.png");
        
    	// Get logo position
        logoPos = new Vector2();
        // >> bitwise operator bill just divide by 2, the explicitly written times, in this case 1
        logoPos.set((Gdx.graphics.getWidth()-logo.getWidth())>>1, Gdx.graphics.getHeight()>>1);

        // ProgressBar position
        pbPos = new Vector2();
        pbPos.set(logoPos.x, logoPos.y - (logo.getHeight()));
        
        //Depending on screen type load appropriate assets
        switch (type) 
        {
        case TYPE_UI_MENU:
        	if(getMyGame().getManager().isLoaded(GAME_ATLAS, TextureAtlas.class)) 
        	{
        	   getMyGame().getManager().unloadGroup("game_screen");
        	}
        	getMyGame().getManager().loadGroup("ui_menu");
            break;
        case TYPE_UI_LEVEL:
        	getMyGame().getManager().unloadGroup("ui_menu");
        	getMyGame().getManager().loadGroup("ui_level");
            break;
        case TYPE_UI_HIGHSCORE:
        	getMyGame().getManager().unloadGroup("ui_level");
        	getMyGame().getManager().loadGroup("ui_highscore");
            break;
        case TYPE_UI_CREDIT:
        	getMyGame().getManager().unloadGroup("ui_highscore");
        	getMyGame().getManager().loadGroup("ui_credit");
            break;
        case TYPE_UI_TUTORIAL:
        	getMyGame().getManager().unloadGroup("ui_credit");
        	getMyGame().getManager().loadGroup("ui_tutorial");
            break;
        case TYPE_GAME:
        	getMyGame().getManager().unloadGroup("ui_tutorial");
        	//myGame.getManager().unloadGroup("base");
        	getMyGame().getManager().loadGroup("game_screen");
            break;
        }
    }
 
    @Override
    public void render(float delta) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Render background image
        batch.begin();
        batch.draw(background_loading, 0, 0);
        batch.end();
        
		// Check if async load is done
		if (!loaded) 
		{
			// Render Logo and Loading Bar
			batch.begin();
			batch.draw(logo, logoPos.x, logoPos.y);
			batch.draw(progressBarBaseImg, pbPos.x, pbPos.y);
			batch.draw(progressBarImg, pbPos.x, pbPos.y,progressBarImg.getWidth() * getMyGame().getManager().getProgress(),progressBarImg.getHeight());
			batch.end();

			if (getMyGame().getManager().update()) 
			{
				loaded = true;
				
				switch (type) 
				{
				case TYPE_UI_MENU:
					((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(getMyGame(), "MainMenu Screen"));
					break;
				case TYPE_UI_LEVEL:
					((Game) Gdx.app.getApplicationListener()).setScreen(new LevelSelectScreen(getMyGame(),"LevelSelect Screen"));
					break;
				case TYPE_UI_HIGHSCORE:
					((Game) Gdx.app.getApplicationListener()).setScreen(new HighScoresScreen(getMyGame(),"Highscore Screen"));
					break;
				case TYPE_UI_CREDIT:
					((Game) Gdx.app.getApplicationListener()).setScreen(new CreditsScreen(getMyGame(),"Credit Screen"));
					break;
				case TYPE_UI_TUTORIAL:
					((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(getMyGame(), "Tutorial Screen"));
					break;
				case TYPE_GAME:
					((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(getMyGame(), "Game Screen"));
					break;
				}
			}
		}
		else 
		{
			
	    }
    	
    }
 
    @Override
    public void resize(int width, int height) 
    {
    
    }
 
	@Override
	public void hide() 
	{
		getMyGame().getManager().unloadGroup("loading_screen");
	}
 
    @Override
    public void pause() 
    {
    	
    }
 
    @Override
    public void resume() 
    {
    	
    }
 
    @Override
    public void dispose() 
    {
    	
    }
    }
 