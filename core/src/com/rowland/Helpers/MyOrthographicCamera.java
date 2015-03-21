package com.rowland.Helpers;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
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

	public void setCameraToPlayer(float x, float y, float z, TweenManager tweenManager)
	{
		Tween.to(this, OrthographicCameraAccessor.POSITION, 0.2f).target(x,y,z).ease(TweenEquations.easeInOutQuad).start(tweenManager);
	}


	public void setPosition(Vector3 newPos)
	{
		position.x = newPos.x;
		position.y = newPos.y;
		position.z = newPos.z;
	}

	public void setPosition(float x, float y)
	{
		setPosition(x,y,0);
	}

	public void setPosition(float x, float y, float z)
	{
		setPosition(new Vector3(x, y, z));
		fixBounds();
	}

	public void setZoom(float zoomValue)
	{
		float maxZoom = 1.3f;
		float minZoom = GameWorld.WORLD_WIDTH/viewportWidth;

		zoom = MathUtils.clamp(zoomValue, minZoom, maxZoom);
		//fixBounds();
	}

	public void fixBounds()
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

	@Override
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
			    .beginParallel()
			    .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 3.5f).target(spanCord.x,spanCord.y,spanCord.z).ease(Elastic.OUT))
                //.push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 3.5f).target(1.20f).ease(Elastic.OUT))
                .end()

                .beginParallel()
                .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 2.8f).target(world.getYoyo().position.x,0,0).ease(Bounce.INOUT))
                //.push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 3.0f).target(1).ease(Bounce.INOUT))
                .end()
				.start(tweenManager);

		/*Timeline.createSequence()
	    .beginParallel()
	    .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 3.5f).target(spanCord.x,spanCord.y,spanCord.z).ease(TweenEquations.easeInOutQuad))
        //.push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 3.5f).target(1.10f).ease(TweenEquations.easeInOutQuad))
        .end()

        .beginParallel()
        .push(Tween.to(this, OrthographicCameraAccessor.POSITION, 2.8f).target(world.getYoyo().position.x,0,0).ease(TweenEquations.easeInOutQuad))
        //.push(Tween.to(this, OrthographicCameraAccessor.ZOOM, 3.0f).target(1).ease(TweenEquations.easeInOutQuad))
        .end()
		.start(tweenManager);*/

			//world.isPanning = false;
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
