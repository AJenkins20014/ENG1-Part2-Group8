/**
 * Utility class for loading game resources such as textures and animations.
 */
package com.eng1.heslingtonhustle.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceLoader {

	public String walkSheetPath;
	public static final String walkSheetPath1 = "character1.png";
	public static final String walkSheetPath2 = "character2.png";
	public static final String walkSheetPath3 = "character3.png";
	public static final String buildingsPath = "images/town.png";
	public Texture walkSheet;
    public static final Texture buildings = new Texture(Gdx.files.internal(buildingsPath));
	
	public static final String debugPath = "images/debug.png";
	public static final String overlayPath = "images/overlay.png";
    public static final Texture debug = new Texture(Gdx.files.internal(debugPath));
    public static final Texture overlay = new Texture(Gdx.files.internal(overlayPath));
    
    public int character = 1;
    
    public ResourceLoader() {
    	
    }

    /**
     * Retrieves the animation for character walking downwards.
     * @return Animation<TextureRegion> representing downward walking animation
     */
    public Animation<TextureRegion> getDownWalk() {
        return getTextureRegionByRow(0);
    }
    
    /**
     * Retrieves the animation for character walking upwards.
     * @return Animation<TextureRegion> representing upward walking animation
     */
    public Animation<TextureRegion> getUpWalk() {
        return getTextureRegionByRow(1);
    }

    /**
     * Retrieves the animation for character walking to the right.
     * @return Animation<TextureRegion> representing right walking animation
     */
    public Animation<TextureRegion> getRightWalk() {
        return getTextureRegionByRow(2);
    }

    /**
     * Retrieves the animation for character walking to the left.
     * @return Animation<TextureRegion> representing left walking animation
     */
    public Animation<TextureRegion> getLeftWalk() {
        return getTextureRegionByRow(3);
    }

    /**
     * Retrieves a TextureRegion array representing a row of the character walking animation.
     * @param x The row index of the animation sheet
     * @return Animation<TextureRegion> representing the animation
     */
    private Animation<TextureRegion> getTextureRegionByRow(int x) {
    	if(character == 1) walkSheet = new Texture(Gdx.files.internal(walkSheetPath1));
    	if(character == 2) walkSheet = new Texture(Gdx.files.internal(walkSheetPath2));
    	if(character == 3) walkSheet = new Texture(Gdx.files.internal(walkSheetPath3));
    	
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);
        TextureRegion[] out = new TextureRegion[8];
        System.arraycopy(tmp[x], 0, out, 0, 8);
        return new Animation<>(0.1f, out);
    }

    /**
     * Retrieves a TextureRegion for a building.
     * @param startX X coordinate of the starting position of the building
     * @param startY Y coordinate of the starting position of the building
     * @param width Width of the building in tiles
     * @param height Height of the building in tiles
     * @return TextureRegion representing the building
     */
    public static TextureRegion getBuildingTextureRegion(int startX, int startY, int width, int height){
        return new TextureRegion(buildings,startX,startY,16*width,16*height);
    }

    /**
     * Retrieves the texture used for debugging purposes.
     * @return Texture representing the debug texture
     */
    public static Texture getDebug() {
        return debug;
    }

    /**
     * Retrieves the overlay texture used for representing daylight.
     * @return Texture representing the overlay texture
     */
    public static Texture getOverlay() {
        return overlay;
    }
}
