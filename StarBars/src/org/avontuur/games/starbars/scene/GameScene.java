package org.avontuur.games.starbars.scene;

import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.avontuur.games.starbars.Constants;
import org.avontuur.games.starbars.base.BaseScene;
import org.avontuur.games.starbars.entity.Pillar;
import org.avontuur.games.starbars.entity.Player;
import org.avontuur.games.starbars.manager.SceneManager;
import org.avontuur.games.starbars.manager.SceneManager.SceneType;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends BaseScene implements IOnSceneTouchListener 
{
	// Game difficulty settings
	private static final int PILLAR_GAP_SIZE = (int)Math.round(Constants.CAMERA_HEIGHT * 0.4);
	private static final int PILLAR_SPEED = -9;
	private static final int PILLAR_DISTANCE = 500;
	private static final int JUMP_FORCE = 35;

	private PhysicsWorld physicsWorld;
    
	private Player player;
	private HUD gameHUD;
	private Text scoreText;
	private Text gameOverText;
	private Text startGameText;
	
	private boolean gameOverDisplayed = false;
	private boolean playerCollided = false;
	private boolean handledOnDie = false;
	private int score = 0;
	private boolean firstTouch = false;
	private Pillar lastScoredPillar = null;
	
	// pillars
	private LinkedList<Pillar> pillars;

	@Override
	public void createScene()
	{
		createBackground();
		createHUD();
		createPhysics();
		createGameOverText();
		createStartGameText();
		createPlayer();
		displayStartGameText();
	    setOnSceneTouchListener(this);
	    createUpdateLoop();
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)	{
		if (gameOverDisplayed) {
    		return false;
		}	

		if (pSceneTouchEvent.isActionDown()) {
	    	if (!firstTouch) {
	    		hideStartGameText();
	    		player.startPlayer();
	    		firstTouch = true;
	    	}
	    	player.jump();
	    }
	    return false;
	}

	@Override
    public void onBackKeyPressed(){
		//SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
    	camera.setHUD(null);
    	camera.setChaseEntity(null);
    	camera.setCenter(Constants.CAMERA_WIDTH/2, Constants.CAMERA_HEIGHT/2);
    }

	private void createHUD() {
	    gameHUD = new HUD();
	    
	    // CREATE SCORE TEXT
	    // use all numbers to preload all data
	    scoreText = new Text(20, Constants.CAMERA_HEIGHT - resourcesManager.fontSize - 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    scoreText.setAnchorCenter(0, 0);    
	    scoreText.setText("Score: 0");
	    gameHUD.attachChild(scoreText);
	 
	    camera.setHUD(gameHUD);
	}

	private void createPhysics() {
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -100), false); 
	    //physicsWorld.setContactListener(contactListener());
	    registerUpdateHandler(physicsWorld);
	    physicsWorld.setContactListener(createContactListener());
	}

	private ContactListener createContactListener() {
		ContactListener contactListener = new ContactListener() {
	        @Override
	        public void beginContact(Contact contact)
	        {
	            final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	           
	            String ud1 = (String)x1.getBody().getUserData();
	            String ud2 = (String)x2.getBody().getUserData();
	            
	            if (ud1 != null && ud2 != null &&
	            	(
	            		(ud1.equals("player") && ud2.equals("pillar"))
	            		|| (ud1.equals("pillar") && ud2.equals("player"))
	            	)
	            ) {
	                GameScene.this.playerCollided = true;
	            }

	        }

	        @Override
	        public void endContact(Contact contact)
	        {
	               
	        }

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
	    };
	    return contactListener;
	}
	private void createBackground()	{
		setBackground(new Background(Color.BLACK));
	}

	private void createPlayer()
	{
	    player = new Player(Constants.CAMERA_WIDTH / 2, Constants.CAMERA_HEIGHT / 2, JUMP_FORCE, vbom, camera, physicsWorld) {
	        @Override
	        public void onDie() {
	        	if (!handledOnDie && !gameOverDisplayed) {
	        		engine.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							GameScene.this.gameOver();
						}
					});
	        		handledOnDie = true;
        	    }
	        }
	    };
	    
	    // only render sprite when it's on-screen
	    player.setCullingEnabled(true);
	    attachChild(player);
	}
	
	private void addPillar(final int x, final int gapYOffset) {
		pillars.add(new Pillar(x, PILLAR_GAP_SIZE, gapYOffset, PILLAR_SPEED, this, physicsWorld, vbom));
	}
	
	private void removePlayer()	{
		detachChild(player);
	}
	
	private void addToScore(int i) {
	    score += i;
	    scoreText.setText("Score: " + score);
	    //TODO: highscores
	}
	
	private void createGameOverText() {
	    gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
	}

	private void displayGameOverText() {
	    camera.setChaseEntity(null);
	    gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
	    attachChild(gameOverText);
	    gameOverDisplayed = true;
	}
	
	// run me in update thread!
	private void gameOver() {
		assert gameOverDisplayed == false;
		displayGameOverText();
		// TODO: add timer to add buttons to do something? Or display the a 'medal'.
	}
	
	private void createStartGameText() {
		startGameText = new Text(Constants.CAMERA_WIDTH / 2, Math.round((Constants.CAMERA_HEIGHT /2) * 1.3), resourcesManager.font, "Tap ship to jump!", vbom);
	}
	
	private void displayStartGameText()	{
		attachChild(startGameText);
	}
	
	private void hideStartGameText() {
		this.detachChild(startGameText);
	}
	
	private void managePillars() {
		// adds and removes pillars as they move over the screen
		// this method must be run in the update thread!
	
		if (pillars == null) {
			pillars = new LinkedList<Pillar>();
			addPillar(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT / 2 - 200);
			return;
		}

		// did the first pillar move out of the camera view?
		Pillar p = pillars.peek();
		if (p != null && p.getX() < -100) {
			p.detach(this, physicsWorld);
			pillars.poll();
		}
		
		// is it time to add another pillar? do this whenever the last pillar in the queue 
		// has passed a threshold value
		p = null;
		if (pillars.size() > 0) {
			p = pillars.getLast();
		}
		
		if (p.getX() <= (Constants.CAMERA_WIDTH - PILLAR_DISTANCE)) {
			// TODO: gap offset @ random location
			int rndNum = new Random().nextInt(Constants.CAMERA_HEIGHT - PILLAR_GAP_SIZE - 100);
			addPillar(Constants.CAMERA_WIDTH, rndNum + 50);
		}
	}
	
	private void checkScore() {
		for (Pillar p: pillars) {
			// count score if the x coordinates are almost identical
			if (Math.abs(player.getX() - p.getX()) < 5) {
				if (lastScoredPillar == null || lastScoredPillar != p) {
					addToScore(1);
					lastScoredPillar = p;
					break;
				}
			}
		}
	}
	private void createUpdateLoop() {
		engine.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (playerCollided && !gameOverDisplayed) {
					gameOver();
        	    }
				
				if (!gameOverDisplayed) {
				    // TODO: count score
					GameScene.this.managePillars();
					checkScore();
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
			}
		});
	}
}
