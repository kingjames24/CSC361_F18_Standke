package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;







public class Raindrops
{
	private Array<TextureRegion> rainDrops;
    private Array<RainDrop> rainDrop;
    private int amount;
	public Vector2 position=new Vector2(0,10);
	public Vector2 dimension=new Vector2();
	public Vector2 origin=new Vector2();
	public Vector2 scale=new Vector2(1,1);
	public float rotation;
	
	
	private class RainDrop //have not created an abstract class yet
    {
		private TextureRegion rainDrop;
		public Vector2 position=new Vector2(0,10);
		public Vector2 dimension=new Vector2();
		public Vector2 origin=new Vector2();
		public Vector2 scale=new Vector2(1,1);
		public float rotation;
		public Body body;
		
	
	
	
	public void setRegion (TextureRegion region) 
	{
		rainDrop = region;
	}
	
	
	public void render(SpriteBatch batch) 
	{
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
    	this.amount = amount;
    	init();
   }
   
   public void init()
   {
	   dimension.set(0.5f, 0.5f);
	   origin.set(new Vector2(dimension.x/2.0f, dimension.y/2.0f)); 
	   rainDrops= new Array<TextureRegion>();
	   rainDrops.add(Assets.instance.drop.rain01);
	   rainDrops.add(Assets.instance.drop.rain02);
	   rainDrops.add(Assets.instance.drop.rain03);
	   int numRaindrops = amount;
	   rainDrop = new Array<RainDrop>(numRaindrops);
   	   for (int i = 0; i < numRaindrops; i++) 
   	   {
   			RainDrop drop = spawnRainDrop();
   			//drop.position.y = i * distFac;will have to create a way to randomize height of raindrops
   			rainDrop.add(drop);
   	   }    
   }
   
   private RainDrop spawnRainDrop () 
   {
     RainDrop drop = new RainDrop();
     drop.setRegion(rainDrops.random());
     drop.dimension.set(dimension);
     
     float x = MathUtils.random(-10, 10);
     float y = MathUtils.random(5,15);
     float rotation = MathUtils.random(0.0f, 5.0f)* MathUtils.degreesToRadians; 
     
     BodyDef bodyDef = new BodyDef();
     bodyDef.position.set(drop.position);
     bodyDef.position.add(x, y); 
     bodyDef.angle=rotation;
     Body body = WorldController.b2world.createBody(bodyDef); 
     body.setType(BodyType.DynamicBody);
     body.setUserData(drop);
     body.setFixedRotation(true);
     body.setGravityScale(0.1f);
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
   
	
	
	
	
}
