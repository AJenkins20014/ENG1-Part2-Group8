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
    	// Temporarily store user's currently saved score
    	int savedScore = ScoreManager.loadHighScore();
    	
    	// Test save system
        int testScore = 100;
        ScoreManager.saveHighScore(testScore);
        assertEquals(100, ScoreManager.loadHighScore());
        
        // Reapply user's saved score
        ScoreManager.saveHighScore(savedScore);
    }

    /**
     * Tests the functionality of the ResourceLoader class.
     * It verifies whether textures, animations, building textures, debug textures, and overlay textures
     * are successfully loaded and retrieved.
     */
    @Test
    public void testResourceLoader() {
        // Test whether ResourceLoader successfully loads textures
        assertNotNull(ResourceLoader.walkSheet);
        assertNotNull(ResourceLoader.buildings);
        assertNotNull(ResourceLoader.debug);
        assertNotNull(ResourceLoader.overlay);

        // Test whether ResourceLoader successfully retrieves animations
        assertNotNull(ResourceLoader.getDownWalk());
        assertNotNull(ResourceLoader.getUpWalk());
        assertNotNull(ResourceLoader.getRightWalk());
        assertNotNull(ResourceLoader.getLeftWalk());

        // Test whether ResourceLoader successfully retrieves building textures
        assertNotNull(ResourceLoader.getBuildingTextureRegion(0, 0, 1, 1));

        // Test whether ResourceLoader successfully retrieves debug texture and overlay
        assertNotNull(ResourceLoader.getDebug());
        assertNotNull(ResourceLoader.getOverlay());
    }
}

