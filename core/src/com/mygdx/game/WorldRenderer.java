package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.util.Constants;



public class WorldRenderer implements Disposable 
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
	private Box2DDebugRenderer b2debugRenderer;
	
	public WorldRenderer (WorldController worldController) 
	{ 
		this.worldController = worldController;
		init();
	}
	
	private void init () 
	{ 
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render () 
	{ 
		renderWorld(batch);
		
	}
	
	public void renderWorld (SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.tim.render(batch);
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

	@Override
	public void dispose() 
	{
		batch.dispose();
		b2debugRenderer.dispose();
		
	}

}
