package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.objects.Ability;
import com.mygdx.objects.Star;
import com.mygdx.screens.MenuScreen;
import com.mygdx.util.Constants;


/**
 * Class that handles that actual rendering of the game to the user's screen
 * @author adam
 *
 */
public class WorldRenderer implements Disposable 
{
	public static OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController; 
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	private Box2DDebugRenderer b2debugRenderer;
	public Stage stage; 
	public Stack stack; 
	public Table leftCorner; 
	private Viewport viewport;
	private Skin hudSkin; 
	public TextureRegionDrawable drawable; 
	private ProgressBar healthBar;
	private Label score;
	public Image powerUp;
	public Image lives;
	public Label gameOver;
	private Image city2;
	private ExtendViewport backViewport;
	private Stage backStage;
	private OrthographicCamera camera2;
	private Table powerup2;
	private FitViewport viewport2;
	private Stage stage2; 
	
	/**
	 * Constructor that takes in an object of the WorldController class and also 
	 * calls a helper method to create the 3d camera to be used to render frames
	 * @param worldController
	 */
	public WorldRenderer (WorldController worldController) 
	{ 
		this.worldController = worldController;
		init();
	}
	
	/**
	 * Method that creates an object of the SpriteBatch class that creates a vertex array  
	 * of 20000 spites and uses the default shader. Method also creates an orthographic camera
	 * with a given viewport height and width(measured in meters)
	 * and then multiplies the projection and camera-view matrix to go  
	 * from an initial 3d world coordinate position to screen space. 
	 * Also another orthographic camera is created called camera2 thats viewport's width and height
	 * are the screen's dimension measured in pixels. This camera will be used to solely capture the 
	 * background image. Lastly, to create a HUD that is displayed during the game play, scene2d's ui 
	 * is used; namely stage, to add various actors on top of the scene, such as a progress bar. Stage
	 * also has its own orthographic camera to keep track of only the on-screen HUD     
	 */
	private void init () 
	{ 
		//main camera that tracks the game play of the player
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		//secondary  stage  that only focuses on the background image with its own seperate camera
		viewport2 = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera()); 
		stage2 = new Stage(viewport2, batch);

		//Scene 2d ui used to create HUD display in game; the stage also has its own orthographic camera too
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera()); 
		stage = new Stage(viewport, batch); 
		stack = new Stack();
		leftCorner = new Table(); 
		//builds skin for HUD
		buildHudSkin();
		
		gameOver = new Label("GAME-OVER", hudSkin, "title", Color.RED); 
		drawable = new TextureRegionDrawable(Assets.instance.up.power);
		
		Table progressBar = buildPrograssBar();
		Table score1 = buildScoreBox();
	    powerup2 = buildPowerBox();
		leftCorner.add(progressBar).top().left();
		leftCorner.add(score1).top().right(); 
		leftCorner.row(); 
		leftCorner.add(powerup2).align(Align.topLeft).expand();
		leftCorner.setFillParent(true);
		stage.addActor(leftCorner);
		
		b2debugRenderer = new Box2DDebugRenderer();
	}
	

	/**
	 * Method that holds the a scene 2d widget Image that represents
	 * a star object that the player is allowed to shoot in the game; 
	 * will appear in the Hud when the star is collected and/or ready to be 
	 * shot again
	 * @return a Table object
	 */
	private Table buildPowerBox() 
	{
		Table powerUpDisplay = new Table();
	    powerUp= new Image(); 
	    powerUpDisplay.add(powerUp);
		return powerUpDisplay;
	}
	
	/**
	 * Method that builds a scene2d label widget that displays the player's current score 
	 * @return a Table object
	 */
	private Table buildScoreBox() 
	{
		Table bar = new Table();
		score = new Label("Score: " + WorldController.score + "" , hudSkin, "title", Color.RED);
		bar.add(score);
		return bar;
	}

	/**
	 * Method that builds a scene2d progress bar widget that displays the player's current health 
	 * @return a Table object
	 */
	private Table buildPrograssBar() 
	{
		Table bar = new Table();
		Label label = new Label("Health:", hudSkin, "title", Color.RED);
		healthBar= new ProgressBar(0f, 100f, 10f, false,  hudSkin);
		healthBar.setValue(100);
		healthBar.setColor(Color.RED);
		bar.add(label); 
		bar.add(healthBar);
		return bar;
	}
	
	/**
	 * Method that loads the skin chosen to display Scene2d's widgets. In this case I used
	 * the Neutralizer UI Ver.1 created by Raymond Raeleus Buckley, all rights 
	 * reserved to him 
	 */
	private void buildHudSkin() 
	{
		hudSkin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
			     new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		
	}
	
	/**
	 * Helper method that is called by the GameScreen class
	 * to render the world and then render the on screen hud
	 * display
	 */
	public void render () 
	{ 
		renderWorld(batch);
		renderHud(batch);
		
		
	}
	
	
	/**
	 * Method that renders the HUD display that appears on top of the game, and 
	 * tracks various game parameters as the player plays the game, such as player's 
	 * score, health, lives, and whether the main character's special ability can be used
	 * To do so, first the stage's camera is  and transformed from camera space to screen space. 
	 * Then all of the in game elements, such as the player's health and score are gathered. 
	 * After which batch's begin and end method are called to draw most of the HuD.    
	 */
	private void renderHud(SpriteBatch batch2)
	{ 
		
		batch.setProjectionMatrix(stage.getCamera().combined);
		healthBar.setValue(WorldController.health);
		score.setText("Score:"+ WorldController.score);
		batch.begin();
		if (WorldController.visible)//fix issue with objects in render 
		{
			
			powerUp.setDrawable(drawable);
			powerUp.setSize(24,24); 
			if (WorldController.shootTimeout<2)
			{
					 
					batch.draw(Assets.instance.up.power, powerUp.getImageX(), powerUp.getImageY()+450, powerup2.getOriginX(), powerup2.getOriginY(), powerup2.getWidth(), powerup2.getHeight(), powerup2.getScaleX(), powerup2.getScaleY(), powerup2.getRotation());
					
			}
			else
			{
					
					powerUp.setDrawable(null);
			}
			
		}
		else
		{
			powerUp.setDrawable(null);
		}
		
		float x = viewport.getWorldWidth()-50-(2.3f*50); 
		float y = viewport.getWorldHeight()-100; 
		for (int i = 0; i < 3; i++) 
		{
				 if (worldController.lives <= i) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
				 batch.draw(Assets.instance.timmy.frame1,  x + i * 50, y, 50, 50, 120, 100, .30f, .30f, 0);
				 batch.setColor(1, 1, 1, 1);
				
		}
		batch.end();
		if(WorldController.isGameOver())
		{
			
			leftCorner.clear();
			leftCorner.add(gameOver).align(Align.center);
			 
			 
		}
		stage.draw();
		
	}
	
		
		
		
	

	/**
	 * Method that render's the game to the screen by composing the
	 * camera-view matrix with the orthographic projection matrix to get
	 * a 2d rendered image to appear on the user's screen. After doing so, 
	 * SpriteBatch's batch.begin() and end() method are called to allow each 
	 * object class to handle its rendering through SpriteBatch's draw method.     
	 * @param batch
	 */
	public void renderWorld (SpriteBatch batch)
	{
		
		//drawing of background image
		OrthographicCamera camera2 = (OrthographicCamera) stage.getCamera(); 
		camera2.zoom=1f; 
		camera2.update();
		batch.setProjectionMatrix(stage2.getCamera().combined);
		batch.begin();
		batch.draw(Assets.instance.leveldecoration.city, 0, 0, 300f, 0, Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, 1.5f, 1, 0 );
		batch.end();
		
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		worldController.rain.render(batch);
		if(Ability.fire)
		{
			worldController.ability.render(batch);
		}
		batch.end();
		
		if (DEBUG_DRAW_BOX2D_WORLD)
		{
			b2debugRenderer.setDrawAABBs(false);
			b2debugRenderer.setDrawVelocities(false);
			b2debugRenderer.AABB_COLOR.set(Color.BLACK); 
			b2debugRenderer.VELOCITY_COLOR.set(Color.BLACK); 
			b2debugRenderer.render(worldController.b2world, camera.combined);
		}
			
	}

	/**
	 * Method that deallocates the vertex array associated with the sprite batch and 
	 * deallocates the shapes drawn by Box2d by openGl  
	 */
	@Override
	public void dispose() 
	{
		batch.dispose();
		stage.dispose();
		stage2.dispose();
		
	}
	
	/**
	 * Method that resizes the camera's viewport width
	 */
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
		stage2.getCamera().viewportWidth=width;
		stage2.getCamera().viewportWidth=height;
		stage2.getCamera().update(); 
		stage2.getViewport().setScreenSize(width, height);
		
		stage.getViewport().setScreenSize(width, height);
		
	}

}
