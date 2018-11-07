package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Assets;
/**
 * Class that represents the platform object that 
 * the player jumps to and from
 * @author adam
 *
 */
public class Platform extends AbstractGameObject 
{
	private TextureRegion platform; 
	private int length;
	public Rectangle bounds;
	
	private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUDE = 1f;
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;
    
    public Vector2 linearVelocity = new Vector2(); 
	
	 
    /*
     * Constructor that is
     * calls the actual helper method
     * init() which initializes 
     * the construction of the platform object 
     */
    public Platform()
    {
    	 
    	init(); 
    }
    /**
     * Method that sets the dimension of the 
     * platform to be 1 meter wide and 1.5 meters high; 
     * also method pulls in the texture for the platform
     * and determines how big the platform should initially be. Also
     * has a random time assigned to it for floating purposes.
     */
    private void init()
    {
    	dimension.set(1,1.5f);
    	bounds= new Rectangle(); 
    	platform = Assets.instance.plat.middle;
    	
    	setLength(1); 
    	
    	floatingDownwards = false;
   	 	floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
   	 	floatTargetPosition = null;
    }
    
    /**
     * Method that sets the dimension of the platform in the game
     * @param dimension a 2d vector representing the height and width of a
     * platform
     */
    public void setdimension (Vector2 dimension) 
    {
       this.dimension.x=dimension.x; 
       this.dimension.y=dimension.y; 
      		  
    }
    
    /**
     * Method to change the length of a platform
     * @param amount an int that represents how much longer a platform object should be
     */
    public void increaseLength (int amount) 
    {
      setLength(length + amount);
    }
    
    /**
	 * Method that renders the platform image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
    public void render(SpriteBatch batch)
    {
    	TextureRegion reg = null;
    	float relX = 0;
    	float relY = 0; 
        reg = platform; 
        position= body.getPosition();
        
        for (int i = 0; i < length; i++) 
        {
        batch.draw(reg.getTexture(), position.x+ relX, position.y+relY, origin.x, origin.y, dimension.x+ 0.1f, dimension.y,
           		 scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
           		 reg.getRegionWidth(), reg.getRegionHeight(), false, false);
        relX += dimension.x;
        }
      
    }
    /**
     * Method that updates the floating up and down movement of
     * the platforms according to their random floatCycle. Then
     * each platforms' linear velocity is set to make the platforms move 
     * from a range of -1 to 1 meters in the y-direction 
     */
    public void update(float deltaTime) 
    {
 	   
 	   
 	   floatCycleTimeLeft -= deltaTime;
 	   	   
 	   if (floatCycleTimeLeft <= 0)
 	   {
 		   floatCycleTimeLeft = FLOAT_CYCLE_TIME;
 		   floatingDownwards = !floatingDownwards;
 		   body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
 	   }
 	   else
 	   {
 		   body.setLinearVelocity(body.getLinearVelocity().scl(.98f));
 	   }
    }

    /**
     * Method that sets the length of the platform; ie., the 
     * greater the length the longer the platform object
     * @param length an int that represents how much longer the platform object should be
     */
    public void setLength (int length) 
    {
      this.length = length;
      bounds.set(0, 0, dimension.x * length, dimension.y);
      
    }

    /**
	 * Method that creates additional Box2d bodies for a platform 
	 */
	@Override
	public void createBody(Vector2 position) {
		// TODO Auto-generated method stub
		
	}
	

}
