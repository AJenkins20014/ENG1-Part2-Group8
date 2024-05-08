package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.heslingtonhustle.graphics.GameUI;
import com.eng1.heslingtonhustle.player.PlayerManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.graphics.CameraManager;

public class GraphicsTest {

    @Test
    public void testCameraManager() {
        CameraManager cameraManager = new CameraManager();
        OrthographicCamera camera = cameraManager.getCamera();
        
        // Verify initial camera setup
        assertEquals(1440, camera.viewportWidth, 0.001);
        assertEquals(810, camera.viewportHeight, 0.001);
        
        // Normally, we would test dynamic camera changes here, but this is outside the scope without mocks
    }

    @Test
    public void testGameUI() {
        Stage stage = new Stage();  // Directly using the Stage without mocking
        PlayerManager playerManager = new PlayerManager(null, 0);  // Directly using PlayerManager without mocks

        GameUI gameUI = new GameUI(stage, playerManager);

        // Since we cannot simulate full UI interactions, we only verify the initial state of components
        assertNotNull(gameUI.progressBar);  // Verify that the progress bar is initialized
        
        // The interaction message can't be shown or hidden without the full UI lifecycle, which is outside unit test scope
    }
}
