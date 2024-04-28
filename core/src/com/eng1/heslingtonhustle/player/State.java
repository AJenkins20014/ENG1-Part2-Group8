/**
 * The State class represents the current state of player input.
 * It tracks directional movement, interaction, and menu state.
 */
package com.eng1.heslingtonhustle.player;
public class State {

    private boolean UP;
    private boolean DOWN;
    private boolean LEFT;
    private boolean RIGHT;

    private boolean INTERACTING;
    private boolean IN_MENU;

    /**
     * Constructs a new State object with default values.
     */
    public State() {
        UP = false;
        DOWN = false;
        LEFT = false;
        RIGHT = false;
        INTERACTING = false;
        IN_MENU = false;
    }

    /**
     * Toggles whether the player is moving upwards.
     */
    public void moveUp() {
        UP = !UP;
    }

    /**
     * Toggles whether the player is moving downwards.
     */
    public void moveDown() {
        DOWN = !DOWN;
    }

    /**
     * Toggles whether the player is moving left.
     */
    public void moveLeft() {
        LEFT = !LEFT;
    }

    /**
     * Toggles whether the player is moving right.
     */
    public void moveRight() {
        RIGHT = !RIGHT;
    }

    /**
     * Signals that the player is interacting with an object.
     * Only allows interacting when not in a menu.
     */
    public void interacting() {
        if (!IN_MENU)
            INTERACTING = true;
    }

    /**
     * Signals that the player has stopped interacting with an object.
     */
    public void stopInteracting(){
        INTERACTING = false;
    }

    /**
     * Checks if the player is currently interacting with an object.
     * @return true if the player is interacting, false otherwise.
     */
    public boolean isINTERACTING() {
        return INTERACTING;
    }

    /**
     * Gets the current direction of vertical movement based on player input.
     * @return 1 if moving up, -1 if moving down, 0 if not moving vertically.
     */
    public int getMoveDirectionY() {
        if (IN_MENU){
            return 0;
        }
        return (UP ? 1 : 0) - (DOWN ? 1 : 0);
    }

    /**
     * Gets the current direction of horizontal movement based on player input.
     * @return 1 if moving right, -1 if moving left, 0 if not moving horizontally.
     */
    public int getMoveDirectionX() {
        if (IN_MENU){
            return 0;
        }
        return (RIGHT ? 1 : 0) - (LEFT ? 1 : 0);
    }

    /**
     * Signals that the player has entered a menu.
     */
    public void inMenu(){
        IN_MENU = true;
    }

    /**
     * Signals that the player has exited a menu.
     */
    public void leftMenu(){
        IN_MENU = false;
    }

}

