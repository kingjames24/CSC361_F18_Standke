package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.util.Constants;




public class Assets implements Disposable, AssetErrorListener  
{
	public AssetTimmy timmy; 
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
    private AssetManager assetManager;
    
    private Assets () {}
    
    
    public class AssetTimmy
    {	public final AtlasRegion frame1; 
    
    	public AssetTimmy (TextureAtlas atlas)
    	{
    		frame1= atlas.findRegion("Tim"); 
    	}	
    }
    
    
    
    
    
    
    
    
    public void init (AssetManager assetManager) 
    {
        this.assetManager = assetManager;
        
        // set asset manager error handler
        assetManager.setErrorListener(this);
        
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        
        // load sounds
       
        
        // load music
        
         
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
	    timmy = new AssetTimmy(atlas); 
    	 
   }

    public void error (String filename, Class type, Throwable throwable) 
    {
    	     Gdx.app.error(TAG, "Couldn't load asset '"
    	       + filename + "'", (Exception)throwable);
    }
    
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
		
	}

	@Override
	public void dispose() 
	{
		assetManager.dispose();
		
	}

}
