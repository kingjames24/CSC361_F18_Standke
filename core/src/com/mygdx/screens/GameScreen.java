package com.mygdx.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;


public class GameScreen implements Screen 
{

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	protected Game game;
	private boolean paused;
	
	
	@Override
	public void show() 
	{
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		
	}

	@Override
	public void render(float delta) 
	{
		if (!paused)
		{			
			worldController.update(delta);
		}
		
		
		Gdx.gl.glClearColor(0, 0, 0, 0xff / 255.0f);
		
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() 
	{
		paused = true;
	}

	@Override
	public void resume() 
	{
		paused=false; 
		
	}

	@Override
	public void hide() 
	{
		worldRenderer.dispose();
	    Assets.instance.dispose();
		
	}

	@Override
	public void dispose() 
	{
		worldRenderer.dispose();
	    Assets.instance.dispose();
		
	}

}
