package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Iterator;
import java.util.Random;

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
import com.mygdx.screens.MenuScreen;
import com.mygdx.screens.HighScore.KeyPair;
import com.mygdx.util.AudioManager;
import com.mygdx.util.CameraHelper;
import com.mygdx.util.Constants;
import com.mygdx.util.GamePreferences;
import com.mygdx.util.HighScoreList;
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
	
	
	public CameraHelper cameraHelper;
    public static World b2world;
    public static int numFootContacts;
    public int jumpTimeout;
    public static int shootTimeout;
    public static boolean goalReached;
    public boolean attached; 
    public RevoluteJoint joint;
    public RevoluteJoint joint2; 
    public Raindrops rain; 
    public Level level;
    public static int score;
	public static int lives;
	public static int health; 
	private int first;
	private Game game;
	private int soundTimeOut;
	public static float timeLeftGameOverDelay;
	private HighScoreList high;
	public static boolean visible; 
     
    
    
    /**
     * Constructor that takes in an object of the Game Class
     * and then calls a helper method to set up the game's
     * logic/gameplay
     * @param game an object of the Game class that determines how the application should operate
     */
	public WorldController(Game game) 
	{
		this.game=game; 
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
	public void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
	    high = HighScoreList.instance;
		high.load();
		goalReached=false;
		lives=3; 
		attached=false; 
		visible=false;
		timeLeftGameOverDelay=0; 
		numFootContacts=0;
		shootTimeout=15;
		jumpTimeout=0;
		initlevel(); 
		
		
		
		 
		
	}
	/**
	 * Method that creates the game's level by instantiating a new level
	 * Class. Method also uses the camera helper to focus only on the player 
	 * and calls a helper method to create the Box2d bodies for Timmy
	 */
	private void initlevel()
	{
		if (b2world != null) 
		{
			b2world.dispose();
		}
		b2world = new World(new Vector2(0, -5f), true);
 	   	b2world.setContactListener(new MyContactListener());
		score=0; 
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.tim);
		initPhysics();
		rain= new Raindrops(50);
		
	}
	
	/**
	 * Method that is continuously called and updates the game
	 * logic based on what the player did in the previous frame
	 * @param deltaTime a float that represents the time span between
	 * the previously rendered frame and the currently rendered frame
	 */
	public void update(float deltaTime)
	{
	
		
		
		handleInputGame(deltaTime);
	
		
		level.update(deltaTime);
		b2world.step(deltaTime, 8, 3);
		
		jumpTimeout--;
		shootTimeout--;
		soundTimeOut--; 
	
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
			int x= Ability.abilityScheduledForRemoval.size;
			for(int j=0; j<x; j++)
			{
			
				Ability point= Ability.abilityScheduledForRemoval.pop(); 
				if(point!=null)
				{
					if(point.hit)
					{ 
						point.body.getWorld().destroyBody(point.body);
						 
						
					}
					
				}
				
			}
			Ability.abilityScheduledForRemoval.clear();
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
					visible=true; 
				}
			}
			Star.starScheduledforRemoval=null; 
		}		
		cameraHelper.update(deltaTime);
		level.people.updateScrollPosition(cameraHelper.getPosition());
		healthStatus(); 
		if ((!isGameOver() && didTimmyfall())||(!isGameOver() && isTimmyDead()))//portion of update that handles the state of game
		{
			
			AudioManager.instance.play(Assets.instance.sounds.death, 1f);
			lives--;
			visible=false;
			if (isGameOver())
			{
				
				high.load();
				high.getScore(score);
				high.save(high.login, score);
				level.tim.dead=true;
				level.tim.jumping=false; 
				level.tim.runningLeft=false; 
				level.tim.runningRight=false; 
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER; 
				
			}
			else
			{
					
				initlevel();
			
			}
			
		}
		if (isGameOver() || goalReached)
		{
			Gdx.input.setInputProcessor(null);
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0)
			{
				if(goalReached)
				{
				   
					high.load();
					high.getScore(score+300);	
					high.save(high.login, score);
				}
				return; 
			}
			 
		}
		
		
		
	}
	
	/**
	 * Method that handles any player input from a peripheral device, such 
	 * as a keyboard/mouse. Depending on what the user pressed, Timmy will take
	 * a certain action in the game, such as moving left, right, jumping, and/or shooting
	 * at an object and a certain audio sound will be played 
	 * @param deltaTime a float that represents the time span between
	 * the previously rendered frame and the currently rendered frame
	 */
	private void handleInputGame (float deltaTime) 
	{
		 if (isGameOver() || goalReached) return; 
	     if (cameraHelper.hasTarget(level.tim)) 
	     {
	       // Player Movement
	       float sprMoveSpeed = 1;
	       if (Gdx.input.isKeyPressed(Keys.A)) 
	       {
	    	   
	    	   
	    	   if(canWalkNow())
	    	   {
	    		   soundTimeOut=10;
	    		   level.people.timmyLeft(false);
		    	   level.tim.left=true;
		    	   
		    	   level.tim.runningLeft=true; 
		    	   level.tim.runningRight=false;
		    	   
		    	   level.tim.body.applyLinearImpulse(new Vector2(-sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	    	   }
	    	   
	       } 
	       else if (Gdx.input.isKeyPressed(Keys.D)) 
	       {
	    	  
	    	  
	    	   
	    	   if(canWalkNow())
	    	   {
	    		   soundTimeOut=10; 
	    		   level.people.timmyLeft(true);
		    	   level.tim.left=false;
		    	   
		    	   level.tim.runningLeft=false; 
		    	   level.tim.runningRight=true; 
		    	   
		    	   level.tim.body.applyLinearImpulse(new Vector2(sprMoveSpeed, 0), level.tim.body.getWorldCenter(), true);
	    	   }
	       }   
	       // Tim Jump
	       else if ( Gdx.input.isKeyPressed(Keys.W))
	       {
	    	   
	    	   if(canJumpNow())
	    	   {
	    		   AudioManager.instance.play(Assets.instance.sounds.jump);
	    		   level.tim.body.applyLinearImpulse(new Vector2(0,level.tim.body.getMass()*4f), level.tim.body.getWorldCenter(), true);
		    	   jumpTimeout=15;
		    	   
	    	   }
	    	  
	       }
	       else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
	       {
	    	   int x = Gdx.input.getX(); 
	    	   int y = Gdx.input.getY();
	    	   
	    	  
	    	   if(Star.collected)
	    	   {
	    		   
	    		   if(shootTimeout>0)
	    		   {
	    			  level.tim.shooting=false; 
	    			   return;
	    		   }
	    		   if(first==0)
	    		   {
	    			   AudioManager.instance.play(Assets.instance.sounds.explode, 1f);
	    			   level.ability.particle=false; 
	    			   level.tim.shooting=true; 
	    			   level.ability.createBody(level.tim.body.getPosition());
	    			   Vector3 screen = new Vector3(x,y,0); 
		    		   Vector3 world = WorldRenderer.camera.unproject(screen);
		    		   Vector2 camera = new Vector2(world.x, world.y);
		    		   Vector2 launcher=joint.getBodyB().getPosition();
		    		   Vector2 distance = new Vector2(); 
		    		   distance.x= camera.x-launcher.x; 
		    		   distance.y= camera.y-launcher.y; 
		    		   level.ability.body.setTransform(joint.getBodyB().getPosition(), 0);
		    		   level.ability.body.setLinearVelocity(distance);
		    		   level.ability.body.setGravityScale(0);
		    		   level.ability.setFire(true); 
		    		   shootTimeout=120;
		    		   
	    		   }
	    		   else
	    		   {
	    			   AudioManager.instance.play(Assets.instance.sounds.explode, 1f);
	    			   level.ability.particle=false; 
	    			   level.tim.shooting=true;
	    			   level.ability.createBody(level.tim.body.getPosition());
	    			   Vector3 screen = new Vector3(x,y,0); 
		    		   Vector3 world = WorldRenderer.camera.unproject(screen);
		    		   Vector2 camera = new Vector2(world.x, world.y);
		    		   Vector2 launcher=joint.getBodyB().getPosition();
		    		   Vector2 distance = new Vector2(); 
		    		   distance.x= camera.x-launcher.x; 
		    		   distance.y= camera.y-launcher.y; 
		    		   level.ability.body.setTransform(joint.getBodyB().getPosition(), 0);
		    		   level.ability.body.setLinearVelocity(distance);
		    		   level.ability.body.setGravityScale(0);
		    		   level.ability.setFire(true); 
		    		   shootTimeout=120;
		    		    
	    		   }
	    		  
	    	   
	    	   }
	    	 
	       }
	       else
	       {
	    	   
	    	   level.tim.runningLeft=false; 
	    	   level.tim.runningRight=false; 
	    	   
	       }
	     }
	 }
	
	/**
	 * Method that determines whether Timmy can jump using Box2d's user data system and is used to tell
	 * if Timmy is actually  on a platform and not in the air. During platform creation a user Data tag is attached 
	 * to its fixture, namely the String 2. Underneath Timmy's foot is a tiny sensor that has a data tag 
	 * attached to it of String 3. This method is initially called when user input is put in(ie., jump pressed). 
	 * So long as Timmy's sensor and the user data of the platform match that means Timmy is on the ground 
	 * and can jump. Prevents air jumping. Also used to add a timeout to jumping, so that physcis engine can take 
	 * a break.  
	 * @return a boolean that means that tim can either jump or not
	 */
	private boolean canJumpNow() 
	{
		if(jumpTimeout>0)return false; 
		Iterator<String> it = MyContactListener.fixturesUnderFoot.iterator();
		while(it.hasNext())
		{
			String fix = it.next(); 
			String userDataTag =fix;  
			if (userDataTag=="2")
			{
				return true; 
			}
		}
		return false;
	}
	
	/**
	 * Method that determines whether Timmy can run using Box2d's user data system and is used to tell
	 * if Timmy is actually walking on a platform. During platform creation a user Data tag is attached 
	 * to its fixture, namely the String 2. Underneath Timmy's foot is a tiny sensor that has a data tag 
	 * attached to it of String 3. This method is initially called when user input is put in(ie., walking) 
	 * and if is also used to do a sound time out so, Tim's walking speed it not too fast.Method also prevents
	 * air walking.      
	 * @return a boolean that means that tim can either walk or not
	 */
	private boolean canWalkNow()
	{
		Iterator<String> it = MyContactListener.fixturesUnderFoot.iterator();
		while(it.hasNext())
		{
			String fix = it.next(); 
			String userDataTag =fix;  
			if (userDataTag=="2")
			{
				   if(soundTimeOut>0)
		    	   {
		    		   ; 
		    	   }
		    	   else
		    	   {
		    		   AudioManager.instance.play(Assets.instance.sounds.walk, 1f);
		    	   }
				 
				return true; 
			}
		}
		return false;
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
		   bodyDef2.type = BodyType.StaticBody;//static body type for boundary
		   bodyDef2.position.set(new Vector2(0f, -10f));//set 10 meters below scene
		   Body body1 = b2world.createBody(bodyDef2); 
		   EdgeShape boundary = new EdgeShape();//use an edge as shape
		   FixtureDef fixtureDef2 = new FixtureDef();
		   fixtureDef2.shape=boundary;
		   boundary.set(new Vector2(0f, 0f), new Vector2(128f, 0));//extends 128 meters in length
		   Fixture data1= body1.createFixture(fixtureDef2);
		   data1.setUserData((Object)("10"));
		   boundary.dispose();
		   
		  
    	   BodyDef bodyDef3 = new BodyDef();
		   bodyDef3.type = BodyType.StaticBody;//static body type for boundary
		   bodyDef3.position.set(new Vector2(0f, 10f));//set 10 meters above scene
		   Body body2 = b2world.createBody(bodyDef3); 
		   EdgeShape boundary1 = new EdgeShape();//use an edge as shape
		   FixtureDef fixtureDef3 = new FixtureDef();
		   fixtureDef3.shape=boundary1;
		   boundary1.set(new Vector2(0f, 0f), new Vector2(128f, 0));//extends 128 meters in length
		   Fixture data2 =body2.createFixture(fixtureDef3);
		   data2.setUserData((Object)"10");
		   boundary1.dispose();
		   
		   
		  
    	   BodyDef bodyDef4 = new BodyDef();
		   bodyDef4.type = BodyType.StaticBody;//static body type for boundary
		   bodyDef4.position.set(new Vector2(-2f, 0f));//set 10 meters to the left scene
		   Body body4 = b2world.createBody(bodyDef4); 
		   EdgeShape boundary2 = new EdgeShape();//use an edge as shape
		   FixtureDef fixtureDef4 = new FixtureDef();
		   fixtureDef4.shape=boundary2;
		   boundary2.set(new Vector2(0f, -10f), new Vector2(0, 10f));//extends 128 meters in length
		   Fixture data3 =body4.createFixture(fixtureDef4);
		   data3.setUserData((Object)"10");
		   boundary2.dispose();
		   
		   BodyDef bodyDef5 = new BodyDef();
		   bodyDef5.type = BodyType.StaticBody;//static body type for boundary
		   bodyDef5.position.set(new Vector2(128f, 0f));//set 10 meters to the left scene
		   Body body5 = b2world.createBody(bodyDef5); 
		   EdgeShape boundary3 = new EdgeShape();//use an edge as shape
		   FixtureDef fixtureDef5 = new FixtureDef();
		   fixtureDef5.shape=boundary3;
		   boundary3.set(new Vector2(0f, -10f), new Vector2(0, 10f));//extends 128 meters in length
		   Fixture data4 = body5.createFixture(fixtureDef5);
		   data4.setUserData((Object)("10"));
		   boundary3.dispose();
		   
		   
		   
		   
    	   // Timmy's main body 
    	   BodyDef bodyDef = new BodyDef();
		   bodyDef.type = BodyType.DynamicBody;//dynamic body type for main character 
		   bodyDef.fixedRotation=true;
		   bodyDef.position.set(level.tim.position);//the level design sets the body's position
		   bodyDef.linearVelocity.set(new Vector2(0,0)); 
		   Body body = b2world.createBody(bodyDef);
		   body.setUserData(level.tim);//body is given main character's info for later querying the world
    	   level.tim.body=body; 
    	   PolygonShape polygonShape = new PolygonShape();
    	   origin.x = level.tim.dimension.x/2;
	       origin.y = level.tim.dimension.y/2;
	       polygonShape.setAsBox(level.tim.dimension.x/2.5f, level.tim.dimension.y/2.5f, origin, 0);
	       FixtureDef fixtureDef = new FixtureDef();
	       fixtureDef.shape = polygonShape;
	       fixtureDef.density =20;//set the main character's weight to 20 kg/m^2
	       fixtureDef.restitution = 0.1f;//low restitution to not make the main character bounce
	       fixtureDef.friction = 0.1f;//low friction so the main character slides on the platform
	       fixtureDef.filter.groupIndex=-1;
	       body.createFixture(fixtureDef);
	       //Timmy's foot-sensor to disallow him from jumping while in the air
	       polygonShape.setAsBox(0.4f, 0.1f, new Vector2(0.5f,.1f), 0);//fixture sits right below main characters hit box 
	       fixtureDef.isSensor=true; 
	       Fixture footSensorFixture = body.createFixture(fixtureDef);
	       footSensorFixture.setUserData((Object)"3");
	       polygonShape.dispose();
	       //Timmy's fire-launcher that allows him to fire an object when the star is collected 
	       BodyDef circle = new BodyDef(); 
	       circle.type= BodyType.DynamicBody; //launcher is a dynamic body type
	       circle.position.set(level.tim.position.x+1, level.tim.position.y+1); 
	       Body launcher= b2world.createBody(circle);
	       CircleShape cir = new CircleShape();//launcher is a circle shape 
	       cir.setRadius(.1f);
	       fixtureDef.shape = cir;
	       fixtureDef.isSensor=true; 
	       launcher.createFixture(fixtureDef);
	       cir.dispose();
	       //A revoluteJoint is created between timmy and the launcher 
	       RevoluteJointDef joint1 = new RevoluteJointDef();//create a revolute joint between launcher and timmy
	       //joint1.initialize(body, launcher, body.getWorldCenter());
	       joint1.bodyA=body; 
	       joint1.bodyB=launcher; 
	       joint1.localAnchorA.set(1f, 1f);//main character's anchor is located at the top right corner of body 
	       joint1.localAnchorB.set(.1f, 0f);//launcher's anchor is located at the radi point of the circle
	       joint1.collideConnected=false;//set to false so that joint bodies should not affect each other  
	       joint1.enableMotor=true;//creates a joint moter with the anchors to make the launcher continuously rotate
	       joint1.maxMotorTorque=150;//max motor speed for Timmy's launcher measured in N-m 
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
 	   		   bodyDef.type = BodyType.KinematicBody;//each platform is a kinematic body type 
 	   		   bodyDef.position.set(plat.position);
 	   		   Body body = b2world.createBody(bodyDef);
 	   		   plat.body = body;
 	   		   PolygonShape polygonShape = new PolygonShape();//platform's shape is determined by a rectangular bounding box
 	   		   origin.x = plat.bounds.width / 2.0f;//platform's origin is half of the platform's rectangular bounding box
 	   		   origin.y = plat.bounds.height / 2.0f;
 	   		   polygonShape.setAsBox(plat.bounds.width/ 2.0f, plat.bounds.height/ 2.0f, origin, 0);
 	   		   FixtureDef fixtureDef = new FixtureDef();
 	   		   fixtureDef.shape = polygonShape;
 	   		   Fixture senor = body.createFixture(fixtureDef);
 	   		   senor.setUserData((Object)("2"));
 	   		   polygonShape.dispose();
 	   		        
 	   }
		
	}
	/**
	 * Method that get's the main player's health during the game
	 */
	public void healthStatus()
	{
		health=level.tim.getLife(); 
	}
	
	/**
	 * Method that updates the player's score as it collects points in the game
	 * @param point an object that represents things timmy can collect in the game
	 */
	public void updateScore(Points point)
	{
		score+=point.getScore(); 
		Gdx.app.log("10 points added collected",":)");
	}
	/**
	 * Method that keeps track of whether the player fell off a platform
	 * If so, true will be returned and the game starts over
	 * @return a boolean that represents whether Timmy fell or not
	 */
	public boolean didTimmyfall() 
	{
		    return level.tim.position.y < -4;
	}
	
	/**
	 * Method that keeps track of whether the game is over due to the
	 * player exhausting the number of lives allowed
	 * @return a boolean that represents whether Timmy has no more lives
	 */
	public static boolean isGameOver ()
	{
			
		   return lives < 0;
	}
	
	/**
	 * Method that keeps track of whether Timmy died from being hit by too many 
	 * rain drops
	 * @return a boolean that represents whether timmy took too many hits from the rain
	 */
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
