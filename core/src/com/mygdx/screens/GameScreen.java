package com.mygdx.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;

/**
 * This handles the events that could take place
 * when the game is running.
 * Adam Standke
 * 
 */
public class GameScreen extends AbstractGameScreen 
{
	
	public GameScreen(Game game) 
	{
		super(game);
		
		
	}
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	/**
	 * Method that shows the game screen, by instantiating two of the
	 * most important classes; namely, WorldController which handles the 
	 * game's logic and WorldRender, which handles the actual rendering of 
	 * the frames to the user's display. Called by the Game class as part of 
	 * LIBGdx's application life cycle 
	 */
	@Override
	public void show() 
	{
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		 
		
	}
	
	/**
	 * Method that forms the game loop of the application that is constantly called by 
	 * the Game class while the game is in progress. During which, the game logic is updated
	 * for the current frame based on previous action taken in previous frame. Then the updated 
	 * actions for the current frame are rendered to screen  
	 */
	@Override
	public void render(float delta) 
	{
		if (!paused)
		{			
			worldController.update(delta);
		}
		
		
		Gdx.gl.glClearColor(0, 0, 0, 0xff / 255.0f);
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		worldRenderer.render();
	}

	/**
	 * Method that resizes the screen
	 */
	@Override
	public void resize(int width, int height) 
	{
		worldRenderer.resize(width, height);
		
	}
	/**
	 * Method that pauses the game 
	 */
	@Override
	public void pause() 
	{
		paused = true;
	}
	/**
	 * Method that resume the game after being paused
	 */
	@Override
	public void resume() 
	{
		paused=false; 
		
	}
	/**
	 * Method that hides the gamescreen when the player 
	 * clicks to display the menuscreen
	 */
	@Override
	public void hide() 
	{
		worldRenderer.dispose();
	    if(WorldController.b2world !=null) 
	    {
	    	WorldController.b2world.dispose();
	    	WorldController.b2world=null; 
	    }
		
	}
	
	

}
