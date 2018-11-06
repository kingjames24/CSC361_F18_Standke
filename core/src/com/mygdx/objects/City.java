package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

/**
 * Class that represents the City background
 * 
 * @author adam
 *
 */
public class City extends AbstractGameObject 
{
	
	private TextureRegion city; 
	private int length; 
	
	/**
	 * Constructor that takes an int representing 
	 * the width of the game in pixels and
	 * calls a helper method to set up the object's properties
	 */
	public City(int width) 
	{
		this.length=width;
		init(); 
	}
	
	/**
	 * Method that initializes the city object to be 15 meters wide and 15 meters tall. 
	 * Also it loads in the city image file, sets origin of the city object to start on 
	 * the negative side of the x-axis, and increases the length of the city image to span
	 * the whole level 
	 */
	private void init() 
	{
		dimension.set(15, 15);
		
		city=Assets.instance.leveldecoration.city; 
		
		origin.x = -dimension.x / 2;
		length += dimension.x * 2;
	}

	/**
	 * Method that renders the city image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
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

	/**
	 * Empty method that creates a box2d body 
	 */
	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}

}
