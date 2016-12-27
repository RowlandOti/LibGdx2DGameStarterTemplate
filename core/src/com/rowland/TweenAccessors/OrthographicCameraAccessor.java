package com.rowland.TweenAccessors;

import com.badlogic.gdx.Gdx;
import com.rowland.Helpers.MyOrthographicCamera;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * @author Rowland
 *
 */
public class OrthographicCameraAccessor implements TweenAccessor<MyOrthographicCamera> {

	/** Tween position */
	public static final int POSITION = 1;
	/** Tween zoom */
	public static final int ZOOM = 2;

	/**
	 * @param camera
	 * camera to get values from
	 * @param tweenType
	 * type of tween (Position or Zoom)
	 * @param returnValues
	 * out parameter with the requested values
	 */
	@Override
	public int getValues(MyOrthographicCamera camera, int tweenType, float[] returnValues)
	{
		switch (tweenType)
		{
			case POSITION:
				returnValues[0] = camera.position.x;
				returnValues[1] = camera.position.y;
				returnValues[2] = camera.position.z;
				return 3;
			case ZOOM:
				returnValues[0] = camera.zoom;
				return 1;
			default:
				return 0;
		}
	}

	/**
	 * @param camera
	 *  camera whose some values are going to be set
	 * @param tweenType
	 * Position or Zoom
	 * @param newValues
	 *  array containing the new values to configure the camera
	 */
	@Override
	public void setValues(MyOrthographicCamera camera, int tweenType, float[] newValues)
	{
		switch (tweenType)
		{
			case POSITION:
				camera.setPosition(newValues[0], 0, 0);
				camera.update();
				break;
			case ZOOM:
				camera.setZoom(camera.zoom = newValues[0]);
				camera.setPosition(camera.position.x, camera.position.y);
			    camera.update();

			    Gdx.app.log("CAMERA", "Clamped Zoom" +camera.zoom);
				break;
			default:
				break;
		}
	}
}
