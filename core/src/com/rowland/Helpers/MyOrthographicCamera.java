package com.rowland.Helpers;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.magnetideas.smoothcam.SmoothCamAccessor;
import com.magnetideas.smoothcam.SmoothCamWorld;
import com.rowland.GameWorld.GameWorld;
import com.rowland.TweenAccessors.OrthographicCameraAccessor;

/**
 * @author Rowland
 *
 */
public class MyOrthographicCamera extends OrthographicCamera {

	private float xmin, xmax, ymin, ymax;
	public MyOrthographicCamera()
	{

	}

	public MyOrthographicCamera(float viewportWidth, float viewportHeight)
	{
		super(viewportWidth, viewportHeight);

	}


	public MyOrthographicCamera(float viewportWidth, float viewportHeight, float xmin, float xmax, float ymin, float ymax)
	{
		super(viewportWidth, viewportHeight);

		setWorldBounds(xmin, xmax, ymin, ymax);
	}

	public void setWorldBounds(float xmin, float xmax, float ymin, float ymax)
	{
		this.xmin = xmin;
		this.xmax = xmax;
	    this.ymin = ymin;
		this.ymax = ymax;
	}

	public void setPosition(float x, float y)
	{
		setPosition(x,y,0);
	}
	public void setPosition(Vector3 position)
	{
		position.set(position);
	}

	public void setPosition(float x, float y, int z)
	{
		position.set(x,y,z);
		fixBounds();

	}

	public void setZoom(float zoomValue)
	{
		float maxZoom = 1.4f;
		float minZoom = GameWorld.WORLD_WIDTH/viewportWidth;

		zoom = MathUtils.clamp(zoomValue, minZoom, maxZoom);
		fixBounds();
	}

	private void fixBounds()
	{
		float scaledViewportWidth =viewportWidth * zoom;
		float scaledViewportHeight = viewportHeight * zoom;

		if(position.x < xmin + scaledViewportWidth/2)
		{
			position.x = xmin + scaledViewportWidth/2;
		}

		if(position.x > xmax + scaledViewportWidth/2)
		{
			position.x = xmax - scaledViewportWidth/2;
		}

		if(position.y < ymin + scaledViewportHeight/2)
		{
			position.y = ymin + scaledViewportHeight/2;
		}

		if(position.y > ymax + scaledViewportHeight/2)
		{
			position.y = ymax - scaledViewportHeight/2;
		}
	}

	public void translate(float x, float y)
	{
		super.translate(x,y);
	}

	public void zoomSafe(TweenManager tweenManager, float newZoomAmount)
	{
		Tween.to(this, OrthographicCameraAccessor.ZOOM, 0.8f).target(newZoomAmount).ease(TweenEquations.easeInOutQuad).start(tweenManager);
	}

	public void translateSafe(Vector3 spanCord, TweenManager tweenManager)
	{
		Tween.to(this, OrthographicCameraAccessor.POSITION, 0.8f).target(spanCord.x,spanCord.y,spanCord.z).ease(TweenEquations.easeInOutQuad).start(tweenManager);
	}

	public void translateSafe(Vector3 spanCord, TweenManager tweenManager, GameWorld world)
	{
		panZoom(spanCord, tweenManager, world);
	}

	public void translateSafe(Vector3 spanCord)
	{
		translate(spanCord.x,spanCord.y);
		update();
		//fixBounds();
		//update();
	}

	private void panZoom(Vector3 spanCord, TweenManager tweenManager, GameWorld world)
	{
			/*
			 * Example Tween-Sequence: Zoom to 120%, Pan to point of interest #1 (0, -50), Wait 1 second, Pan back to the
			 * starting position, Zoom back to the initial value
			 */
			Timeline.createSequence()
			    .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 3.5f).target(spanCord.x,spanCord.y,spanCord.z).ease(Elastic.INOUT)).pushPause(1.0f)
			    .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 4.5f).target(world.getYoyo().position.x,0,0).ease(Elastic.INOUT))

			    .beginParallel()
                .push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 3.5f).target(1.26f).ease(Quad.OUT).start(tweenManager)).pushPause(3.0f).pushPause(1.0f)
                .end()

                .push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 4.0f).target(1).ease(Elastic.INOUT).start(tweenManager))
				.start(tweenManager);
	}




	public float calcZoom(float initialDistance, float distance)
	{
		float nextZoom;

		if(initialDistance < distance)
		{
			float ratio = (initialDistance/distance)/10;
			nextZoom = zoomIn(ratio);
		}
		else
		{
			float ratio = (distance/initialDistance)/10;
			nextZoom = zoomOut(ratio);
		}

		return nextZoom;
	}

	private float zoomIn(float ratio)
	{
		float nextZoom = zoom - ratio;

		return nextZoom;
	}

	private float zoomOut(float ratio)
	{
		float nextZoom = zoom + ratio;

		return nextZoom;
	}

}
