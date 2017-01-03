package com.rowland.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rowland.GameData.GameData;

public class AssetLoader {
    //Constants
	private final static String FILE_SPRITE_ATLAS = "data/yoyo_sprite.atlas";	
	private final static String FILE_UI_SKIN = "skin/uiskin.json";
    //Textures
	public static TextureAtlas spriteAtlas;
	public static Skin skin;
	//Defining the Texture Regions
	//Backgrounds
	public static TextureRegion background_menu_berge;
	public static TextureRegion bg;
	public static TextureRegion background_home;
	public static TextureRegion holder;
	public static TextureRegion button_overlay_right;
	public static TextureRegion button_overlay_left;
	public static TextureRegion logo;
	//Mad Boy
	public static TextureRegion firstFrame;
	public static TextureRegion secondFrame;
	public static TextureRegion thirdFrame;
	public static TextureRegion fourthFrame;
	public static TextureRegion fifthFrame;
	public static TextureRegion sixthFrame;
	public static TextureRegion seventhFrame;
	public static TextureRegion eighthFrame;
	public static TextureRegion ninethFrame;
	public static TextureRegion tenthFrame;
	public static TextureRegion eleventhFrame;
	public static TextureRegion twelvethFrame;
	public static TextureRegion thirteenthFrame;
	public static TextureRegion fourteenthFrame;
	public static TextureRegion fifteenthFrame;
	//User Interface UI
	public static TextureRegion lock_closed;
	public static TextureRegion lock_open;
	public static TextureRegion star_empty;
	public static TextureRegion star_normal;
	public static TextureRegion star_shinny;
	//Buttons
	public static TextureRegion button_green;
	public static TextureRegion button_level_green;
	public static TextureRegion button_level_grey;
	public static TextureRegion button_rect_green;
	public static TextureRegion button_rect_grey;
	public static TextureRegion button_credit_up;
	public static TextureRegion button_credit_down;
	public static TextureRegion button_menu_up;
	public static TextureRegion button_menu_down;
	public static TextureRegion button_start_up;
	public static TextureRegion button_start_down;
	public static TextureRegion button_quit_up;
	public static TextureRegion button_quit_down;
	public static TextureRegion button_highscore_up;
	public static TextureRegion button_highscore_down;
	public static TextureRegion button_resume_up;
	public static TextureRegion button_resume_down;
	public static TextureRegion button_overlay_pause;
	public static TextureRegion button_sound_on;
	public static TextureRegion button_sound_off;
	//Animation
	public static Animation pummaStill;
	public static Animation pummaWalk;
	public static Animation pummaJump;
	//Defining the Audio Files here
	public static Music music;
	//Defining Our Fonts
	public static BitmapFont smallFont, bigFont, whiteFont, maroonFont, blackFont;
	
	
	public static Texture loadTexture(String file) 
	{
		return new Texture(Gdx.files.internal(file));
	}

	
	public static TextureAtlas getSpriteAtlas() 
	{
		if (spriteAtlas == null) 
		{
			spriteAtlas = new TextureAtlas(Gdx.files.internal(FILE_SPRITE_ATLAS));
		}
		
		return spriteAtlas;
	}
	
	public static Skin getSkin() 
	{
		if (skin == null) {
			//FileHandle skinFile = Gdx.files.internal(FILE_UI_SKIN);
			//skin = new Skin(skinFile);
		}
		return skin;
		
	}

	public static void loadAll() 
	{
		dispose();
		loadImages();
		loadButtons();
		loadMadBoy();
		loadFonts();
		loadAnimations();
		loadSoundsAndMusics();
	}
	
	public static void dispose() 
	{
		spriteAtlas = null;
		skin = null;
	}
	
	public static void loadImages() 
	{
		//TEXTURE REGIONS FROM THE SPRITE ATLAS
		holder = getSpriteAtlas().findRegion("button_green");
		bg = getSpriteAtlas().findRegion("background_nairobi_night");
		background_menu_berge = getSpriteAtlas().findRegion("background_level");
		background_home = getSpriteAtlas().findRegion("background_home");
		logo = getSpriteAtlas().findRegion("logo");
		
		//User Interface UI
		lock_closed = getSpriteAtlas().findRegion("lock_closed");
		lock_open = getSpriteAtlas().findRegion("lock_open");
		star_empty = getSpriteAtlas().findRegion("star_empty");
		star_normal = getSpriteAtlas().findRegion("star_normal");
		star_shinny = getSpriteAtlas().findRegion("star_shinny");
		button_green = getSpriteAtlas().findRegion("button_green");
		star_shinny = getSpriteAtlas().findRegion("star_shinny");
		button_green = getSpriteAtlas().findRegion("button_green");
		
		
	}
	
	public static void loadButtons() 
	{
		// Buttons
		button_level_green = getSpriteAtlas().findRegion("button_level_green");
		button_level_grey = getSpriteAtlas().findRegion("button_level_grey");
		button_rect_green = getSpriteAtlas().findRegion("button_rect_green");
		button_rect_grey = getSpriteAtlas().findRegion("button_rect_grey");
		button_credit_up = getSpriteAtlas().findRegion("button_credit_up");
		button_credit_down = getSpriteAtlas().findRegion("button_credit_down");
		button_menu_up = getSpriteAtlas().findRegion("button_menu_up");
		button_menu_down = getSpriteAtlas().findRegion("button_menu_down");
		button_resume_up = getSpriteAtlas().findRegion("button_resume_up");
		button_resume_down = getSpriteAtlas().findRegion("button_resume_down");
		button_quit_up = getSpriteAtlas().findRegion("button_quit_up");
		button_quit_down = getSpriteAtlas().findRegion("button_quit_down");
		button_highscore_up = getSpriteAtlas().findRegion("button_highscore_up");
		button_highscore_down = getSpriteAtlas().findRegion("button_highscore_down");
		button_start_up = getSpriteAtlas().findRegion("button_start_up");
		button_start_down = getSpriteAtlas().findRegion("button_start_down");
		button_sound_on = getSpriteAtlas().findRegion("button_sound_on");
		button_sound_off = getSpriteAtlas().findRegion("button_sound_off");
		button_overlay_right = getSpriteAtlas().findRegion("button_overlay_right");
		button_overlay_left = getSpriteAtlas().findRegion("button_overlay_left");
		button_overlay_pause = getSpriteAtlas().findRegion("button_overlay_pause");
	}
	
	public static void loadMadBoy() 
	{
		// Mad Boy Frames
		firstFrame = getSpriteAtlas().findRegion("1");
		secondFrame = getSpriteAtlas().findRegion("2");
		thirdFrame = getSpriteAtlas().findRegion("2");
		fourthFrame = getSpriteAtlas().findRegion("4");
		fifthFrame = getSpriteAtlas().findRegion("5");
		sixthFrame = getSpriteAtlas().findRegion("6");
		seventhFrame = getSpriteAtlas().findRegion("7");
		eighthFrame = getSpriteAtlas().findRegion("8");
		ninethFrame = getSpriteAtlas().findRegion("9");
		tenthFrame = getSpriteAtlas().findRegion("10");
		eleventhFrame = getSpriteAtlas().findRegion("11");
		twelvethFrame = getSpriteAtlas().findRegion("12");
		thirteenthFrame = getSpriteAtlas().findRegion("13");
		fourteenthFrame = getSpriteAtlas().findRegion("14");
		fifteenthFrame = getSpriteAtlas().findRegion("15");
	}

	public static void loadFonts() 
	{
		//Load our fonts
		smallFont = new BitmapFont(Gdx.files.internal("data/smallFont.fnt"), Gdx.files.internal("data/smallFont.png"), false);
		bigFont = new BitmapFont(Gdx.files.internal("data/bigFont.fnt"), Gdx.files.internal("data/bigFont.png"), false);
		whiteFont = new BitmapFont(Gdx.files.internal("data/whiteFont.fnt"), Gdx.files.internal("data/whiteFont.png"), false);
		maroonFont = new BitmapFont(Gdx.files.internal("data/maroonFont.fnt"), Gdx.files.internal("data/maroonFont.png"), false);
		blackFont = new BitmapFont(Gdx.files.internal("data/blackFont.fnt"), Gdx.files.internal("data/blackFont.png"), false);
	}

	public static void loadAnimations() 
	{
		TextureRegion[] yoyos = { firstFrame, secondFrame, thirdFrame, fourthFrame, fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, twelvethFrame, thirteenthFrame, fourteenthFrame, fifteenthFrame  };
		TextureRegion[] yoyoStill = {  eleventhFrame, twelvethFrame };
		TextureRegion[] yoyoJump = { fifthFrame, sixthFrame, seventhFrame, eighthFrame, ninethFrame, tenthFrame, eleventhFrame, };
		
        pummaWalk = new Animation(1/30f, yoyos);
        pummaWalk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		
        pummaStill = new Animation(0.3f, yoyoStill);
        pummaStill.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);	
        
        pummaJump = new Animation(1/30f, yoyoJump);
        pummaJump.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
	}

	public static void loadSoundsAndMusics()
	{   
		if (music == null) 
		{
			music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		    music.setLooping(true);
	    }
		//Start Playing the Music if the sound is enabled
		if (GameData.getSoundEnabled()) AssetLoader.music.play();
				
	}
	
	public static void playSound (Sound sound) 
	{
		if (GameData.getSoundEnabled()) sound.play(1);
	}


}