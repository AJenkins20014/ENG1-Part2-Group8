/**
 * The Building class represents a structure within the game world.
 * It contains information about its name, position, texture, visibility, and associated activity.
 */
package com.eng1.heslingtonhustle.building;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.activities.*;

public class Building {

    private final String name;
    private Vector2 position;
    private final TextureRegion textureRegion;
    private boolean isVisible = true;
    private Activity activity;

    /**
     * Constructs a new Building with the specified BuildingInfo.
     * @param buildingInfo The BuildingInfo containing information about the building
     */
    public Building(BuildingInfo buildingInfo) {
        this.name = buildingInfo.name;
        this.textureRegion = ResourceLoader.getBuildingTextureRegion(
                buildingInfo.textureStartX, buildingInfo.textureStartY,
                buildingInfo.textureWidth, buildingInfo.textureHeight);
        int energy = buildingInfo.energy;
        int time = buildingInfo.time;
        switch (buildingInfo.activityName){
            case "Study":
                activity = new Study(time,energy);
                break;
            case "Eat":
                activity = new Eat(time,energy);
                break;
            case "Relax":
                activity = new Relax(time,energy);
                break;
            case "Sleep":
                activity = new Sleep(time,energy);
                break;
            default:
                //TODO Handle error
        }
   }

    /**
     * Sets the position of the building.
     * @param position The position vector of the building
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Checks if a given value is within a specified range.
     * @param variable The value to check
     * @param minValueInclusive The inclusive minimum value of the range
     * @param maxValueInclusive The inclusive maximum value of the range
     * @return True if the value is within the range, otherwise false
     */
    private static boolean between(float variable, float minValueInclusive, float maxValueInclusive) {
        return variable >= minValueInclusive && variable <= maxValueInclusive;
    }

    /**
     * Checks if the player is within interaction range of the building.
     * @param playerPosition The position of the player
     * @return True if the player is within interaction range, otherwise false
     */
    public boolean inRange(Vector2 playerPosition) {

        return between(playerPosition.x, getInteractSpot().x, getInteractSpot().x+32*5) &&
                between(playerPosition.y, getInteractSpot().y, getInteractSpot().y+32*5);
    }

    /**
     * Retrieves the texture region of the building.
     * @return The texture region of the building
     */
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    
    /**
     * Retrieves the position of the building.
     * @return The position vector of the building
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Retrieves the name of the building.
     * @return The name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the interaction spot of the building.
     * @return The interaction spot of the building
     */
    public Vector2 getInteractSpot() {
        float X  = position.x + ((float) (textureRegion.getRegionWidth() - 32) / 2) * 5;
        float Y = position.y - 32*5;
        if (name.equals("Home")){
            X = position.x + ((float) (textureRegion.getRegionWidth() - 80) / 2) * 5;
        }
        return new Vector2(X,Y);
    }

    /**
     * Checks if the building is visible.
     * @return True if the building is visible, otherwise false
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    /**
     * Sets the visibility of the building.
     * @param visible True to set the building visible, false otherwise
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    /**
     * Retrieves the activity associated with the building.
     * @return The activity associated with the building
     */
    public Activity getActivity() {
        return activity;
    }
}
