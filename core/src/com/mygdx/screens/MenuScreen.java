package com.mygdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.util.Constants;


public class MenuScreen extends AbstractGameScreen 
{

	private Stage stage;
	private Skin skinRainMaker;
	private Skin skinLibgdx;
	private Image imgBackground;
	private Image imgCloud;
	private Image imgTim;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	private Button btnLogin;
	private Button btnMenuLogin;
	private Button btnMenuHighScore;

	public MenuScreen(Game game) 
	{
		super(game);	
	}

	@Override
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) 
	{
		stage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() 
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT)); 
		Gdx.input.setInputProcessor(stage);
		rebuildStage(); 
	}

	private void rebuildStage() 
	{
		
		skinRainMaker = new Skin(Gdx.files.internal(Constants.SKIN_RainMaker_UI),
				  new TextureAtlas(Constants.TEXTURE_ATLAS_UI));

		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
		     new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI)); 
		
		Table layerBackground = buildBackgroundLayer();
	    Table layerObjects = buildObjectsLayer();
	    Table layerControls = buildControlsLayer();
	    
	    Table layerLoginWindow = buildPlayerLoginWindow(); 
	    Table layerHighScoreWindow= buildHighScroeWindow(); 
	    Table layerOptionsWindow = buildOptionsWindowLayer();
		
	    stage.clear();
	    Stack stack = new Stack();
	    stage.addActor(stack);
	    stack.setSize(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
	    stack.add(layerBackground);
	    stack.add(layerObjects);
	    stack.add(layerControls);
	    stage.addActor(layerLoginWindow);
	    stage.addActor(layerHighScoreWindow);
	    stage.addActor(layerOptionsWindow);
	       
		
	}

	private Table buildPlayerLoginWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	private Table buildHighScroeWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	private Table buildOptionsWindowLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	private Table buildControlsLayer() 
	{
		 Table layer = new Table();
		 
		//Play Button
		 layer.right().bottom();
		 btnMenuPlay = new Button(skinRainMaker, "play");
		 layer.add(btnMenuPlay);
		 btnMenuPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onPlayClicked(); 
				
			}

			private void onPlayClicked() {
				// TODO Auto-generated method stub
				
			}
			 
		 }); 
		 //options button; 
		 layer.row(); 
		 btnMenuOptions = new Button(skinRainMaker, "options");
		 layer.add(btnMenuOptions);
		 btnMenuOptions.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onOptionsClicked(); 
				
			}

			private void onOptionsClicked() 
			{
				
				
			}
			 
		 }); 
		 //login Button
		 layer.left().bottom();
		 btnMenuLogin = new Button(skinRainMaker, "login");
		 layer.add(btnMenuLogin);
		 btnMenuLogin.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onLoginClicked(); 
				
			}

			private void onLoginClicked() {
				// TODO Auto-generated method stub
				
			}
			 
		 }); 
		 //high Score Button
		 layer.row(); 
		 btnMenuHighScore = new Button(skinRainMaker, "highscore"); 
		 layer.add(btnMenuHighScore);
		 btnMenuHighScore.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onHighScoreClicked(); 
				
			}

			private void onHighScoreClicked() 
			{
				// TODO Auto-generated method stub
				
			}
			 
		 }); 
		 
		return layer;
	}

	private Table buildObjectsLayer() 
	{
		Table layer = new Table();
		imgCloud = new Image(skinRainMaker, "Cloud"); 
		layer.add(imgCloud).align(Align.top);
		imgTim = new Image (skinRainMaker, "Tim");
		layer.add(imgTim).align(Align.center);
		return layer; 
		
	}

	private Table buildBackgroundLayer() 
	{
		Table layer= new Table(); 
		imgBackground = new Image(skinRainMaker, "superman-city-background-clipart-9");
		layer.add(imgBackground);
		return layer; 
	}

	@Override
	public void hide() 
	{
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

}
