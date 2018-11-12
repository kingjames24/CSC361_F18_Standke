package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
/**
 * Class that contains the background objects of the scene such
 * as the helicopter and the girl with an umbrella 
 * @author adam
 *
 */
public class People extends AbstractGameObject 
{
	private TextureRegion man1; 
	private TextureRegion woman1; 
	private int length;
	public boolean left=true; 
	/**
	 * Constructor that sets how long the background elements should be
	 * appear in the game. Also, calls helper method that instantiates the object
	 * @param width
	 */
	public People(int width) 
	{
		this.length=width; 
		init(); 	
	}
	/**
	 * Method sets the dimension of each object to be 2 by 2 meters. Also, loads in 
	 * both images and sets the origin and length of each object in the game. 
	 */
	private void init() 
	{
		dimension.set(1f,1f);
		
		man1=Assets.instance.leveldecoration.chopper; 
		woman1=Assets.instance.leveldecoration.woman1; 
		
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
		
	}
	
	/**
	 * Method that calls the actual methods that implement the
	 * draw feature of the SpriteBatch class. This method, determines the
	 * rendering order(ie., methods called after others are rendered on top of 
	 * earlier called methods),the x and y offset of the images(ie., 
	 * each image does not lie directly next to each other)and the parrallax speed
	 * (ie., an illusion that gives the perception of depth in a 2d game)
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		drawmanone(batch, 0.5f, 0.5f, 0.8f); 
		drawwoman(batch, 0.0f, 0.0f, 0.3f); 

	}

	/**
	 * Method that renders the girl with the umbrella image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
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

	
	/**
	 * Method that renders the helicoppter image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
	private void drawmanone(SpriteBatch batch, float offsetX, float offsetY, float parralexSpeedX) 
	{
		
		TextureRegion reg = null;
		float xRel = dimension.x * offsetX;
		float yRel = (dimension.y-1) * offsetY;
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
	/**
	 * Method that determines whether the sprite that textures Timmy in the game 
	 * has turned left. If so, a boolean is set that will set SpriteBatch's flipX method 
	 * to true.  
	 * @param left
	 */
	public void timmyLeft(boolean left)
	{
		this.left=left; 
	}
	
	/**
	 * Method that is used to update the position of each background 
	 * object based on the cameraHelper's position, so that background
	 * objects can implement the parallax scrolling illusion. 
	 * @param camPosition
	 */
	public void updateScrollPosition(Vector2 camPosition)
	{
		// set the camera's x, y position.
		position.set(camPosition.x, position.y);
	}

	/**
	 * Method that creates additional Box2d bodies if need be 
	 */
	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}

}
