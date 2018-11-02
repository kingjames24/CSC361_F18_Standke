package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
		dimension.set(length *10, 10);
		
		city=Assets.instance.leveldecoration.city; 
		
		origin.x = -dimension.x / 2;
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		TextureRegion reg=null;
		reg = city;
		batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(), 
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}

}
