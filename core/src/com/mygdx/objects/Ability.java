package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
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

public class Ability extends AbstractGameObject
{

	private TextureRegion star;
	public static boolean fire=false;
	public float time;
	public float step; 
	public Vector2 startingVelocity;
	public Vector2 startingPosition;
	public Vector2 traject; 
	 
	
	public Ability()
	{
		init(); 
	}
	
	private void init() 
	{
		dimension.set(.2f, .2f); 
		star=Assets.instance.up.power;
		traject = new Vector2();
		step=0; 
		 
	}

	
	
	@Override
	public void createBody(Vector2 position) 
	{
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
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
	    body.createFixture(fixtureDef);
	    polygonShape.dispose();
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		if(!fire) return; 
		TextureRegion reg = null;
		reg = star;
		position = body.getPosition(); 
		
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		
	}

	public void shoot(float deltaTime) 
	{
		float t = deltaTime; 
		Vector2 stepVelocity = new Vector2(); 
		stepVelocity.x= t* startingVelocity.x;
		stepVelocity.y= t* startingVelocity.y; 
		Vector2 stepGravity = WorldController.b2world.getGravity(); 
		stepGravity.x *= t*t;
		stepGravity.y *= t*t;
		traject.x=startingPosition.x+step* stepVelocity.x + 0.5f*(step*step+step) *stepGravity.x; 
		traject.y=startingPosition.y+step* stepVelocity.y + 0.5f*(step*step+step) *stepGravity.y;
		step++; 
			
	}
	
	 public void update(float deltaTime)
	 {
		   if(step<time)
		   {
			   shoot(deltaTime); 
		   }
		   else if (step>=time)
		   {
			   body.setLinearVelocity(traject); 
		   }
		   
	 	   
	 }
	 
	 public float getMaxHeight(float deltaTime)
	 {
		 if(startingVelocity.y<0)
		 {
			 return startingVelocity.y; 
		 }
		 
		 float t = deltaTime; 
		 Vector2 stepVelocity = new Vector2(); 
		 stepVelocity.x= t* startingVelocity.x;
		 stepVelocity.y= t* startingVelocity.y; 
		 Vector2 stepGravity = WorldController.b2world.getGravity();
		 
		 float n = -stepVelocity.y/stepGravity.y-1; 
		 return startingPosition.y+n * stepVelocity.y + 0.5f*(n*n+n) *stepGravity.y;
		 
	 }
	 
	 public void setVelocity(Vector2 velocity)
	 {
		 this.startingVelocity=velocity; 
	 }

	public float calulateStartingVelocity(float i, float deltaTime) 
	{
		
		  if ( i <= 0 )return 0; 
	      //gravity is given per second but we want time step values here
	      float t = deltaTime;
	      Vector2 stepGravity = WorldController.b2world.getGravity(); 
	      stepGravity.x *= t*t;
	      stepGravity.y *= t*t; 
	      //quadratic equation setup (ax² + bx + c = 0)
	      float a = 0.5f / stepGravity.y;
	      float b = 0.5f;
	      float c = i;
	      //check both possible solutions
	      float quadraticSolution1 = (float) ( -b - Math.sqrt(( b*b - 4*a*c ) ) / (2*a));
	      float quadraticSolution2 = (float) ( -b + Math.sqrt(( b*b - 4*a*c ) ) / (2*a));
	      //use the one which is positive
	      float v = quadraticSolution1;
	      if ( v < 0 )v = quadraticSolution2;
	      //convert answer back to seconds
	      return v * 60f;
			
	}

	public void setTimeSpan(float time2) 
	{
		this.time=time2; 
		
	}

	public void setStartingPos(Vector2 position) 
	{
		this.startingPosition=position;
		
		
	}

	public void setFire(boolean b) 
	{
		this.fire=b; 
		
	}
	
	

}
