package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Star extends AbstractGameObject{

	private TextureRegion star; 
	public boolean collected; 
	
	public Star()
	{
		init(); 
	}
	
	private void init() 
	{
		dimension.set(0.5f, 0.5f); 
		star=Assets.instance.up.power; 
		collected=false; 
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		if (collected) return;
		
		TextureRegion reg = null;
		reg = star;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		
	}

	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}

}
