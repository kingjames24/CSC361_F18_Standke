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
	private Animation animDead; 
	public float rotation; 
	public TextureRegion regTim;
	public boolean left;
	public boolean runningLeft; 
	public boolean runningRight;
	public boolean jumping;
	public boolean dead; 
	public boolean hit;
	public int life=100;
	public float i=0f;
	private Animation animShooting;
	public boolean shooting; 
	
	
	/**
	 * Constructor that calls a helper method to set up a Timmy object
	 */
	public Timmy()
	{
		init(); 
	}
	/**
	 * Method that initializes a Timmy object to have its 
	 * game origin centered at (.5,.5).  
	 * Also loads in the Timmy image files which are now 
	 * a bunch of frames of animation. Timmy's default animation 
	 * is also set here   
	 */
	public void init()
	{
		origin.set(dimension.x/2, dimension.y/2);
		regTim = Assets.instance.timmy.frame1;
		animRunning=Assets.instance.timmy.animRunning;
		animNormal=Assets.instance.timmy.animNormal;
		animJumping=Assets.instance.timmy.animJumping;
		animDead=Assets.instance.timmy.animDead;
		animShooting = Assets.instance.timmy.animShooting; 
		dead=false; 
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
		
		float correctX=0; //slight correction is made to animNormal since texture is a bit taller than the rest 
		float correctY=0; 
		if(animation == animNormal)
		{
			correctX= -0.02f; 
			correctY= -0.12f; 
		}
		if (animation==animShooting)//slight correction is made to animShotting since texture is a bit smaller
		{
			correctX=.2f; 
			correctY=.3f; 
		}
		
		if(animation==animDead)//Run-time modification to the basic state machine for when timmy dies
		{
			if(animDead.isAnimationFinished(stateTime))//Timmy's animation fully plays
			{
				reg = (TextureRegion) animation.getKeyFrame(stateTime, false);//then stops on the last frame  
			} 
			else
			{
				reg = (TextureRegion) animation.getKeyFrame(stateTime, true);
			}
		}
		else
		{
			reg = (TextureRegion) animation.getKeyFrame(stateTime, true);
		}
		
		
		
		

		batch.draw(reg.getTexture(), position.x, 
				position.y, origin.x, origin.y, dimension.x+correctX,
				dimension.y+correctY, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), left, false);
	}
	/**
	 * Method that creates additional Box2d bodies for Timmy 
	 *   
	 */
	@Override
	public void createBody(Vector2 position) {}
	
	
	/**
	 * update method that implements a basic/rudimentary FSM to keep 
	 * track of the animation state that Timmy is in and how to transition from
	 * one animation to another in the next frame
	 */
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		if(animation==animDead)
		{
			return; 
		}
		
		if(runningLeft||runningRight)
		{
			if(shooting)
			{
				stateTime=0; 
				setAnimation(animShooting);
				shooting=false; 
			}
			else if(animation==animShooting) 
			{
				if(animShooting.isAnimationFinished(stateTime))
				{
					setAnimation(animRunning);
				}
				 
			}
			else
			{
				setAnimation(animRunning);
			}
			
		}
		else if(jumping)
		{
			if (shooting)
			{
				stateTime=0; 
				setAnimation(animShooting);
				shooting=false; 
			}
			else if (animation==animShooting)
			{
				if(animShooting.isAnimationFinished(stateTime))
				{
					setAnimation(animJumping);
				}
				
			}
			else
			{
				setAnimation(animJumping);
			}
			 	
		}
		else if(dead)
		{
			stateTime=0; 
			setAnimation(animDead);
			
		}
		else
		{
			if (shooting)
			{
				stateTime=0; 
				setAnimation(animShooting);
				shooting=false; 
			}
			else if(animation==animShooting)
			{	
				if(animShooting.isAnimationFinished(stateTime))
				{
					setAnimation(animNormal);
				}	
			}
			else
			{
				setAnimation(animNormal);
			}
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
	
	/**
	 * Method called by contact listener to indicate that Timmmy is allowed to 
	 * jump in the game(ie., prevents air jumping)
	 */
	public void jumping()
	{
		jumping=true; 
	}
	
	/**
	 * Method called by contact listener to indicate that Timmmy is not allowed to 
	 * jump in the game(ie., prevents air jumping)
	 */
	public void notJumping()
	{
		jumping=false; 
	}
	
	
	
	

}
