/**
 * The main class representing the game.
 * This class extends the com.badlogic.gdx.Game class and
 * initialises manager objects that are crucial for the game
 * to function.
 */
package com.eng1.heslingtonhustle.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eng1.heslingtonhustle.building.Building;
import com.eng1.heslingtonhustle.building.BuildingManager;
import com.eng1.heslingtonhustle.graphics.CameraManager;
import com.eng1.heslingtonhustle.graphics.RenderingManager;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.helper.ScoreManager;
import com.eng1.heslingtonhustle.map.MapManager;
import com.eng1.heslingtonhustle.player.InputHandler;
import com.eng1.heslingtonhustle.player.PlayerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game extends ApplicationAdapter {

    public static final int SCALE = 5;
    public static final int PLAYER_SIZE = 32 * SCALE;
    public List<Building> buildings = new ArrayList<>();
    public PlayerManager playerManager;
    public Stage stage, menuStage;
    public RenderingManager renderingManager;
    public CameraManager cameraManager;
    public GameManager gameManager;
    public MapManager mapManager;
    public static final String bgMusic = "bgtrack.mp3";
    public Music backgroundMusic;

    boolean isStartGame = false;
    public static Skin menuSkin;
    public static final String menuSkinPath = "skin/craftacular/skin/craftacular-ui.json";
    public static final String backgroundPath = "background.png";
    int frameRateIndex = 2;
    private boolean showTutorial;
    public static final String tutorialImage = "tutorial.png";
    public Table leaderboard;
    public ResourceLoader resourceLoader;
    private Texture tutorialTexture;

    /**
     * Initialises the game.
     */
    @Override
    public void create() {
    	isStartGame = false;
    	showTutorial = false;
    	menuSkin = new Skin(Gdx.files.internal(menuSkinPath));
    	resourceLoader = new ResourceLoader();
    	tutorialTexture = new Texture(Gdx.files.internal(tutorialImage));
    	
    	// Load saved user settings
        Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
        Gdx.graphics.setForegroundFPS(prefs.getInteger("FPS", 60));
        if (!prefs.getBoolean("fullscreen")) {
            Gdx.graphics.setWindowedMode(1440, 810);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    	
        // Import and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(bgMusic));
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(prefs.getFloat("volume", 0.5f));

        // Create manager objects
        cameraManager = new CameraManager();
        mapManager = new MapManager();
        BuildingManager buildingManager = new BuildingManager();
        stage = new Stage(cameraManager.getViewport());

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(6200, 1780);
        playerManager = new PlayerManager(spawn, 320 * 2, this);

        // Set tile collision fields
        playerManager.getMovement().setCollidableTiles(mapManager.getCollidableTiles());

        // Initialise buildings
        buildings = buildingManager.getCampusBuildings();
        renderingManager = new RenderingManager(cameraManager, mapManager, playerManager, this);
        gameManager = new GameManager(stage, mapManager, playerManager, buildingManager, renderingManager);

        menuStage = new Stage(cameraManager.getViewport());
        
    	// Start menu
        CreateLeaderboard();
        int[] frameRateList = {30, 60, 75, 90, 120, 165};
        
        Table rootTable = new Table(menuSkin);
        rootTable.setFillParent(true);
        rootTable.background(new TextureRegionDrawable(new Texture(backgroundPath)));

        rootTable.add("HESLINGTON HUSTLE").pad(64).row();
        TextButton startButton = new TextButton("START", menuSkin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	prefs.putFloat("volume", backgroundMusic.getVolume());
            	prefs.putBoolean("fullscreen", Gdx.graphics.isFullscreen());
            	prefs.flush();
                isStartGame = true;
            }
        });
        rootTable.add(startButton).pad(16).row();
        
        TextButton changeCharacterButton = new TextButton("CHARACTER 1", menuSkin);
        changeCharacterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	resourceLoader.character++;
            	if(resourceLoader.character == 4) resourceLoader.character = 1;
            	changeCharacterButton.setText("CHARACTER " + resourceLoader.character);
            	playerManager.movement.refreshAnimations();
            }
        });
        rootTable.add(changeCharacterButton).pad(16).row();
        
        TextButton optionButton = new TextButton("OPTIONS", menuSkin);
        Dialog optionDialog = new Dialog("OPTIONS", menuSkin);
        optionDialog.getContentTable().add("VOLUME").pad(16).padBottom(0).left().bottom().row();

        Slider volumeSlider = new Slider(0.0f, 1.0f, 0.01f, false, menuSkin);
        volumeSlider.setValue(backgroundMusic.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backgroundMusic.setVolume(volumeSlider.getValue());
            }
        });
        optionDialog.getContentTable().add(volumeSlider).fill().pad(16).row();
        TextButton fullScreenButton = new TextButton("FULL SCREEN: OFF", menuSkin);
        if (Gdx.graphics.isFullscreen()) fullScreenButton.setText("FULL SCREEN: ON");
        fullScreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.graphics.isFullscreen()) {
                    fullScreenButton.setText("FULL SCREEN: OFF");
                    Gdx.graphics.setWindowedMode(1440, 810);
                } else {
                    fullScreenButton.setText("FULL SCREEN: ON");
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                cameraManager.getViewport().setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        });
        optionDialog.getContentTable().add(fullScreenButton).pad(16).row();

        TextButton frameRateButton = new TextButton("FRAME RATE: 60 HZ", menuSkin);
        frameRateButton.setText("FRAME RATE: " + prefs.getInteger("FPS", 60) + " HZ");
        frameRateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setForegroundFPS(frameRateList[frameRateIndex % frameRateList.length]);
                frameRateButton.setText("FRAME RATE: " + frameRateList[frameRateIndex % frameRateList.length] + " HZ");
                prefs.putInteger("FPS", frameRateList[frameRateIndex % frameRateList.length]);
                frameRateIndex++;
            }
        });
        optionDialog.getContentTable().add(frameRateButton).pad(16).row();
        optionDialog.button("OK");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionDialog.show(menuStage);
            }
        });

        rootTable.add(optionButton).pad(16).row();
        
        TextButton returnButton = new TextButton("BACK", Game.menuSkin);
        returnButton.setTransform(true);
        returnButton.setSize(180, 80);
        returnButton.setScale(1f);
        returnButton.setPosition(10, 10);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	showTutorial = false;
            	menuStage.addActor(rootTable);
            	menuStage.addActor(leaderboard);
            	rootTable.background(new TextureRegionDrawable(new Texture(backgroundPath)));
            }
        });
        
        TextButton tutorialButton = new TextButton("HOW TO PLAY", menuSkin);
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTutorial = true;
                rootTable.background(new TextureRegionDrawable(new Texture(tutorialImage)));
                rootTable.remove();
                leaderboard.remove();
                menuStage.addActor(returnButton);
            }
        });
        rootTable.add(tutorialButton).pad(16).row();
        
        TextButton exitButton = new TextButton("EXIT", menuSkin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	prefs.putFloat("volume", backgroundMusic.getVolume());
            	prefs.putBoolean("fullscreen", Gdx.graphics.isFullscreen());
            	prefs.flush();
                Gdx.app.exit();
            }
        });
        rootTable.add(exitButton).pad(16).row();
        
        
        menuStage.addActor(rootTable);
        menuStage.addActor(leaderboard);
        

        // Initialise input system
        inputSetup();
    }

    /**
     * Initialises the game with a mocked OrthogonalTiledMapRenderer, Stage and SpriteBatch for testing.
     *
     * @param mapRendererMock The mocked OrthogonalTiledMapRenderer
     * @param stageMock       The mocked Stage
     * @param spriteBatchMock The mocked SpriteBatch
     */
    public void testCreate(OrthogonalTiledMapRenderer mapRendererMock, Stage stageMock, SpriteBatch spriteBatchMock) {
        // Create manager objects
    	resourceLoader = new ResourceLoader();
        cameraManager = new CameraManager();
        MapManager mapManager = new MapManager(mapRendererMock);
        BuildingManager buildingManager = new BuildingManager();
        stage = stageMock;

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(4608, 960);
        playerManager = new PlayerManager(spawn, 320 * 2, this);

        // Set tile collision fields
        playerManager.getMovement().setCollidableTiles(mapManager.getCollidableTiles());

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
        InputMultiplexer inputMultiplexer = new InputMultiplexer(inputHandler, stage, menuStage, renderingManager.getGameUI().getUiStage());
    	Gdx.input.setInputProcessor(inputMultiplexer);
    }


    /**
     * Called when the screen size changes.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        cameraManager.getViewport().update(width, height, true);
        renderingManager.getGameUI().getUiStage().getViewport().update(width, height);
        menuStage.getViewport().update(width, height, true);
    }
    
    /**
     * Renders the game and its components. Called every frame.
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (isStartGame) {
            playerManager.getMovement().update(deltaTime);
            gameManager.update();
            renderingManager.render(buildings, playerManager);
            stage.act();
            stage.draw();
        }
        
        else if(showTutorial) {
        	renderingManager.batch.begin();
            renderingManager.batch.draw(tutorialTexture, 0, 0);
        	renderingManager.batch.end();
        	
            menuStage.act();
            menuStage.draw();
        }
        
        else {
            menuStage.act();
            menuStage.draw();
        }
    }
    
    /**
     * Sorts saved scores into a top 10 leaderboard
     * @return Table of top 10 scores and usernames
     */
    public void CreateLeaderboard() {
    	Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleScores");
    	
    	leaderboard = new Table(menuSkin);
    	leaderboard.add("-- LEADERBOARD --").pad(16).row();
    	leaderboard.setPosition(1200, 400);
    	
    	String allUsers = ScoreManager.getAllUsers();
    	if(allUsers == "") return;
    	
    	String[] usersArray = allUsers.split(",");
    	Integer[] scores = new Integer[usersArray.length];
    	Integer[] indices = new Integer[usersArray.length];
    	
    	for (int i = 0; i < usersArray.length; i++) {
            indices[i] = i;
        }
    	for (int i = 0; i < usersArray.length; i++) {
    		scores[i] = prefs.getInteger(usersArray[i], 0);
        }
    	
    	Arrays.sort(indices, (i1, i2) -> scores[i2] - scores[i1]);
    	Integer[] sortedScores = new Integer[usersArray.length];
        String[] sortedUsers = new String[usersArray.length];
        for (int i = 0; i < indices.length; i++) {
            sortedScores[i] = scores[indices[i]];
            sortedUsers[i] = usersArray[indices[i]];
        }
    	for(int i = 0; i < 10; i++) {
    		if(sortedUsers.length <= i) {
    			break;
    		}
    		leaderboard.add(sortedUsers[i] + ": " + sortedScores[i]).pad(16).row();
    	}
    }
}
