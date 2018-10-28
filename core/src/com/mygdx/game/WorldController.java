package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.objects.Platform;
import com.mygdx.objects.Timmy;
import com.mygdx.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;



public class WorldController extends InputAdapter implements Disposable
{
	private Game game;
	public CameraHelper cameraHelper;
    public World b2world;
    
    //will be replaced when level is added
    public Timmy tim;
    public Platform[] platform; 
	
	public WorldController(Game game) 
	{
		this.game = game;
		tim = new Timmy();
		
		platform = new Platform[8]; 
		for(int i=0; i<platform.length; i++)
		{
			
				platform[i]=new Platform();	
		}
		init();
	}
	
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initPhysics();
	}
	
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		b2world.step(deltaTime, 8, 3);
		
		platform[0].update(deltaTime);//moving platforms
		platform[7].update(deltaTime);//moving platforms
		
		cameraHelper.update(deltaTime);	
	}
	
	

	private void handleDebugInput(float deltaTime)              //used only for debuging will not be in game
	{
			if (Gdx.app.getType() != ApplicationType.Desktop) return;
			
			//controls player in game
			float sprMoveSpeed = 2;
		    if (Gdx.input.isKeyPressed(Keys.A))tim.body.applyLinearImpulse(new Vector2(-sprMoveSpeed, 0), tim.body.getWorldCenter(), true);
		    if (Gdx.input.isKeyPressed(Keys.D))tim.body.applyLinearImpulse(new Vector2(sprMoveSpeed, 0), tim.body.getWorldCenter(), true);
		    if (Gdx.input.isKeyPressed(Keys.W)) tim.body.applyLinearImpulse(new Vector2(0,6), tim.body.getWorldCenter(), true);
		    if (Gdx.input.isKeyPressed(Keys.S)) tim.body.applyLinearImpulse(new Vector2(0,-sprMoveSpeed), tim.body.getWorldCenter(), true);


		   // Camera Controls (move)
	       float camMoveSpeed = 5 * deltaTime;
	       float camMoveSpeedAccelerationFactor = 5;
	       if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=camMoveSpeedAccelerationFactor;
	       if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
	       if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,0);
	       if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
	       if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0,-camMoveSpeed);
	       if (Gdx.input.isKeyPressed(Keys.BACKSPACE))cameraHelper.setPosition(0, 0);
	       // Camera Controls (zoom)
	       float camZoomSpeed = 1 * deltaTime;
	       float camZoomSpeedAccelerationFactor = 5;
	       if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
	       if (Gdx.input.isKeyPressed(Keys.COMMA))cameraHelper.addZoom(camZoomSpeed);
	       if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
	       if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		
	}
	
	private void moveCamera (float x, float y) 
	{
	       x += cameraHelper.getPosition().x;
	       y += cameraHelper.getPosition().y;
	       cameraHelper.setPosition(x, y);
	}
	

	@Override
	public void dispose() 
	{
		if (b2world != null) b2world.dispose();
	}
	
	private void initPhysics () 
    {
    	   if (b2world != null) b2world.dispose();
    	   b2world = new World(new Vector2(0, -4f), true);
    	   Vector2 origin = new Vector2();

    	 
    	   // Timmy
    	   BodyDef bodyDef = new BodyDef();
		   bodyDef.type = BodyType.DynamicBody;
		   //bodyDef.angle = rotation;
		   bodyDef.position.set(tim.position);
		   bodyDef.linearVelocity.set(new Vector2(1,1)); 
		   Body body = b2world.createBody(bodyDef);
		   body.setUserData(tim);
    	   tim.body=body; 
    	   
    	   PolygonShape polygonShape = new PolygonShape();
    	   origin.x = tim.dimension.x/2;
	       origin.y = tim.dimension.y/2;
	       polygonShape.setAsBox(tim.dimension.x/2.5f, tim.dimension.y/2.5f, origin, 0);
	       
	       FixtureDef fixtureDef = new FixtureDef();
	       fixtureDef.shape = polygonShape;
	       fixtureDef.density = 50;
	       fixtureDef.restitution = 0.1f;
	       fixtureDef.friction = 0.5f;
	       body.createFixture(fixtureDef);
	       polygonShape.dispose();
	       //creating a static boundary to play with physics
	       createBoundary();       
    }
	
	
	private void createBoundary() //used for physics testing 
	{
		 BodyDef bodyDef = new BodyDef();
		 bodyDef.type = BodyType.StaticBody;
		 bodyDef.position.set(0,0); 
		 Body staticBody = b2world.createBody(bodyDef);
		 
		 
		 PolygonShape polygonShape = new PolygonShape();
		 FixtureDef fixtureDef = new FixtureDef();
	     fixtureDef.shape = polygonShape;
	     polygonShape.setAsBox(4, 1, new Vector2(0,0), 0);
	     staticBody.createFixture(fixtureDef);
	     polygonShape.setAsBox(4, 1, new Vector2(0,10), 0);
	     staticBody.createFixture(fixtureDef);
	     polygonShape.setAsBox(1, 4, new Vector2(-5,5), 0);
	     staticBody.createFixture(fixtureDef);
	     polygonShape.setAsBox(1, 4, new Vector2(5,5), 0);
	     staticBody.createFixture(fixtureDef);
	     polygonShape.dispose();
	     
	     createPlatforms(); 
	}
	
	private void createPlatforms()
	{	
		float x=-4, y=3;
		Vector2 origin = new Vector2();
 	   	for (int i=0; i<platform.length; i++) 
 	   	{
 	   		    
 	   	       if (i==4)
 	   	       {
 	   	    	   x++; 
 	   	    	   continue; 
 	   	       }
 	   		   BodyDef bodyDef = new BodyDef();
 	   		   bodyDef.type = BodyType.KinematicBody;
 	   		   platform[i].position.x=x; 
 	   		   platform[i].position.y=y; 
 	   		   bodyDef.position.set(platform[i].position);
 	   		   Body body = b2world.createBody(bodyDef);
 	   		   body.setUserData(platform[i]);
 	   		   platform[i].body = body;
 	       
 	   		   PolygonShape polygonShape = new PolygonShape();
 	   		   origin.x = platform[i].dimension.x / 2.0f;
 	   		   origin.y = platform[i].dimension.y / 2.0f;
 	   		   polygonShape.setAsBox(platform[i].dimension.x/ 2.0f, platform[i].dimension.y/ 2.0f, origin, 0);
 	       
 	   		   FixtureDef fixtureDef = new FixtureDef();
 	   		   fixtureDef.shape = polygonShape;
 	   		   body.createFixture(fixtureDef);
 	   		   polygonShape.dispose();
 	   		   x++; 
 	   	   
 	        
 	   }
		
		
		
	}
	
	
	
		
	

}
