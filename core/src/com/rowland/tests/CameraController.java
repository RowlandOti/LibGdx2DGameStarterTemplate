package com.rowland.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * @author Rowland
 *
 */
public class CameraController extends InputAdapter 
{
	OrthographicCamera camera = null;

	private final Vector3 current = new Vector3();
	private final Vector3 mouse = new Vector3();
	private final Vector3 delta = new Vector3();
	private final Vector3 last = new Vector3(-1, -1, -1);
	//hold our zoom boundaries for us. I just went with two hardcoded values that I found worked out quite well in practise. But you’re free to experiment around.
	// x = min, y = max 
	private  Vector2 zoomBounds = new Vector2(1f, 1.75f);

	public CameraController(OrthographicCamera camera) 
	{
       this.camera = camera;
	}
	
	@Override
	  public boolean mouseMoved(int screenX, int screenY) 
	{
		mouse.set(screenX, screenY, 0);
		Gdx.app.log("GESTURE", "Mouse Moved");
		
		return false;
	  }

	  @Override
	  public boolean scrolled(int amount) 
	  {
		// This is a general syntax that can be used, a shortened if-statement:
		// (a < b ? 1 : 2)
		// means: "Is a smaller than b? If that's the case return 1, if not, return 2"
		float newZoom = camera.zoom * (1 + (amount < 0 ? 0.1f : -0.1f));
		//This means that the screen will zoom towards the mouse, like in any decent game
		changeZoom(newZoom, mouse.x, mouse.y);
		//If you wanted it to zoom towards the center of the screen you could use this.
		//changeZoom(newZoom, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		Gdx.app.log("GESTURE", "Game Screen Scrolled , zooming");
		
		return true;
	  }

	public void changeZoom(float zoom, float x, float y) 
	{
		Vector3 before = new Vector3(x, y, 0);
		camera.unproject(before);

		if (zoom <= zoomBounds.x || zoom >= zoomBounds.y) 
		{
			return;
		}
		
		camera.zoom = zoom;
		camera.update();
		Vector3 after = new Vector3(x, y, 0);
		camera.unproject(after);

		camera.translate(before.x - after.x, before.y - after.y, 0);
		
	}
	 
	  @Override
	  public boolean touchUp(int x, int y, int pointer, int button) 
	  {
		last.set(-1, -1, -1);
		
		return false;
	  }
	  
	@Override
	public boolean touchDragged(int x, int y, int pointer) 
	{
		camera.unproject(current.set(x, y, 0));

		if (!(last.x == -1 && last.y == -1 && last.z == -1)) 
		{
			camera.unproject(delta.set(last.x, last.y, 0));
			delta.sub(current);
			camera.position.add(delta.x, delta.y, 0);
		}
		
		last.set(x, y, 0);
		Gdx.app.log("GESTURE", "Game touch dragged");
		
		return false;
	}
	 
	public void update() 
	{
		camera.position.x = MathUtils.clamp(camera.position.x, (Gdx.graphics.getWidth() * camera.zoom) / 2, 1024 - (Gdx.graphics.getWidth() * camera.zoom) / 2);
		camera.position.y = MathUtils.clamp(camera.position.y, (Gdx.graphics.getHeight() * camera.zoom) / 2, 1024 - (Gdx.graphics.getHeight() * camera.zoom) / 2);
		camera.update();
	}

	}
