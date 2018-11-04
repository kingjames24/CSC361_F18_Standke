package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Assets;

public class Timmy extends AbstractGameObject 
{

	
	
	
	
	public float rotation; 
	public TextureRegion regTim;
	public boolean left; 
	
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
		
		position= body.getPosition();
		rotation= (float) Math.toDegrees(body.getAngle());
		
		reg = regTim;
		batch.draw(reg.getTexture(), position.x, 
				position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), left, false);
	}

	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	

}
