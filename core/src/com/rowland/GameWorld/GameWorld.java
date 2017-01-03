package com.rowland.GameWorld;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.magnetideas.smoothcam.SmoothCamWorld;
import com.moribitotech.mtx.settings.AppSettings;
import com.rowland.GameData.PlayerData;
import com.rowland.GameObjects.Yoyo;
import com.rowland.Screens.GameScreen;
import com.rowland.madboy.MadBoyGame;

public class GameWorld extends SmoothCamWorld

{
    public interface WorldListener {


    }

    public static final float DEFAULT_VIEWPORT_WIDTH = 32;
    public static final float DEFAULT_VIEWPORT_HEIGHT = 18;
    public static final float GRAVITY = -2.5f;
    public static final float WORLD_UNIT = 1 / 32f;

    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;

    public int state;

    public static int levelID = 1;

    //The time spent after the world is created (in seconds)
    private float startTime = System.nanoTime();
    public static float SECONDS_TIME = 0;

    //VARIABLES FOR LEVEL MAPS AND THEIR PROPERTIES

    public TiledMap map;
    public MapProperties prop;
    public static int mapWidth;
    public static int mapHeight;
    public final WorldListener listener;

    //Create the Player here
    private Yoyo yoyo;


    //VARIABLES FOR PLAYER ACHIEVEMENTS/SCORES/HURTS
    public static int score;


    //Rectangles for Checking Bounds between Player and Level map
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    private Array<Rectangle> tiles = new Array<Rectangle>();

    public GameWorld(WorldListener listener, MadBoyGame game) {
        this.listener = listener;
        this.state = WORLD_STATE_RUNNING;

        SECONDS_TIME = 0;
        //TAKE THIS LEVEL ID FROM GAMESCREEN AS A PARAMETER
        //IF THE GAME IS CONTINUED THEN IT WILL READ THE CURRENTLEVEL FROM JSON/PREFERENCES/DATABASES

        //IDEA IS SAVE BOOLEAN GAMECONTINUED IF THE CONTINUE BUTTON IS PRESSED AND USE IF O DETERMINE
        levelID = GameScreen.currentlevel;
        generateLevelMap(levelID, game);

        //score = 0;

        //create player
        this.yoyo = new Yoyo(0, 0);
        yoyo.setPosition(PlayerData.getPlayerPosition(levelID)[0][0], PlayerData.getPlayerPosition(levelID)[0][1]);
    }

    private void generateLevelMap(int levelID, MadBoyGame game) {
        // Load the map
        game.getManager().loadTiledMap(levelID);
        game.getManager().finishLoading();

        map = game.getManager().get("data/level/level" + levelID + ".tmx");

        prop = map.getProperties();
        // Get the properties
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
    }

    /**
     * MOST IMPORTANT METHOD THAT UPDATES THE WHOLE GAME WORLD
     * JUST THE INPUT EVENTS FOR PLAYER AND GAME PAUSE ARE HANDLED IN THE GAMESCREEN
     * UPDATES THE PLAYER AND ALL OTHER GAME OBJECTS
     * CHECKS FOR COLLISION BETWEEN VARIOUS GAME OBJECTS INCLUDING LAYER TILES
     * CHECKS FOR THE CONDITION FOR GAME OVER, LEVEL END
     **/

    public void update(float deltaTime) {
        if (deltaTime == 0) return;

        updateYoyo(deltaTime);
        checkCollisions(deltaTime);
        checkGameOver();

        if (System.nanoTime() - startTime >= 1000000000) {
            SECONDS_TIME++;
            startTime = System.nanoTime();
        }

        //if (SECONDS_TIME % 5 == 0)
             //yoyo.health--;
    }


    private void checkGameOver() {
        //IF THE PLAYER FALLS DOWN
        if (yoyo.position.y <= -AppSettings.SCREEN_H / 10) {
            state = WORLD_STATE_GAME_OVER;
            GameScreen.gameoverinfo = "Yoyo Fell Down";
        }

        if (yoyo.health <= 0) {
            state = WORLD_STATE_GAME_OVER;
            GameScreen.gameoverinfo = "Yoyo lost Health";
        }
    }

    //UPDATES NECESSARY FOR THE PLAYER LIKE APPLYING GRAVITY, DAMPING ETC
    private void updateYoyo(float deltaTime) {

        if (deltaTime == 0)
            return;

        yoyo.stateTime += deltaTime;
        //Updates the Player helping not to cross the map Boundaries
        yoyo.update(deltaTime);
        //Apply gravity if we are falling
        yoyo.velocity.add(0, GRAVITY);

        //Clamp the velocity to the maximum, x-axis only
        if (Math.abs(yoyo.velocity.x) > yoyo.MAX_VELOCITY) {
            yoyo.velocity.x = Math.signum(yoyo.velocity.x) * yoyo.MAX_VELOCITY;
        }

        // clamp the velocity to 0 if it's < 1, and set the state to Standing
        if (Math.abs(yoyo.velocity.x) < 1) {
            yoyo.velocity.x = 0;
            if (yoyo.grounded)
                yoyo.setState(Yoyo.STILL);
        }

        // multiply by delta time so we know how far we go
        // in this frame
        yoyo.velocity.scl(deltaTime);

    }

    public Yoyo getYoyo() {
        return yoyo;
    }

    //CHECK FOR COLLISION REGULARLY IN GAMES
    private void checkCollisions(float deltaTime) {
        // Check for collision regularly in games
        checkCollisionsPlayervsMap(deltaTime);
    }

    private void checkCollisionsPlayervsMap(float deltaTime) {
        // perform collision detection & response, on each axis, separately
        // if the Yoyo is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left

        Rectangle yoyoRect = rectPool.obtain();
        yoyoRect.set(yoyo.position.x, yoyo.position.y, Yoyo.width, Yoyo.height);
        int startX, startY, endX, endY;


        if (yoyo.velocity.x > 0) {
            startX = endX = (int) (yoyo.position.x + Yoyo.width + yoyo.velocity.x);
        } else {
            startX = endX = (int) (yoyo.position.x + yoyo.velocity.x);
        }

        startY = (int) (yoyo.position.y);
        endY = (int) (yoyo.position.y + Yoyo.height);

        getTiles(startX, startY, endX, endY, tiles);
        yoyoRect.x += yoyo.velocity.x;

        for (Rectangle tile : tiles) {
            if (yoyoRect.overlaps(tile)) {
                yoyo.velocity.x = 0;
                break;
            }
        }
        yoyoRect.x = yoyo.position.x;

        // if the Yoyo is moving upwards, check the tiles to the top of it's
        // top bounding box edge, otherwise check the ones to the bottom

        if (yoyo.velocity.y > 0) {
            startY = endY = (int) (yoyo.position.y + Yoyo.height + yoyo.velocity.y);
        } else {
            startY = endY = (int) (yoyo.position.y + yoyo.velocity.y);
        }
        startX = (int) (yoyo.position.x);
        endX = (int) (yoyo.position.x + 3 * Yoyo.width / 4);
        getTiles(startX, startY, endX, endY, tiles);
        yoyoRect.y += yoyo.velocity.y;

        //Stores the property of the tile the player collides with in the foreground layer
        String type = "";

        for (Rectangle tile : tiles) {
            if (yoyoRect.overlaps(tile)) {

                //Here we are checking for the tile properties that can be set using Tiled
                TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("foreground_wall");
                try {
                    type = layer.getCell((int) tile.x, (int) tile.y).getTile().getProperties().get("type").toString();
                } catch (Exception e) {
                    //System.out.print("Tile "+type+" :not found : Exception in Tiles Property :" +e);
                    //type="unknown";
                }

                if (yoyo.velocity.y > 0) {

                    if (("1up".equals(type))) {

                        //TiledMapTileSet tileset = map.getTileSets().getTileSet("worldmaptileset1");

                        layer.setCell((int) tile.x, (int) tile.y, layer.getCell(22, 5));
                        layer.setCell((int) tile.x + 1, (int) tile.y, layer.getCell(22, 5));
                        layer.setCell((int) tile.x, (int) tile.y + 1, layer.getCell(22, 5));
                        layer.setCell((int) tile.x + 1, (int) tile.y + 1, layer.getCell(22, 5));

                        score += 100;
                    }

                    if (("breakable1".equals(type))) {
                        layer.setCell((int) tile.x, (int) tile.y, null);

                        score += 20;
                    }

                    yoyo.position.y = (tile.y - Yoyo.height);

                } else {
                    //If the player is falling down this tile i.e.Velocity<=0
                    //Let us change this tile with other
                    yoyo.position.y = (tile.y + tile.height);
                    // if we hit the ground, mark us as grounded so we can jump
                    yoyo.grounded = true;
                }

                yoyo.velocity.y = 0;
                break;
            }
        }
        rectPool.free(yoyoRect);

        // unscale the velocity by the inverse delta time and set
        // the latest position

        yoyo.position.add(yoyo.velocity);
        //yoyo.getVelocityVector().scl(1 / deltaTime);
        yoyo.velocity.scl(1 / deltaTime);
        // Apply damping to the velocity on the x-axis so we don't
        // walk infinitely once a key was pressed

        yoyo.velocity.x *= Yoyo.DAMPING;
    }

    //RESEARCH THIS METHOD
    private void getTiles(int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("foreground_wall");
        //String test = (String) layer.getCell(22, 9).getTile().getProperties().get("tileType");

        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }
}
