package com.mygdx.rainmaker;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Assets;
import com.mygdx.screens.GameScreen;


public class RainMaker extends Game{
	
	
	@Override
	public void create () 
	{
		Assets.instance.init(new AssetManager());
		setScreen(new GameScreen()); 
	}

	
}
