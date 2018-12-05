package com.mygdx.objects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.objects.Raindrops.RainDrop;
import com.mygdx.util.AudioManager;

/**
 * Class that represents all of the rain drops that fall from 
 * the sky
 * @author adam
 *
 */
public class Raindrops extends AbstractGameObject
{
	
	public  Array<RainDrop> raindropScheduledForRemoval;
	private Array<TextureRegion> rainDrops;
    private ArrayList<RainDrop> rainDrop;
    private int amount;
	public Vector2 position=new Vector2(0,10);
	public Vector2 dimension=new Vector2();
	public Vector2 origin=new Vector2();
	public Vector2 scale=new Vector2(1,1);
	public float rotation;
	public int count; 
	
	/**
	 * Inner Class that represents a single rain droplet 
	 * @author adam
	 *
	 */
	public class RainDrop 
    {
		private TextureRegion rainDrop;
		public Vector2 position=new Vector2(0,6);
		public Vector2 dimension=new Vector2();
		public Vector2 origin=new Vector2();
		public Vector2 scale=new Vector2(1,1);
		public float rotation;
		public Body body;
		public Boolean hit=false;
		public int key; 
	
	
	/**
	 * Method that is called by the ContactListener when 
	 * a rain drop hits a Box2d body(ie., either a dynamic or static body).
	 * If so this method says that the raindrop has hit something
	 * and is also added to an array that schedules the removal of the object 
	 * at a later time 
	*/	
	public void startContact() 
	{
		AudioManager.instance.play(Assets.instance.sounds.drip, 0.2f);
		hit=true;
		raindropScheduledForRemoval.add(this);	  	
	}

	/**
	 * Method is used to set the type of raindrop image file that 
	 * will be attached to this object  
	 * @param region an object of the TextureRegion Class that represents an image file
	 */
	public void setRegion (TextureRegion region) 
	{
		rainDrop = region;
	}
	
	/**
	 * Method that renders a raindrop texture. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
	public void render(SpriteBatch batch) 
	{
		
		if(hit) return; 
		
		position= body.getPosition();
		rotation= body.getAngle() * MathUtils.radiansToDegrees;
		 
		
		TextureRegion reg = rainDrop;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y,  dimension.x,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
				reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
				false, false);
	}
  
   }
	
   /**
    * Constructor that takes in the number of raindrops the game should continuously spawn. 
    * Also, the constructor initializes a new Array of RainDrop objects and calls the helper 
    * method to set various other parameters.  
    * @param amount
    */
   public Raindrops(int amount)
   {
    	this.amount=count=amount;
    	raindropScheduledForRemoval= new Array<RainDrop>();
    	init();
   }
   
   /**
    * Method sets the dimension of each raindrop to be .25 meters wide and .25 meters high, with 
    * an origin that is half the size of the listed dimensions. Next an array containing TextureRegion
    * objects is created that will hold the three different rain images. Depending on the amount of 
    * raindrops created, as dictated by the class' constructor, a for loop is run in which a single 
    * raindrop is added to an array containing raindrop objects
    */
   public void init()
   {
	   dimension.set(0.25f, 0.25f);
	   origin.set(new Vector2(dimension.x/2.0f, dimension.y/2.0f)); 
	   rainDrops= new Array<TextureRegion>();
	   rainDrops.add(Assets.instance.drop.rain01);
	   rainDrops.add(Assets.instance.drop.rain02);
	   rainDrops.add(Assets.instance.drop.rain03);
	   int numRaindrops = amount;
	   int key=0; 
	   rainDrop = new ArrayList<RainDrop>();
   	   for (int i = 0; i < numRaindrops; i++) 
   	   {
   			RainDrop drop = spawnRainDrop();
   			rainDrop.add(drop);
   			key++; 
   	   }    
   }
   
   /**
    * Method that sets the Box2d body, shape, and fixture of a single raindrop. A raindrop's
    * position and rotation is generated randomly using a uniform probability distribution.
    * After which, a fixture and polygon shape are attached to a raindrop. To make the game 
    * more interesting different raindrops fall at a different speeds as the player progresses 
    * through out the game. This is done through Box2d's set gravity feature and density calculation.  
    * @return
    */
   private RainDrop spawnRainDrop () 
   {
     RainDrop drop = new RainDrop();
     drop.setRegion(rainDrops.random());
     drop.dimension.set(dimension);
     float rainScale= 0.5f; 
     float x = MathUtils.random(0, 118);
     float y = MathUtils.random(0,1);
     float rotation = MathUtils.random(0.0f, 5.0f)* MathUtils.degreesToRadians; 
     
     BodyDef bodyDef = new BodyDef();
     bodyDef.position.set(drop.position);
     bodyDef.position.add(x, y);
     bodyDef.angle=rotation;
     Body body = WorldController.b2world.createBody(bodyDef); 
     body.setType(BodyType.DynamicBody);
     body.setUserData(drop);
     body.setFixedRotation(true);
     if(x>50)
     {
    	 body.setGravityScale(0.5f);
     }
     else
     {
    	 body.setGravityScale(0.05f); 
     }
     
     drop.body=body;
 
     
     PolygonShape polygonShape = new PolygonShape();
	 drop.origin.x =drop.dimension.x/2f;
     drop.origin.y =drop.dimension.x/2f;
     polygonShape.setAsBox(drop.dimension.x/2f*rainScale, drop.dimension.y/2f*rainScale, drop.origin, 0);
     
     
     FixtureDef fixtureDef = new FixtureDef();
     fixtureDef.shape = polygonShape;
     fixtureDef.density = .1f;
     fixtureDef.restitution = 0.2f;
     fixtureDef.friction = 0.5f;
     Fixture fixtureData =body.createFixture(fixtureDef);
     fixtureData.setUserData((Object)("1"));
     polygonShape.dispose();
   
     return drop;
   }
   /**
    * Method that calls each individual raindrop's render/draw method 
    * to render the raindrops on screen 
    */
   public void render (SpriteBatch batch) 
   {
     for (RainDrop drop : rainDrop)
         drop.render(batch);
   }
   
   /**
    * Method that spawns a new raindrop after a raindrop's body has been destroyed
    * (due to colliding with a static or dynamic object) 
    * @param drop
    */
   public void destroy(RainDrop drop)
   {
	   rainDrop.remove(drop);
	   rainDrop.add(spawnRainDrop());
   }

   	/**
	 * Method that creates additional Box2d bodies, if need be
	 */
   @Override
   public void createBody(Vector2 position) {
	// TODO Auto-generated method stub
	
   }
   
   
	
	
	
	
}
