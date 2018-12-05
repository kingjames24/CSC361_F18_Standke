package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Abstract Class that all game objects extend to have 
 * access to common  and methods used in the game
 * @author adam
 *
 */
public abstract class AbstractGameObject 
{
	
		public Vector2 position;
		public Vector2 dimension;
		public Vector2 origin;
		public Vector2 scale;
		
		public float stateTime=0;
		public Animation animation;
		public float rotation;
		public Body body;
		
		/**
		 * Abstract method that each subclass implements in their own 
		 * way. Used to create the box2d bodies for objects in the game. 
		 * @param position an object of the Vector2 class that represents the (x,y) position of 
		 * an object in view coordinate space
		 */
		public abstract void createBody(Vector2 position); 
		
		public void update(float deltaTime)
		{
			stateTime+=deltaTime; 
		}
		/*
		 * Starts the animation time
		 */
		public void setAnimation (Animation animation)
		{
			this.animation = animation;
			 
			
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
		/**
		 * Abstract method implemented by each sub-class 
		 * @param batch represents a set of vertex points/positions to render the current frame(sent to GPU)
		 */
		public abstract void render(SpriteBatch batch);
}
