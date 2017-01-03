package com.rowland.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.rowland.GameWorld.GameWorld;



public class Yoyo extends Sprite

{

	public float MAX_VELOCITY = 10f;
	public float JUMP_VELOCITY = 30f;
	public static float DAMPING = 0.87f;

	public static final int JUMP = 2;
	public static final int WALK = 1;
    public static final int STILL = 0;

    public boolean visible;

    public static final float width = 80f * GameWorld.WORLD_UNIT;
    public static final float height = 80f *323/498 * GameWorld.WORLD_UNIT;
    public boolean hasKey;

    private int state;
    public int health;
	public float stateTime;

	//This state will be used during the drawing to decided whether to invert horizontally or not
	public boolean facesRight;
	public boolean grounded;

	public void setState(int currentState){
		this.state = currentState;
		//stateTime =0;
	}

	public int getState(){
		return this.state;
	}

	public Yoyo(float x, float y)

	{
		super(x, y);
		state = STILL;
		stateTime = 0;
		//make this decision in game screen
		facesRight = true;
		grounded = false;
	    visible = true;
	    health = 88;
	    hasKey = false;
	}

	@Override
    public void update(float deltaTime)
	{
		super.update(deltaTime);
		if(stateTime>=50)
			stateTime =0;
    }

    public Rectangle getBounds()
    {
    	return new Rectangle(position.x, position.y, width, height);
    }


}
