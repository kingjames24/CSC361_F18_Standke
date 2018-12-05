package com.mygdx.screens;

import java.util.ArrayList;

import java.util.ListIterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Assets;
import com.mygdx.util.Constants;
import com.mygdx.util.GamePreferences;
import com.mygdx.util.HighScoreList;
/**
 * Separate screen that displays user's score if it has logged in and played the game. 
 * Displayed in descending order 
 * @author adam
 *
 */
public class HighScore extends AbstractGameScreen
{

	private Stage stage;
	private FitViewport viewport;
	private Skin skinLibgdx;
	private Skin skinRainMaker;
	private Image imgBackground;
	private Button btnMenuHighScore;
	public ArrayList<KeyPair> pair;
	private GamePreferences name;
	
	
	/**
	 * Basic Constructor that takes an instance of the Game class
	 * and calls its parent to store instance of object
	 * @param game an object that used to transition between screens/scenes in the game  
	 */
	public HighScore(Game game) {
		super(game);
		
	}
	
	/**
	 * Method that renders the scene/screen for the High Score list. Basically, low level calls 
	 * are made to open gl to clear the screen and draw the stage(ie., scene graph)
	 */
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(30, 144, 255, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
	    stage.draw();
	}
	
	/**
	 * Method that resizes the screen based on how the viewport changes
	 */
	public void resize(int width, int height) 
	{
		 stage.getViewport().setScreenSize(width, height); 	
	}

	/**
	 * Method that is called initially by the Game class to show the scene/screen. In doing so, 
	 * a new viewport is constructed using pixels as a measurement and orthographic camera. 
	 * Then a new stage/scene graph is created in which all inputs are re-routed and handled by this
	 * portion of the program. Also a helper method is used to build the actual UI implementation
	 */
	public void show() 
	{
		
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		rebuildStage(); 
	}

	/**
	 * Main method that builds the scene/screen to be displayed on screen. First two Skins are loaded in
	 * used to texture LibGDX widgets. Then an arraylist is created to keep track of the username and score
	 * in a separate inner class. Then the actual UI elements such as layout widgets, scrollpane and stack widget
	 * are added to the stage to display the screen in a certain way
	 */
	private void rebuildStage() 
	{
		skinRainMaker = new Skin(Gdx.files.internal(Constants.SKIN_RainMaker_UI),
				  new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
		     new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		
		pair= new ArrayList<KeyPair>(); 
		Table layerBackground = buildBackgroundLayer();
		Table  HighScoreList = buildHighScoreList();
		Table layerControls = buildControlsLayer();
		
		ScrollPane pane = new ScrollPane(HighScoreList, skinLibgdx, "list" );
		
		Stack stack = new Stack();
	    stack.setFillParent(true);
	    stack.addActor(layerBackground);
	    stack.addActor(pane);
	    stack.addActor(layerControls);
	    stage.addActor(stack);
	    
		
	}
	
	/**
	 * Method that builds the button widget that when pressed goes back to the main menu
	 * @return a table widget to be added to the stage
	 */
	private Table buildControlsLayer() 
	{
		Table layer = new Table(); 
		btnMenuHighScore = new Button(skinRainMaker, "score");
		//btnMenuHighScore.setName("Go Back to Main Menu");
		//btnMenuHighScore.setColor(Color.WHITE);
		layer.add(btnMenuHighScore).align(Align.bottomRight).expand().width(60).height(60);
		btnMenuHighScore.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) 
				{
					onHighScoreClicked(); 
					
				}

				private void onHighScoreClicked() 
				{
					
					game.setScreen(new MenuScreen(game));
				}
				 
			 }); 
		return layer;
	}

	/**
	 * Main method that actually builds the high score list. After creating a title both the 
	 * files handled internally by LibGDX are loaded in(ie., the preferences and the file containing 
	 * people on the high score list). After which the high score file is manipulated to get it in a 
	 * use-able form to store as a key/value pair.
	 * @return a table that contains the high-score list so far
	 */
	private Table buildHighScoreList() 
	{
		Table layer = new Table();
		BitmapFont titleFont = Assets.instance.fonts.defaultNormal; 
	    Label title = new Label("High Score List", new Label.LabelStyle(titleFont, Color.SKY));
		layer.add(title).top();
		layer.row(); 
		
	    name = GamePreferences.instance; 
		name.load();
		
		HighScoreList pref = HighScoreList.instance;
		pref.load();
		
		
		for(int i=0; i<HighScoreList.topTenPlayers.size(); i++)
		{
			String s = HighScoreList.topTenPlayers.get(i);
			String[] hash =  s.split(":");
			String key = hash[0]; 
			int value = Integer.parseInt(hash[1]);
			KeyPair p = new KeyPair(key,value); 
			pair.add(p); 
				
		}
		for(int i=0; i<pair.size(); i++)
		{
			for(int j=1; j<pair.size(); j++)
			{
				KeyPair z = pair.get(i);
				KeyPair s = pair.get(j);
				if (z.key.equals(s.key)&& z.value==s.value)
				{
					pair.remove(j); 
				}
				
			}
		}
		
		sort(HighScoreList.scoreGame);
		
		
		
		ListIterator<KeyPair> it = pair.listIterator();
		while(it.hasNext())
		{
			
				KeyPair p = it.next();
				if(p.key==null || p.key.equals("") || p.key.isEmpty()|| p.key.equals("null")|| p.key.equals("  " ))
				{
					; 
				}
				else
				{
					layer.add(new Label(p.toString(), skinLibgdx,"title", Color.RED));
					layer.row(); 
				}
	
			
		} 
		return layer; 
	}

	/**
	 * Helper method that calls the actual selection sort algorithm
	 * to organize the scores in descending order 
	 * @param score int that represents the user's score for the game
	 */
	private void sort(int score) 
	{
				
				bubble(score); 
				return; 	
		
	}
	
	/**
	 * Method that adds the user's score to the game and sort's the array list
	 * based on top scores so far. Algo selection sort using a dynamic arraylist
	 * @param score int that represents the user's score for the game
	 */
	private void bubble(int score)
	{
		pair.add(new KeyPair(name.login, score));
		int n = pair.size(); 
		for (int j=0; j<n-1; j++)
		{
			int min=j; 
			for(int p=j+1; p<n; p++)
			{
				KeyPair z = pair.get(p);
				KeyPair s = pair.get(min);
				if(z.value>s.value)
				{
					min=p; 
				}
			}
			
			KeyPair temp = pair.get(min); 
			pair.set(min, pair.get(j)); 
			pair.set(j, temp); 
		}
		 
	}
	

	
	/**
	 * Method that build's the sky-line background for the game in which
	 * other objects are laid/stacked on-top of
	 * @return a table widget for the background image 
	 */
	private Table buildBackgroundLayer() 
	{
		Table layer= new Table(); 
		imgBackground = new Image(skinRainMaker, "background");
		imgBackground.scaleBy(.1f);
		layer.add(imgBackground).width(Constants.VIEWPORT_GUI_WIDTH).height(Constants.VIEWPORT_GUI_HEIGHT);
		return layer; 
		
	}
	
	/**
	 * Inner Class that stores the player's name and score together, which
	 * becomes usuefull for sorting later based on the player's score
	 * @author adam
	 *
	 */
	public class KeyPair
	{
		public String key; 
		public int value; 
		
		/**
		 * Constructor that stores the player's name and score for later use
		 * @param key a string that represents the player's login name
		 * @param value an int that represents the player's score
		 */
		public KeyPair(String key, int value)
		{
			this.key=key; 
			this.value=value; 
		}
		/**
		 * Method overrides Object's toString method that will be used 
		 * to format the object's look when displayed on the screen
		 */
		public String toString()
		{
			return String.format(key+ ":    "+value); 
		}
		
	}

	/**
	 * Called by Game Class to hide the screen when player presses the main menu
	 * button. Free's up memory 
	 */
	@Override
	public void hide() 
	{
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		HighScoreList.topTenPlayers.clear();
		
	}
	/**
	 * Method that is useful for mobile systems 
	 */
	@Override
	public void pause() {}
}
