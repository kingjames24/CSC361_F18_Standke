package com.mygdx.rainmaker;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Assets;
import com.mygdx.screens.GameScreen;
import com.mygdx.screens.MenuScreen;

/**
 * Class that is called by the DesktopLauncher to 
 * create the game 
 * @author adam
 *
 */
public class RainMaker extends Game{
	
	/**
	 * Method that forms part of LIBGdx's application life cycle
	 * and is the first method called by the Game class to at the beginning of
	 * the application. This method creates an instance of the AssetManager class
	 * and hands responsibility to the GameScreen class, which manages the game. 
	 */
	@Override
	public void create () 
	{
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
		//setScreen(new GameScreen(this)); 
	}

	
}
