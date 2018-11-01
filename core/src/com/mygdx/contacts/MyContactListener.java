package com.mygdx.contacts;
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
		else //timmy colliding with rain 
		{
			RainDrop rain = (RainDrop)body1;
			rain.startContact();
		}
		
	}

	@Override
	public void endContact(Contact contact) 
	{
		
		
		
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
