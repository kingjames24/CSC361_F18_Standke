package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Assets;

public class Timmy 
{
	public Vector2 position = new Vector2();
	public Vector2 dimension = new Vector2(1,1);
	public Vector2 origin = new Vector2();
	public Vector2 scale = new Vector2(1,1);
	public Body body;
	public float rotation;
	private TextureRegion regTim;
	
	public Timmy()
	{
		init(); 
	}
	
	public void init()
	{
		origin.set(dimension.x/2, dimension.y/2);
		regTim = Assets.instance.timmy.frame1; 
		
	}
	
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		reg = regTim;
		batch.draw(reg.getTexture(), position.x, 
				position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
	}
	

}
