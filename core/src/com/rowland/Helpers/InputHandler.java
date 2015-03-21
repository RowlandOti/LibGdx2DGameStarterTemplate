package com.rowland.Helpers;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.rowland.GameWorld.GameWorld;

public class InputHandler implements InputProcessor, GestureListener {

	private MyOrthographicCamera camera;
	private TweenManager tweenManager;
	private float nextZoom;
	private GameWorld world;

	public InputHandler(OrthographicCamera camera, TweenManager tweenManger, GameWorld world)
	{
		super();
		this.tweenManager = tweenManger;
		this.camera =  (MyOrthographicCamera) camera;
		this.world = world;
		this.nextZoom = 0f;
	}

	public InputHandler() {

	}

	//Implementation for my GestureListener begins here
	@Override
	public boolean keyDown(int keycode)
	{

		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{

		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{

		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		// Zoom Out
		if (amount > 0) {
			camera.zoom += 0.1f;
		}
		// Zoom In
		if (amount < 0) {
			camera.zoom -= 0.1f;
		}

		camera.setZoom(camera.zoom);
		camera.setPosition(camera.position.x, camera.position.y);
		camera.update();

		Gdx.app.log("GESTURE", "Scrolled Zoom Event Occurred");

		return true;
	}
    //Implementation for my GestureListener begins here
	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		Gdx.app.log("GESTURE", "Game Screen Touched Down");

		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		Gdx.app.log("GESTURE", "Game Screen Tapped" + Integer.toString(button));

		return false;
	}

	@Override
	public boolean longPress(float x, float y)
	{
		Gdx.app.log("GESTURE", "Game Screen LongPressed");

		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		Gdx.app.log("GESTURE", "Game Screen Flinged");

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		Vector3 spanCord = new Vector3(x, 0, 0);
		camera.unproject(spanCord);
	    camera.translateSafe(spanCord, tweenManager, world);

		Gdx.app.log("GESTURE", "Game Screen Panned");

		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		nextZoom = camera.calcZoom(initialDistance, distance);

		camera.zoomSafe(tweenManager, nextZoom);

		Gdx.app.log("GESTURE", "Game Screen Zoomed by" +(nextZoom));

		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		Gdx.app.log("GESTURE", "Game Screen Pinched");

		return false;
	}

	public void update() {

	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {

		return false;
	}

}
