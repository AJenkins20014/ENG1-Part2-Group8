/**
 * This class contains unit tests for the ScoreManager and ResourceLoader helper classes.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.helper.ScoreManager;

public class HelperTest {
	
	/**
     * Tests high score saving and loading.
     * It verifies whether the high score can be successfully saved and loaded.
     */
    @Test
    public void testSaveHighScore() {
    	// Test save system
        int testScore = 100;
        ScoreManager.saveHighScore(testScore, ScoreManager.HIGHSCORE_TESTING_KEY);
        assertEquals(100, ScoreManager.loadHighScore(ScoreManager.HIGHSCORE_TESTING_KEY));
    }

    /**
     * Tests the functionality of the ResourceLoader class.
     * It verifies whether textures, animations, building textures, debug textures, and overlay textures
     * are successfully loaded and retrieved.
     */
    @Test
    public void testResourceLoader() {
    	ResourceLoader resourceLoader = new ResourceLoader();
        // Test whether ResourceLoader successfully loads textures
        assertNotNull(ResourceLoader.buildings);
        assertNotNull(ResourceLoader.debug);
        assertNotNull(ResourceLoader.overlay);

        // Test whether ResourceLoader successfully retrieves animations
        assertNotNull(resourceLoader.getDownWalk());
        assertNotNull(resourceLoader.getUpWalk());
        assertNotNull(resourceLoader.getRightWalk());
        assertNotNull(resourceLoader.getLeftWalk());

        // Test whether ResourceLoader successfully retrieves building textures
        assertNotNull(ResourceLoader.getBuildingTextureRegion(0, 0, 1, 1));

        // Test whether ResourceLoader successfully retrieves debug texture and overlay
        assertNotNull(ResourceLoader.getDebug());
        assertNotNull(ResourceLoader.getOverlay());
    }
}

