package com.mygdx.util;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.objects.AbstractGameObject; 

/**
 * Camera helper that helps move the actual camera to the correct position 
 * for rendering frames
 * @author Adam Standke
 *
 */
public class CameraHelper 
{
	private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private Vector2 position;
    private float zoom;
    private AbstractGameObject target;
    private final float FOLLOW_SPEED = 4.0f;
    
    /**
	 * Constructor that creates a new position vector for where the 
	 * camera should be positioned in the game and sets the camera's zoom  
	 */
    public CameraHelper () 
    {
    	  position = new Vector2();
    	  zoom = 2.0f;
    }
    
    /**
	 * Method that updates the camera's position based some previous action done 
	 * in the game 
	 * @param deltaTime
	 */
    public void update (float deltaTime) 
    {
    	  if (!hasTarget()) return;
    	  //the linear interpolation function will smoothen the cameras transition
  		  //as it follow the target object at the given follow speed
    	  position.lerp(target.position,  FOLLOW_SPEED * deltaTime);
    	  // Prevent camera from moving down too far
    	  position.y = Math.max(0,position.y);
    }
    
    /**
	 * set the position of the camera
	 * @param x
	 * @param y
	 */
    public void setPosition (float x, float y) 
    {
    	  this.position.set(x, y);
    }
    /**
	 * get the position of the camera
	 * @return
	 */
    public Vector2 getPosition () { return position; }
    /**
	 * add zoom to the camera
	 * @param amount
	 */
    public void addZoom (float amount) { setZoom(zoom + amount); }
    /**
	 * Set the zoom of the camera
	 * @param zoom
	 */	
    public void setZoom (float zoom) 
    {
    	  this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }
    /**
	 * get the zoom of the camera
	 * @return
	 */
    public float getZoom () { return zoom; }
    /**
	 * Set the target to point at a specific game object
	 * @param target
	 */
    public void setTarget (AbstractGameObject target) { this.target = target; }
    /**
	 * Get the target of the camera
	 * @return target
	 */
    public AbstractGameObject getTarget () { return target; }
    /**
	 * ask if the target is null
	 * @return true if the target is null
	 */
    public boolean hasTarget () { return target != null; }
    /**
	 * ask the camera if the game object is the target
	 * @param target
	 * @return
	 */
    public boolean hasTarget (AbstractGameObject target) 
    {
    	  return hasTarget() && this.target.equals(target);
    }
    /**
	 * Method that is called by worldRender at the beginning  
	 * of its render method to position the actual camera in the right
	 * area for rendering the frame
	 * @param camera
	 */
    public void applyTo (OrthographicCamera camera) 
    {
    	  camera.position.x = position.x;
    	  camera.position.y = position.y;
    	  camera.zoom = zoom;
    	  camera.update();
    }
    
    
    
}
