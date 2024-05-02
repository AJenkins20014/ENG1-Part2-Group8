/**
 * This class contains unit tests for the game's map loading system.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.eng1.heslingtonhustle.map.ActivityTile;
import com.eng1.heslingtonhustle.map.MapManager;

@RunWith(GdxTestRunner.class)
public class MapTest {

    private MapManager mapManager;
    private ActivityTile activityTile;
    private final Rectangle testRectangle = new Rectangle(0, 0, 10, 10);
    
    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setUp() {
    	// Mock OrthogonalTiledMapRenderer
        OrthogonalTiledMapRenderer mapRendererMock = mock(OrthogonalTiledMapRenderer.class);
        
        // Instantiate MapManager with mocked objects
        mapManager = new MapManager(mapRendererMock);
        
        // Create new activity tile
        activityTile = new ActivityTile(testRectangle);
    }
    
    /**
     * Tests the initialization of the MapManager.
     * Verifies that the MapManager instance is not null and that necessary arrays are initialized.
     */
    @Test
    public void testMapManagerInitialization() {
        assertNotNull("MapManager is not null after initialisation", mapManager);
        assertNotNull("Collidable tiles found on default map", mapManager.getCollidableTiles());
        assertNotNull("Exit tiles found on default map", mapManager.getExitTiles());
        assertNotNull("Activity tiles found on default map", mapManager.getActivityTiles());
    }
    
    /**
     * Tests map changes.
     * Verifies that the default map path is the campus map path.
     */
    @Test
    public void testChangeMap() {
        mapManager.changeMapToCampus();
        assertEquals("Map changes to campus correctly", MapManager.defaultMapPath, mapManager.currentMapPath);
        mapManager.changeMap(MapManager.cafeMapPath);
        assertEquals("Map changes to specific locations correctly (tested with cafe)", MapManager.cafeMapPath, mapManager.currentMapPath);
    }
    
    /**
     * Tests the retrieval of map paths.
     * Verifies that the correct path is returned for a specified map name.
     */
    @Test
    public void testGetMapPath() {
        String libraryPath = mapManager.getMapPath("Library");
        assertEquals("Map path is retrieved correctly (tested with Library)", MapManager.libraryMapPath, libraryPath);
    }
    
    /**
     * Tests rendering overlay.
     * No exception should be thrown.
     */
    @Test
    public void testRenderOverlay() {
        mapManager.renderOverlay(null, "overlayLayer");
        // Test passes if no exception is thrown
    }
    
    /**
     * Tests displaying the end game map.
     */
    @Test
    public void testDisplayEndMap() {
        mapManager.displayEndMap();
        assertEquals("End game map was loaded successfully", MapManager.endGameMapPath, mapManager.currentMapPath);
    }
    
    /**
     * Tests the construction of an ActivityTile instance.
     * Verifies that the ActivityTile instance is not null and that the provided rectangle is set correctly.
     */
    @Test
    public void testActivityTileConstruction() {
        assertNotNull("ActivityTile should not be null after initialisation", activityTile);
        assertEquals("ActivityTile area is set correctly", testRectangle, activityTile.getRectangle());
    }
    
    /**
     * Tests the retrieval of the rectangle representing the tile area.
     * Verifies that the correct rectangle is returned.
     */
    @Test
    public void testGetRectangle() {
        Rectangle rectangle = activityTile.getRectangle();
        assertEquals("ActivityTile correctly returns its area", testRectangle, rectangle);
    }
}