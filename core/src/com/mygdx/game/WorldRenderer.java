package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.objects.Ability;
import com.mygdx.util.Constants;


/**
 * Class that handles that actual rendering of the game to the user's screen
 * @author adam
 *
 */
public class WorldRenderer implements Disposable 
{
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private Box2DDebugRenderer b2debugRenderer;
	
	/**
	 * Constructor that takes in an object of the WorldController class and also 
	 * calls a helper method to create the 3d camera to be used to render frames
	 * @param worldController
	 */
	public WorldRenderer (WorldController worldController) 
	{ 
		this.worldController = worldController;
		init();
	}
	
	/**
	 * Method that creates an object of the SpriteBatch class that creates a vertex array  
	 * of 20000 spites and uses the default shader. Method also creates an orthographic camera
	 * with a given viewport height and width and then recalculates the projection and camera-view matrix
	 * from an initial 3d world coordinate position.   
	 */
	private void init () 
	{ 
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	/**
	 * Helper method that is called by the GameScreen class
	 */
	public void render () 
	{ 
		renderWorld(batch);
		
	}
	/**
	 * Method that render's the game to the screen by composing the
	 * camera-view matrix with the orthographic projection matrix to get
	 * a 2d rendered image to appear on the user's screen. After doing so, 
	 * SpriteBatch's batch.begin() and end() method are called to allow each 
	 * object class to handle its rendering through SpriteBatch's draw method.     
	 * @param batch
	 */
	public void renderWorld (SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		worldController.rain.render(batch);
		if(Ability.fire)
		{
			worldController.ability.render(batch);
		}
		batch.end();
		
		if (DEBUG_DRAW_BOX2D_WORLD)
		{
			b2debugRenderer.setDrawAABBs(false);
			b2debugRenderer.setDrawVelocities(false);
			b2debugRenderer.AABB_COLOR.set(Color.BLACK); 
			b2debugRenderer.VELOCITY_COLOR.set(Color.BLACK); 
			b2debugRenderer.render(worldController.b2world, camera.combined);
		}
			
	}

	/**
	 * Method that deallocates the vertex array associated with the sprite batch and 
	 * deallocates the shapes drawn by Box2d by openGl  
	 */
	@Override
	public void dispose() 
	{
		batch.dispose();
		b2debugRenderer.dispose();
		
	}
	
	/**
	 * Method that resizes the camera's viewport width
	 */
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
	}

}
