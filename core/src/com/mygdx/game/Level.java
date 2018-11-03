package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.objects.Chopper;
import com.mygdx.objects.City;
import com.mygdx.objects.People;
import com.mygdx.objects.Platform;
import com.mygdx.objects.Points;
import com.mygdx.objects.Star;
import com.mygdx.objects.Timmy;
import com.mygdx.objects.AbstractGameObject;

public class Level 
{
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE 
	{
		EMPTY(0, 0, 0), // black
		PLATFORMS(0, 255, 0), // green
		PLAYER_SPAWNPOINT(255, 255, 255), // white
		POWERUP(255, 0, 255), // purple
		ITEMS(255, 255, 0); // yellow
		 
		
		private int color;
		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		/**
		 * Checks if the colors are the same
		 * @param color
		 * @return
		 */
		public boolean sameColor (int color)
		{
			return this.color == color;
		}
		
		/**
		 * Gets the current color
		 * @return
		 */
		public int getColor ()
		{
			return color;
		}
	}
	
	public Array<Platform> platforms;
	public Timmy tim;
	public Array<Points> point;
	public Array<Star> enhancement;
	
	
	// ending
	//public Goal goal;
	
	// decoration
	public People people;
	public City background;
	public Chopper chop;

	
	public Level (String filename)
	{
		init(filename);
	}
	
	
	
	private void init (String filename)
	{
		// player character
		tim = null;
		
		// objects
		platforms = new Array<Platform>();	
		point = new Array<Points>();
		enhancement = new Array<Star>();
		
		
		// load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		
		// scan pixels from top-left to bottom-right
		int lastPixel = -1;
		
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) 
		{
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) 
			{
				AbstractGameObject obj = null;
				float offsetHeight = 0;
						
				// height grows from bottom to top
				float baseHeight = pixmap.getHeight() - pixelY;
						
				// get color of current pixel as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
						
				// find matching color value to identify block type at (x,y)
				// point and create the corresponding game object if there is
				// a match
				
				// empty space
				if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) 
				{
						// do nothing
				}
				// platform
				else if (BLOCK_TYPE.PLATFORMS.sameColor(currentPixel)) 
				{
					if (lastPixel != currentPixel) 
					{
						obj = new Platform();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y
								* heightIncreaseFactor + offsetHeight);
						platforms.add((Platform)obj);
					} 
					else 
					{
						platforms.get(platforms.size - 1).increaseLength(1);
					}
				}
				// Timmy
				else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) 
				{
					obj = new Timmy();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y +
							offsetHeight);
					tim = (Timmy)obj;
				}		
				// Adds feathers to the map
				else if (BLOCK_TYPE.POWERUP.sameColor(currentPixel)) 
				{
					/*obj = new Star();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y +
							offsetHeight);
					enhancement.add((Star)obj);*/
				}		
				// Adds gold coins to the map
				else if (BLOCK_TYPE.ITEMS.sameColor(currentPixel)) 
				{
					/*obj = new Points();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y +
							offsetHeight);
					point.add((Points)obj);*/
				}
				// unknown object/pixel color
				else 
				{
					int r = 0xff & (currentPixel >>> 24); //red color channel
					int g = 0xff & (currentPixel >>> 16); //green color channel
					int b = 0xff & (currentPixel >>> 8); //blue color channel
					int a = 0xff & currentPixel; //alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
							+ pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
				
		// decoration
		people = new People(pixmap.getWidth());
		people.position.set(0, -5);
		background = new City(pixmap.getWidth());
		background.position.set(0, -5);
		chop = new Chopper(pixmap.getWidth());
		chop.position.set(0, 2.0f);
				
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}
	public void update(float deltatime)
	{
		for (Platform plat : platforms)
			plat.update(deltatime);
	}
	
	public void render (SpriteBatch batch)
	{
		// Draw City background
		background.render(batch);
		
		// Draw Platform
		for (Platform plat : platforms)
			plat.render(batch);
		
		// Draw items
		for (Points p : point)
			p.render(batch);
		
		// Draw Star
		for (Star up : enhancement)
			up.render(batch);
		
		
		// Draw background people
		people.render(batch);
		
		/*// Draw choppers
		chop.render(batch);*/ 
		
		tim.render(batch);
	}
	
	
	
	
	
	
}
