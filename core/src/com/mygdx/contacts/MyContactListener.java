package com.mygdx.contacts;
import com.mygdx.game.WorldController;
import com.mygdx.objects.*;
import com.mygdx.objects.Raindrops.RainDrop;



import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;


public class MyContactListener implements ContactListener
{
	 
	
	@Override
	public void beginContact(Contact contact) 
	{
		Object body=contact.getFixtureA().getBody().getUserData(); 
		Object body1=contact.getFixtureB().getBody().getUserData(); 
		if(body1 != null && body==null)
		{
			if(body1 instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body1;
				rain.startContact();
				
			}
			
		}
		else if(body != null && body1==null)
		{
			if(body instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body;
				rain.startContact();
				
			}
		}
		else  
		{
			if(body1 instanceof Raindrops.RainDrop && body instanceof Raindrops.RainDrop)//rain colliding with rain
			{
				return; 
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Points || body1 instanceof Points && body instanceof Raindrops.RainDrop)
			{
				return; 
			}
			else if(body1 instanceof Raindrops.RainDrop && body instanceof Star || body1 instanceof Star && body instanceof Raindrops.RainDrop)
			{
				return; 
			}
			else if(body1 instanceof Timmy && body instanceof Points || body1 instanceof Points && body instanceof Timmy)
			{
				if(body1 instanceof Points)
				{
					
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
					
					Star up = (Star)body1;
					up.startContract();
				}
				else
				{
					Star up = (Star)body;
					up.startContract();
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
