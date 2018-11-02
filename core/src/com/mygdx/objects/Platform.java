package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.Assets;

public class Platform extends AbstractGameObject 
{
	private TextureRegion platform; 
	private int length;
	
	private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUDE = 3.0f;
    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;
    
    public Vector2 linearVelocity = new Vector2(); 
	
	 
    
    public Platform()
    {
    	 
    	init(); 
    }
    
    private void init()
    {
    	dimension.set(1,1.5f); 
    	platform = Assets.instance.plat.middle;
    	
    	setLength(1); 
    	
    	floatingDownwards = false;
   	 	floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
   	 	floatTargetPosition = null;
    }
    
    public void setdimension (Vector2 dimension) 
    {
       this.dimension.x=dimension.x; 
       this.dimension.y=dimension.y; 
      		  
    }
    
    
    public void increaseLength (int amount) 
    {
      setLength(length + amount);
    }
    
    public void render(SpriteBatch batch)
    {
    	TextureRegion reg = null;
    	//float relX = 0;
    	//float relY = 0; 
        reg = platform; 
        position= body.getPosition();
        
        for (int i = 0; i < length; i++) 
        {
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y,
           		 scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
           		 reg.getRegionWidth(), reg.getRegionHeight(), false, false);
        }
      
    }
    
    public void update(float deltaTime) //work on fixing this for game to have different moving platforms
    {
 	   
 	   
 	   floatCycleTimeLeft -= deltaTime;
 	   	   
 	   if (floatCycleTimeLeft <= 0)
 	   {
 		   floatCycleTimeLeft = FLOAT_CYCLE_TIME;
 		   floatingDownwards = !floatingDownwards;
 		   body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? 1 : -1));
 	   }
 	   else
 	   {
 		   body.setLinearVelocity(body.getLinearVelocity().scl(.98f));
 	   }
    }

    public void setLength (int length) 
    {
      this.length = length;
      
    }
	

}
