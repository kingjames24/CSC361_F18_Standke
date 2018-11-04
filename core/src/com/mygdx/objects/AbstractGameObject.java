package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject 
{
	
		public Vector2 position;
		public Vector2 dimension;
		public Vector2 origin;
		public Vector2 scale;
		
		public float stateTime;
		public Animation animation;
		public float rotation;
		public Body body;
		
		
		
		
		
		
		public abstract void createBody(Vector2 position); 
		
		
		/*
		 * Starts the animation time
		 */
		public void setAnimation (Animation animation)
		{
			this.animation = animation;
			stateTime = 0;
		}
		
		/**
		 * All game objects should have instantiated 
		 * variables to work with.
		 */
		public AbstractGameObject()
		{
			position = new Vector2();
			dimension = new Vector2(1, 1);
			origin = new Vector2();
			scale = new Vector2(1, 1);
			rotation = 0;
			
			
		}
		
		public abstract void render(SpriteBatch batch);
}
