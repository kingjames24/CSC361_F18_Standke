package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.objects.Raindrops.RainDrop;

/**
 * Class that represents the star object that 
 * the player is allowed to fire when collected 
 * @author adam
 *
 */
public class Ability extends AbstractGameObject
{

	private TextureRegion star;
	public boolean hit;
	public static Array<Ability> abilityScheduledForRemoval;
	public static boolean fire=false;
	public ParticleEffect fireBlast = new ParticleEffect(); 
	
	 
	/**
	 * Constructor that calls a helper method to set up the star object
	 */
	public Ability()
	{
		init(); 
	}
	
	/**
	 * Method that initializes a star object to be .5 meters wide and .5 meters tall. 
	 * Also loads in the star image file. 
	 */
	private void init() 
	{
		dimension.set(.5f, .5f); 
		star=Assets.instance.up.power;
		abilityScheduledForRemoval= new Array<Ability>();
		fireBlast.load(Gdx.files.internal("../core/assets/particles/particle3.p"), Gdx.files.internal("../core/assets/particles/"));	 
	}
	
	/**
	 * Method that creates the Box2d body for the star object to be 
	 * a dynamic type.  
	 */
	@Override
	public void createBody(Vector2 position) 
	{
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation=true;
		bodyDef.position.set(position); 
		body = WorldController.b2world.createBody(bodyDef);
		body.setUserData(this);
		
		PolygonShape polygonShape = new PolygonShape();
		origin.x = this.dimension.x/2; 
		origin.y = this.dimension.y/2; 
		polygonShape.setAsBox(this.dimension.x/2,this.dimension.y/2, origin, 0);
		FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = polygonShape; 
	    body.createFixture(fixtureDef);
	    polygonShape.dispose();
		
	}
	
	public void update(float deltaTime)
	{
		
		fireBlast.start();
		fireBlast.update(deltaTime);
	}

	/**
	 * Method that renders the star image. Together, the texture's position and other 
	 * attributes are stored in SpriteBatch's vertex array
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		if(!fire) return; 
		TextureRegion reg = null;
		reg = star;
		
		fireBlast.draw(batch);
		
		
		if(body== null) return; 
		position = body.getPosition(); 
		fireBlast.setPosition(position.x, position.y);
		
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		
	}
	/**
	 * Method that determines whether the player can shoot other game objects 
	 * or not
	 * @param b a boolean that represents either true or false
	 */
	public void setFire(boolean b) 
	{
		Ability.fire=b; 
		
	}
	
	public void startContact() 
	{
		hit=true;
		abilityScheduledForRemoval.add(this);	  	
	}
	
	

}
