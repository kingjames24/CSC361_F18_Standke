package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;


public class Chopper extends AbstractGameObject
{

	private float length; 
	private TextureRegion chopper; 
	private Array<Helicopter> choppers; 
	
	
	private class Helicopter extends AbstractGameObject
	{
		private TextureRegion helicopter; 
			
		
		
		@Override
		public void render(SpriteBatch batch) 
		{
			TextureRegion reg = helicopter;
    		batch.draw(reg.getTexture(), position.x + origin.x,
    				position.y + origin.y, origin.x, origin.y, dimension.x,
    				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
    				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
    				false, false);
			
		}
		
	}
	
	
	

	
	public Chopper(float width) 
	{
		this.length=width; 
		intit(); 
		
	}

	private void intit() 
	{
		dimension.set(3.0f, 1.5f);
		chopper= Assets.instance.leveldecoration.chopper;
		int distFac = 20;
    	int numChoppers = (int)(length / distFac);
		choppers = new Array<Helicopter>(2*numChoppers); 
		for(int i=0; i<numChoppers; i++)
		{
			Helicopter cop = spawnCoppter();
			cop.position.x = i*distFac;
			choppers.add(cop);
			
		}
	}
	
	

	private Helicopter spawnCoppter() 
	{
		Helicopter cop = new Helicopter(); 
		cop.dimension.set(dimension); 
		cop.helicopter=chopper;
		
		Vector2 pos = new Vector2(); 
		pos.x= length+10; 
		pos.y+=4;
		pos.y+= MathUtils.random(0.0f, 0.2f)*(MathUtils.randomBoolean()?1:-1); 
		cop.position.set(pos); 
		return cop;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		for (Helicopter cop: choppers)
		{
			cop.render(batch);
		}
		
	}

}
