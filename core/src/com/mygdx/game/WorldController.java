package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.contacts.MyContactListener;
import com.mygdx.objects.Ability;
import com.mygdx.objects.Platform;
import com.mygdx.objects.Points;
import com.mygdx.objects.Raindrops;
import com.mygdx.objects.Raindrops.RainDrop;
import com.mygdx.objects.Star;
import com.mygdx.objects.Timmy;
import com.mygdx.util.CameraHelper;
import com.mygdx.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;



public class WorldController extends InputAdapter implements Disposable
{
	private Game game;
	public CameraHelper cameraHelper;
    public static World b2world;
    public static int numFootContacts=0;
    public int jumpTimeout=0;
    public boolean attached=false; 
    
    public RevoluteJoint joint;
    public RevoluteJoint joint2; 
    public Raindrops rain;
    public Ability ability; 
    public Level level;
	
	public WorldController(Game game) 
	{
		this.game = game;
		init();
	}
	
	
	
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		if (b2world != null) b2world.dispose();
 	   	b2world = new World(new Vector2(0, -5f), true);
 	   	b2world.setContactListener(new MyContactListener());
		initlevel(); 
		rain= new Raindrops(50);
		ability = new Ability(); 
		ability.createBody(level.tim.body.getPosition());
		
	}
	
	private void initlevel()
	{
		
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.tim);
		initPhysics();
		
	}
	
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		
		handleInputGame(deltaTime);
		
	
		
		
		level.update(deltaTime);
		b2world.step(deltaTime, 8, 3);
		jumpTimeout--; 
		
		if(!b2world.isLocked()) 
		{
			int x= rain.raindropScheduledForRemoval.size;
			for(int j=0; j<x; j++)
			{
			
				RainDrop drop=rain.raindropScheduledForRemoval.pop();
				if(drop!=null)
				{
					if(drop.hit)
					{
						
						drop.body.getWorld().destroyBody(drop.body);
						
					}
					rain.destroy(drop);
				}
				
			}
			rain.raindropScheduledForRemoval.clear();
		}
		if(!b2world.isLocked()) 
		{
			int x= Points.pointScheduledForRemoval.size;
			for(int j=0; j<x; j++)
			{
			
				Points point= Points.pointScheduledForRemoval.pop(); 
				if(point!=null)
				{
					if(point.collected)
					{
						
						point.body.getWorld().destroyBody(point.body);
						
					}
					
				}
				
			}
			Points.pointScheduledForRemoval.clear();
		}
				
		cameraHelper.update(deltaTime);
		level.people.updateScrollPosition(cameraHelper.getPosition());
	}
	
	
	
	
	private void handleInputGame (float deltaTime) 
	{
	     if (cameraHelper.hasTarget(level.tim)) 
	     {
	       // Player Movement
	       float sprMoveSpeed = 1;
	       if (Gdx.input.isKeyPressed(Keys.LEFT)) 
	       {
	    	   if(numFootContacts<1)return;
	    	   level.people.timmyLeft(false);
	    	   level.tim.left=true; 
	    	   level.tim.body.applyLinearImpulse(new Vector2(-sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	       } 
	       else if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
	       {
	    	   if(numFootContacts<1)return;
	    	   level.people.timmyLeft(true);
	    	   level.tim.left=false;
	    	   level.tim.body.applyLinearImpulse(new Vector2(sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	       }
	       // Tim Jump
	       if ( Gdx.input.isKeyPressed(Keys.SPACE))
	       {
	    	   if(numFootContacts<1)return;
	    	   if(jumpTimeout>0)return; 
	    	   level.tim.body.applyLinearImpulse(new Vector2(0,level.tim.body.getMass()*2), level.tim.body.getWorldCenter(), true);
	    	   jumpTimeout=15; 
	       }
	       if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
	       {
	    	   int x = Gdx.input.getX(); 
	    	   int y = Gdx.input.getY();
	    	   
	    	  
	    	   if(Star.collected)
	    	   {
	    		   float angle = (float) Math.atan(y/x); 
	    		   Vector3 screen = new Vector3(x,y,0); 
	    		   Vector3 world = WorldRenderer.camera.unproject(screen);
	    		   //ability.body.setTransform(new Vector2(level.tim.position.x+.5f, level.tim.position.y+1f), 0);
	    		   Body launcher=joint.getBodyB();
	    		   launcher.setTransform((launcher.getPosition()), (float) Math.toRadians(angle));
	    		   launcher.setAngularVelocity((float) Math.toRadians(angle));
	    		   ability.body.setTransform(launcher.getPosition(), (float)Math.toRadians(angle));
	    		   ability.body.setAngularVelocity((float) Math.toRadians(angle));
	    		   
	    		   ability.body.setLinearVelocity(new Vector2(world.x, world.y));
	    
	    		   ability.body.setGravityScale(0);
	    		   ability.setFire(true); 
	    		    
	    	   }
	    	   
	       }

	
	     } 
	 }
	
	public boolean keyUp (int keycode) 
	{
      /*// Reset game world
      if (keycode == Keys.R) 
      {
    	  	init();
    	  	Gdx.app.debug(TAG, "Game world resetted");
      }*/
      // Toggle camera follow
      if (keycode == Keys.ENTER) 
      {
        cameraHelper.setTarget(cameraHelper.hasTarget()
        		? null: level.tim);
        /*Gdx.app.debug(TAG, "Camera follow enabled: "
        			+ cameraHelper.hasTarget());*/
      }
      
      //added p234 denny fleagle
      //Back to menu
      /*else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
      {
    	  backToMenu();
      }
      */
      return false;
	}
	

	private void handleDebugInput(float deltaTime)              //used only for debbuging camera
	{
			if (Gdx.app.getType() != ApplicationType.Desktop) return;
			
			
			
			if (!cameraHelper.hasTarget(level.tim)) {
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
    	  
    	   Vector2 origin = new Vector2();
    	   BodyDef bodyDef2 = new BodyDef();
		   bodyDef2.type = BodyType.StaticBody;
		   bodyDef2.position.set(new Vector2(0f, -10f));
		   Body body1 = b2world.createBody(bodyDef2); 
		   EdgeShape boundary = new EdgeShape();
		   boundary.set(new Vector2(0f, 0f), new Vector2(128f, 0));
		   FixtureDef fixtureDef2 = new FixtureDef();
		   fixtureDef2.shape=boundary;
		   body1.createFixture(fixtureDef2); 
		   boundary.dispose();
    	   

    	 
    	   // Timmy
    	   BodyDef bodyDef = new BodyDef();
		   bodyDef.type = BodyType.DynamicBody;
		   bodyDef.fixedRotation=true;
		   bodyDef.position.set(level.tim.position);
		   bodyDef.linearVelocity.set(new Vector2(0,0)); 
		   Body body = b2world.createBody(bodyDef);
		   body.setUserData(level.tim);
    	   level.tim.body=body; 
    	   
    	   PolygonShape polygonShape = new PolygonShape();
    	   origin.x = level.tim.dimension.x/2;
	       origin.y = level.tim.dimension.y/2;
	       polygonShape.setAsBox(level.tim.dimension.x/2.5f, level.tim.dimension.y/2.5f, origin, 0);
	       
	       FixtureDef fixtureDef = new FixtureDef();
	       fixtureDef.shape = polygonShape;
	       fixtureDef.density =30;
	       fixtureDef.restitution = 0.1f;
	       fixtureDef.friction = 0.1f;
	       body.createFixture(fixtureDef);
	       
	       polygonShape.setAsBox(0.3f, 0.3f, new Vector2(0.5f,-0.1f), 0);
	       fixtureDef.isSensor=true; 
	       Fixture footSensor= body.createFixture(fixtureDef);
	       footSensor.setUserData(3);
	       polygonShape.dispose();
	       
	       BodyDef circle = new BodyDef(); 
	       circle.type= BodyType.DynamicBody; 
	       circle.position.set(level.tim.position.x+1, level.tim.position.y+1); 
	       Body launcher = b2world.createBody(circle);
	       CircleShape cir = new CircleShape();
	       cir.setRadius(.1f);
	       fixtureDef.shape = cir; 
	       Fixture circleFixture = launcher.createFixture(fixtureDef); 
	       
	       RevoluteJointDef joint1 = new RevoluteJointDef();
	       joint1.initialize(body, launcher, body.getWorldCenter()); 
	       joint1.enableMotor=true;
	       joint1.maxMotorTorque=150; 
	       joint1.motorSpeed=0; 
	       joint =(RevoluteJoint) b2world.createJoint(joint1); 
	       
	       
	       
	       
	       
	       createPlatforms();
	       
    }
	
	



	@SuppressWarnings("deprecation")
	private void createPlatforms()
	{	
		
		Vector2 origin = new Vector2();
 	   	for (Platform plat : level.platforms) 
 	   	{
 	   		    
 	   	     
 	   		   BodyDef bodyDef = new BodyDef();
 	   		   bodyDef.type = BodyType.KinematicBody; 
 	   		   bodyDef.position.set(plat.position);
 	   		   Body body = b2world.createBody(bodyDef);
 	   		   //body.setUserData(platform[i]);
 	   		   plat.body = body;
 	       
 	   		   PolygonShape polygonShape = new PolygonShape();
 	   		   origin.x = plat.bounds.width / 2.0f;
 	   		   origin.y = plat.bounds.height / 2.0f;
 	   		   polygonShape.setAsBox(plat.bounds.width/ 2.0f, plat.bounds.height/ 2.0f, origin, 0);
 	       
 	   		   FixtureDef fixtureDef = new FixtureDef();
 	   		   fixtureDef.shape = polygonShape;
 	   		   body.createFixture(fixtureDef);
 	   		   polygonShape.dispose();
 	   		    
 	   	   
 	        
 	   }
		
		
		
	}
	
	
	
		
	

}
