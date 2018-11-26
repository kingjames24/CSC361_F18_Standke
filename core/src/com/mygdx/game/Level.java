package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.objects.City;
import com.mygdx.objects.People;
import com.mygdx.objects.Platform;
import com.mygdx.objects.Points;
import com.mygdx.objects.Star;
import com.mygdx.objects.Timmy;
import com.mygdx.objects.Ability;
import com.mygdx.objects.AbstractGameObject;

/**
 * Sets up the level by taking a png image and then decompresses it.
 * Then, it examines each individual pixel of the image and initializes
 * various game object based on the color property of the pixel.
 * by Adam Standke
 */
public class Level 
{
	public static final String TAG = Level.class.getName();
	
	/**
	 * 
	 *An enum that represents a group of game objects
	 *by attaching a RGB color value to each object.    
	 *
	 */
	public enum BLOCK_TYPE 
	{
		EMPTY(0, 0, 0), // empty space is black
		PLATFORMS(0, 255, 0), // platforms are green
		PLAYER_SPAWNPOINT(255, 255, 255), // player spawn point is white
		POWERUP(255, 0, 255), // star location is purple
		ITEMS(255, 255, 0); // collectible game points are yellow
		 
		//store decimal value of each game object's associated color  
		private int color;
		
		/**
		 * Enum method that translates three separate RGB values into one 32-bit
		 * number. This number is automatically converted by the program into an
		 * equivalent decimal number that ranges from 0 to 4294967295   
		 * @param r an int representing the R value from the RGB color model 
		 * @param g an int representing the G value from the RGB color model
		 * @param b an int representing the B value from the RGB color model
		 */
		private BLOCK_TYPE (int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		/**
		 * Method checks to see if the color
		 * stored in  object's field is the same value 
		 * as the pixel stored in the pixel map
		 * @param color an int representing a RGB color value
		 * @return a boolean that either output true or false
		 */
		public boolean sameColor (int color)
		{
			return this.color == color;
		}
		
		/**
		 * Method that gets the current color
		 * @return an int representing a RGB color value
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
	public People people;
	public City background;
	public Ability ability; 
	 
	

	/**
	 * Constructor that accesses the level by its filename and
	 * then calls a helper method to actually create the game's level
	 * layout
	 * @param filename a string that represents a level's filename
	 */
	public Level (String filename)
	{
		init(filename);
	}
	
	
	/**
	 * Reads in the level and makes sure the colors used to I.D the specific
	 * elements of the level are placed correctly
	 * @param filename a string that represents a level's filename
	 */
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
						float heightIncreaseFactor = 0.50f;
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
					obj = new Star();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y +
							offsetHeight);
					obj.createBody(obj.position);
					enhancement.add((Star)obj);
				}		
				// Adds gold coins to the map
				else if (BLOCK_TYPE.ITEMS.sameColor(currentPixel)) 
				{
					obj = new Points();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y +
							offsetHeight);
					obj.createBody(obj.position);
					point.add((Points)obj);
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
		ability = new Ability(); 
		
				
		// free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "level '" + filename + "' loaded");
	}
	
	/**
	 * Method that updates game objects based on a previous action 
	 * taken by the player in the last frame
	 * @param a float that represents the time-span between the
	 * previously rendered frame and currently rendered frame
	 */
	public void update(float deltatime)
	{
		//for (Platform plat : platforms) plat.update(deltatime);
		ability.update(deltatime);
		tim.update(deltatime);
	}
	
	/**
	 * Method that draws the level in the order specified; namely,
	 * objects that are rendered before other objects will be overlayed
	 * by other objects that are rendered later
	 * @param batch an object of the SpriteBatch class that represents a batch of sprites
	 */
	public void render (SpriteBatch batch)
	{
		
		
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
