package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.Assets;

/**
 * Class that represents the main character in the game
 * Timmy
 * @author adam
 *
 */
public class Timmy extends AbstractGameObject 
{
	private Animation animRunning;
	private Animation animNormal;
	private Animation animJumping; 
	public float rotation; 
	public TextureRegion regTim;
	public boolean left;
	public boolean runningLeft; 
	public boolean runningRight;
	public boolean jumping; 
	public boolean hit;
	public int life=100; 
	
	/**
	 * Constructor that calls a helper method to set up a Timmy object
	 */
	public Timmy()
	{
		init(); 
	}
	/**
	 * Method that initializes a Timmy object to have its 
	 * origin centered at (.5,.5).  
	 * Also loads in the Timmy image file. 
	 */
	public void init()
	{
		origin.set(dimension.x/2, dimension.y/2);
		regTim = Assets.instance.timmy.frame1;
		animRunning=Assets.instance.timmy.animRunning;
		animNormal=Assets.instance.timmy.animNormal;
		animJumping=Assets.instance.timmy.animJumping; 
		
		setAnimation(animNormal); 
		
	}
	/**
	 * Method that renders the Timmy texture. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		position= body.getPosition();
		rotation= (float) Math.toDegrees(body.getAngle());
		
	    reg = (TextureRegion) animation.getKeyFrame(stateTime, true); 
		
		
		
		batch.draw(reg.getTexture(), position.x, 
				position.y, origin.x, origin.y, dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), left, false);
	}
	/**
	 * Method that creates additional Box2d bodies for Timmy 
	 *   
	 */
	@Override
	public void createBody(Vector2 position) {}
	
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		
		if(runningLeft||runningRight)
		{
			setAnimation(animRunning);
		}
		else if(jumping)
		{
			setAnimation(animJumping); 	
		}
		else
		{
			setAnimation(animNormal); 
		}
		 		 
		
	}
	/**
	 * Method that is called by the ContactListener when 
	 * Timmy is hit by a rain drop. If so, he loses 10 hit points 
	 */
	public void startContract()
	{ 
		life-=10; 	
	}
	/**
	 * Method that returns Timmy's current life score. Timmy starts out with 
	 * 100 life points  
	 * @return
	 */
	public int getLife()
	{
		return life; 
	}
	
	public void jumping()
	{
		jumping=true; 
	}
	
	public void notJumping()
	{
		jumping=false; 
	}
	
	
	
	

}
