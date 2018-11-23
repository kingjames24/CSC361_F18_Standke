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
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
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


/**
 * Class that sets up the main menu screen in which the player
 * chooses various options for the game and also logs in a name 
 * to be used in playing the game
 * 
 * @author Adam Standke
 *
 */
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

	/**
     * Constructor that is passed in the Game Class 
     * which implements the application interface allowing
     * different screens to be loaded in 
     * @param game an object from the Game Class
     */
	public MenuScreen(Game game) 
	{
		super(game);
		
	}
	  /*
	  * A private method that loads in the specific
	  * settings for a game such as sound, volume, 
	  * music, name of the player
	  */
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
	 /**
	  * Private method that saves the 
	  * game preferences that the user decided 
	  * for the game
	  */
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
	 
	 /**
	 * Private method is called when the save
	 * button of the option window is called. 
	 * Then two helper methods are called to save
	 * the game settings and not display the window
	 * options any more. Also saves the audio settings
	 */
	 private void onSaveClicked() 
	 {
	     saveSettings();
	     onCancelClicked();
	 }
	 /**
	  * Private method that is called when the 
	  * the cancel and/or save button is pressed by 
	  * the user. It will then hide the window option 
	  * pane and will then display the main two menu-buttons;
	  * namely, play and menu-options
	  */
	  private void onCancelClicked() 
	  {
		     btnMenuPlay.setVisible(true);
		     btnMenuOptions.setVisible(true);
		     winOptions.setVisible(false);
	  }
	  
	  
		    
	  
	  
	  /**
	   * Method that builds the audio portion of
	   * option window's UI that houses the game's audio 
	   * settings. A new table object is which a labels, 
	   * checkboxes and movable sliders are contained within  
	   * @return a Table object that contains various widgets
	   */
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
	 
	  /**
	   * Method that builds the login portion of
	   * option window's UI that houses the player's name 
	   * @return a Table object that contains various widgets
	   */
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
						
						if(event.getCharacter()=='\b')//backspace character is not added to the login array
						{
							; 
						}
						else
						{
							if(count>=player.length)//an array out of bounds exception
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
	  
	  /**
	  Method that builds the button portion of
	  the option window's UI that will contain two buttons-
	  a save and cancel button. A new table layout widget
	  is created in which labels and a textbutton 
	  widget are contained within. Allowing the user to select
	  to save the new game settings or cancel opening up the 
	  option's window in the first place.  
	  @return a Table object that contains various widgets 
	  */
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
	  
	  /**
	  * private method that builds the pop-out option window
	  * and layers one table on top of each other from top to bottom
	  * @return a Table object that is a layout widget
	  */
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
	 
	/**
	* Method that is called after the method Show(). It makes two 
	* calls to openGL's interface to clear the given screen 
	* and then the stage calls each of the actors in the scene to take some 
	* action and calls the draw method to render the UI on the 
	* user's display
	*/
	@Override
	public void render(float deltaTime) 
	{
		Gdx.gl.glClearColor(30, 144, 255, 1f);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
	    stage.draw();
	}
	
	/**
	* Method that is called to resize the main menu 
	* through getting the stage's viewport which handles the stage's 
	* camera  
	*/
	@Override
	public void resize(int width, int height) 
	{
		 stage.getViewport().setScreenSize(width, height); 	
	}

	/**
	* Method that is routinely called by the Game class in which 
	* the game's main menu is displayed. A new Stage is created and 
	* is set to handle user input while shown to the user. Then the
	* main menu is created through the helper method of rebuildStage(). 
	*/
	@Override
	public void show() 
	{
		
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		rebuildStage(); 
	}
	

	/**
	* Key method that builds the 2d scene graph in which the stage widget 
	* is the root of the graph and uses a stack and table layout
	* widget to add various actors to the overall stage.In which the UI is 
	* ready to be rendered. First two new skin textures are created one for the 
	* the main menu UI and another for the window option UI. Then each layer of 
	* the UI is constructed through helper methods and a table is sent back. After
	* clearing the stage's previous root child and new stack widgetgroup is created 
	* to overlay/position each of the previous tables on top of each other 
	*/
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
	    title.setFontScale(3.5f);//figure out to make font less pixilly when increasing size
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

	

	/**
	* Private method that builds the 
	* rain drop images for the main menu's UI
	* @return a Table object that is a layout widget
	*/
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

	/**
	* Private method that builds the cloud
	* images for the main menu's UI
	* @return a Table object that is a layout widget
	*/
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

	
	/**
	* Private method that builds the two interactive buttons
	* for main menu's UI that are seen in the bottom portion
	* of the screen 
	* @return a Table object that is a layout widget
	*/
	private Table buildControlsLayer() 
	{
		 Table layer = new Table(); 
		 btnMenuOptions = new Button(skinRainMaker, "options"); 
		 layer.add(btnMenuOptions).align(Align.bottomRight).expand().width(60).height(60);
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
		 
		 
		 layer.row();
		 btnMenuPlay = new Button(skinRainMaker, "play");
		 layer.add(btnMenuPlay).align(Align.bottomRight).width(60).height(60);
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

	/**
	* Private method that builds the main character's image
	* ie., Timmy, for the main menu's UI
	* @return a Table object that is a layout widget
	*/
	private Table buildTimmyLayer() 
	{
		Table layer = new Table();
		imgTim = new Image (skinRainMaker, "Tim");
		layer.add(imgTim).align(Align.bottom).expand().width(150).height(150);
		return layer; 
		
	}

	/**
	* Private method that builds the background image for the
	* main menu's UI
	* @return a Table object that is a layout widget
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
	* Method that is routinely called by the Game class in which the 
	* game's main menu is hidden and the memory deallocated for items 
	* such as the skins and widgets held in memory  
	*/
	@Override
	public void hide() 
	{
		
		stage.dispose();
		skinRainMaker.dispose();
		skinLibgdx.dispose();
		
	}

	/**
	* non-implemented method inherited from AbstractScreen
	*/
	@Override
	public void pause() {}

}
