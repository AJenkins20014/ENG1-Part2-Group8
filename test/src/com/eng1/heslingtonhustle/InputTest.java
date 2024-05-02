/**
 * This class contains unit tests for the player input handling and movement logic.
 * It tests the behavior of the InputHandler and Movement classes under various scenarios.
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
        assertTrue("Player state is set to UP upon W key down", movement.getPlayerState().UP);
        inputHandler.keyUp(Input.Keys.W);
        assertFalse("Player state is set to UP upon W key up", movement.getPlayerState().UP);
        
        // Down
        inputHandler.keyDown(Input.Keys.S);
        assertTrue("Player state is set to DOWN upon S key down", movement.getPlayerState().DOWN);
        inputHandler.keyUp(Input.Keys.S);
        assertFalse("Player state is set to DOWN upon S key up", movement.getPlayerState().DOWN);
        
        // Left
        inputHandler.keyDown(Input.Keys.A);
        assertTrue("Player state is set to LEFT upon A key down", movement.getPlayerState().LEFT);
        inputHandler.keyUp(Input.Keys.A);
        assertFalse("Player state is set to LEFT upon A key up", movement.getPlayerState().LEFT);
        
        // Right
        inputHandler.keyDown(Input.Keys.D);
        assertTrue("Player state is set to RIGHT upon D key down", movement.getPlayerState().RIGHT);
        inputHandler.keyUp(Input.Keys.D);
        assertFalse("Player state is set to RIGHT upon D key up", movement.getPlayerState().RIGHT);
        
        // Interact
        inputHandler.keyDown(Input.Keys.E);
        assertTrue("Player state is set to INTERACTING upon E key down", movement.getPlayerState().isINTERACTING());
        inputHandler.keyUp(Input.Keys.E);
        assertFalse("Player state is no longer set to INTERACTING upon E key up", movement.getPlayerState().isINTERACTING());
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
        assertEquals("Player moves as expected with no collidable tiles in its path", new Vector2(0, 1), movement.getPosition());
        
        // Test movement update with collision
        movement.getPlayerState().moveUp();
        movement.update(0.1f);
        assertEquals("Player does not move when hitting a collidable tile", new Vector2(0, 1), movement.getPosition());
    }
    
    /**
     * Tests the toggling of movement functionality.
     */
    @Test
    public void testMovementToggle() {
        movement.disableMovement();
        assertFalse("Movement is disabled as expected", movement.movementEnabled);
        movement.enableMovement();
        assertTrue("Movement is enabled as expected", movement.movementEnabled);
    }
    
}
