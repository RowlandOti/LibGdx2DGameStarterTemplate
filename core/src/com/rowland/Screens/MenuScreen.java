package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.madboy.buttons.JungleGameButton;
import com.moribitotech.mtx.input.InputIntent;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.effects.EffectCreator;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.scene2d.models.SmartActor;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.ScreenHelpers.MainMenuScreenButtons;
import com.rowland.ScreenHelpers.MainMenuScreenEnvironment;
import com.rowland.ScreenHelpers.MainMenuScreenInstructions;
import com.rowland.Screens.LoadingScreen;

import java.util.ArrayList;

public class MenuScreen extends MyAbstractScreen implements IScreen {

	// Splash
	public EmptyActorLight splashLoading;

	// Main Menu Screen elements
	public EmptyActorLight gameName, mountains;
	public ArrayList<SmartActor> backgroundBalloons,flowers;
	public EmptyActorLight instructions;
	public JungleGameButton btnPlay,btnScores,btnSettings,btnSocialFacebook, btnSocialTwitter,btnSocialGoogle,btnSwipeForMenu,btnSwipeForInstructions;
	public TableModel menuTable;

	public static TextureRegion img_bg_1_,img_obj_btn_play,img_obj_btn_scores,img_obj_btn_credit,img_obj_circle,img_obj_flower_1_,img_obj_flower_2_,img_obj_loading,img_obj_mountains,img_obj_rectangle,img_obj_social_facebook,img_obj_social_google,img_obj_social_twitter,img_obj_sound_off,img_obj_sound_on,img_obj_swipe_down_menu,img_obj_swipe_up_instructions,img_obj_text_junglegamemenu;

	// Splash, Menu, Instruction activeness management
	private boolean isSplashCompleted;
	private boolean isMenuActive;

	// Main Menu Screen element managers
	// These creates and animates the above screen elements
	MainMenuScreenButtons jungleMainMenuScreenButtons;
	MainMenuScreenEnvironment jungleMainMenuScreenEnvironment;
	MainMenuScreenInstructions jungleMainMenuScreenInstructions;

	// Main Menu Screen helpers
	private final float SPLASH_TIME = 1.0f;
	private final float GAME_NAME_LOOP_ANIMATION_TIME = 3.0f;
	private float gameNameAnimationTimer;

	// Swipe controls
	InputIntent inputIntent;
	float touchDragInterval;

	public MenuScreen(Game game, String screenName)
	{
		super(game, screenName);

		initScreenAssets();
		setUpScreenElements();
		setUpMenu();
		setUpSwipeListener();
	}

	private void initScreenAssets()
	{
		//Getting previously loaded atlas
        atlas = getMyGame().getManager().get(LoadingScreen.UI_MENU_ATLAS, TextureAtlas.class);
        atlas_base = getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

        img_bg_1_ = atlas.findRegion("img_bg_1_");
		img_obj_btn_play = atlas.findRegion("img_obj_btn_play");
		img_obj_btn_scores = atlas.findRegion("img_obj_btn_scores");
		img_obj_btn_credit = atlas.findRegion("img_obj_btn_credit");
		img_obj_circle = atlas.findRegion("img_obj_circle");
		img_obj_flower_1_ = atlas.findRegion("img_obj_flower_1_");
		img_obj_flower_2_ = atlas.findRegion("img_obj_flower_2_");
		img_obj_loading = atlas.findRegion("img_obj_loading");
		img_obj_mountains = atlas.findRegion("img_obj_mountains");
		img_obj_rectangle = atlas.findRegion("img_obj_rectangle");
		img_obj_social_facebook = atlas.findRegion("img_obj_social_facebook");
		img_obj_social_google = atlas.findRegion("img_obj_social_google");
		img_obj_social_twitter = atlas.findRegion("img_obj_social_twitter");
		img_obj_sound_off = atlas.findRegion("img_obj_sound_off");
		img_obj_sound_on = atlas.findRegion("img_obj_sound_on");
		img_obj_swipe_down_menu = atlas.findRegion("img_obj_swipe_down_menu");
		img_obj_swipe_up_instructions = atlas.findRegion("img_obj_swipe_up_instructions");
		img_obj_text_junglegamemenu = atlas.findRegion("img_obj_text_junglegamemenu");
	}

	@Override
	public void setUpScreenElements() {
		//
		// Reset system
		// #################################################################
		setSecondsTime(0);
		isSplashCompleted = false;
		isMenuActive = false;

		//
		// Set background image
		// #################################################################
		setBackgroundTexture(img_bg_1_);

		//
		// Set game name animation timer
		// #################################################################
		gameNameAnimationTimer = SPLASH_TIME + GAME_NAME_LOOP_ANIMATION_TIME;

		//
		// InputIntent for swipes/drags
		// #################################################################
		inputIntent = new InputIntent();
		touchDragInterval = AppSettings.SCREEN_H / 3.0f;
		inputIntent.setTouchDragIntervalRange(touchDragInterval);

		//
		// Construct Main Menu Screen element managers
		// #################################################################
		jungleMainMenuScreenButtons = new MainMenuScreenButtons(this);
		jungleMainMenuScreenEnvironment = new MainMenuScreenEnvironment(this);
		jungleMainMenuScreenInstructions = new MainMenuScreenInstructions(this);

		//
		// Prepare splash
		// #################################################################
		splashLoading = new EmptyActorLight(150f, 150f, true);
		splashLoading.setTextureRegion(img_obj_loading, true);
		splashLoading.setOrigin(splashLoading.getWidth() / 2.0f, splashLoading.getHeight() / 2.0f);
		splashLoading.setPosition(AppSettings.SCREEN_W - splashLoading.getWidth(),AppSettings.SCREEN_H - splashLoading.getHeight());//
		getStage().addActor(splashLoading);
	}

	@Override
	public void setUpMenu() {
		//
		// Main menu (Order is important)
		// #################################################################
		//
		// They are invisible or scaled to 0f until splash is completed
		// We will send the elements after splash is completed
		// Check the render() method

		jungleMainMenuScreenEnvironment.setUpBackgroundBalloons();
		jungleMainMenuScreenEnvironment.setUpMounatins();
		jungleMainMenuScreenEnvironment.setUpFlowers();
		jungleMainMenuScreenEnvironment.setUpGameName();

		jungleMainMenuScreenButtons.setUpMainMenuButtons();
		jungleMainMenuScreenButtons.setUpSocialButtons();
		jungleMainMenuScreenButtons.setUpSwipeButtons();

		jungleMainMenuScreenInstructions.setUpInstructions();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		//
		// Splash
		// #################################################################
		//
		// When splash is completed, send main menu elements (Name, Buttons,
		// etc...), I faked the splash/loading here. Normally use assetmanager
		// and when its really completed the asset loading, send game elements
		//
		if (!isSplashCompleted)
		{
			if (getSecondsTime() > SPLASH_TIME)
			{
				jungleMainMenuScreenButtons.sendInMainMenuButtons();
				jungleMainMenuScreenButtons.sendInSocialButtons();
				jungleMainMenuScreenButtons.sendInSwipeForInstruction();
				jungleMainMenuScreenEnvironment.sendInGameName();

				isSplashCompleted = true;
				isMenuActive = true;
			}

			if (getSecondsTime() > SPLASH_TIME - 1.0f)
			{
				splashLoading.addAction(Actions.rotateBy(-5f));
				EffectCreator.create_FO(splashLoading, 0.7f, 0, null, false);
			}
			else
			{
				splashLoading.addAction(Actions.rotateBy(-5f));
			}
		}

		//
		// Game name animation
		// #################################################################
		//
		// Every some specified seconds animate the game name
		// Shake effect
		//
		if (getSecondsTime() > gameNameAnimationTimer)
		{
			if (gameName != null) {

				// Create Scale>Shake>BackToNormal effect
				EffectCreator.create_SC_SHK_BTN(gameName, 1.1f, 1.1f, 8f, 0, 0.11f, null, false);

				// Renew timer for next effect
				gameNameAnimationTimer = getSecondsTime() + GAME_NAME_LOOP_ANIMATION_TIME;
			}
		}
	}

	private void setUpSwipeListener()
	{
		getStage().addListener(new ActorGestureListener()
		{
			// Touch Down an actor
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				super.touchDown(event, x, y, pointer, button);

				// Set touch down initials for input intent
				// ########################################
				inputIntent.setTouchInitials(x, y);
			}

			// Pan/Drag an actor
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX,float deltaY)
			{
				super.pan(event, x, y, deltaX, deltaY);

				// Set touch currents
				// ########################################
				inputIntent.setTouchCurrents(x, y);

				//
				// Swipe/Drag detection
				// ########################################
				if (inputIntent.getDirectionIntent() == InputIntent.DirectionIntent.TOUCH_D_UP) {

					// if swipe up confirmed, send menu away and get
					// instructions
					if (isMenuActive & isSplashCompleted) {
						if (inputIntent.isTouchDragInterval()) {

							// Reset all actor actions
							resetMenuElementsActions();

							jungleMainMenuScreenEnvironment.sendAwayGameName();
							jungleMainMenuScreenButtons.sendAwaySocialButtons();
							jungleMainMenuScreenButtons.sendAwayMainMenuButtons();
							jungleMainMenuScreenButtons.sendInSwipeForMenu();
							jungleMainMenuScreenButtons.sendAwaySwipeForInstructions();
							jungleMainMenuScreenInstructions.sendInInstructions();

							isMenuActive = false;
						}
					}
				}

				else if (inputIntent.getDirectionIntent() == InputIntent.DirectionIntent.TOUCH_D_DOWN) {

					// if swipe down confirmed, send instructions away, get menu
					if (!isMenuActive && isSplashCompleted)
					{
						if (inputIntent.isTouchDragInterval())
						{
							// Reset all actor actions
							resetMenuElementsActions();
							//
							jungleMainMenuScreenEnvironment.sendInGameName();
							jungleMainMenuScreenButtons.sendInSocialButtons();
							jungleMainMenuScreenButtons.sendInMainMenuButtons();
							jungleMainMenuScreenButtons.sendAwaySwipeForMenu();
							jungleMainMenuScreenButtons.sendInSwipeForInstruction();
							jungleMainMenuScreenInstructions.sendAwayInstructions();

							isMenuActive = true;
						}
					}
				}
			}

			// Touch Up an actor
			@Override
			public void touchUp(InputEvent event, float x, float y,int pointer, int button)
			{
				super.touchUp(event, x, y, pointer, button);

				// Reset after touch up
				// ########################################
				inputIntent.reset();
			}
		});
	}

	private void resetMenuElementsActions() {
		gameName.clearActions();
		btnPlay.clearActions();
		btnScores.clearActions();
		btnSettings.clearActions();
		btnSocialFacebook.clearActions();
		btnSocialTwitter.clearActions();
		btnSocialGoogle.clearActions();
		btnSwipeForMenu.clearActions();
		btnSwipeForInstructions.clearActions();
	}

	@Override
	public void setUpInfoPanel()
	{

	}
}
