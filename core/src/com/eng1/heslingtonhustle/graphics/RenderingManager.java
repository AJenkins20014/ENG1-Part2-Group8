/**
 * The RenderingManager class handles rendering of game elements such as buildings, player, and UI.
 * It manages the camera, shaders, and rendering logic.
 */
package com.eng1.heslingtonhustle.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.building.Building;
import com.eng1.heslingtonhustle.map.MapManager;
import com.eng1.heslingtonhustle.player.Movement;
import com.eng1.heslingtonhustle.player.PlayerManager;

import java.util.List;

public class RenderingManager {

	private Game game;
    private static final float SCALE = 5f;
    public final SpriteBatch batch;

    private ShaderProgram shader;
    private final CameraManager cameraManager;
    private final MapManager mapManager;
    private final Stage uiStage;
    private final GameUI gameUI;
    private boolean playerVisible = true;

    public static final String vertexShaderPath = "../assets/shader/vertexShader.glsl";
    public static final String fragmentShaderPath = "../assets/shader/fragmentShader.glsl";
    
    private TextButton pauseButton;
    private TextButton exitButton;

    /**
     * Constructs a new RenderingManager with the given camera, map manager, and player manager.
     *
     * @param cameraManager The CameraManager instance
     * @param mapManager    The MapManager instance
     * @param playerManager The PlayerManager instance
     */
    public RenderingManager(CameraManager cameraManager, MapManager mapManager, PlayerManager playerManager, Game game) {
        this.batch = new SpriteBatch();
        this.cameraManager = cameraManager;
        this.mapManager = mapManager;
        this.uiStage = new Stage(new FitViewport(1440,810), batch);
        this.gameUI = new GameUI(uiStage, playerManager);
        this.game = game;

        if (!shaderSetup()) {
            Gdx.app.error("RenderingManager", "Error initializing shaders. Rendering may be affected.");
        }
        
        // Create in game exit button
        exitButton = new TextButton("EXIT", Game.menuSkin);
        exitButton.setTransform(true);
        exitButton.setSize(180, 80);
        exitButton.setScale(0.5f);
        exitButton.setPosition(10, 710);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.create();
            }
        });
        
        // Create pause button
        pauseButton = new TextButton("PAUSE", Game.menuSkin);
        pauseButton.setTransform(true);
        pauseButton.setSize(180, 80);
        pauseButton.setScale(0.5f);
        pauseButton.setPosition(10, 760);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerManager.movement.movementEnabled) {
                    playerManager.movement.disableMovement();
                    pauseButton.setText("RESUME");
                    uiStage.addActor(exitButton);
                } else {
                    playerManager.movement.enableMovement();
                    pauseButton.setText("PAUSE");
                    exitButton.remove();
                }
            }
        });
        uiStage.addActor(pauseButton);
        
        
    }

    /**
     * Constucts a new RenderingManager with a mocked sprite batch for testing.
     *
     * @param cameraManager   The CameraManager instance
     * @param mapManager      The MapManager instance
     * @param playerManager   The PlayerManager instance
     * @param spriteBatchMock The mocked sprite batch
     */
    public RenderingManager(CameraManager cameraManager, MapManager mapManager, PlayerManager playerManager, SpriteBatch spriteBatchMock) {
        this.batch = spriteBatchMock;
        this.cameraManager = cameraManager;
        this.mapManager = mapManager;
        this.uiStage = new Stage(new ScreenViewport(), batch);
        this.gameUI = new GameUI(uiStage, playerManager);
    }

    /**
     * Sets up the shader program for special effects.
     *
     * @return True if shader setup is successful, false otherwise
     */
    private boolean shaderSetup() {
        try {
            String vertexShader = Gdx.files.internal(vertexShaderPath).readString();
            String fragmentShader = Gdx.files.internal(fragmentShaderPath).readString();
            shader = new ShaderProgram(vertexShader, fragmentShader);

            if (!shader.isCompiled()) {
                Gdx.app.error("Shader", "Error compiling shader: " + shader.getLog());
                return false;
            }

            // Set the texture uniform
            shader.setUniformi("u_texture", 1);
            shader.setUniformi("u_mask", 1);
            return true;
        } catch (Exception e) {
            Gdx.app.error("Shader", "Error loading shaders: " + e.getMessage());
            return false;
        }
    }

    /**
     * Renders the game elements including buildings, player, and UI.
     *
     * @param buildings     The list of buildings to render
     * @param playerManager The PlayerManager instance
     */
    public void render(List<Building> buildings, PlayerManager playerManager) {
        Movement playerMovement = playerManager.getMovement();
        cameraManager.render(batch, mapManager, playerMovement.getPosition());


        batch.begin();
        try {
            renderBuildings(buildings, playerMovement);
            renderPlayer(playerMovement);
            mapManager.renderOverlay(cameraManager.getCamera(), "overlay");
            daylight(playerManager, playerMovement);
        } finally {
            batch.end();
        }

        gameUI.updateProgressBar();
        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();
        
       if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
    	   if (playerManager.movement.movementEnabled) {
               playerManager.movement.disableMovement();
               pauseButton.setText("RESUME");
               uiStage.addActor(exitButton);
           } else {
               playerManager.movement.enableMovement();
               pauseButton.setText("PAUSE");
               exitButton.remove();
           }
       }
    }

    /**
     * Adjusts the daylight brightness based on the time of day and renders the overlay accordingly.
     *
     * @param playerManager  The PlayerManager instance
     * @param playerMovement The Movement instance of the player
     */
    private void daylight(PlayerManager playerManager, Movement playerMovement) {
        float brightness = calculateBrightness(playerManager.getTime().getTime());
        batch.setColor(1, 1, 1, 1 - brightness);
        batch.draw(ResourceLoader.getOverlay(), playerMovement.getPosition().x - Gdx.graphics.getWidth(),
                playerMovement.getPosition().y - Gdx.graphics.getHeight()
                , Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
    }

    /**
     * Calculates the brightness of the daylight based on the time of day.
     *
     * @param timeOfDay The current time of day
     * @return The calculated brightness value
     */
    private float calculateBrightness(int timeOfDay) {

        float initialBrightness = 1.0f;
        float finalBrightness = 0.2f;
        float totalHours = 24 - 8;
        float hoursElapsed = timeOfDay - 8;
        float rateOfChange = (initialBrightness - finalBrightness) / totalHours;
        float currentBrightness = initialBrightness - rateOfChange * hoursElapsed;

        return Math.max(finalBrightness, Math.min(initialBrightness, currentBrightness));
    }

    /**
     * Renders all the buildings on the map.
     *
     * @param buildings The list of buildings to render
     * @param player    The Movement instance of the player
     */
    private void renderBuildings(List<Building> buildings, Movement player) {

        for (Building building : buildings) {
            if (!building.isVisible()) {
                continue;
            }
            if (building.inRange(player.getPosition())) {

                outlineBuilding(building);
            } else {
                renderBuilding(building);
            }
            boolean DEBUG = false;
            if (DEBUG) {
                Vector2 interactSpot = building.getInteractSpot();

                batch.draw(ResourceLoader.getDebug(), interactSpot.x, interactSpot.y, 32 * SCALE, 32 * SCALE);
            }
        }
    }

    /**
     * Renders a single building on the map.
     *
     * @param building The Building object to render
     */
    private void renderBuilding(Building building) {
        renderTexture(building.getTextureRegion(), building.getPosition());
    }

    /**
     * Renders an outline around a building.
     *
     * @param building The Building object to outline
     */
    public void outlineBuilding(Building building) {
        TextureRegion textureRegion = building.getTextureRegion();
        Vector2 position = building.getPosition();
        float scaleX = SCALE + (SCALE / 40f);
        float scaleY = SCALE + (SCALE / 20f);

        batch.setShader(shader);
        textureRegion.getTexture().bind(1);
        renderTexture(textureRegion, position, scaleX, scaleY, true);
        textureRegion.getTexture().bind(0);
        batch.setShader(null);
        renderTexture(textureRegion, position, SCALE, SCALE, false);
    }

    /**
     * Renders a texture with optional outline effect.
     *
     * @param textureRegion The texture region to render
     * @param position      The position to render the texture
     * @param scaleX        The scale factor on the X-axis
     * @param scaleY        The scale factor on the Y-axis
     * @param outline       Whether to render an outline effect
     */
    private void renderTexture(TextureRegion textureRegion, Vector2 position, float scaleX, float scaleY, boolean outline) {
        float x = position.x;
        float y = position.y;
        float width = textureRegion.getRegionWidth() * scaleX;
        float height = textureRegion.getRegionHeight() * scaleY;

        if (outline) {
            x -= calculateOffset(textureRegion.getRegionWidth(), scaleX);
            y -= calculateOffset(textureRegion.getRegionHeight(), scaleY);
        }

        batch.draw(textureRegion, x, y, width, height);
    }

    /**
     * Renders a texture at the specified position with the default scale.
     *
     * @param textureRegion The texture region to render
     * @param position      The position to render the texture
     */
    private void renderTexture(TextureRegion textureRegion, Vector2 position) {
        renderTexture(textureRegion, position, SCALE, SCALE, false);
    }

    /**
     * Calculates the offset needed to center a texture based on its scale.
     *
     * @param length The length of the texture region
     * @param scale  The scale factor of the texture
     * @return The offset value
     */
    private float calculateOffset(float length, float scale) {
        return length * (scale - SCALE) / 2f;
    }

    /**
     * Renders the player character on the map.
     *
     * @param playerMovement The Movement object representing the player
     */
    private void renderPlayer(Movement playerMovement) {
        if (playerVisible) {
            TextureRegion currentFrame = playerMovement.getCurrentFrame();
            Vector2 playerPosition = playerMovement.getPosition();
            float PLAYER_SIZE = 32 * SCALE;
            batch.draw(currentFrame, (playerPosition.x - PLAYER_SIZE / 2f), (playerPosition.y - PLAYER_SIZE / 2f) + 60, PLAYER_SIZE, PLAYER_SIZE);
        }
    }

    /**
     * Retrieves the GameUI instance associated with this rendering manager.
     *
     * @return The GameUI instance
     */
    public GameUI getGameUI() {
        return gameUI;
    }

    /**
     * Hides the player character from rendering.
     */
    public void hidePlayer() {
        playerVisible = false;
    }
}
