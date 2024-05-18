/**
 * Handles input events for player movement and interaction.
 */
package com.eng1.heslingtonhustle.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {
    private final State playerState;

    /**
     * Constructs an InputHandler with the specified player state.
     * @param state The state of the player.
     */
    public InputHandler(State state) {
        this.playerState = state;
    }

    /**
     * Called when a key is pressed.
     * @param keycode The keycode of the pressed key.
     * @return Whether the input was processed.
     */
    @Override
    public boolean keyDown(int keycode) {
        handleInput(keycode);
        if (keycode == Input.Keys.E) {
            playerState.interacting();
        }
        return true;
    }

    /**
     * Called when a key is released.
     * @param keycode The keycode of the released key.
     * @return Whether the input was processed.
     */
    @Override
    public boolean keyUp(int keycode) {
        handleInput(keycode);
        if (keycode == Input.Keys.E) {
            playerState.stopInteracting();
        }
        return true;
    }

    /**
     * Handles the input based on the keycode.
     * @param keycode The keycode of the input.
     */
    public void handleInput(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                playerState.moveUp();
                break;
            case Input.Keys.S:
                playerState.moveDown();
                break;
            case Input.Keys.A:
                playerState.moveLeft();
                break;
            case Input.Keys.D:
                playerState.moveRight();
                break;
        }
    }

}
