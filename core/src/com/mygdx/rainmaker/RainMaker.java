package com.mygdx.rainmaker;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.screens.GameScreen;

public class RainMaker extends Game{
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () 
	{
		
		setScreen(new GameScreen()); 
	}

	
}
