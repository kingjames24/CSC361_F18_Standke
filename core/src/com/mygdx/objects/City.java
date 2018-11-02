package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Assets;

public class City extends AbstractGameObject 
{
	
	private TextureRegion city; 
	private int length; 
	
	public City(int width) 
	{
		this.length=width;
		init(); 
	}

	private void init() 
	{
		dimension.set(15, 15);
		
		city=Assets.instance.leveldecoration.city; 
		
		origin.x = -dimension.x / 2;
		length += dimension.x * 2;
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg=null;
		reg = city;
		float xRel = 0;
    	float yRel = 0; 
    	int cityLength = 0;
		cityLength += MathUtils.ceil(length);  
		for (int i = 0; i <cityLength ; i++) 
	    {
	        batch.draw(reg.getTexture(), origin.x + xRel,
					origin.y + yRel + position.y,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), 
					false, false);
	        xRel += dimension.x;
	    }
		
	}

}
