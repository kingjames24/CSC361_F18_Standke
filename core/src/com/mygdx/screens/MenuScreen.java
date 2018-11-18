package com.mygdx.screens;

import java.util.ArrayList;

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
import com.badlogic.gdx.physics.box2d.DestructionListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.WorldController;
import com.mygdx.util.Constants;
import com.mygdx.util.GamePreferences;



public class MenuScreen  extends AbstractGameScreen implements DestructionListener
{
	
	private Stage stage;
	private Skin skinRainMaker;
	private Skin skinLibgdx;
	private Image imgBackground;
	private Image imgCloud;
	private Image imgTim;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	private FitViewport viewport;
	private Image imgCloud2;
	private Image imgCloud3;
	private Image imgRain;
	private Image imgRain2;
	private Image imgRain3;
	private Image imgRain4;
	private char[] player; 
	private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private TextField login;
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private int count; 

	public MenuScreen(Game game) 
	{
		super(game);
		
	}
	
	 private void loadSettings() 
	 {
		 player= new char[20];
		 count=0; 
	     GamePreferences prefs = GamePreferences.instance;
	     prefs.load();
	     chkSound.setChecked(prefs.sound);
	     sldSound.setValue(prefs.volSound);
	     chkMusic.setChecked(prefs.music);
	     sldMusic.setValue(prefs.volMusic);
	     login.setText(prefs.login);
	 }
	 
	 private void saveSettings() 
	 {
		 String login = new String(player);
		 player=null;
		 count=0; 
	     GamePreferences prefs = GamePreferences.instance;
	     prefs.sound = chkSound.isChecked();
	     prefs.volSound = sldSound.getValue();
	     prefs.music = chkMusic.isChecked();
	     prefs.volMusic = sldMusic.getValue();
	     prefs.login= login; 
	     prefs.save();
	 }
	 
	 private void onSaveClicked() 
	 {
	     saveSettings();
	     onCancelClicked();
	}
	 
	  private void onCancelClicked() 
	  {
		     btnMenuPlay.setVisible(true);
		     btnMenuOptions.setVisible(true);
		     winOptions.setVisible(false);
	  }
	  
	  
		    
	  
	  
	  
	  private Table buildOptWinAudioSettings () 
	  {
		     Table tbl = new Table();
		     // + Title: "Audio"
		     tbl.pad(10, 10, 0, 10);
		     tbl.add(new Label("Audio", skinLibgdx, "font",
		             Color.GREEN)).colspan(3);
		     tbl.row();
		     tbl.columnDefaults(0).padRight(10);
		     tbl.columnDefaults(1).padRight(10);
		     // + Checkbox, "Sound" label, sound volume slider
		     chkSound = new CheckBox("", skinLibgdx);
		     tbl.add(chkSound);
		     tbl.add(new Label("Sound", skinLibgdx));
		     sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		     tbl.add(sldSound);
		     tbl.row();
		     // + Checkbox, "Music" label, music volume slider
		     chkMusic = new CheckBox("", skinLibgdx);
		     tbl.add(chkMusic);
		     tbl.add(new Label("Music", skinLibgdx));
		     sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		     tbl.add(sldMusic);
		     tbl.row();
		     return tbl;
	}
	 
	  private Table buildOptWinLoginSettings ()
	  {
		  Table tbl = new Table(); 
		  tbl.pad(10, 10, 0, 10);
		  tbl.add(new Label("Player Login", skinLibgdx, "font", Color.GREEN)).colspan(2);
		  tbl.row();
		  login= new TextField("", skinLibgdx, "default");
		  login.setProgrammaticChangeEvents(false);
		  tbl.add(login);
		  login.addListener(new InputListener() {
			  		
					public boolean keyTyped(InputEvent event, char character)
					{
						
						if(event.getCharacter()=='\b')
						{
							; 
						}
						else
						{
							if(count>=player.length)
							{
								; 
							}
							else
							{
								player[count++]=character; 
							}
							
						}
						 
						return false; 
					}
		  	}); 
				
			
		return tbl;
		  
	  }
	  
	  
	  private Table buildOptWinButtons () 
	  {
		     Table tbl = new Table();
		     // + Separator
		     Label lbl = null;
		     lbl = new Label("", skinLibgdx);
		     lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		     lbl.setStyle(new LabelStyle(lbl.getStyle()));
		     lbl.getStyle().background = skinLibgdx.newDrawable("white");
		     tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		     tbl.row();
		     lbl = new Label("", skinLibgdx);
		     lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		     lbl.setStyle(new LabelStyle(lbl.getStyle()));
		     lbl.getStyle().background = skinLibgdx.newDrawable("white");
		     tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		     tbl.row();
		     // + Save Button with event handler
		     btnWinOptSave = new TextButton("Save", skinLibgdx);
		     tbl.add(btnWinOptSave).padRight(30);
		     btnWinOptSave.addListener(new ChangeListener() {
		       @Override
		       public void changed (ChangeEvent event, Actor actor) 
		       {
		         onSaveClicked();
		       }
		     });
		     // + Cancel Button with event handler
		     btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		     tbl.add(btnWinOptCancel);
		     btnWinOptCancel.addListener(new ChangeListener() {
		       @Override
		       public void changed (ChangeEvent event, Actor actor) 
		       {
		         onCancelClicked();
		       }
		     });
		     return tbl; 
	  }
	  
	  
	  private Table buildOptionsWindowLayer () 
	  {
			 winOptions = new Window("Options", skinLibgdx);
		     // + Audio Settings: Sound/Music CheckBox and Volume Slider
		     winOptions.add(buildOptWinAudioSettings()).row();
		     // + Character Skin: Selection Box (White, Gray, Brown)
		     winOptions.add(buildOptWinLoginSettings()).row();
		     // + Separator and Buttons (Save, Cancel)
		     winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
		     // Make options window slightly transparent
		     winOptions.setColor(1, 1, 1, 0.8f);
		     // Hide options window by default
		     winOptions.setVisible(false);
		     // Let TableLayout recalculate widget sizes and positions
		     winOptions.pack();
		     // Move options window to bottom right corner
		     winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.getWidth() - 50, 50);
		     return winOptions;
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
	    Table layerOptionsWindow = buildOptionsWindowLayer();
	    
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
	    stage.addActor(layerOptionsWindow);
	       
		
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

	

	private Table buildControlsLayer() 
	{
		 Table layer = new Table();
		 
		 
		 
		 btnMenuOptions = new Button(skinRainMaker, "options");
		 btnMenuOptions.scaleBy(.38f);
		 layer.add(btnMenuOptions).align(Align.bottomRight).expand();
		 btnMenuOptions.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) 
			{
				onOptionsClicked(); 
				
			}

			private void onOptionsClicked() 
			{
				 loadSettings();
			     btnMenuPlay.setVisible(false);
			     btnMenuOptions.setVisible(false);
			     winOptions.setVisible(true);
				
			}
			 
		 }); 
		 
		//Play Button
		 
		 layer.add().expand();
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
		
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

}
