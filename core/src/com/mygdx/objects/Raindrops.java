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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.objects.Raindrops.RainDrop;





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
	
	
	public class RainDrop //have not created an abstract class yet
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
	
	
		
	public void startContact() 
	{
		hit=true;
		raindropScheduledForRemoval.add(this);	  	
	}

	
	
	
	public void setRegion (TextureRegion region) 
	{
		rainDrop = region;
	}
	
	
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
	
	
   public Raindrops(int amount)
   {
    	this.amount=count=amount;
    	raindropScheduledForRemoval= new Array<RainDrop>();
    	init();
   }
   
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
   
   private RainDrop spawnRainDrop () 
   {
     RainDrop drop = new RainDrop();
     drop.setRegion(rainDrops.random());
     drop.dimension.set(dimension);
     
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
     polygonShape.setAsBox(drop.dimension.x/2f, drop.dimension.y/2f, drop.origin, 0);
     
     
     FixtureDef fixtureDef = new FixtureDef();
     fixtureDef.shape = polygonShape;
     fixtureDef.density = .1f;
     fixtureDef.restitution = 0.2f;
     fixtureDef.friction = 0.5f;
     body.createFixture(fixtureDef);
     polygonShape.dispose();
   
     return drop;
   }
   
   public void render (SpriteBatch batch) 
   {
     for (RainDrop drop : rainDrop)
         drop.render(batch);
   }
   
   
   public void destroy(RainDrop drop)
   {
	   rainDrop.remove(drop);
	   rainDrop.add(spawnRainDrop());
   }

@Override
public void createBody(Vector2 position) {
	// TODO Auto-generated method stub
	
}
   
   
	
	
	
	
}
