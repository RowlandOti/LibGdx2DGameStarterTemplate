package com.magnetideas.tweenaccessors;

/**
 * @author Rowland
 *
 */
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.TweenAccessor;

public class ActorAccessor implements TweenAccessor<Actor> {
	
	public static final int POS_XY = 1;
	public static final int CPOS_XY = 2;
	public static final int SCALE_XY = 3;
	public static final int ROTATION = 4;
	public static final int OPACITY = 5;
	public static final int TINT = 6;

	@Override
	public int getValues(Actor target, int tweenType, float[] returnValues) 
	{
		switch (tweenType) {
		case POS_XY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;

		case CPOS_XY:
			returnValues[0] = target.getX() + target.getWidth() * .5f;
			returnValues[1] = target.getY() + target.getHeight() * .5f;
			return 2;

		case SCALE_XY:
			returnValues[0] = target.getScaleX();
			returnValues[1] = target.getScaleY();
			return 2;

		case ROTATION:
			returnValues[0] = target.getRotation();
			return 1;

		case OPACITY:
			returnValues[0] = target.getColor().a;
			return 1;

		case TINT:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
			return 3;

		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Actor target, int tweenType, float[] newValues) 
	{
		switch (tweenType) {

		case POS_XY:
			target.setPosition(newValues[0], newValues[1]);
			break;

		case CPOS_XY:
			target.setPosition(newValues[0] - target.getWidth() * .5f,
					newValues[1] - target.getHeight() * .5f);
			break;

		case SCALE_XY:
			target.setScale(newValues[0], newValues[1]);
			break;

		case ROTATION:
			target.setRotation(newValues[0]);
			break;

		case OPACITY:
			Color c = target.getColor();
			c.set(c.r, c.g, c.b, newValues[0]);
			target.setColor(c);
			break;

		case TINT:
			c = target.getColor();
			c.set(newValues[0], newValues[1], newValues[2], c.a);
			target.setColor(c);
			break;

		default:
			assert false;
		}
	}
}
