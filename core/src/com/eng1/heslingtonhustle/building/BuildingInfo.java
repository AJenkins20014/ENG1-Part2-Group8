/**
 * The BuildingInfo class represents information about a building.
 * It contains details such as the building's ID, name, texture coordinates, associated activity, energy, and time.
 * Information is imported from the buildings.json file.
 */
package com.eng1.heslingtonhustle.building;

public class BuildingInfo {
    String id;
    String name;
    int textureStartX;
    int textureStartY;
    int textureWidth;
    int textureHeight;
    String activityName;
    int energy;
    int time;
}
