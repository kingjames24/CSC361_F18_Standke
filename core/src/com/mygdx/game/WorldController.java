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


/**
 * Class that controls all of the game logic of the game
 * @author adam
 *
 */
public class WorldController extends InputAdapter implements Disposable
{
	private Game game;
	public CameraHelper cameraHelper;
    public static World b2world;
    public static int numFootContacts=0;
    public int jumpTimeout=0;
    public int shootTimeout=15; 
    public boolean attached=false; 
    public RevoluteJoint joint;
    public RevoluteJoint joint2; 
    public Raindrops rain;
    public Ability ability; 
    public Level level;
    public int score;
	private int lives=3;
	private int first; 
     
    
    
    /**
     * Constructor that takes in an object of the Game Class
     * and then calls a helper method to set up the game's
     * logic/gameplay
     * @param game an object of the Game class that determines how the application should operate
     */
	public WorldController(Game game) 
	{
		this.game = game;
		init();
	}
	/**
	 * Helper method called by the constructor that does the following:
	 * It uses Gdx input module and tells it that this class will handle all
	 * input events. It creates the camerahelper that will be used to track
	 * where the player is in the game world. It creates the actual box2d world, with
	 * a default type of gravity and sets the world's event hander to a custom class. 
	 * It calls another helper method that actually creates the game's level. And finally, 
	 * it instantiates two objects; namely an object from the Raindrop class and an object
	 * from the Ability class.     
	 */
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
		
		 
		
	}
	/**
	 * Method that creates the game's level by instantiating a new level
	 * Class. Method also uses the camera helper to focus only on the player 
	 * and calls a helper method to create the Box2d bodies for Timmy
	 */
	private void initlevel()
	{
		score=0; 
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.tim);
		initPhysics();
		
	}
	
	/**
	 * Method that is continuously called and updates the game
	 * logic based on what the player did in the previous frame
	 * @param deltaTime a float that represents the time span between
	 * the previously rendered frame and the currently rendered frame
	 */
	public void update(float deltaTime)
	{
		handleDebugInput(deltaTime);
		
		handleInputGame(deltaTime);
		
	
		
		
		level.update(deltaTime);
		b2world.step(deltaTime, 8, 3);
		jumpTimeout--;
		shootTimeout--; 
		
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
						updateScore(point); 
						point.body.getWorld().destroyBody(point.body);
						
					}
					
				}
				
			}
			Points.pointScheduledForRemoval.clear();
		}
		if(!b2world.isLocked()) 
		{
			Star star = Star.starScheduledforRemoval; 
			if(star != null)
			{
				if(Star.collected)
				{
					star.body.getWorld().destroyBody(star.body);
				}
			}
			Star.starScheduledforRemoval=null; 
		}		
		cameraHelper.update(deltaTime);
		level.people.updateScrollPosition(cameraHelper.getPosition());
		if (isGameOver() || didTimmyfall()|| isTimmyDead())
		{
			
			lives--;
			if (isGameOver())
			{
				
				init();
			}
			else
			{
				
				init();
			}
			
		}
	}
	
	/**
	 * Method that handles any player input from a peripheral device, such 
	 * as a keyboard/mouse. Depending on what the user pressed, Timmy will take
	 * a certain action in the game, such as moving left, right, jumping, and/or shooting
	 * at an object 
	 * @param deltaTime a float that represents the time span between
	 * the previously rendered frame and the currently rendered frame
	 */
	private void handleInputGame (float deltaTime) 
	{
	     if (cameraHelper.hasTarget(level.tim)) 
	     {
	       // Player Movement
	       float sprMoveSpeed = 1;
	       if (Gdx.input.isKeyPressed(Keys.A)) 
	       {
	    	   if(numFootContacts<1)return;
	    	   level.people.timmyLeft(false);
	    	   level.tim.left=true; 
	    	   level.tim.body.applyLinearImpulse(new Vector2(-sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	       } 
	       else if (Gdx.input.isKeyPressed(Keys.D)) 
	       {
	    	   if(numFootContacts<1)return;
	    	   level.people.timmyLeft(true);
	    	   level.tim.left=false;
	    	   level.tim.body.applyLinearImpulse(new Vector2(sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	       }
	       // Tim Jump
	       if ( Gdx.input.isKeyPressed(Keys.W))
	       {
	    	   if(numFootContacts<1)return;
	    	   if(jumpTimeout>0)return; 
	    	   level.tim.body.applyLinearImpulse(new Vector2(0,level.tim.body.getMass()*2.5f), level.tim.body.getWorldCenter(), true);
	    	   jumpTimeout=15; 
	       }
	       if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
	       {
	    	   int x = Gdx.input.getX(); 
	    	   int y = Gdx.input.getY();
	    	   
	    	  
	    	   if(Star.collected)
	    	   {
	    		   if(shootTimeout>0)return;
	    		   if(first==0)
	    		   {
	    			   ability.createBody(level.tim.body.getPosition());
	    			   Vector3 screen = new Vector3(x,y,0); 
		    		   Vector3 world = WorldRenderer.camera.unproject(screen);
		    		   Vector2 camera = new Vector2(world.x, world.y);
		    		   Vector2 launcher=joint.getBodyB().getPosition();
		    		   Vector2 distance = new Vector2(); 
		    		   distance.x= camera.x-launcher.x; 
		    		   distance.y= camera.y-launcher.y; 
		    		   ability.body.setTransform(joint.getBodyB().getPosition(), 0);
		    		   ability.body.setLinearVelocity(distance);
		    		   ability.body.setGravityScale(0);
		    		   ability.setFire(true); 
		    		   shootTimeout=120; 
	    		   }
	    		   else
	    		   {
	    			   Vector3 screen = new Vector3(x,y,0); 
		    		   Vector3 world = WorldRenderer.camera.unproject(screen);
		    		   Vector2 camera = new Vector2(world.x, world.y);
		    		   Vector2 launcher=joint.getBodyB().getPosition();
		    		   Vector2 distance = new Vector2(); 
		    		   distance.x= camera.x-launcher.x; 
		    		   distance.y= camera.y-launcher.y; 
		    		   ability.body.setTransform(joint.getBodyB().getPosition(), 0);
		    		   ability.body.setLinearVelocity(distance);
		    		   ability.body.setGravityScale(0);
		    		   ability.setFire(true); 
		    		   shootTimeout=120; 
	    		   }
	    		  
	    	   
	    	   }

	       }
	     }
	 }
	
	/**
	 * Method used currently for debugging purposes. When the enter
	 * key is pressed the camera is allowed to follow any object it wants
	 * in the game. Later when the menu screen is created this method will 
	 * either restart the game or return to the menu screen  
	 */
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
      
      //Back to menu
      /*else if(keycode == Keys.ESCAPE || keycode == Keys.BACK)
      {
    	  backToMenu();
      }
      */
      return false;
	}
	
	/**
	 * Method used currently for debugging purposes. When the enter
	 * key is pressed the camera is allowed to follow any object it wants
	 * in the game by using certain input commands. Also, when the camera
	 * is allowed to follow any game object it can also use zoom in or out.  
	 * @param deltaTime a float that represents the time span between
	 * the previously rendered frame and the currently rendered frame
	 */
	private void handleDebugInput(float deltaTime)//used only for debbuging camera
	{
			if (Gdx.app.getType() != ApplicationType.Desktop) return;
		    if (!cameraHelper.hasTarget(level.tim)) 
			{
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
	/**
	 * Method that sets the position of the camera to a certain 
	 * position within the scene 
	 * @param x an int representing the x coordinate
	 * @param y an int representing the y coordinate
	 */
	private void moveCamera (float x, float y) 
	{
	       x += cameraHelper.getPosition().x;
	       y += cameraHelper.getPosition().y;
	       cameraHelper.setPosition(x, y);
	}
	
	/**
     * Method that frees memory that was once 
     * allocated to objects and other things used
     * by Box2d
     */
	@Override
	public void dispose() 
	{
		if (b2world != null) b2world.dispose();
	}
	
	/**
	 * Method that creates two box2d bodies; namely one for Timmy and 
	 * a game boundary for cleanup purposes. First a static edge shaped
	 * geometric entity is created so that all dynamic bodies will be 
	 * eventually destroyed when no longer needed by the game(ie., for memory
	 * deallocation). After doing so, Timmy's dynamic box2d body is then created, in
	 * three stages. The first stage deals with Timmy's main body, will be used to test
	 * collisions with other dynamic objects such as rain drops. The second stage deals
	 * with Timmy's foot-sensor that is situated a .1 meters below timmy's main body. Since
	 * it is a sensor a collision response will be generated and handled by the contactlistener. 
	 * This is used to make sure that Timmy can only jump from static/kinematic objects(which in 
	 * the game are platforms). Lastly, a small circle is attached to  the upper-right hand portion of 
	 * Timmy's main body through the use of revoluteJoint. The small circle is timmy's fire-launcher when 
	 * he has collected the star power up.      
	 */
	private void initPhysics () 
    {
    	   //edgeShapped boundary
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
    	   // Timmy's main body 
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
	       fixtureDef.density =20;
	       fixtureDef.restitution = 0.1f;
	       fixtureDef.friction = 0.1f;
	       body.createFixture(fixtureDef);
	       //Timmy's foot-sensor to disallow him from jumping while in the air
	       polygonShape.setAsBox(0.3f, 0.3f, new Vector2(0.5f,-0.1f), 0);
	       fixtureDef.isSensor=true; 
	       body.createFixture(fixtureDef);
	       //footSensor.setUserData(3);
	       polygonShape.dispose();
	       //Timmy's fire-launcher that allows him to fire an object when the star is collected 
	       BodyDef circle = new BodyDef(); 
	       circle.type= BodyType.DynamicBody; 
	       circle.position.set(level.tim.position.x+1, level.tim.position.y+1); 
	       Body launcher= b2world.createBody(circle);
	       CircleShape cir = new CircleShape();
	       cir.setRadius(.1f);
	       fixtureDef.shape = cir;
	       fixtureDef.isSensor=true; 
	       launcher.createFixture(fixtureDef);
	       cir.dispose();
	       //A revoluteJoint is created between timmy and the launcher 
	       RevoluteJointDef joint1 = new RevoluteJointDef();
	       //joint1.initialize(body, launcher, body.getWorldCenter());
	       joint1.bodyA=body; 
	       joint1.bodyB=launcher; 
	       joint1.localAnchorA.set(1f, 1f); 
	       joint1.localAnchorB.set(.1f, 0f); 
	       joint1.collideConnected=false; 
	       joint1.enableMotor=true;
	       joint1.maxMotorTorque=150; 
	       joint1.motorSpeed=0; 
	       joint =(RevoluteJoint) b2world.createJoint(joint1); 
	       //Creates the box2d bodies for the platforms 
	       createPlatforms();
	       
    }
	/**
	 * Method that creates static box2d bodies for the game's platforms. 
	 */
	private void createPlatforms()
	{	
		Vector2 origin = new Vector2();
 	   	for (Platform plat : level.platforms) 
 	   	{
 	   		   BodyDef bodyDef = new BodyDef();
 	   		   bodyDef.type = BodyType.KinematicBody; 
 	   		   bodyDef.position.set(plat.position);
 	   		   Body body = b2world.createBody(bodyDef);
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
	
	public void updateScore(Points point)
	{
		score+=point.getScore(); 
		Gdx.app.log("10 points added collected", null);
	}
	
	public boolean didTimmyfall() 
	{
		    return level.tim.position.y < -4;
	}
	
	public boolean isGameOver ()
	{
		   return lives < 0;
	}
	
	public boolean isTimmyDead()
	{
		 if(level.tim.life<=0)
		 {
			 lives--; 
			 return true; 
		 }
		 return false; 
	}
	
		
	

}
