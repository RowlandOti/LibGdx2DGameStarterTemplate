package com.rowland.Screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.magnetideas.helpers.MyAbstractScreen;
import com.magnetideas.helpers.ScreenShotSaver;
import com.magnetideas.parallax.ParallaxBackground;
import com.magnetideas.parallax.TextureRegionParallaxLayer;
import com.magnetideas.parallax.Utils.WH;
import com.magnetideas.smoothcam.SmoothCamAccessor;
import com.magnetideas.smoothcam.SmoothCamWorld;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.GameData;
import com.rowland.GameObjects.Yoyo;
import com.rowland.GameWorld.GameRenderer;
import com.rowland.GameWorld.GameWorld;
import com.rowland.GameWorld.GameWorld.WorldListener;
import com.rowland.Helpers.AssetLoader;
import com.rowland.Helpers.InputHandler;
import com.rowland.Helpers.MyOrthographicCamera;
import com.rowland.ScreenHelpers.GameScreenGameOverMenu;
import com.rowland.ScreenHelpers.GameScreenGamePauseMenu;
import com.rowland.ScreenHelpers.GameScreenGameReadyMenu;
import com.rowland.ScreenHelpers.GameScreenLevelEndMenu;
import com.rowland.TweenAccessors.OrthographicCameraAccessor;

public class GameScreen extends MyAbstractScreen implements IScreen

{
	private TweenManager tweenManager;

	// THREE ACTORS ONLY FOR DEMONSTRATION
	private BitmapFont gameFont;
	public static int gameOverCounterForAds = 0;

	// DEFINITION OF SCREEN HELPERS THAT HELP CREATE MENU FOR VARIOUS GAME STATES
	public GameScreenGameReadyMenu gameScreenGameReadyMenu;
	public GameScreenGamePauseMenu gameScreenGamePauseMenu;
	public GameScreenGameOverMenu gameScreenGameOverMenu;
	public GameScreenLevelEndMenu gameScreenLevelEndMenu;
	EmptyActorLight healthBar;
	private float buttonSize = 100 * AppSettings.getWorldSizeRatio();
	private MyOrthographicCamera camera;
	boolean isBlurred = false;

	public enum State
	{
		GAME_READY,
		GAME_LEVEL_END,
		GAME_PAUSED,
		GAME_RUNNING,
	    GAME_RESUME,
	    GAME_OVER
	}
	public static State state;
    //DEFINE OUR GAME DATA
	public static int lastScore;
	public static String gameoverinfo;
	public static String scoreString;
	public static int currentlevel = 1;
	// CREATE AN INSTANCE OF WORLD AND WORLD RENDERER
	GameWorld world;
	WorldListener worldListener;
	GameRenderer renderer;

	private InputMultiplexer inputMux;
	private InputProcessor inputProcessor;
	private GestureDetector gestureDetector;
	// KEEP A MODE FOR TESTING
	public static boolean DEBUG_MODE = false;

	public static int creditsPoint;

	private TextureRegion button_overlay_pause, button_overlay_left, button_overlay_right, holder, background_menu_berge, button_menu_up, button_menu_down,button_resume_up, button_resume_down,button_quit_up, button_quit_down;
	public static TextureRegion firstFrame, secondFrame, thirdFrame, fourthFrame,fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, twelvethFrame, thirteenthFrame, fourteenthFrame, fifteenthFrame;
	public static Animation pummaStill, pummaWalk, pummaJump;
	private ParallaxBackground parallaxBackground, parallaxForeground;



		 public GameScreen(Game game, String screenName)
		 {
			super(game, screenName);

			initScreenAssets();
			gameFont = new BitmapFont(Gdx.files.internal("data/maroonFont.fnt"), Gdx.files.internal("data/maroonFont.png"), false);
			state = State.GAME_READY;

			setUpTheWorld();
			setUpScreenElements();
		    setUpInfoPanel();
			setUpMenu();
			setupTween();
			setUpListeners();
		    setUpCamera();
		    setUpInputProcessor();

			//Called once the player completes a Level
			resetGame();
		}

		 private void initScreenAssets()
		 {
			 //Getting previously loaded atlas
			 atlas = getMyGame().getManager().get(LoadingScreen.GAME_ATLAS, TextureAtlas.class);
			 atlas_base = getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

			 button_overlay_pause = atlas.findRegion("button_overlay_pause");
			 button_overlay_left = atlas.findRegion("button_overlay_left");
			 button_overlay_right = atlas.findRegion("button_overlay_right");
			 button_menu_up = atlas.findRegion("button_menu_up");
			 button_menu_down = atlas.findRegion("button_menu_down");
			 button_resume_up = atlas.findRegion("button_resume_up");
			 button_resume_down = atlas.findRegion("button_resume_down");
			 button_quit_up = atlas.findRegion("button_quit_up");
			 button_quit_down = atlas.findRegion("button_quit_down");

			 holder = atlas_base.findRegion("holder");
			 background_menu_berge = atlas_base.findRegion("background_menu_berge");

			 // Mad Boy Frames
			 firstFrame = atlas.findRegion("1");
			 secondFrame = atlas.findRegion("2");
			 thirdFrame = atlas.findRegion("2");
			 fourthFrame = atlas.findRegion("4");
			 fifthFrame = atlas.findRegion("5");
			 sixthFrame = atlas.findRegion("6");
			 seventhFrame = atlas.findRegion("7");
			 eighthFrame = atlas.findRegion("8");
			 ninethFrame = atlas.findRegion("9");
			 tenthFrame = atlas.findRegion("10");
			 eleventhFrame = atlas.findRegion("11");
			 twelvethFrame = atlas.findRegion("12");
			 thirteenthFrame = atlas.findRegion("13");
			 fourteenthFrame = atlas.findRegion("14");
			 fifteenthFrame = atlas.findRegion("15");

			 TextureRegion[] yoyos = { firstFrame, secondFrame, thirdFrame, fourthFrame, fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, twelvethFrame, thirteenthFrame, fourteenthFrame, fifteenthFrame  };
			 TextureRegion[] yoyoStill = {  eleventhFrame, twelvethFrame };
			 TextureRegion[] yoyoJump = { fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, };

			 pummaWalk = new Animation(1/30f, yoyos);
			 pummaWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

			 pummaStill = new Animation(0.3f, yoyoStill);
			 pummaStill.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

			 pummaJump = new Animation(1/30f, yoyoJump);
			 pummaJump.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

			 TextureRegion skyRegion = atlas.findRegion("sky");
			 TextureRegionParallaxLayer skyLayer = new TextureRegionParallaxLayer(skyRegion, GameWorld.WORLD_WIDTH, new Vector2(.3f,.3f), WH.width);

			 TextureRegion downtownNairobiRegion = atlas.findRegion("background_downtown");
			 TextureRegionParallaxLayer downtownNairobiLayer = new TextureRegionParallaxLayer(downtownNairobiRegion, GameWorld.WORLD_WIDTH, new Vector2(.6f,.6f), WH.width);

			 TextureRegion uptownNairobiRegion = atlas.findRegion("nairobi_city_uptown");
			 TextureRegionParallaxLayer uptownNairobiLayer = new TextureRegionParallaxLayer(uptownNairobiRegion, GameWorld.WORLD_WIDTH, new Vector2(.75f,.75f), WH.width);

			 TextureRegion ghettoFenceRegion = atlas.findRegion("ghettofence");
			 TextureRegionParallaxLayer ghettofenceLayer = new TextureRegionParallaxLayer(ghettoFenceRegion, GameWorld.WORLD_WIDTH, new Vector2(1.3f,1.3f), WH.width);

			 parallaxBackground = new ParallaxBackground();
			 parallaxForeground = new ParallaxBackground();
			 parallaxBackground.addLayers(skyLayer,downtownNairobiLayer,uptownNairobiLayer);
			 parallaxForeground.addLayers(ghettofenceLayer);

		}

		public void resetGame()
		{
			// The game is reset each time the game is over
			lastScore = GameWorld.score;

			if(!DEBUG_MODE)
			{
				if(state == State.GAME_READY)
				{
					gameScreenGameReadyMenu.sendInMenu(GameScreen.this);
				}
			}
			creditsPoint =0;
		}

		public void setUpTheWorld()
		{
			world = new GameWorld(worldListener, getMyGame());
			renderer = new GameRenderer(world);
		}


		private void setUpListeners()
		{
			worldListener = new WorldListener()
			{

		     };
		}
		private void setUpCamera()
		{
			//CREATE AN ORTHOGRAPHIC CAMERA THAT SHOWS US 32X18 UNITS OF THE WORLD
	        //IN THIS FRAMEWORK 1 WORLD UNIT = 32 SCREEN PIXELS
			camera = new MyOrthographicCamera();
			camera.setToOrtho(false, 32f, 18f);
			camera.setWorldBounds(0, GameWorld.mapWidth/GameWorld.WORLD_UNIT, 0, GameWorld.mapHeight/GameWorld.WORLD_UNIT);
			world.setBoundingBox(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.8f);

			camera.update();

			Gdx.app.log("WORLD", "Width" +GameWorld.mapWidth +GameWorld.mapHeight);
			Gdx.app.log("SCREEN", "Width" +AppSettings.SCREEN_W +AppSettings.SCREEN_H);
		}
		private void setUpInputProcessor()
		{
		    this.inputMux = new InputMultiplexer();
		    this.inputProcessor = new InputHandler(this.camera,this.tweenManager,this.world);
		    gestureDetector = new GestureDetector((GestureListener) this.inputProcessor);
		    inputMux.addProcessor(getStage());
			toggleGestureProcessor(false);
			Gdx.input.setInputProcessor(this.inputMux);
		}

		private void setupTween()

		{
			tweenManager = new TweenManager();
			Tween.registerAccessor(OrthographicCamera.class, new OrthographicCameraAccessor());
			Tween.registerAccessor(SmoothCamWorld.class, new SmoothCamAccessor());
		}

		@Override
		public void setUpScreenElements()
		{


			if (!DEBUG_MODE)
			{
				TextureRegion[] readyMenu = { holder, button_overlay_left, button_overlay_right, background_menu_berge };
				TextureRegion[] pauseMenu = { holder, button_overlay_left, button_overlay_right, background_menu_berge, button_menu_up, button_menu_down, button_resume_up, button_resume_down, button_quit_up, button_quit_down };
				TextureRegion[] overMenu = { holder, button_overlay_left, button_overlay_right, background_menu_berge };
				TextureRegion[] levelEndMenu = { holder, button_overlay_left, button_overlay_right, background_menu_berge };

				gameScreenGameReadyMenu = new GameScreenGameReadyMenu(readyMenu);
				gameScreenGamePauseMenu = new GameScreenGamePauseMenu(pauseMenu);
				gameScreenGameOverMenu = new GameScreenGameOverMenu(overMenu);
				gameScreenLevelEndMenu = new GameScreenLevelEndMenu(levelEndMenu);
			}

				healthBar = new EmptyActorLight(500*AppSettings.getWorldPositionXRatio(), 18, true);
				healthBar.setPosition(140*AppSettings.getWorldPositionXRatio(), AppSettings.SCREEN_H - 25*AppSettings.getWorldPositionYRatio());
				//healthBar.setTextureRegion(AssetLoader.transparent, true);
		}

		@Override
		public void setUpInfoPanel()
		{

		}

		@Override
		public void setUpMenu()
		{
			//Set up all the screen helpers here
			if (!DEBUG_MODE)
			{
				gameScreenGameReadyMenu.setUpMenu(this);
				gameScreenGameOverMenu.setUpMenu(this);
				gameScreenGamePauseMenu.setUpMenu(this);
				gameScreenLevelEndMenu.setUpMenu(this);
			}
		}

		@Override
		public void render(float delta)
		{
		super.render(delta);

		/************* VERY IMPORTANT ***********************************************************************/
		// SET THE VIEW OF THE WORLD RENDERER AS PER MY CAMERA DEFINED HERE SO
		// THAT IT CAN MAP TO WORLD UNITS
		renderer.renderer.setView(camera);

		// BASE THE HORIZONTAL/VERTICAL MOVEMENT OF THE CAMERA ON THE PLAYER
		// MOVEMENT
		camera.setPosition(world.getYoyo().position.x, 0);

		tweenManager.update(delta);
		// UPDATE THE CAMERA TO REFLECT ALL THE CHANGES
		camera.update();

		// UPDATE THE GAMESCREEN ACCORDING TO THE CURRENT GAME STATE
		update(delta);

		// THIS METHOD IS CALLED IN RENDER() LOOP SO THAT IT CAN CONTINUOSLY
		// CHECK THE
		// GAME STATES AND CALL A PARTICULAR RENDERSTATE() TO DRAW THINGS
		// ACCORDINGLY
		/******************************************************************************/

		// IMPLEMENT DIFFERENT UPDATE AND PRESENT METHODS FOR VARIOUS GAME
		// STATES
		getStage().getBatch().begin();
		switch (state)
		{

		case GAME_READY:
			renderReady();
			break;

		case GAME_RUNNING:
			renderRunning(delta);
			break;

		case GAME_PAUSED:
			renderPaused();
			break;

		case GAME_LEVEL_END:
			renderLevelEnd();
			break;

		case GAME_OVER:
			renderGameOver();
			break;
		}

		if (DEBUG_MODE)
		{
			gameFont.draw(getStage().getBatch(), "DEBUGGING MODE: ON", 300, 50);
		}

		getStage().getBatch().end();

	}



		private void update(float delta)
		{
			    switch(state)
			    {
			    case GAME_READY:
			        updateReady();
			        break;
			    case GAME_RUNNING:
			        updateRunning(delta);
			        break;
			    case GAME_PAUSED:
			        updatePaused();
			        break;
			    case GAME_LEVEL_END:
			        updateLevelEnd();
			        break;
			    case GAME_OVER:
			        updateGameOver();
			        break;
			    }
		}

		private void updateGameOver()
		{
			if(DEBUG_MODE)
			{

				if (Gdx.input.justTouched())
				{
					Gdx.app.log("A HIT", "GAME WENT FROM GAMEOVER TO MAINMENU");
				    getMyGame().setScreen(new LoadingScreen(getMyGame(), "MainMenuScreen", LoadingScreen.TYPE_UI_MENU));
				}
			}
		}

		private void updateLevelEnd()
		{
			// TODO Auto-generated method stub
			if(DEBUG_MODE)
			{
				if (Gdx.input.justTouched())
				{
					Gdx.app.log("A HIT", "GAME WENT FROM LEVEL TO LEVEL");
				    currentlevel++;
				    GameData.addToUnLockedLevel(GameScreen.currentlevel);

			        world = new GameWorld(worldListener,getMyGame());
			        renderer = new GameRenderer(world);
			        GameWorld.score = lastScore;
			        state = State.GAME_READY;

			        resetGame();
			    }
		    }
		}

		private void updatePaused()
		{

			 if(DEBUG_MODE)
			 {
				 if (Gdx.input.justTouched())
				 {
					 Gdx.app.log("A HIT", "GAME WENT TO PAUSE STATE");
	                 state = State.GAME_RUNNING;

	                 return;
	             }
			 }

		}

		private void updateRunning(float delta)
		{
			renderer.renderBackground(parallaxBackground, camera);
			//parallaxBackground.draw(camera, getStage().getBatch());
			renderer.render(new int[] { 0, 1 });
			renderer.renderPlayer(delta);
			renderer.renderForeground(parallaxForeground, camera);
			//parallaxForeground.draw(camera, getStage().getBatch());

			 lastScore = GameWorld.score;
			 currentlevel = GameWorld.levelID;
		     scoreString = "" + lastScore;

			if(world.state == GameWorld.WORLD_STATE_GAME_OVER)
			{
				saveGameStates();
				lastScore = GameWorld.score =0;

				if(!DEBUG_MODE)
				{
					gameScreenGameOverMenu.sendInMenu(this);
				}

				state = State.GAME_OVER;

		    }

			 if(world.state == GameWorld.WORLD_STATE_NEXT_LEVEL)
			 {
				 saveGameStates();
				 try
				 {

				 }
				 catch(Exception e)
				 {

				 }

				 if(!DEBUG_MODE)
				 {
					gameScreenLevelEndMenu.sendInMenu(this);
				 }
					state = State.GAME_LEVEL_END;


			    }

			//UPDATE THE PLAYER FOR USER INPUT
			updatePlayerForUserInput(delta);

			//UPDATE THE WORLD
			  world.update(delta);
		}

		private void updatePlayerForUserInput(float delta)
		{
			boolean left = false;
			boolean right = false;
			boolean jump = false;
			boolean pause = false;

			if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS)
			{
					for (int i = 0; i < 2; i++)
					{
						int x = (int)(Gdx.input.getX(i) / (float)Gdx.graphics.getWidth() * AppSettings.SCREEN_W);
						int y = (int)(Gdx.input.getY(i) / (float)Gdx.graphics.getWidth() * AppSettings.SCREEN_W);
						if (!Gdx.input.isTouched(i)) continue;

						if(y<= AppSettings.SCREEN_H && y>= AppSettings.SCREEN_H -50)
						{
							if (x <=90)
							{
								left |= true;
							}
							if (x > 1.2f*buttonSize && x <= 2.2f*buttonSize)
							{
								right |= true;
							}
							if (x >= AppSettings.SCREEN_W - 50 && x < AppSettings.SCREEN_W)
							{
								jump |= true;
							}
						}
	                    if (x >= AppSettings.SCREEN_W -45 && y >= 0 && y <= 50)
	                    {
							pause |= true;
						}
					}
				}

			// CHECK USER INPUT AND APPLY TO VELOCITY AND STATES OF THE MAIN PLAYER
					if ((Gdx.input.isKeyPressed(Keys.SPACE) && world.getYoyo().grounded) || (jump && world.getYoyo().grounded))
					{
						world.getYoyo().velocity.y += world.getYoyo().JUMP_VELOCITY;
						world.getYoyo().setState(Yoyo.JUMP);
						world.getYoyo().grounded = false;
					}


					if (Gdx.input.isKeyPressed(Keys.LEFT) ||left)
					{
						world.getYoyo().velocity.x = -world.getYoyo().MAX_VELOCITY;
						if (world.getYoyo().grounded)
							world.getYoyo().setState(Yoyo.WALK);
						    world.getYoyo().facesRight = false;
					}

					if (Gdx.input.isKeyPressed(Keys.RIGHT) || right)
					{
						world.getYoyo().velocity.x = world.getYoyo().MAX_VELOCITY;
						if (world.getYoyo().grounded)
							world.getYoyo().setState(Yoyo.WALK);
						    world.getYoyo().facesRight = true;

					}

					if (Gdx.input.isKeyPressed(Keys.P) || pause)
					{
						state = State.GAME_PAUSED;

						ScreenShotSaver.saveScreenShot();

						if(!DEBUG_MODE)
						{
							toggleGestureProcessor(false);

							setBackgroundTexture(new TextureRegion(applyBlurEffect(Gdx.files.external("madboy/screenshot.png"))));

							gameScreenGamePauseMenu.sendInMenu(this);
						}
					}
		}

		public void toggleGestureProcessor(boolean isKeepGestures)
		{
			if (isKeepGestures == true)
			{
				inputMux.addProcessor(gestureDetector);
			    inputMux.addProcessor(inputProcessor);
			}
			else
			{
			    inputMux.removeProcessor(inputProcessor);
				inputMux.removeProcessor(gestureDetector);
			}


		}

	private void updateReady()
	{

		if (DEBUG_MODE)
		{
			if (Gdx.input.justTouched())
			{
				Gdx.app.log("A HIT", "GAME WENT FROM READY TO RUNNING STATE");
				state = State.GAME_RUNNING;

				return;
			}
		}
	}

		private void renderGameOver() {
			// TODO Auto-generated method stub
			if(DEBUG_MODE){
				gameFont.draw(getStage().getBatch(), " "+gameoverinfo, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2 +80);
				getStage().getBatch().draw(AssetLoader.logo, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2);
			}
		}

		private void renderLevelEnd()
		{
			// TODO Auto-generated method stub
			if(DEBUG_MODE)
			{
				gameFont.draw(getStage().getBatch(), "Level "+currentlevel+" Completed", AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2 +80);
				getStage().getBatch().draw(AssetLoader.logo, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2);
			}
		}

		private void renderPaused()
		{
			if(DEBUG_MODE)
			{
				gameFont.draw(getStage().getBatch(), " TOUCH TO RESUME", AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2 +80);
				getStage().getBatch().draw(AssetLoader.firstFrame, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2);
			}
		}

		private void renderRunning(float delta)
		{

			gameFont.setScale(0.6f);
			gameFont.draw(getStage().getBatch(), "Score :"+GameWorld.score,
			140*AppSettings.getWorldPositionXRatio(), AppSettings.SCREEN_H - 30*AppSettings.getWorldPositionYRatio());


			//Display the Health Bar here using Scene2D Actor
			healthBar.setWidth((Math.min(460, world.getYoyo().health/2.2f))*AppSettings.getWorldPositionXRatio());
			healthBar.draw(getStage().getBatch(), 1.0f);

			gameFont.draw(getStage().getBatch(), Math.min(100, world.getYoyo().health/10)+" %", healthBar.getX()+1.05f*healthBar.getWidth(), AppSettings.SCREEN_H - 8*AppSettings.getWorldPositionYRatio());

	        //TO BE DRAWN ONLY AFTER ALL GAME ELEMENTS ARE DRAWN TO AVOID OVERRIDE
	        //DRAW THE GAME CONTROL UI ONLY ON ANDROID DEVICES IF THE GAME STATE IS RUNNING

			if ((Gdx.app.getType() == ApplicationType.Android) || (Gdx.app.getType() == ApplicationType.iOS)){

				getStage().getBatch().draw(button_overlay_left, 0f, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
				getStage().getBatch().draw(button_overlay_right, 1.2f*buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
				getStage().getBatch().draw(button_overlay_right, AppSettings.SCREEN_W -0.03f*buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1, 1, 90);
				getStage().getBatch().draw(button_overlay_pause, AppSettings.SCREEN_W-45,AppSettings.SCREEN_H -45, 0, 0, 45, 45, 1, 1, 0);

			}

			//camera.position.set(world.getX(), world.getY(), 0);
			camera.viewportWidth = camera.viewportWidth * world.getZoom();
			camera.viewportHeight = camera.viewportHeight * world.getZoom();
			camera.update();

		}

		private void renderReady() {

			if(DEBUG_MODE){
				gameFont.draw(getStage().getBatch(), " Touch to Begin Level "+currentlevel, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2 +80);
				getStage().getBatch().draw(AssetLoader.logo, AppSettings.SCREEN_W/3, AppSettings.SCREEN_H/2);
			}
		}

		private void saveGameStates() {
			// STATES ARE SAVED IN KEY AND VALUE PAIRS AS PREFERENCES

			//GET THE EXISTING HIGHSCORES FROM PREFERENCES
		        int[] scoresfromdb = GameData.getHighScores();

		        //CHECK IF THE CURRENT SCORE IS GREATER THAN THE STORED ONE
		        if(lastScore > scoresfromdb[4])
		            scoreString = "NEW RECORD : " + lastScore;
		        else
		            scoreString = "SCORE : " + lastScore;

		        //ADD THE NEW SCORE TO THE PREFERENCES IN DECREASING ORDER
		        GameData.addScore(lastScore);
				GameData.savePefs();
		}

		@Override
		public void pause()
		{
			super.pause();
			this.state = State.GAME_PAUSED;
		}

}
