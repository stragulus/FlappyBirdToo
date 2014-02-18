package org.avontuur.games.starbars.entity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.avontuur.games.starbars.Constants;
import org.avontuur.games.starbars.manager.ResourcesManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Pillar
{
	final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
	
	private List<Sprite> sprites;
	private List<Body> bodies;
	private List<PhysicsConnector> connectors;
	private int speed;
	
	public Pillar(final float pX, final int pGapSize, final int gapYOffset, final int speed, final Scene pScene, final PhysicsWorld physicsWorld, final VertexBufferObjectManager pVbom)
	{
		//x is the leftmost coordinate of the pillar.
		//gapYOffset is the bottom Y coordinate where the gap starts
		final int pillarTopHeight = 56;
		final int w = 200;

		this.speed = speed;
		sprites = new ArrayList<Sprite>(4);
		bodies = new ArrayList<Body>(4);
		connectors = new ArrayList<PhysicsConnector>(4);
		
		// draw the bottom pillar
		float x = pX;
		int y = 0;
		int h = gapYOffset - pillarTopHeight - y;
		addPiece(x + (w/2), y + (h / 2), w, h, ResourcesManager.getInstance().pillar_base_region, pScene, physicsWorld, pVbom);
		y += h;
		h = pillarTopHeight;
		addPiece(x + (w/2), y + (h / 2), w, h, ResourcesManager.getInstance().pillar_top_region, pScene, physicsWorld, pVbom);
		y += h + pGapSize;;
		
		// draw the top pillar
		h = pillarTopHeight;
		addPiece(x + (w/2), y + (h / 2), w, h, ResourcesManager.getInstance().pillar_top_region, pScene, physicsWorld, pVbom);
		y += h;
		h = Constants.CAMERA_HEIGHT - y;
		addPiece(x + (w/2), y + (h / 2), w, h, ResourcesManager.getInstance().pillar_base_region, pScene, physicsWorld, pVbom);
	}
	
	public void setX(final float pX)
	{
		for (Sprite sprite: sprites) {
			sprite.setX(pX);
		}
	}
	
	public void detach(Scene scene, PhysicsWorld physicsWorld)
	{
		// XXX untested, not sure this will even work..
		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.get(i);
			Body body = bodies.get(i);
			PhysicsConnector connector = connectors.get(i);
			// XXX Does this method of finding the connector actually work?
    		physicsWorld.unregisterPhysicsConnector(connector);
    		physicsWorld.destroyBody(body);
    		scene.detachChild(sprite);
		}
	}
	
	
	public final float getX()
	{
		return sprites.get(0).getX();
	}
	
	private void addPiece(final float x, final float y, final float w, final float h, final ITextureRegion region, final Scene scene, final PhysicsWorld physicsWorld, final VertexBufferObjectManager vbom)
	{
		final Sprite sprite = new Sprite(x, y, w, h, region, vbom);
		sprite.setCullingEnabled(true);
		scene.attachChild(sprite);
		sprites.add(sprite);
		final Body body = PhysicsFactory.createBoxBody(physicsWorld, sprite, BodyType.KinematicBody, FIXTURE_DEF);
		body.setUserData("pillar");
		body.setFixedRotation(true);
		body.setLinearVelocity(this.speed, 0);
		bodies.add(body);
		PhysicsConnector connector;
		
		connector = new PhysicsConnector(sprite, body, true, false);
		physicsWorld.registerPhysicsConnector(connector);
		connectors.add(connector);
		//XXX The connector has access to the body and sprite, so can ditch keeping track of those
	}
}
