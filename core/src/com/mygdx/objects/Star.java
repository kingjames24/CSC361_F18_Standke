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
 * Class that represents the star object that 
 * the player is allowed collect to unlock a special
 * ability of firing stars 
 * @author adam
 *
 */
public class Star extends AbstractGameObject{

	private TextureRegion star; 
	public static boolean collected;
	public static Star starScheduledforRemoval;
	
	
	 
	/**
	 * Constructor that calls a helper method to set up the star object
	 */
	public Star()
	{
		init(); 
	}
	/**
	 * Method that initializes a star object to be .5 meters wide and .5 meters tall. 
	 * Also loads in the star image file. And sets the boolean of collected to false
	 */
	private void init() 
	{
		dimension.set(0.5f, 0.5f); 
		star=Assets.instance.up.power; 
		collected=false; 
		
	}
	
	/**
	 * Method that renders the star image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
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
	/**
	 * Method that creates the Box2d body for the star object to be 
	 * a static type.  
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
		origin.x = this.dimension.x/2; 
		origin.y = this.dimension.y/2; 
		polygonShape.setAsBox(this.dimension.x/2,this.dimension.y/2, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = polygonShape; 
	    Fixture f= body.createFixture(fixtureDef);
	    f.setSensor(true);
	    polygonShape.dispose();
		
	}
	/**
	 * Method that is called by the ContactListener when 
	 * Timmy collects a star object. If so this method says that 
	 * the player has collected the item and is flagged for removal. 
	 */
	public void startContract() 
	{
		collected=true;
		starScheduledforRemoval=this; 
		
	}

}
