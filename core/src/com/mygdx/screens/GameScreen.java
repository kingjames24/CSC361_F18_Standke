package com.mygdx.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;


public class GameScreen implements Screen 
{

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	
	@Override
	public void show() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) 
	{
		
		
		if (!paused)
		{
					
			//**worldController.update(deltaTime);
		}
		
		
		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed /
				255.0f, 0xff / 255.0f);
		
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//**worldRenderer.render();
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
		
		
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
		
	}

}
