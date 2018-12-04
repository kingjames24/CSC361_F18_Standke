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
	
	

	public HighScore(Game game) {
		super(game);
		
	}
	
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(30, 144, 255, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
	    stage.draw();
	}
	
	public void resize(int width, int height) 
	{
		 stage.getViewport().setScreenSize(width, height); 	
	}

	public void show() 
	{
		
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		rebuildStage(); 
	}

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

	private void sort(int score) 
	{
		
				
				bubble(score); 
				return; 
			
	
		
		
	}
	
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
	

	

	private Table buildBackgroundLayer() 
	{
		Table layer= new Table(); 
		imgBackground = new Image(skinRainMaker, "background");
		imgBackground.scaleBy(.1f);
		layer.add(imgBackground).width(Constants.VIEWPORT_GUI_WIDTH).height(Constants.VIEWPORT_GUI_HEIGHT);
		return layer; 
		
	}
	
	public class KeyPair
	{
		public String key; 
		public int value; 
		
		public KeyPair(String key, int value)
		{
			this.key=key; 
			this.value=value; 
		}
		
		public String toString()
		{
			return String.format(key+ ":    "+value); 
		}
		
	}

	@Override
	public void hide() 
	{
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		HighScoreList.topTenPlayers.clear();
		
	}

	@Override
	public void pause() {}
}
