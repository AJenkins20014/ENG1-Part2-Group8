/**
 * The BuildingManager class manages buildings within the game.
 * It loads building information from a JSON file and creates building instances.
 * It also provides methods to access campus buildings and manage their visibility.
 */
package com.eng1.heslingtonhustle.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eng1.heslingtonhustle.Game.SCALE;

public class BuildingManager {

    private final List<Building> campusBuildings;

    /**
     * Constructs a new BuildingManager and loads building information from a JSON file.
     * It creates building instances based on the loaded information.
     */
    public BuildingManager() {
        Map<String, Building> buildingMap = loadBuildingInfo();
        campusBuildings = createBuildings(buildingMap);
    }

    /**
     * Loads building information from a JSON file and creates a map of building IDs to building instances.
     * @return A map containing building IDs as keys and corresponding building instances as values
     */
    private static Map<String, Building> loadBuildingInfo() {
        Gson gson = new Gson();
        Map<String, Building> buildingMap = new HashMap<>();
        FileHandle fileHandle = Gdx.files.internal("buildings.json");

        try (InputStream inputStream = fileHandle.read();
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            BuildingInfo[] buildingInfos = gson.fromJson(reader, BuildingInfo[].class);
            for (BuildingInfo buildingInfo : buildingInfos) {
                buildingMap.put(buildingInfo.id, new Building(buildingInfo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildingMap;
    }

    /**
     * Creates building instances based on the provided building map and campus layout.
     * @param buildingMap A map containing building IDs as keys and corresponding building instances as values
     * @return A list of created building instances
     */
    private List<Building> createBuildings(Map<String, Building> buildingMap) {
        List<Building> buildings = new ArrayList<>();
        TiledMap map = new TmxMapLoader().load("maps/campus_east.tmx");
        for (MapObject buildingCorner : (map.getLayers().get("buildingCorners").getObjects())) {
            String id = (String) buildingCorner.getProperties().get("name");
            if (buildingMap.containsKey(id)) {
                float buildingX = Float.parseFloat(buildingCorner.getProperties().get("x").toString()) * SCALE;
                float buildingY = Float.parseFloat(buildingCorner.getProperties().get("y").toString()) * SCALE;
                Building building = buildingMap.get(id);
                building.setPosition(new Vector2(buildingX, buildingY));
                buildings.add(building);
            }
        }
        return buildings;
    }

    /**
     * Retrieves the list of campus buildings.
     * @return The list of campus buildings
     */
    public List<Building> getCampusBuildings() {
        return campusBuildings;
    }

    /**
     * Sets the visibility of all campus buildings to false, making them disappear.
     */
    public void makeBuildingsDisappear() {
        for (Building building : campusBuildings) {
            building.setVisible(false);
        }
    }

    /**
     * Sets the visibility of all campus buildings to true, making them appear.
     */
    public void makeBuildingsAppear() {
        for (Building building : campusBuildings) {
            building.setVisible(true);
        }
    }


}
