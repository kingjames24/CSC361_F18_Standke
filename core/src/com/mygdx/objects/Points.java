package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Level;
import com.mygdx.game.WorldController;
import com.mygdx.objects.Raindrops.RainDrop;
/**
 * Class that represents Points objects that 
 * the player is allowed to collect
 * @author adam
 *
 */
public class Points extends AbstractGameObject
{
	private TextureRegion point; 
	public boolean collected;
	public static Array<Points> pointScheduledForRemoval;
	/**
	 * Constructor that calls a helper method to set up points objects
	 */
	public Points()
	{
		init(); 
	}

	/**
	 * Method that initializes a point objects to be .5 meters wide and .5 meters tall. 
	 * Also loads in the points' image file. Also an array of points is created that is used 
	 * to store any point objects that have been collected by the main player. After the simulation
	 * is run, all of these objects will be destroyed. Furthermore, a boolean is initialized with 
	 * false to indicate that it has not been collected yet.  
	 */
	private void init() 
	{
		dimension.set(.5f, .5f); 
		point = Assets.instance.plus.points;
		pointScheduledForRemoval = new Array<Points>(); 
		collected=false; 
		
	}
	/**
	 * Method that is called by the ContactListener when 
	 * Timmy collects a point object. If so this method says that 
	 * the player has collected the item and is also added to an array 
	 * that schedules the removal of the object at a later time 
	 */
	public void startContract()
	{
		collected=true; 
		pointScheduledForRemoval.add(this);
		
		
	}
	/**
	 * Method that creates the Box2d body for the a point object to be 
	 * a static type.  
	 */
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
	 * Method that renders a point image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		if (collected) return;
	      
	      TextureRegion reg = null;
	      reg = point;  
	      batch.draw(reg.getTexture(), position.x, position.y,
	    		  origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	    		  rotation, reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	 /**
	 * Method that returns the amount of points a player receives after collecting a point 
	 * object  
	 * @return an int that represents the player's score for collecting this object
	 */
	 public int getScore() 
	    {
	        return 10;
	    } 

}
