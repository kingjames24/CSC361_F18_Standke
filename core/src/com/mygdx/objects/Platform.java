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
    	dimension.set(1f,.5f);
    	bounds= new Rectangle(); 
    	platform = Assets.instance.plat.middle;
    	
    	setLength(1); 
    	
    	
   	 	
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
