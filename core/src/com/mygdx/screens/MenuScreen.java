package com.mygdx.screens;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
	private FitViewport viewport;
	private SpriteBatch batch;
	private OrthographicCamera camera2;
	private Image imgCloud2;
	private Image imgCloud3;
	private Image imgRain;
	private Image imgRain2;
	private Image imgRain3;
	private Image imgRain4;

	public MenuScreen(Game game) 
	{
		super(game);	
	}

	@Override
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(1f, 1f, 1f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(deltaTime);
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) 
	{
		 stage.getViewport().setScreenSize(width, height); 
		
		
	}

	@Override
	public void show() 
	{
		batch = new SpriteBatch();
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
		
		
		
	
		
		
		Table layerBackground = buildBackgroundLayer();
		Table layerClouds = buildCloudsLayer();
		Table layerRain = buildRainLayer(); 
	    Table layerObjects = buildTimmyLayer();
	    Table layerControls = buildControlsLayer();
	   //Table layerOptionsWindow = buildOptionsWindowLayer();
	    
	    Label title = new Label("RainMaker", skinLibgdx, "title", Color.SKY);
	    title.setFontScale(3.5f);
	    Label name = new Label("By Adam Standke", skinLibgdx, "title", Color.SKY);
	    name.setFontScale(1f);
	    Table credit = new Table();
	    credit.add(title).align(Align.center).expand();
	    credit.row();
	    credit.add(name).align(Align.top).expand();
		
	    stage.clear();
	    Stack stack = new Stack();
	    stack.setFillParent(true);
	    stack.addActor(layerBackground);
	    stack.addActor(layerClouds);
	    stack.add(layerRain);
	    stack.add(layerObjects);
	    stack.add(layerControls);
	    stack.add(credit);
	    stage.addActor(stack);
	    //stage.addActor(layerOptionsWindow);
	       
		
	}

	

	private Table buildRainLayer() 
	{
		Table layer = new Table();
		imgRain = new Image(skinRainMaker, "raindrops");
		layer.add(imgRain).align(Align.topLeft);
		imgRain2 = new Image(skinRainMaker, "raindrops");
		layer.add(imgRain2).align(Align.center);
		imgRain3 = new Image(skinRainMaker, "raindrops");
		layer.add(imgRain3).align(Align.top);
		imgRain4 = new Image(skinRainMaker, "raindrops");
		layer.add(imgRain4).align(Align.topRight).expand(); 
		return layer;
	}

	private Table buildCloudsLayer() 
	{
		Table layer = new Table(); 
		imgCloud = new Image(skinRainMaker, "Cloud");
		imgCloud.scaleBy(.5f);
		layer.add(imgCloud).align(Align.topLeft).expand().pad(10);
		imgCloud2 = new Image(skinRainMaker, "Cloud");
		imgCloud2.scaleBy(.5f);
		layer.add(imgCloud2).align(Align.top);
		imgCloud3 = new Image(skinRainMaker, "Cloud");
		imgCloud3.scaleBy(.5f);
		layer.add(imgCloud3).align(Align.topRight);
		return layer;
	}

	private Table buildOptionsWindowLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	private Table buildControlsLayer() 
	{
		 Table layer = new Table();
		 
		 
		 
		 btnMenuOptions = new Button(skinRainMaker, "options");
		 layer.add(btnMenuOptions).align(Align.bottomRight).expand();
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
		 
		//Play Button
		 //layer.right().bottom();
		 layer.add().expand();
		 btnMenuPlay = new Button(skinRainMaker, "play");
		 layer.add(btnMenuPlay).align(Align.bottomLeft).expand();
		 btnMenuPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onPlayClicked(); 
				
			}

			private void onPlayClicked() 
			{
				game.setScreen(new GameScreen(game));
				
			}
			 
		 }); 
		
		 
		return layer;
	}

	private Table buildTimmyLayer() 
	{
		Table layer = new Table();
		
		imgTim = new Image (skinRainMaker, "Tim");
		imgTim.scaleBy(.01f, .01f);
		layer.add(imgTim).align(Align.bottom).expand();
		return layer; 
		
	}

	private Table buildBackgroundLayer() 
	{
		Table layer= new Table(); 
		imgBackground = new Image(skinRainMaker, "background");
		imgBackground.scaleBy(1f, 1f);
		layer.add(imgBackground);
		layer.align(Align.bottomLeft);
		
		return layer; 
	}

	@Override
	public void hide() 
	{
		batch.dispose();
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

}
