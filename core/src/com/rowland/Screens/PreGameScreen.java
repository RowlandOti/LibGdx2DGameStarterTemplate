package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.magnetideas.helpers.MyAbstractScreen;
import com.moribitotech.mtx.input.InputIntent;
import com.moribitotech.mtx.interfaces.IScreen;
import com.moribitotech.mtx.scene2d.models.EmptyActorLight;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.ScreenHelpers.GameScreenInstructions;
import com.rowland.ScreenHelpers.PreGameScreenButtons;
import com.rowland.UI.MadBoyGameButton;

/**
 * @author Rowland
 *
 */
public class PreGameScreen extends MyAbstractScreen implements IScreen {

	public EmptyActorLight instructions;
	public MadBoyGameButton btnOk, btnCancel, btnSwipeForMenu, btnSwipeForInstructions;
	public static TextureRegion button_overlay_left, button_overlay_right, img_obj_rectangle, img_obj_swipe_down_menu, img_obj_swipe_up_instructions;
	public TableModel menuTable;

	// Swipe controls
	InputIntent inputIntent;
	float touchDragInterval;
	// Splash, Menu, Instruction activeness management
	private boolean isMenuActive;

	// Pre Game Screen element managers
	// These creates and animates the above screen elements

	GameScreenInstructions  gameScreenGameInstructionMenu;
	PreGameScreenButtons preGameScreenButtons;

	public PreGameScreen(Game game, String screenName)
	{
		super(game, screenName);
		initScreenAssets();
		setUpScreenElements();
		setUpMenu();
		setUpSwipeListener();
		preGameScreenButtons.sendInSwipeForInstruction();
		preGameScreenButtons.sendInMenuButtons();
	}
	private void initScreenAssets()
	{
		//Getting previously loaded atlas
        atlas = getMyGame().getManager().get(LoadingScreen.UI_INSTRUCTION, TextureAtlas.class);
        atlas_base = getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

        img_obj_rectangle = atlas.findRegion("img_obj_rectangle");
        img_obj_swipe_down_menu = atlas.findRegion("img_obj_swipe_down_menu");
		img_obj_swipe_up_instructions = atlas.findRegion("img_obj_swipe_up_instructions");
		button_overlay_left = atlas.findRegion("button_overlay_left");
		button_overlay_right = atlas.findRegion("button_overlay_right");
	}
	@Override
	public void setUpScreenElements()
	{

		isMenuActive = true;

		//
		// Set background image
		// #################################################################
		//setBackgroundTexture(img_obj_rectangle);

		// InputIntent for swipes/drags
		// #################################################################
		inputIntent = new InputIntent();
		touchDragInterval = AppSettings.SCREEN_H / 3.0f;
		inputIntent.setTouchDragIntervalRange(touchDragInterval);

		//
		// Construct Screen element managers
		// #################################################################
		preGameScreenButtons = new PreGameScreenButtons(this);
		gameScreenGameInstructionMenu = new GameScreenInstructions(this);

	}

	@Override
	public void setUpMenu()
	{
		preGameScreenButtons.setUpMenuButtons();
		preGameScreenButtons.setUpSwipeButtons();
		gameScreenGameInstructionMenu.setUpInstructions();

	}

	@Override
	public void setUpInfoPanel()
	{


	}

	@Override
	public void render(float delta)
	{
		super.render(delta);
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
				if (inputIntent.getDirectionIntent() == InputIntent.DirectionIntent.TOUCH_D_UP)
				{

					// if swipe up confirmed, send menu away and get
					// instructions
					if (isMenuActive)
					{
						if (inputIntent.isTouchDragInterval())
						{

							// Reset all Actor actions
							resetMenuElementsActions();

							preGameScreenButtons.sendAwayMenuButtons();
							preGameScreenButtons.sendInSwipeForMenu();
							preGameScreenButtons.sendAwaySwipeForInstructions();
							gameScreenGameInstructionMenu.sendInInstructions();

							isMenuActive = false;
						}
					}
				}

				else if (inputIntent.getDirectionIntent() == InputIntent.DirectionIntent.TOUCH_D_DOWN)
				{

					// if swipe down confirmed, send instructions away, get menu
					if (!isMenuActive)
					  {
						if (inputIntent.isTouchDragInterval())
						{
							//
							// Reset all actor actions
							resetMenuElementsActions();

							preGameScreenButtons.sendAwaySwipeForMenu();
							preGameScreenButtons.sendInMenuButtons();
							preGameScreenButtons.sendInSwipeForInstruction();
							gameScreenGameInstructionMenu.sendAwayInstructions();

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

		btnSwipeForMenu.clearActions();
		btnSwipeForInstructions.clearActions();
		btnOk.clearActions();
		btnCancel.clearActions();
	}

}
