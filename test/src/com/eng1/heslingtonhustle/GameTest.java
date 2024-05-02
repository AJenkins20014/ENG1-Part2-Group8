/**
 * This class contains unit tests for game initialisation and building interaction.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.heslingtonhustle.building.Building;

@RunWith(GdxTestRunner.class)
public class GameTest {

    private Game game;
    private GameManager gameManager;
    private Building testBuilding;
    
    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setUp() {
    	// Mock required classes
    	OrthogonalTiledMapRenderer mapRendererMock = mock(OrthogonalTiledMapRenderer.class);
        Stage stageMock = mock(Stage.class);
        SpriteBatch spriteBatchMock = mock(SpriteBatch.class);
        
        // Initialise new game with mocked classes
        game = new Game();
        game.testCreate(mapRendererMock, stageMock, spriteBatchMock);
        gameManager = game.gameManager;
        testBuilding = game.buildings.get(0);
    }
    
    /**
     * Tests the initialization of the Game instance.
     * Verifies that the Game instance and its components are not null after initialisation.
     */
    @Test
    public void testGameInitialization() {
        assertNotNull("Game should not be null after initialisation", game);
        assertNotNull("Player Manager should not be null after initialisation", game.playerManager);
        assertNotNull("Stage should not be null after initialisation", game.stage);
        assertNotNull("Rendering Manager should not be null after initialisation", game.renderingManager);
        assertNotNull("Camera Manager should not be null after initialisation", game.cameraManager);
        assertNotNull("Game Manager should not be null after initialisation", game.gameManager);
        assertNotNull("Buildings loaded successfully", game.buildings);
    }

    /**
     * Tests the interaction with a building.
     */
    @Test
    public void testInteractWithBuilding() {
    	gameManager.playerManager.movement.state.INTERACTING = false;
        gameManager.interactWithBuilding(testBuilding);
        assertNotEquals("Map is not changed if player is not interacting",
        		gameManager.mapManager.getMapPath(testBuilding.getName()), gameManager.mapManager.currentMapPath);
        
        gameManager.playerManager.movement.state.INTERACTING = true;
        gameManager.interactWithBuilding(testBuilding);
        assertEquals("Map is set to building's map upon interacting with it",
        		gameManager.mapManager.getMapPath(testBuilding.getName()), gameManager.mapManager.currentMapPath);
    }
}
