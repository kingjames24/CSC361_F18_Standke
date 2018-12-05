package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
/**
 * Class that represents the princess that was stolen the evil 
 * warlock. Timmy is trying to wake her up from a deep sleep spell!
 * @author adam
 *
 */
public class Princess extends AbstractGameObject 
{
	private TextureRegion goal; 
	
	/**
	 * Constructor that calls other helper methods to construct
	 * her Box2d and size in the game
	 */
	public Princess()
	{
		init(); 
	}
	/**
	 * method that sets her dimension to be 1 meter wide and 1 meter high(which is 
	 * approximately 100 by 100 pixels), imports her 2d texture from the assetmanager
	 * and sets her origin for her Box2d polygon to be at (0,0)
	 */
	private void init() 
	{
		dimension.set(1f, 1f); 
		goal= Assets.instance.leveldecoration.prince; 
		origin.set(0, 0);
	}

	/**
	 * Method that actually creates the princess' Box2d body. Is a typical 
	 * Box2d static body that uses a rectangular polygon shape. The only difference
	 * is that its area is 4 times as big as other objects in the game. This is due to
	 * the princess being the goal and we want the game to end when Timmy crosses her 
	 * box2d body.  
	 */
	@Override
	public void createBody(Vector2 position) 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.fixedRotation=true;
		bodyDef.position.set(position); 
		body = WorldController.b2world.createBody(bodyDef);
		body.setUserData(this);
		
		PolygonShape polygonShape = new PolygonShape();
		origin.x = this.dimension.x/this.dimension.x; 
		origin.y = this.dimension.y/this.dimension.y; 
		polygonShape.setAsBox(this.dimension.x/2*4,this.dimension.y/2*4, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = polygonShape; 
	    Fixture f= body.createFixture(fixtureDef);
	    f.setSensor(true);
	    polygonShape.dispose();
		
		
	}
	/**
	 * Render method used to render the princess' 2d texture on the display
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		 TextureRegion reg = null;
	      reg = goal;  
	      batch.draw(reg.getTexture(), position.x, position.y,
	    		  origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	    		  rotation, reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		
	}
	
	/**
	 * Method that tells the game world 
	 * that timmy has reached his goal and 
	 * the game should transition back to the
	 * main menu
	 */
	public void startContract() 
	{
		WorldController.goalReached=true; 
		
	}

}
