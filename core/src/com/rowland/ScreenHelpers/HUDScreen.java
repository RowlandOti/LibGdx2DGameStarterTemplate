package com.rowland.ScreenHelpers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameObjects.Yoyo;
import com.rowland.Screens.GameScreen;
import com.rowland.Screens.LoadingScreen;

/**
 * Created by Rowland on 12/30/2016.
 */
public class HUDScreen {

    private GameScreen gameScreen;
    private TextureAtlas atlas, atlas_base;
    // Player Object
    private Yoyo yoyo;
    // Health
    private TextureRegion health_bar, health_bar_drawable;
    // Controls
    private TextureRegion button_overlay_left, button_overlay_right, button_overlay_jump, button_overlay_pause;
    // Control - Button size
    private float buttonSize = 100 * AppSettings.getWorldSizeRatio();

    public HUDScreen(final GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.yoyo = gameScreen.getWorld().getYoyo();
        initScreenAssets();
    }

    private void initScreenAssets() {
        // Getting previously loaded atlas
        atlas = gameScreen.getMyGame().getManager().get(LoadingScreen.GAME_ATLAS, TextureAtlas.class);
        atlas_base = gameScreen.getMyGame().getManager().get(LoadingScreen.BASE_ATLAS, TextureAtlas.class);

        // Health
        health_bar = atlas.findRegion("health_bar");
        health_bar_drawable = atlas.findRegion("health_bar_drawable");
        // Controls
        button_overlay_left = atlas_base.findRegion("nav_left");
        button_overlay_right = atlas_base.findRegion("nav_right");
        button_overlay_jump = button_overlay_right;
        button_overlay_pause = atlas_base.findRegion("control_pause");
    }

    public void updateHUD() {

        boolean left = false;
        boolean right = false;
        boolean jump = false;
        boolean pause = false;

        Batch batch = gameScreen.getStage().getBatch();

        batch.begin();
        // Draw the Health Bar
        batch.draw(health_bar, 10f, AppSettings.SCREEN_H - health_bar.getRegionHeight(), 0f, 0f, health_bar.getRegionWidth(), health_bar.getRegionHeight(), 1f, 1f, 0f);
        batch.draw(health_bar_drawable, 47.5f, (AppSettings.SCREEN_H - 24.0f), 0f, 0f, health_bar_drawable.getRegionWidth(), health_bar_drawable.getRegionHeight(), 1f, 1f, 0f);

        // Draw the game control UI only on Android and iOs devices if the game is running
        if ((Gdx.app.getType() == Application.ApplicationType.Android) || (Gdx.app.getType() == Application.ApplicationType.iOS)) {
            Color c = batch.getColor();
            batch.setColor(c.r, c.g, c.g, 0.35f);

            batch.draw(button_overlay_left, 0f, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
            batch.draw(button_overlay_right, 1.2f * buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0f);
            batch.draw(button_overlay_jump, AppSettings.SCREEN_W - 0.03f * buttonSize, 0f, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 90);
            batch.draw(button_overlay_pause, AppSettings.SCREEN_W - buttonSize, AppSettings.SCREEN_H - buttonSize, 0f, 0f, buttonSize, buttonSize, 1f, 1f, 0);

            batch.setColor(c.r, c.g, c.g, 1.0f);

            for (int i = 0; i < 2; i++) {
                int x = (int) (Gdx.input.getX(i) / (float) Gdx.graphics.getWidth() * AppSettings.SCREEN_W);
                int y = (int) (Gdx.input.getY(i) / (float) Gdx.graphics.getHeight() * AppSettings.SCREEN_H);
                if (!Gdx.input.isTouched(i)) continue;

                if (y <= AppSettings.SCREEN_H && y >= AppSettings.SCREEN_H - buttonSize) {
                    if (x <= buttonSize) {
                        left |= true;
                    }
                    if (x > 1.2f * buttonSize && x <= 2.2f * buttonSize) {
                        right |= true;
                    }
                    if (x >= AppSettings.SCREEN_W - buttonSize && x < AppSettings.SCREEN_W) {
                        jump |= true;
                    }
                }
                if (x <= AppSettings.SCREEN_W && y <= buttonSize) {
                    if (x >= AppSettings.SCREEN_W - buttonSize)
                        pause |= true;
                }
            }
        }
        batch.end();

        //CheckUser input and apply to velocity and states of the main player
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) && yoyo.grounded) || (jump && yoyo.grounded)) {
            yoyo.velocity.y += yoyo.JUMP_VELOCITY;
            yoyo.setState(Yoyo.JUMP);
            yoyo.grounded = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || left) {
            yoyo.velocity.x = -yoyo.MAX_VELOCITY;
            if (yoyo.grounded)
                yoyo.setState(Yoyo.WALK);
            yoyo.facesRight = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || right) {
            yoyo.velocity.x = yoyo.MAX_VELOCITY;
            if (yoyo.grounded)
                yoyo.setState(Yoyo.WALK);
            yoyo.facesRight = true;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.P) || pause) {
            gameScreen.pause();
        }
    }
}