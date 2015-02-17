package com.rowland.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.scene2d.models.EmptyActor;
import com.moribitotech.mtx.scene2d.ui.ButtonGame;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
import com.moribitotech.mtx.scene2d.ui.TableModel;
import com.moribitotech.mtx.screen.AbstractScreen;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.Helpers.AssetLoader;

public class CreditsScreen extends AbstractScreen implements Screen{

	 private EmptyActor testActor;
	 private TableModel tableMenu;

	 public CreditsScreen(Game game, String screenName) {
		super(game, screenName);

		// TODO Auto-generated constructor stub
		setUpScreenElements();
		setUpInfoPanel();
		setUpActors();
		setUpMenu();
	}

	private void setUpMenu() {
		// TODO Auto-generated method stub


	    testActor = new EmptyActor(AssetLoader.holder.getRegionWidth(), AssetLoader.holder.getRegionHeight(), true);
		testActor.setTextureRegion(AssetLoader.holder, true);

		tableMenu = new TableModel(null, AppSettings.WORLD_WIDTH , AppSettings.WORLD_HEIGHT);
		tableMenu.defaults().space(100);
		tableMenu.add(testActor);

         // put the table inside a scrollpane
         ScrollPane scrollPane = new ScrollPane(tableMenu);
         scrollPane.setBounds(0, 0, AppSettings.SCREEN_W, AppSettings.SCREEN_H-80);
         scrollPane.setScrollingDisabled(true, false);
         scrollPane.setOverscroll(false, false);
         scrollPane.invalidate();
         getStage().addActor(scrollPane);
	}

	private void setUpActors() {
		// TODO Auto-generated method stub

	}

	private void setUpInfoPanel() {
		// Add the Back Button
		// Back Button
				final ButtonGame btnBack = MenuCreator.createCustomGameButton(null, AssetLoader.button_overlay_left, AssetLoader.button_overlay_left, 64, 64, true);
				btnBack.setPosition(AppSettings.WORLD_WIDTH - btnBack.getWidth(), AppSettings.WORLD_HEIGHT - btnBack.getHeight());
				btnBack.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						tableMenu.addAction(Actions.moveTo(0, -tableMenu.getHeight(), 0.3f));
						getGame().setScreen(new MenuScreen(getGame(), "MainMenu"));
					}
				});

			getStage().addActor(btnBack);
			}

	private void setUpScreenElements() {
		// SET UP THE BACKGROUND AND MAKE THE BACK BUTTON ACTIVE
		setBackgroundTexture(AssetLoader.background_home);
		setBackButtonActive(true);
	}

     @Override
     public void render(float delta) {
    	 getStage().act(delta);
         getStage().draw();
     }

}
