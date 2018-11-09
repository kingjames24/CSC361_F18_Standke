package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.objects.Ability;
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
	
	private ProgressBar healthBar;
	private Label healthScore;
	private Image fullHealth; 
	
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
	 * with a given viewport height and width and then recalculates the projection and camera-view matrix
	 * from an initial 3d world coordinate position.   
	 */
	private void init () 
	{ 
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		viewport = new FitViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, new OrthographicCamera()); 
		stage = new Stage(viewport, batch);
		stack = new Stack();
		leftCorner = new Table(); 
		buildHudSkin();
		Table progressBar = buildPrograssBar();
		Table progressBar2 = buildPrograssBar2(); 
		Label label = new Label("Health:", hudSkin, "title", Color.LIGHT_GRAY);
		stack.add(progressBar);
		stack.add(progressBar2);
		leftCorner.add(label);
		leftCorner.add(stack);
		leftCorner.top().left(); 
		leftCorner.setFillParent(true);
		stage.addActor(leftCorner);
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	private Table buildPrograssBar2() 
	{
		Table bar = new Table();
		
		Pixmap pixmap = new Pixmap(2, 7, Format.RGB888);
		pixmap.setColor(Color.GOLD);
		pixmap.fill();
		
		TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		drawable.setBottomHeight(0);
		drawable.setTopHeight(7);
		drawable.setLeftWidth(0);
		drawable.setRightWidth(18);
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;
		
		
		ProgressBar progress = new ProgressBar(0f, 100f, 10f, false,  progressBarStyle);
		//progress.setBounds(0, 0, 0, 0);
		
		bar.add(progress);
		return bar;
	}

	private Table buildPrograssBar() 
	{
		Table bar = new Table();
		healthBar= new ProgressBar(0f, 100f, 10f, false,  hudSkin);
		bar.add(healthBar);
		return bar;
	}
	
	private void buildHudSkin() 
	{
		hudSkin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
			     new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		
	}
	
	/**
	 * Helper method that is called by the GameScreen class
	 */
	public void render () 
	{ 
		renderWorld(batch);
		renderHud(batch); 
		
	}
	private void renderHud(SpriteBatch batch2)
	{ 
		batch.setProjectionMatrix(stage.getCamera().combined);
		stage.draw();
		
	}
	
	public void update(float deltaTime)//updates the hud based on game
	{
		
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
		b2debugRenderer.dispose();
		stage.dispose();
		
	}
	
	/**
	 * Method that resizes the camera's viewport width
	 */
	public void resize(int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
	}

}
