package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Princess extends AbstractGameObject 
{
	private TextureRegion goal; 

	public Princess()
	{
		init(); 
	}
	
	private void init() 
	{
		dimension.set(1f, 1f); 
		goal= Assets.instance.leveldecoration.prince; 
		origin.set(0, 0);
	}

	@Override
	public void createBody(Vector2 position) 
	{
		
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		 TextureRegion reg = null;
	      reg = goal;  
	      batch.draw(reg.getTexture(), position.x, position.y,
	    		  origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	    		  rotation, reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		
	}

}
