package org.avontuur.games.starbars.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.avontuur.games.starbars.Constants;
import org.avontuur.games.starbars.GameActivity;

import android.graphics.Color;

public class ResourcesManager {
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public Font font;
    public int fontSize;

    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------

    
    // MENU resources
    public ITextureRegion menu_background_region;
    public ITextureRegion menubutton_play_region;
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    
    
    //SPLASH SCREEN resources
    public ITextureRegion splash_region;
    private BitmapTextureAtlas splashTextureAtlas;
        
    //GAME SCENE resources
    public ITextureRegion player_region;
    public ITextureRegion pillar_base_region;
    public ITextureRegion pillar_top_region;
    public BuildableBitmapTextureAtlas gameTextureAtlas;
    private BitmapTextureAtlas repeatablePillarAtlas;
    


    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }
    
    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        //XXX so the 1024 values here determine the maximum # of characters that can be displayed..silly.
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        fontSize = Math.round(Constants.CAMERA_HEIGHT / 12f);
        //fontSize = 80;
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, 
        		activity.getAssets(), "capture_it.ttf", fontSize, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    private void loadMenuGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1920, 1920, TextureOptions.BILINEAR);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.png");
    	menubutton_play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "button_play.png");
    	//options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "button_options.png");
    	       
    	try 
    	{
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e)
    	{
    	        Debug.e(e);
    	}

    }
    
    private void loadMenuAudio()
    {
        
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }

    private void loadGameGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        
        //player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
        player_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "player.png");
        pillar_top_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "pillar_top.png"); //200x56
        repeatablePillarAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 28, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        pillar_base_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(repeatablePillarAtlas, activity, "pillar_base.png", 0, 0); //200x28
        repeatablePillarAtlas.load();
        
        try 
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }

    }
    
    public void unloadGameTextures()
    {
        repeatablePillarAtlas.unload();
        gameTextureAtlas.unload();
    }

    private void loadGameFonts()
    {
        
    }
    
    private void loadGameAudio()
    {
    	//TODO: add sound!
    }
    
    public void loadSplashScreen()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 720, 720, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen()
    {
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}
