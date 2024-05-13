/**
 * The main class representing the game.
 * This class extends the com.badlogic.gdx.Game class and
 * initialises manager objects that are crucial for the game
 * to function.
 */
package com.eng1.heslingtonhustle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public Stage stage, menuStage;
    public RenderingManager renderingManager;
    public CameraManager cameraManager;
    public GameManager gameManager;

    public static final String bgMusic = "../assets/bgtrack.mp3";
    Music backgroundMusic;

    boolean isStartGame = false;
    public static Skin menuSkin;
    int frameRateIndex = 2;

    /**
     * Initialises the game.
     */
    @Override
    public void create() {
        menuSkin = new Skin(Gdx.files.internal("skin/craftacular/skin/craftacular-ui.json"));
        // Import and play background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(bgMusic));
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.5f);

        // Create manager objects
        cameraManager = new CameraManager();
        MapManager mapManager = new MapManager();
        BuildingManager buildingManager = new BuildingManager();
        stage = new Stage(cameraManager.getViewport());

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(6200, 1780);
        playerManager = new PlayerManager(spawn, 320 * 2);

        // Set tile collision fields
        playerManager.getMovement().setCollidableTiles(mapManager.getCollidableTiles());

        // Initialise buildings
        buildings = buildingManager.getCampusBuildings();
        renderingManager = new RenderingManager(cameraManager, mapManager, playerManager);
        gameManager = new GameManager(stage, mapManager, playerManager, buildingManager, renderingManager);

        menuStage = new Stage(cameraManager.getViewport());

        Table rootTable = new Table(menuSkin);
        rootTable.setFillParent(true);
        rootTable.background(new TextureRegionDrawable(new Texture("background.png")));

        rootTable.add("Heslington Hustle").pad(64).row();
        TextButton startButton = new TextButton("START", menuSkin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isStartGame = true;
            }
        });
        rootTable.add(startButton).pad(16).row();
        TextButton optionButton = new TextButton("OPTION", menuSkin);
        Dialog optionDialog = new Dialog("OPTION", menuSkin);
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
            }
        });
        optionDialog.getContentTable().add(fullScreenButton).pad(16).row();

        TextButton frameRateButton = new TextButton("FRAME RATE: 60 HZ", menuSkin);
        int[] frameRateList = {30, 60, 75, 90, 120, 165};
        frameRateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setForegroundFPS(frameRateList[frameRateIndex % frameRateList.length]);
                frameRateButton.setText("FRAME RATE: " + frameRateList[frameRateIndex % frameRateList.length] + " HZ");
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

        TextButton exitButton = new TextButton("EXIT", menuSkin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        rootTable.add(exitButton).pad(16).row();
        menuStage.addActor(rootTable);

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
        cameraManager = new CameraManager();
        MapManager mapManager = new MapManager(mapRendererMock);
        BuildingManager buildingManager = new BuildingManager();
        stage = stageMock;

        // Set spawn location and spawn player
        Vector2 spawn = new Vector2(4608, 960);
        playerManager = new PlayerManager(spawn, 320 * 2);

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
        } else {
            menuStage.act();
            menuStage.draw();
        }
    }
}
