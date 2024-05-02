/**
 * This class contains unit tests for the player input handling and movement logic.
 * It tests the behavior of the InputHandler and Movement classes under various scenarios.
 * The tests are run using a custom test runner, GdxTestRunner, to facilitate testing
 * of LibGDX-dependent code.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eng1.heslingtonhustle.player.InputHandler;
import com.eng1.heslingtonhustle.player.Movement;


@RunWith(GdxTestRunner.class)
public class InputTest {

	private Movement movement;
    private Array<Rectangle> collidableTiles;

    /**
     * Sets up the test environment by initialising the Movement instance
     * and adding collidable tiles for testing collision detection.
     */
    @Before
    public void setUp() {
        movement = new Movement(new Vector2(0, 0), 10);
        collidableTiles = new Array<>();
        collidableTiles.add(new Rectangle(0, 1, 10, 10));
    }

    /**
     * Tests the handling of player input events by simulating key presses and releases.
     */
    @Test
    public void testHandleInput() {
        InputHandler inputHandler = new InputHandler(movement.getPlayerState());
        
        // Up
        inputHandler.keyDown(Input.Keys.W);
        assertTrue(movement.getPlayerState().UP);
        inputHandler.keyUp(Input.Keys.W);
        assertFalse(movement.getPlayerState().UP);
        
        // Down
        inputHandler.keyDown(Input.Keys.S);
        assertTrue(movement.getPlayerState().DOWN);
        inputHandler.keyUp(Input.Keys.S);
        assertFalse(movement.getPlayerState().DOWN);
        
        // Left
        inputHandler.keyDown(Input.Keys.A);
        assertTrue(movement.getPlayerState().LEFT);
        inputHandler.keyUp(Input.Keys.A);
        assertFalse(movement.getPlayerState().LEFT);
        
        // Right
        inputHandler.keyDown(Input.Keys.D);
        assertTrue(movement.getPlayerState().RIGHT);
        inputHandler.keyUp(Input.Keys.D);
        assertFalse(movement.getPlayerState().RIGHT);
        
        // Interact
        inputHandler.keyDown(Input.Keys.E);
        assertTrue(movement.getPlayerState().isINTERACTING());
        inputHandler.keyUp(Input.Keys.E);
        assertFalse(movement.getPlayerState().isINTERACTING());
    }

    /**
     * Tests the movement update logic with and without collision.
     */
    @Test
    public void testMovementUpdate() {
    	movement.setCollidableTiles(collidableTiles);
    	
        // Test movement update with no collision
        movement.getPlayerState().moveUp();
        movement.update(0.1f);
        assertEquals(new Vector2(0, 1), movement.getPosition());
        
        // Test movement update with collision
        movement.getPlayerState().moveUp();
        movement.update(0.1f);
        assertEquals(new Vector2(0, 1), movement.getPosition());
    }
    
    /**
     * Tests the toggling of movement functionality.
     */
    @Test
    public void testMovementToggle() {
        movement.disableMovement();
        assertFalse(movement.movementEnabled);
        movement.enableMovement();
        assertTrue(movement.movementEnabled);
    }
    
}
