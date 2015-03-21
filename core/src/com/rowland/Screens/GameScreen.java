package com.rowland.Screens;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.magnetideas.helpers.MyAbstractScreen;
import com.magnetideas.helpers.ScreenBlurUtils;
import com.magnetideas.helpers.ScreenShotForVideo;
import com.magnetideas.helpers.ScreenShotSaver;
import com.magnetideas.parallax.ParallaxBackground;
import com.magnetideas.parallax.TextureRegionParallaxLayer;
import com.magnetideas.parallax.Utils.WH;
import com.moribitotech.mtx.input.InputIntent;
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
import com.rowland.ScreenHelpers.GameScreenInstructions;
import com.rowland.ScreenHelpers.GameScreenLevelEndMenu;
import com.rowland.TweenAccessors.OrthographicCameraAccessor;
import com.rowland.UI.MadBoyGameButton;

public class GameScreen extends MyAbstractScreen implements IScreen

{
	private TweenManager tweenManager;

	//private ExecutorService executor;
	public EmptyActorLight instructions;

	private BitmapFont gameFont;
	public static int gameOverCounterForAds = 0;

	//Here is a declaration of the screen helpers that will help create menu for various game states
	public GameScreenGameReadyMenu gameScreenGameReadyMenu;
	public GameScreenGamePauseMenu gameScreenGamePauseMenu;
	public GameScreenGameOverMenu gameScreenGameOverMenu;
	public GameScreenLevelEndMenu gameScreenLevelEndMenu;
	public GameScreenInstructions gameScreenGameInstruction;
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
	//GameWorld and WorldRenderer instance
	GameWorld world;
	WorldListener worldListener;
	GameRenderer renderer;

	private InputMultiplexer inputMux;
	private InputProcessor inputProcessor;
	private GestureDetector gestureDetector;
	//Keeping a mode for debugging
	public static boolean DEBUG_MODE = false;

	public static int creditsPoint;

	public static TextureRegion button_overlay_pause, button_overlay_left, button_overlay_right, holder, background_menu_berge, button_menu_up, button_menu_down,button_resume_up, button_resume_down,button_quit_up, button_quit_down,img_obj_swipe_down_menu,img_obj_swipe_up_instructions;
	public static TextureRegion firstFrame, secondFrame, thirdFrame, fourthFrame,fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, twelvethFrame, thirteenthFrame, fourteenthFrame, fifteenthFrame;
	public static Animation pummaStill, pummaWalk, pummaJump;
	private ParallaxBackground parallaxBackground, parallaxForeground;
	public static TextureRegion skyRegion, downtownNairobiRegion,uptownNairobiRegion, ghettoFenceRegion ;

	// Splash, Menu, Instruction activeness management
	private boolean isSplashCompleted;
	private boolean isMenuActive;

	// Swipe controls
	InputIntent inputIntent;
	float touchDragInterval;

	public GameScreen(Game game, String screenName)
	{
		super(game, screenName);

		executor = Executors.newFixedThreadPool(25);

		initScreenAssets();
		gameFont = new BitmapFont(Gdx.files.internal("data/maroonFont.fnt"), Gdx.files.internal("data/maroonFont.png"), false);
		//state = State.GAME_READY;
		state = State.GAME_RUNNING;

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
		img_obj_swipe_down_menu = atlas.findRegion("img_obj_swipe_down_menu");
		img_obj_swipe_up_instructions = atlas.findRegion("img_obj_swipe_up_instructions");

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

		skyRegion = atlas.findRegion("sky");
		skyRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegionParallaxLayer skyLayer = new TextureRegionParallaxLayer(skyRegion, GameWorld.WORLD_WIDTH, new Vector2(.3f,.3f), WH.width);

		downtownNairobiRegion = atlas.findRegion("background_downtown");
		TextureRegionParallaxLayer downtownNairobiLayer = new TextureRegionParallaxLayer(downtownNairobiRegion, GameWorld.WORLD_WIDTH, new Vector2(.6f,.6f), WH.width);

		uptownNairobiRegion = atlas.findRegion("nairobi_city_uptown");
		TextureRegionParallaxLayer uptownNairobiLayer = new TextureRegionParallaxLayer(uptownNairobiRegion, GameWorld.WORLD_WIDTH, new Vector2(.75f,.75f), WH.width);

		ghettoFenceRegion = atlas.findRegion("ghettofence");
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
				//gameScreenGameReadyMenu.sendInMenu();
			}
		}
		creditsPoint =0;
	}

	public void restartGame()
	{
		world = new GameWorld(worldListener,getMyGame());
		renderer = new GameRenderer(world);
		GameWorld.score = lastScore;
		state = State.GAME_READY;
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
		toggleGestureProcessor(true);
		Gdx.input.setInputProcessor(this.inputMux);
	}

	private void setupTween()

	{
		tweenManager = new TweenManager();
		Tween.registerAccessor(OrthographicCamera.class, new OrthographicCameraAccessor());
	}

	@Override
	public void setUpScreenElements()
	{


		if (!DEBUG_MODE)
		{


			gameScreenGameReadyMenu = new GameScreenGameReadyMenu(this);
			gameScreenGamePauseMenu = new GameScreenGamePauseMenu(this);
			gameScreenGameOverMenu = new GameScreenGameOverMenu(this);
			gameScreenLevelEndMenu = new GameScreenLevelEndMenu(this);

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
			gameScreenGameReadyMenu.setUpMenu();
			gameScreenGameOverMenu.setUpMenu();
			gameScreenGamePauseMenu.setUpMenu();
			gameScreenLevelEndMenu.setUpMenu();
		}
	}

	@Override
	public void render(float delta)
	{
		super.render(delta);

		/**********************************VERY IMPORTANT ************************************/
		/*Set the view of the world renderer as per my camera defined so that it can map to world units*/
		renderer.renderer.setView(camera);

		//Update the gamescreen according to the current game state
		update(delta);

		/*Method called in render loop so that it can continuosly check the game states and call a particular
		render state to draw things accordingly for various game states*/
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

		if (DEBUG_MODE)
		{
			ScreenShotForVideo worker = new ScreenShotForVideo();
			worker.prepare(); // grab screenshot
			executor.execute(worker); // delayed save in other thread
		}

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
		if (Gdx.input.justTouched())
		{
			Gdx.app.log("A HIT", "GAME WENT FROM GAMEOVER TO MAINMENU");
			getMyGame().setScreen(new LoadingScreen(getMyGame(), "MainMenuScreen", LoadingScreen.TYPE_UI_MENU));
		}
	}

	private void updateLevelEnd()
	{
		if (Gdx.input.justTouched())
		{
			Gdx.app.log("A HIT", "GAME WENT FROM LEVEL TO LEVEL");
			currentlevel++;
			GameData.addToUnLockedLevel(GameScreen.currentlevel);

			restartGame();

			resetGame();
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

		/*renderer.renderBackground(parallaxBackground, camera);
			renderer.render(new int[] { 0, 1 });
			renderer.renderPlayer(delta);
			renderer.renderForeground(parallaxForeground, camera);*/

		lastScore = GameWorld.score;
		currentlevel = GameWorld.levelID;
		scoreString = "" + lastScore;

		if(world.state == GameWorld.WORLD_STATE_GAME_OVER)
		{
			saveGameStates();
			lastScore = GameWorld.score =0;

			gameScreenGameOverMenu.sendInMenu();

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
				gameScreenLevelEndMenu.sendInMenu();
			}
			state = State.GAME_LEVEL_END;


		}

		updatePlayerForUserInput(delta);

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

		//CheckUser input and apply to velocity and states of the main player
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

			setBackgroundTexture(getBlurredTextureRegion());

			toggleGestureProcessor(false);

			Gdx.app.log("BLURRRING", "Blurring Efect");

			gameScreenGamePauseMenu.sendInMenu();
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


	}

	private void renderGameOver() {

	}

	private void renderLevelEnd()
	{

	}

	private void renderPaused()
	{

	}

	private void renderRunning(float delta)
	{
		renderer.renderBackground(parallaxBackground, camera);
		renderer.render(new int[] { 0, 1 });
		renderer.renderPlayer(delta);
		renderer.renderForeground(parallaxForeground, camera);

		tweenManager.update(delta);
		//Update the camera to refect all the changes
		camera.update();

		/*Base the horizontal/vertical movement of the camera on the player*/

		camera.setPosition(world.getYoyo().position.x, 0);
		//camera.setCameraToPlayer(world.getYoyo().position.x, 0f, 0f, tweenManager);

		gameFont.setScale(0.6f);
		gameFont.draw(getStage().getBatch(), "Score :"+GameWorld.score,
				140*AppSettings.getWorldPositionXRatio(), AppSettings.SCREEN_H - 30*AppSettings.getWorldPositionYRatio());


		//Display the Health Bar here using Scene2D Actor
		healthBar.setWidth((Math.min(460, world.getYoyo().health/2.2f))*AppSettings.getWorldPositionXRatio());
		healthBar.draw(getStage().getBatch(), 1.0f);

		gameFont.draw(getStage().getBatch(), Math.min(100, world.getYoyo().health/10)+" %", healthBar.getX()+1.05f*healthBar.getWidth(), AppSettings.SCREEN_H - 8*AppSettings.getWorldPositionYRatio());

		//To be drawn only after all gameelements are drawn to avoid override
		//Drawthe game control UI only on Android and iOs devices if the game is running

		if ((Gdx.app.getType() == ApplicationType.Android) || (Gdx.app.getType() == ApplicationType.iOS)){

			getStage().getBatch().draw(button_overlay_left, 0f, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
			getStage().getBatch().draw(button_overlay_right, 1.2f*buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
			getStage().getBatch().draw(button_overlay_right, AppSettings.SCREEN_W -0.03f*buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1, 1, 90);
			getStage().getBatch().draw(button_overlay_pause, AppSettings.SCREEN_W-45,AppSettings.SCREEN_H -45, 0, 0, 45, 45, 1, 1, 0);

		}


	}

	private void renderReady() {

	}

	private void saveGameStates()
	{
		//States are saved in key and value pairs as preference

		//Get the existing highscore from prefernces
		int[] scoresfromdb = GameData.getHighScores();

		//Check if the current score is greater than the stored one
		if(lastScore > scoresfromdb[4])
		{
			scoreString = "NEW RECORD : " + lastScore;
		}
		else
		{
			scoreString = "SCORE : " + lastScore;
		}

		//Add the new score to the prefernmces in decreasing order
		GameData.addScore(lastScore);
		GameData.savePefs();
	}

	@Override
	public void pause()
	{
		super.pause();
		GameScreen.state = State.GAME_PAUSED;
	}

	@Override
	public void resume()
	{
		super.pause();
		GameScreen.state = State.GAME_RUNNING;
	}

}
