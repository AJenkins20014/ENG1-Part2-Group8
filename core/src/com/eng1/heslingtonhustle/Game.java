/**
 * The main class representing the game.
 * This class extends the com.badlogic.gdx.Game class and
 * initialises manager objects that are crucial for the game
 * to function.
 */
package com.eng1.heslingtonhustle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.heslingtonhustle.building.Building;
import com.eng1.heslingtonhustle.building.BuildingManager;
import com.eng1.heslingtonhustle.graphics.CameraManager;
import com.eng1.heslingtonhustle.graphics.RenderingManager;
import com.eng1.heslingtonhustle.map.MapManager;
import com.eng1.heslingtonhustle.player.InputHandler;
import com.eng1.heslingtonhustle.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class Game extends ApplicationAdapter {

    public static final int SCALE = 5;
    public static final int PLAYER_SIZE = 32 * SCALE;
    public List<Building> buildings = new ArrayList<>();
    public PlayerManager playerManager;
    public Stage stage;
    public RenderingManager renderingManager;
    public CameraManager cameraManager;
    public GameManager gameManager;
    public MapManager mapManager;
    
    public static final String bgMusic = "../assets/bgtrack.mp3";

    /**
     * Initialises the game.
     */
    @Override
    public void create() {
    	// Import and play background music
    	Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(bgMusic));
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.5f);
       
        // Create manager objects
        cameraManager = new CameraManager();
        mapManager = new MapManager();
        BuildingManager buildingManager = new BuildingManager();
        stage = new Stage(cameraManager.getViewport());

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(4608, 960);
        playerManager = new PlayerManager(spawn, 320*2, this);
       
        // Set tile collision fields
        playerManager.getMovement().setCollidableTiles(mapManager.getCollidableTiles());

        // Initialise input system
        inputSetup();

        // Initialise buildings
        buildings = buildingManager.getCampusBuildings();
        renderingManager = new RenderingManager(cameraManager, mapManager, playerManager);
        gameManager = new GameManager(stage, mapManager, playerManager, buildingManager, renderingManager);
    }
    
    /**
     * Initialises the game with a mocked OrthogonalTiledMapRenderer, Stage and SpriteBatch for testing.
     * @param mapRendererMock The mocked OrthogonalTiledMapRenderer
     * @param stageMock The mocked Stage
     * @param spriteBatchMock The mocked SpriteBatch
     */
    public void testCreate(OrthogonalTiledMapRenderer mapRendererMock, Stage stageMock, SpriteBatch spriteBatchMock) {
        // Create manager objects
        cameraManager = new CameraManager();
        MapManager mapManager = new MapManager(mapRendererMock);
        BuildingManager buildingManager = new BuildingManager();
        stage = stageMock;

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(4608, 960);
        playerManager = new PlayerManager(spawn, 320*2, this);
       
        // Set tile collision fields
        playerManager.getMovement().setCollidableTiles(mapManager.getCollidableTiles());

        // Initialise input system
        inputSetup();

        // Initialise buildings
        buildings = buildingManager.getCampusBuildings();
        renderingManager = new RenderingManager(cameraManager, mapManager, playerManager, spriteBatchMock);
        gameManager = new GameManager(stage, mapManager, playerManager, buildingManager, renderingManager);
    }

    /*
     * Initialises the input system by creating a new InputHandler,
     * InputMultiplexer and setting the input processer to the multiplexer
     */
    private void inputSetup() {
        InputHandler inputHandler = new InputHandler(playerManager.getState());
        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputHandler, stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    /**
     * Called when the screen size changes.
     * @param width The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        cameraManager.getViewport().update(width, height, true);
    }

    /**
     * Renders the game and its components. Called every frame.
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        playerManager.getMovement().update(deltaTime);
        gameManager.update();
        renderingManager.render(buildings, playerManager);
        stage.act();
        stage.draw();

    }
}
