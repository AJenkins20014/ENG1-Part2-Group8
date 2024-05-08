package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.heslingtonhustle.graphics.GameUI;
import com.eng1.heslingtonhustle.player.PlayerManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.graphics.CameraManager;

/**
 * Tests the graphical components of the game including camera settings and game UI elements.
 */
public class GraphicsTest {

    /**
     * Tests the CameraManager for correct camera initialisation and viewport settings.
     */
    @Test
    public void testCameraManager() {
        CameraManager cameraManager = new CameraManager();
        OrthographicCamera camera = cameraManager.getCamera();
        
        // Verify initial camera setup
        assertEquals(1440, camera.viewportWidth, 0.001);
        assertEquals(810, camera.viewportHeight, 0.001);
    }

    /**
     * Tests the GameUI for the initialisation and existence of its components.
     */
    @Test
    public void testGameUI() {
        Stage stage = new Stage();
        PlayerManager playerManager = new PlayerManager(null, 0);

        GameUI gameUI = new GameUI(stage, playerManager);

        // Since we cannot simulate full UI interactions, we only verify the initial state of components
        assertNotNull(gameUI.progressBar);
    }
}
