/**
 * This class contains unit tests for the player management and game logic.
 * It tests the behavior of the PlayerManager class.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eng1.heslingtonhustle.game.Game;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.player.PlayerManager;

@RunWith(GdxTestRunner.class)
public class PlayerTest {
	
	private PlayerManager playerManager;
	private Game game;
	
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
		playerManager = new PlayerManager(new Vector2(0, 0), 0, game);
	}
	
	/**
     * Tests the day change functionality.
     */
	@Test
	public void testDayChange() {
		Day day = new Day();
		playerManager.setCurrentDay(day);
		assertEquals("Setting the current day works correctly", day, playerManager.currentDay);
		playerManager.sleep();
		assertNotEquals("The day changes when sleeping", playerManager.currentDay, day);
		assertTrue("The current day is added to the week list upon sleeping", playerManager.getWeek().contains(day));
	}
	
	/**
     * Tests the statistics update for a day.
     */
	@Test
	public void testDayStats() {
		Day day = new Day();
		playerManager.setCurrentDay(day);
		playerManager.study();
		assertEquals("Studying increases the amount of study session that day by 1", playerManager.currentDay.studySessions, 1);
		playerManager.eat();
		assertEquals("Eating increases the amount of times eaten that day by 1", playerManager.currentDay.eaten, 1);
		playerManager.relax();
		assertEquals("Relaxing increases the amount of times relaxed that day by 1", playerManager.currentDay.relaxed, 1);
	}
	
	/**
     * Tests the performActivity function and its effects on energy and time.
     */
	@Test
	public void testPerformActivity() {
		assertEquals("Player starts with 100 energy", playerManager.getEnergy().energy, 100);
		assertEquals("Time starts at 08:00", playerManager.getTime().time, 8);
		assertTrue("Function returns true when performing an activity is possible", playerManager.performActivity(20, 2));
		assertEquals("Performing an activity correctly changes energy", playerManager.getEnergy().energy, 80);
		assertEquals("Performing an activity correctly changes time", playerManager.getTime().time, 10);
		assertFalse("Function returns false when player has insufficient energy to perform an activity", playerManager.performActivity(100, 0));
		assertFalse("Function returns false when player has insufficient time to perform an activity", playerManager.performActivity(0, 24));
	}
	
	/**
     * Tests the game over condition and the week cycle.
     */
	@Test
	public void testGameOver() {
		playerManager = new PlayerManager(new Vector2(0, 0), 0, game);
		assertFalse("Game is not over upon initialisation", playerManager.gameOver());
		for(int i = 0; i < 6; i++) {
			playerManager.testSleep();
		}
		assertFalse("Game is not over after 6 days", playerManager.gameOver());
		playerManager.sleep();
		assertTrue("Game is over after 7 days", playerManager.gameOver());
		assertEquals("At the end of the game, the week list has 7 days", playerManager.getWeek().size(), 7);
	}
}
