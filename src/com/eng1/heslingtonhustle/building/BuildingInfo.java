/**
 * The BuildingInfo class represents information about a building.
 * It contains details such as the building's ID, name, texture coordinates, associated activity, energy, and time.
 * Information is imported from the buildings.json file.
 */
package com.eng1.heslingtonhustle.building;

public class BuildingInfo {
    String id;
    public String name;
    public int textureStartX;
    public int textureStartY;
    public int textureWidth;
    public int textureHeight;
    public String activityName;
    public int energy;
    public int time;
}
