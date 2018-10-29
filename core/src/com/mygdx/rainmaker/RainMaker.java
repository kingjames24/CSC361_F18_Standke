package com.mygdx.rainmaker;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Assets;
import com.mygdx.screens.GameScreen;


public class RainMaker extends Game{
	
	
	@Override
	public void create () 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new GameScreen()); 
	}

	
}
