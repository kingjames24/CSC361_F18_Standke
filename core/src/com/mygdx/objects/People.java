package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class People extends AbstractGameObject 
{
	private TextureRegion man1; 
	private TextureRegion man2; 
	private TextureRegion woman1; 
	private int length;
	public boolean left=true; 
	
	public People(int width) 
	{
		this.length=width; 
		init(); 
		
	}

	private void init() 
	{
		dimension.set(1f,1f);
		
		man1=Assets.instance.leveldecoration.chopper; 
		woman1=Assets.instance.leveldecoration.woman1; 
		
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		drawmanone(batch, 0.5f, 0.5f, 0.8f); 
		drawwoman(batch, 0.0f, 0.0f, 0.3f); 
		
		
	}

	private void drawwoman(SpriteBatch batch, float offsetX, float offsetY, float parrallaxSpeedX) 
	{
		TextureRegion reg = null;
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		int manoneLength = 0;
		manoneLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parrallaxSpeedX));
		manoneLength += MathUtils.ceil(0.5f + offsetX);
		for(int i=0; i<manoneLength; i++)
		{
			reg=woman1; 
			batch.draw(reg.getTexture(), origin.x + xRel + position.x * parrallaxSpeedX,
					origin.y + yRel + position.y,
					origin.x, origin.y,
					dimension.x, dimension.y,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), 
					false, false);
			xRel += dimension.x+5;
		}
	}

	

	private void drawmanone(SpriteBatch batch, float offsetX, float offsetY, float parralexSpeedX) 
	{
		
		TextureRegion reg = null;
		float xRel = dimension.x * offsetX;
		float yRel = (dimension.y+15) * offsetY;
		int manoneLength = 0;
		manoneLength += MathUtils.ceil(length / (2 * dimension.x) * (1 - parralexSpeedX));
		manoneLength += MathUtils.ceil(0.5f + offsetX);
		for(int i=0; i<manoneLength; i++)
		{
			reg=man1; 
			batch.draw(reg.getTexture(), origin.x + xRel + position.x * parralexSpeedX,
					origin.y + yRel + position.y,
					origin.x, origin.y,
					dimension.x+2, dimension.y+1,
					scale.x, scale.y,
					rotation,
					reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), 
					left, false);
			xRel += dimension.x+5;
		}
		
	}
	
	public void timmyLeft(boolean left)
	{
		this.left=left; 
	}
	
	public void updateScrollPosition(Vector2 camPosition)
	{
		// set the camera's x, y position.
		position.set(camPosition.x, position.y);
	}

	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}

}
