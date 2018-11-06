package com.mygdx.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Level;
import com.mygdx.game.WorldController;
import com.mygdx.objects.Raindrops.RainDrop;

public class Points extends AbstractGameObject
{
	private TextureRegion point; 
	public boolean collected;
	public static Array<Points> pointScheduledForRemoval;
	
	public Points()
	{
		init(); 
	}

	private void init() 
	{
		dimension.set(0.5f, 0.5f); 
		point = Assets.instance.plus.points;
		pointScheduledForRemoval = new Array<Points>(); 
		collected=false; 
		
	}
	
	public void startContract()
	{
		collected=true; 
		pointScheduledForRemoval.add(this);
		
		
	}
	
	public void createBody(Vector2 position)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
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
	    Fixture f= body.createFixture(fixtureDef);
	    f.setSensor(true);
	    polygonShape.dispose();
		
	}

	@Override
	public void render(SpriteBatch batch) 
	{
		if (collected) return;
	      
	      TextureRegion reg = null;
	      reg = point;  
	      batch.draw(reg.getTexture(), position.x, position.y,
	    		  origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	    		  rotation, reg.getRegionX(), reg.getRegionY(),
	    		  reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
	}
	
	 public int getScore() 
	    {
	        return 10;
	    } 

}
