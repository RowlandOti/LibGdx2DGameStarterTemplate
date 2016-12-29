package com.rowland.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.magnetideas.smoothcam.SmoothCamSubject;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameWorld.GameWorld;

public class Sprite extends SmoothCamSubject {

    public final Vector2 position;
    public final Vector2 velocity;

    public Sprite(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
    }

    public void update(float deltaTime) {
        //Don't let the Sprite visit places where there is no map
        if (position.x <= 0) {
            position.x = 0;
        } else if (position.x >= GameWorld.mapWidth - 2) {
            position.x = GameWorld.mapWidth - 2;
        }


        //This was needed for two way scrolling
        if (position.y <= -AppSettings.SCREEN_H / 3) {
            position.y = -AppSettings.SCREEN_H / 3;
        } else if (position.y >= GameWorld.mapHeight) {
            position.y = GameWorld.mapHeight;
        }
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }
}