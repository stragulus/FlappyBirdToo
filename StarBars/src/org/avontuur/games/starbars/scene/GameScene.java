package org.avontuur.games.starbars.scene;

import org.andengine.engine.camera.hud.HUD;
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
import org.avontuur.games.starbars.Player;
import org.avontuur.games.starbars.base.BaseScene;
import org.avontuur.games.starbars.manager.SceneManager;
import org.avontuur.games.starbars.manager.SceneManager.SceneType;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends BaseScene implements IOnSceneTouchListener 
{
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";

	private PhysicsWorld physicsWorld;
    
	private Player player;
	private HUD gameHUD;
	private Text scoreText;
	private Text gameOverText;
	
	private boolean gameOverDisplayed = false;
	private int score = 0;


	@Override
	public void createScene()
	{
		createBackground();
		createHUD();
		createPhysics();
		createPlayer();
		createGameOverText();
	    setOnSceneTouchListener(this);
	    
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
	    if (pSceneTouchEvent.isActionDown())
	    {
	    	player.jump();
	    }
	    return false;
	}

	@Override
    public void onBackKeyPressed()
    {
		SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	camera.setHUD(null);
    	camera.setChaseEntity(null);
    }

	private void createHUD()
	{
	    gameHUD = new HUD();
	    
	    // CREATE SCORE TEXT
	    // use all numbers to preload all data
	    scoreText = new Text(20, Constants.CAMERA_HEIGHT - resourcesManager.fontSize - 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    scoreText.setAnchorCenter(0, 0);    
	    scoreText.setText("Score: 0");
	    gameHUD.attachChild(scoreText);
	 
	    camera.setHUD(gameHUD);
	}

	private void createPhysics()
	{
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
	    registerUpdateHandler(physicsWorld);
	}

	private void createBackground()
	{
		setBackground(new Background(Color.BLUE));
	}

	private void createPlayer()
	{
	    player = new Player(Constants.CAMERA_WIDTH / 2, Constants.CAMERA_HEIGHT / 2, vbom, camera, physicsWorld)
	    {
	        @Override
	        public void onDie()
	        {
	        	if (!gameOverDisplayed)
        	    {
        	        displayGameOverText();
        	    }
	        }
	    };
	    player.setCullingEnabled(true);
	}
	private void addToScore(int i)
	{
	    score += i;
	    scoreText.setText("Score: " + score);
	}
	
	private void createGameOverText()
	{
	    gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
	}

	private void displayGameOverText()
	{
	    camera.setChaseEntity(null);
	    gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
	    attachChild(gameOverText);
	    gameOverDisplayed = true;
	}
}
