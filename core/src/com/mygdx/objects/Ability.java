package com.mygdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;

public class Ability extends AbstractGameObject
{

	private TextureRegion star;
	public static boolean fire=false;
	public float time;
	public float step; 
	public Vector2 startingVelocity;
	public Vector2 startingPosition;
	public Vector2 traject; 
	 
	
	public Ability()
	{
		init(); 
	}
	
	private void init() 
	{
		dimension.set(.5f, .5f); 
		star=Assets.instance.up.power;
		traject = new Vector2();
		step=0; 
		 
	}

	
	
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

	@Override
	public void render(SpriteBatch batch) 
	{
		if(!fire) return; 
		TextureRegion reg = null;
		reg = star;
		position = body.getPosition(); 
		
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), 
				reg.getRegionY(), reg.getRegionWidth(),
				reg.getRegionHeight(), false, false);
		
	}


	
	

	public void setFire(boolean b) 
	{
		this.fire=b; 
		
	}
	
	

}
