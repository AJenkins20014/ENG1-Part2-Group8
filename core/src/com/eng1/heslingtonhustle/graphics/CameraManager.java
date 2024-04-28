/**
 * The CameraManager class manages the camera used for rendering the game.
 * It handles camera setup, updates, and rendering.
 */
package com.eng1.heslingtonhustle.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.map.MapManager;

public class CameraManager {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private static final float WINDOW_WIDTH = 1440;
    private static final float WINDOW_HEIGHT = 810;

    /**
     * Constructs a new CameraManager and initializes the camera and viewport.
     */
    public CameraManager() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT, camera);
    }

    /**
     * Updates the camera position based on the player's position.
     * @param playerPosition The position of the player
     */
    private void updateCameraPosition(Vector2 playerPosition) {
        int PLAYER_SIZE = Game.PLAYER_SIZE;
        camera.position.set(playerPosition.x + (PLAYER_SIZE / 2f),
                playerPosition.y + (PLAYER_SIZE / 2f), 0);
        camera.update();
    }

    /**
     * Retrieves the viewport associated with the camera.
     * @return The FitViewport instance
     */
    public FitViewport getViewport() {
        return viewport;
    }

    /**
     * Renders the game using the provided SpriteBatch, MapManager, and player position.
     * @param batch The SpriteBatch used for rendering
     * @param mapManager The MapManager instance
     * @param playerPosition The position of the player
     */
    public void render(SpriteBatch batch, MapManager mapManager, Vector2 playerPosition) {
        updateCameraPosition(playerPosition);
        camera.update();
        mapManager.render(camera);

        batch.setProjectionMatrix(camera.combined);
    }

    /**
     * Retrieves the OrthographicCamera instance managed by the CameraManager.
     * @return The OrthographicCamera instance
     */
    public OrthographicCamera getCamera() {
        return camera;
    }
}
