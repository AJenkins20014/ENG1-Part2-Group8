/**
 * Handles the movement and animation of the player character.
 */
package com.eng1.heslingtonhustle.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eng1.heslingtonhustle.game.Game;
import com.eng1.heslingtonhustle.helper.ResourceLoader;

public class Movement {
	private Game game;
    public static final float DIAGONAL_MODIFIER = (float) (Math.sqrt(2) / 2);
    private static final float PLAYER_WIDTH = 16;
    private static final float PLAYER_HEIGHT = 20;
    private final Vector2 position;
    private final float speed;
    public final State state;
    private Animation<TextureRegion> downWalkAnimation;
    private Animation<TextureRegion> upWalkAnimation;
    private Animation<TextureRegion> leftWalkAnimation;
    private Animation<TextureRegion> rightWalkAnimation;
    private float stateTime;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion currentFrame;
    private Array<Rectangle> collidableTiles;
    public boolean movementEnabled = true;

    /**
     * Constructs a Movement instance with the specified player position and speed.
     * @param position The initial position of the player.
     * @param speed The speed of the player's movement.
     */
    public Movement(Vector2 position, float speed, Game game) {
        this.position = position;
        this.speed = speed;
        this.state = new State();
        this.game = game;
        
        downWalkAnimation = game.resourceLoader.getDownWalk();
        upWalkAnimation = game.resourceLoader.getUpWalk();
        leftWalkAnimation = game.resourceLoader.getLeftWalk();
        rightWalkAnimation = game.resourceLoader.getRightWalk();
        
        currentAnimation = downWalkAnimation;
    }
    
    public void refreshAnimations() {
    	downWalkAnimation = game.resourceLoader.getDownWalk();
        upWalkAnimation = game.resourceLoader.getUpWalk();
        leftWalkAnimation = game.resourceLoader.getLeftWalk();
        rightWalkAnimation = game.resourceLoader.getRightWalk();
        currentAnimation = downWalkAnimation;
    }
    
    /**
     * Sets the collidable tiles for collision detection.
     * @param collidableTiles Array of collidable tiles represented by rectangles.
     */
    public void setCollidableTiles(Array<Rectangle> collidableTiles) {
        this.collidableTiles = collidableTiles;
    }

    /**
     * Updates the player's position and animation.
     * @param deltaTime Time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        if (!movementEnabled) {
            return;
        }

        int moveDirectionY = state.getMoveDirectionY();
        int moveDirectionX = state.getMoveDirectionX();

        float speedModifier = 1f;
        if (moveDirectionX != 0 && moveDirectionY != 0) {
            speedModifier = DIAGONAL_MODIFIER;
        }

        float velocity = speedModifier * speed * deltaTime;

        float potentialNewX = position.x + moveDirectionX * velocity;
        float potentialNewY = position.y + moveDirectionY * velocity;

        if (!collidesX(potentialNewX, potentialNewY)) {
            position.x = potentialNewX;
        }
        if (!collidesY(potentialNewX, potentialNewY)) {
            position.y = potentialNewY;
        }


        stateTime += deltaTime;
        updateAnimation();
        currentFrame = currentAnimation.getKeyFrame(stateTime, true);

    }

    /**
     * Checks for collision along the X axis.
     * @param x The potential new X coordinate.
     * @param y The current Y coordinate.
     * @return True if collision occurs, false otherwise.
     */
    private boolean collidesX(float x, float y) {
        Rectangle playerRect = new Rectangle(x, y, PLAYER_WIDTH, 0);
        return collides(playerRect);
    }

    /**
     * Checks for collision along the Y axis.
     * @param x The current X coordinate.
     * @param y The potential new Y coordinate.
     * @return True if collision occurs, false otherwise.
     */
    private boolean collidesY(float x, float y) {
        Rectangle playerRect = new Rectangle(x, y, 0, PLAYER_HEIGHT);
        return collides(playerRect);
    }

    /**
     * Checks for collision with collidable tiles.
     * @param playerRect The rectangle representing the player's position.
     * @return True if collision occurs, false otherwise.
     */
    private boolean collides(Rectangle playerRect) {
        for (Rectangle rect : collidableTiles) {
            if (rect.overlaps(playerRect)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the current animation based on player movement.
     */
    private void updateAnimation() {
        int moveDirectionY = state.getMoveDirectionY();
        int moveDirectionX = state.getMoveDirectionX();


        if (moveDirectionX == 0 && moveDirectionY == 0) {
            stateTime = 0f;
        } else if (moveDirectionX == 1 && !currentAnimation.equals(rightWalkAnimation)) {
            currentAnimation = rightWalkAnimation;
        } else if (moveDirectionX == -1 && !currentAnimation.equals(leftWalkAnimation)) {
            currentAnimation = leftWalkAnimation;
        } else if (moveDirectionY == 1 && !currentAnimation.equals(upWalkAnimation) && moveDirectionX == 0) {
            currentAnimation = upWalkAnimation;
        } else if (moveDirectionY == -1 && !currentAnimation.equals(downWalkAnimation) && moveDirectionX == 0) {
            currentAnimation = downWalkAnimation;
        }
    }

    /**
     * Retrieves the current frame of the player's animation.
     * @return The current frame of the animation.
     */
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Retrieves the current position of the player.
     * @return The current position of the player.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the position of the player.
     * @param newPosition The new position of the player.
     */
    public void setPosition(Vector2 newPosition) {
        this.position.x = newPosition.x;
        this.position.y = newPosition.y;
    }

    /**
     * Retrieves the state of the player.
     * @return The state of the player.
     */
    public State getPlayerState() {
        return state;
    }

    /**
     * Disables movement of the player.
     */
    public void disableMovement() {
        movementEnabled = false;
    }
    
    /**
     * Enables movement of the player.
     */
    public void enableMovement() {
    	movementEnabled = true;
    }
}
