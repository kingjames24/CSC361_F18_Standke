package com.mygdx.contacts;
import com.mygdx.objects.*;
import com.mygdx.objects.Raindrops.RainDrop;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;


public class MyContactListener implements ContactListener
{
	
	
	@Override
	public void beginContact(Contact contact) 
	{
		Object body=contact.getFixtureA().getBody().getUserData(); 
		Object body1=contact.getFixtureB().getBody().getUserData(); 
		if(body != null)
		{
			if(body instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body;
				rain.startContact();
				//rain.EndContact();
			}
			
		}
		if(body1 != null)
		{
			if(body1 instanceof Raindrops.RainDrop)
			{
				RainDrop rain = (RainDrop)body1;
				rain.startContact();
				//rain.EndContact();
			}
			
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
