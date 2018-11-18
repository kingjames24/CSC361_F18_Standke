package com.mygdx.contacts;
import com.mygdx.game.WorldController;

import com.mygdx.objects.*;
import com.mygdx.objects.Raindrops.RainDrop;



import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

/**
 * Class that implements Box2d's ContactListener feature 
 * by handling/resolving collision events that happen in the
 * game  
 * @author adam
 *
 */
public class MyContactListener implements ContactListener
{
	 
	/**
	 * Method that is called when a collision has just started between
	 * bodies in the game. The method receives the beginning contact points
	 * of the two bodies and determines what type of bodies just collided. If 
	 * a dynamic body collided with a static/kinematic body(which box2d considers null) the 
	 * body is flagged for removal after exiting the Box2d simulation. If a dynamic
	 * body collided with another dynamic body the method will determine what should happen
	 * next in the game.     
	 */
	@Override
	public void beginContact(Contact contact) 
	{
		Object body=contact.getFixtureA().getBody().getUserData(); 
		Object body1=contact.getFixtureB().getBody().getUserData(); 
		if(body1 != null && body==null) //rain drop collides with a static object
		{
			if(body1 instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body1;
				rain.startContact();
				
			}
			
		}
		else if(body != null && body1==null)//rain drop collides with a static obejct
		{
			if(body instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body;
				rain.startContact();
				
			}
		}
		else  
		{
			if(body1 instanceof Raindrops.RainDrop && body instanceof Raindrops.RainDrop)
			{
				return; //rain drop colliding with another dynamic rain drop
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Points || body1 instanceof Points && body instanceof Raindrops.RainDrop)
			{
				return; //rain drop colliding with a dynamic point object 
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Star || body1 instanceof Star && body instanceof Raindrops.RainDrop)
			{
				return; //rain drop colliding with a dynamic star object 
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Ability || body1 instanceof Ability && body instanceof Raindrops.RainDrop)
			{
				if(body1 instanceof Raindrops.RainDrop)
				{
															//rain drop colliding with a dynamic ability object
					RainDrop rain = (RainDrop)body1;
					rain.startContact();
				}
				else
				{
					RainDrop rain = (RainDrop)body;
					rain.startContact();
				}
				 
			}
			else if(body1 instanceof Timmy && body instanceof Points || body1 instanceof Points && body instanceof Timmy)
			{
				if(body1 instanceof Points)
				{
														//Timmy colliding with a dynamic point object(ie., collected)
					Points point = (Points)body1;
					point.startContract();
				}
				else
				{
					Points point = (Points)body;
					point.startContract();
				}
			}
			else if(body1 instanceof Timmy && body instanceof Star || body1 instanceof Star && body instanceof Timmy)
			{
				if(body1 instanceof Star)
				{
													//Timmy colliding with a dynamic star object(ie., collected)
					Star up = (Star)body1;
					up.startContract();
				}
				else
				{
					Star up = (Star)body;
					up.startContract();
				}
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Timmy || body1 instanceof Timmy && body instanceof Raindrops.RainDrop)
			{
				if(body1 instanceof Timmy)
				{
													//Timmy colliding with a dynamic rain object(ie., hit)
					Timmy up = (Timmy)body1;
					up.startContract();
					RainDrop rain = (RainDrop)body;
					rain.startContact();
					
				}
				else
				{
					Timmy up = (Timmy)body;
					up.startContract();
					
					RainDrop rain = (RainDrop)body1;
					rain.startContact();
					
				} 
			}
			
		}
		
		if(body!=null && body instanceof Timmy)
		{
			WorldController.numFootContacts++; 
		}
		if (body1!=null && body1 instanceof Timmy)
		{
			WorldController.numFootContacts++; 
		}
		
	}

	/**
	 * Method that is called when a collision between two bodies has 
	 * been resolved/ended by the Box2d simulation. Currently, the method 
	 * is used to stop the player from jumping in the air by keeping
	 * track of the player on static surfaces(which in the game are platforms).   
	 */
	@Override
	public void endContact(Contact contact) 
	{
		Object body=contact.getFixtureA().getBody().getUserData(); 
		Object body1=contact.getFixtureB().getBody().getUserData(); 
		if(body!=null && body instanceof Timmy)
		{
			WorldController.numFootContacts--; 
		}
		if (body1!=null && body1 instanceof Timmy)
		{
			WorldController.numFootContacts--; 
		}
		
	}
		
	

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		
		
	}
	
	
	
	   
}
