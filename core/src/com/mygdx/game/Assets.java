package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.util.Constants;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;





/*
* By Adam Standke
* Class that contains the asset manager that loads 
* textures from the texture atlas to their respective
* inner classes for use by other classes in the game  
*/
public class Assets implements Disposable, AssetErrorListener  
{
	public AssetLevelDecoration leveldecoration; 
	public AssetPoints plus; 
	public AssetPowerUp up; 
	public AssetRainDrop drop;
	public AssetPlatform plat; 
	public AssetTimmy timmy; 
	public static final String TAG = Assets.class.getName();
	//static object that holds the only instance of the class
	public static final Assets instance = new Assets();
    private AssetManager assetManager;
	public AssetFonts fonts;
	public AssetSounds sounds;
	public AssetMusic music;
    
    /**
     * Private constructor since the Assets' class is modeled after
     * the singleton design pattern, in that only one instance of the 
     * class will be created to which all of the other classes in the
     * game will have access to. When the class is loaded into the jvm
     * one single instance of this class will be created for all to use. 
     */
    private Assets () {}
    
    
    
		
	
	/**
	 * class that will handle the loading of the sound files for each sound type
	 * @author Adam
	 *
	 */
	public class AssetSounds
	{
		public final Sound explode;
		public final Sound walk;
		public final Sound death;
		public final Sound jump;
		public final Sound drip;
		public final Sound points; 
		public final Sound power; 
		
		
		/**
		 * Constructor that will handle the loading/assigning of the sound files
		 * @param am
		 */
		public AssetSounds(AssetManager am)
		{
			explode = am.get("sounds/110391__soundscalpel-com__firework-explosion-fizz.wav", Sound.class);
			walk = am.get("sounds/Trim.wav", Sound.class);
			death = am.get("sounds/239579__ryanconway__evil-laugh-2.wav", Sound.class);
			jump = am.get("sounds/344004__reitanna__heavy-grunt.wav", Sound.class);
			//drip = am.get("sounds/25879__acclivity__drip1.wav", Sound.class);
			drip = am.get("sounds/348022__mattix__water-drop-01.wav", Sound.class);
			points = am.get("sounds/320655__rhodesmas__level-up-01.wav", Sound.class);
			power =  am.get("sounds/220173__gameaudio__spacey-1up-power-up.wav", Sound.class);
		}
	}
	
	/**
	 * Class that will handle the loading of the music assets
	 * @author Adam
	 *
	 */
	public class AssetMusic
	{
		public final Music song02; 
		public final Music song01;
		
		/**
		 * Constructor will assign the music files for the given sound
		 * @param am
		 */
		public AssetMusic(AssetManager am)
		{
			song01 = am.get("music/rain-01.mp3", Music.class);
			song02 = am.get("music/Yung_Kartz_-_11_-_Lonely.mp3", Music.class); 
		}
	}
    
    /**
	 * Inner class that holds a reference to the various Timmy image files 
	 * used in the game to animate Timmy. Each image is loaded from the 
	 * TextureAtlas and assigned to a specific variable to keep track of it for 
	 * later use. 
	 */
    public class AssetTimmy
    {	
    	public final AtlasRegion frame1;
    	public final Animation animNormal; 
    	public final Animation animRunning;
    	public final Animation animJumping;
    	public final Animation animDead;
    	public final Animation animShooting; 
    	
    	
    	public AssetTimmy (TextureAtlas atlas)
    	{
    		frame1= atlas.findRegion("Tim");
    		
    		Array<AtlasRegion> regions = null;
    		AtlasRegion region = null; 
    		
    		//Animation: Timmy moving
    		regions = atlas.findRegions("Tim_running"); 
    		animRunning= new Animation(1.0f/10.0f, regions, Animation.PlayMode.LOOP_PINGPONG);
    		
    		regions = atlas.findRegions("Tim_jumping"); 
    		animJumping= new Animation(1.0f/4.0f, regions, Animation.PlayMode.NORMAL);
    		
    		region= atlas.findRegion("Tim_normal", 1); 
    		animNormal= new Animation(1.0f/10.0f, region);
    		
    		regions =atlas.findRegions("Tim_dying"); 
    		animDead = new Animation(1.0f/4.0f, regions, Animation.PlayMode.NORMAL);
    		
    		regions = atlas.findRegions("Tim"); 
    		animShooting = new Animation(1.0f/9.0f, regions, Animation.PlayMode.NORMAL); 
    		
    		
    		
    	}	
    }
    
    /**
     * Inner class that holds a reference to a Platform image file 
	 * used in the game to texture the game's platform. The image is loaded from the 
	 * TextureAtlas and assigned to a specific variable to keep track of it for 
	 * later use.  
     *
     */
    public class AssetPlatform
    {
    	
    	public final AtlasRegion middle;
    	
    	public AssetPlatform (TextureAtlas atlas)
    	{
    		
    		middle = atlas.findRegion("platform");
    	}
    }
    
    /**
     * Inner class that holds references to three RainDrop image files 
	 * used in the game to texture rain drops. The images are loaded from the 
	 * TextureAtlas and assigned to specific variables for later use by other 
	 * classes.  
     *
     */
    public class AssetRainDrop
    {
    	
    	public final AtlasRegion rain01;
    	public final AtlasRegion rain02; 
    	public final AtlasRegion rain03; 
    	
    	public AssetRainDrop (TextureAtlas atlas)
    	{
    		
    		rain01 = atlas.findRegion("raindrop01");
    		rain02 = atlas.findRegion("raindrop02");
    		rain03 = atlas.findRegion("raindrop03");
    	}
    }
    
    /**
     * Inner class that holds a reference to a Star image file 
	 * used in the game to represent a player's power up. The image is loaded from the 
	 * TextureAtlas and assigned to a specific variable to keep track of it for 
	 * later use.  
     *
     */
    public class AssetPowerUp
    {
    	
    	public final AtlasRegion power;
    	
    	public AssetPowerUp (TextureAtlas atlas)
    	{
    		
    		power = atlas.findRegion("special_power");
    	}
    }
    
    /**
     * Inner class that holds a reference to a Point's image file 
	 * used in the game to represent a collectible item that
	 * the player can collect. The image is loaded from the 
	 * TextureAtlas and assigned to a specific variable to keep track of it for 
	 * later use.  
     *
     */
    public class AssetPoints
    {
    	
    	public final AtlasRegion points;
    	
    	public AssetPoints (TextureAtlas atlas)
    	{
    		
    		points = atlas.findRegion("5points");
    	}
    }
    
    /**
     * Inner class for handling background images in the game 
     * that don't interact with the player but provide aesthetics  
     * to the game.
     */
    public class AssetLevelDecoration
    {
    	
    	public final AtlasRegion chopper;
    	public final AtlasRegion city;
    	public final AtlasRegion woman1;
    	public final AtlasRegion bar; 
    	
    	public AssetLevelDecoration (TextureAtlas atlas)
    	{
    		bar = atlas.findRegion("prog2"); 
    		chopper = atlas.findRegion("chopper");
    		city = atlas.findRegion("city_background");
    		woman1 = atlas.findRegion("woman_rain");
    	}
    }
    
   
    
    
    /*
     * Method that uses an AssetManager to interface with 
     * various asset types, such as textures, sound, and music
     * and load them for use by other part of the program.  
     */
    public void init (AssetManager assetManager) 
    {
        this.assetManager = assetManager;
        
        // set asset manager error handler
        assetManager.setErrorListener(this);
        
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        
        assetManager.load("music/rain-01.mp3", Music.class);
		assetManager.load("music/Yung_Kartz_-_11_-_Lonely.mp3", Music.class); 
		assetManager.load("sounds/110391__soundscalpel-com__firework-explosion-fizz.wav", Sound.class);
		assetManager.load("sounds/Trim.wav", Sound.class);
		assetManager.load("sounds/239579__ryanconway__evil-laugh-2.wav", Sound.class);
		
		assetManager.load("sounds/348022__mattix__water-drop-01.wav", Sound.class);
		assetManager.load("sounds/344004__reitanna__heavy-grunt.wav", Sound.class);

		
		assetManager.load("sounds/320655__rhodesmas__level-up-01.wav", Sound.class);
		assetManager.load("sounds/220173__gameaudio__spacey-1up-power-up.wav", Sound.class);
        
         
        // start loading assets and wait until finished
        assetManager.finishLoading();
         
        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        
        for (String a : assetManager.getAssetNames())
        	Gdx.app.debug(TAG, "asset: " + a);
     
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
            
		// enable texture filtering for pixel smoothing
	    for (Texture t : atlas.getTextures()) 
	    {
	          t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    }
	    
	    // create game resource objects
	    
	    fonts = new AssetFonts();
	    timmy = new AssetTimmy(atlas);
	    plat = new AssetPlatform(atlas); 
	    drop = new AssetRainDrop(atlas); 
	    up = new AssetPowerUp(atlas); 
	    plus = new AssetPoints(atlas); 
	    leveldecoration = new AssetLevelDecoration(atlas); 
	    sounds = new AssetSounds(assetManager);
	    music = new AssetMusic(assetManager);
    	 
   }
    
    /**
     * Error method that may be called after the AssetErrorListener
     * has had a problem loading an asset, due to an external input/output 
     * failure
     * @param filename a string that represent the filename of the asset
     * @param type an object of a generic class that is the class associated with the error
     * @param throwable an object of the Throwable class that the jvm throws to the method 
     */
    public void error (String filename, Class type, Throwable throwable) 
    {
    	     Gdx.app.error(TAG, "Couldn't load asset '"
    	       + filename + "'", (Exception)throwable);
    }
    
    /**
     * Error method that may be called after the AssetErrorListener
     * has had a problem loading an asset, due to an external input/output 
     * failure
     */
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
		
	}
	
	
	
	/**
	 * Disposes the assets loaded in main memory
	 */
	@Override
	public void dispose() 
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
		
		
	}
	
	public class AssetFonts
    {
    	public final BitmapFont defaultSmall;
    	public final BitmapFont defaultNormal;
    	public final BitmapFont defaultBig;
    	
    	public AssetFonts()
    	{
    		//create three fonts using Libgdx's 15px bitmap font
    		defaultSmall = new BitmapFont(Gdx.files.internal("images/font-title-export.fnt"), false);
    		defaultNormal = new BitmapFont(Gdx.files.internal("images/font-title-export.fnt"), false);
    		defaultBig = new BitmapFont(Gdx.files.internal("images/font-title-export.fnt"), false);
    		
    		//set font sizes
    		defaultSmall.getData().setScale(0.75f);
    		defaultNormal.getData().setScale(1.0f);
    		defaultBig.getData().setScale(3.0f);
    		
    		//enable linear textrue filtering for smooth fonts
    		defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear,  TextureFilter.Linear);
    		defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear,  TextureFilter.Linear);
    		defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	}
    }

}
