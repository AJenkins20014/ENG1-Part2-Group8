/**
 * Manages the tiled map and its rendering.
 */
package com.eng1.heslingtonhustle.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Map;
import java.util.HashMap;

import static com.eng1.heslingtonhustle.Game.SCALE;

public class MapManager {
    private TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Array<Rectangle> collidableTiles = new Array<>();
    private final Map<String, String> mapPaths;
    private final Array<Rectangle> exitTiles = new Array<>();
    private final Array<ActivityTile> activityTiles = new Array<>();
    
    public String currentMapPath = "maps/campus_east.tmx";
    public static final String defaultMapPath = "../assets/maps/campus_east.tmx";
    public static final String cafeMapPath = "../assets/maps/cafe.tmx";
    public static final String cinemaMapPath = "../assets/maps/cinema.tmx";
    public static final String compSciMapPath = "../assets/maps/compSci.tmx";
    public static final String endGameMapPath = "../assets/maps/end_game.tmx";
    public static final String homeMapPath = "../assets/maps/home.tmx";
    public static final String libraryMapPath = "../assets/maps/library.tmx";

    /**
     * Constructs a new MapManager instance.
     * Loads the default map, initializes the map renderer,
     * and parses the collidable tiles, exit tiles, and activity tiles.
     * Also initializes the map paths for various locations.
     */
    public MapManager() {
        tiledMap = new TmxMapLoader().load(defaultMapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, SCALE);
        parseCollidableTiles();

        mapPaths = new HashMap<>();
        mapPaths.put("Library", libraryMapPath);
        mapPaths.put("Cafe", cafeMapPath);
        mapPaths.put("Cinema", cinemaMapPath);
        mapPaths.put("Home", homeMapPath);
        mapPaths.put("Computer Science Building", compSciMapPath);
        mapPaths.put("Campus", defaultMapPath);

    }

    /**
     * Constructs a new MapManager instance with a mock OrthogonalTiledMapRenderer for unit testing.
     */
    public MapManager(OrthogonalTiledMapRenderer mapRendererMock) {
    	tiledMap = new TmxMapLoader().load(defaultMapPath);
        mapRenderer = mapRendererMock;
        parseCollidableTiles();

        mapPaths = new HashMap<>();
        mapPaths.put("Library", libraryMapPath);
        mapPaths.put("Cafe", cafeMapPath);
        mapPaths.put("Cinema", cinemaMapPath);
        mapPaths.put("Home", homeMapPath);
        mapPaths.put("Computer Science Building", compSciMapPath);
        mapPaths.put("Campus", defaultMapPath);
	}

	/**
     * Parses the tiles from the given MapObjects and adds them to the provided Array.
     * @param objects The MapObjects to parse.
     * @param tiles The Array to add the parsed tiles to.
     */
    private void parseTiles(MapObjects objects, Array<Rectangle> tiles) {
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                tiles.add(new Rectangle(rect.x * SCALE, rect.y * SCALE, rect.width * SCALE, rect.height * SCALE));
            }
        }
    }

    /**
     * Parses the collidable tiles from the tiled map layers.
     * If the "collisions" layer exists, extracts the collidable tiles and adds them to the collidableTiles array.
     */
    private void parseCollidableTiles() {
        if (tiledMap.getLayers().get("collisions") != null) {
            MapObjects objects = tiledMap.getLayers().get("collisions").getObjects();
            parseTiles(objects, collidableTiles);
        }
    }

    /**
     * Parses the exit tiles from the tiled map layers.
     * If the "exit" layer exists, extracts the exit tiles and adds them to the exitTiles array.
     */
    private void parseExitTiles() {
        if(tiledMap.getLayers().get("exit") != null) {
            MapObjects objects = tiledMap.getLayers().get("exit").getObjects();
            parseTiles(objects, exitTiles);
        }
    }
    
    /**
     * Parses the activity tiles from the tiled map layers.
     * If the "activities" layer exists, extracts the activity tiles and adds them to the activityTiles array.
     */
    private void parseActivityTiles() {
        if (tiledMap.getLayers().get("activities") != null) {
            MapObjects objects = tiledMap.getLayers().get("activities").getObjects();
            for (MapObject object : objects) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectObject = (RectangleMapObject) object;
                    Rectangle rect = rectObject.getRectangle();
                    Rectangle scaledRect = new Rectangle(rect.x * SCALE, rect.y * SCALE, rect.width * SCALE, rect.height * SCALE);
                    activityTiles.add(new ActivityTile(scaledRect));

                }
            }
        }
    }

    /**
     * Retrieves the array of collidable tiles.
     * @return The array of collidable tiles.
     */
    public Array<Rectangle> getCollidableTiles() {
        return collidableTiles;
    }

    /**
     * Retrieves the array of exit tiles.
     * @return The array of exit tiles.
     */
    public Array<Rectangle> getExitTiles() {
        return exitTiles;
    }

    /**
     * Retrieves the array of activity tiles.
     * @return The array of activity tiles.
     */
    public Array<ActivityTile> getActivityTiles() {
        return activityTiles;
    }

    /**
     * Sets the view of the OrthogonalTiledMapRenderer to the specified OrthographicCamera and renders the map.
     * @param camera The OrthographicCamera to set the view to.
     */
    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();

    }

    /**
     * Changes the current map to the one specified by the newMapPath. Disposes the current map if it exists,
     * loads the new map, sets it to the OrthogonalTiledMapRenderer, clears collidableTiles, and parses collidable,
     * exit, and activity tiles.
     * @param newMapPath The path to the new map file.
     */
    public void changeMap(String newMapPath) {
        if (tiledMap != null) {
            tiledMap.dispose();
        }
        currentMapPath = newMapPath;
        tiledMap = new TmxMapLoader().load(newMapPath);
        mapRenderer.setMap(tiledMap);
        collidableTiles.clear();
        parseCollidableTiles();
        parseExitTiles();
        parseActivityTiles();
    }
    
    /**
     * Changes the current map to the default campus map. Disposes the current map if it exists,
     * loads the default map, sets it to the OrthogonalTiledMapRenderer, clears exitTiles, activityTiles,
     * and collidableTiles, and parses collidable tiles.
     */
    public void changeMapToCampus() {
        if (tiledMap!= null) {
            tiledMap.dispose();
        }
        currentMapPath = defaultMapPath;
        tiledMap = new TmxMapLoader().load(defaultMapPath);
        mapRenderer.setMap(tiledMap);
        exitTiles.clear();
        activityTiles.clear();
        collidableTiles.clear();
        parseCollidableTiles();
    }

    /**
     * Retrieves the path of the specified map name from the mapPaths HashMap.
     * @param mapName The name of the map.
     * @return The path of the specified map.
     */
    public String getMapPath(String mapName) {
        return mapPaths.get(mapName);
    }

    /**
     * Renders the specified layer overlay on top of the current map if it's the default campus map.
     * @param camera The OrthographicCamera to set the view to.
     * @param layerName The name of the layer to render.
     */
    public void renderOverlay(OrthographicCamera camera, String layerName) {
        if (currentMapPath.equals(defaultMapPath)) {
            int layerIndex = tiledMap.getLayers().getIndex(layerName);
            mapRenderer.setView(camera);
            mapRenderer.render(new int[] {layerIndex});
        }
    }

    /**
     * Displays the end game map by disposing the current map, loading the end game map,
     * setting it to the OrthogonalTiledMapRenderer, and clearing collidableTiles.
     */
    public void displayEndMap() {
        if (tiledMap!= null) {
            tiledMap.dispose();
        }
        currentMapPath = endGameMapPath;
        tiledMap = new TmxMapLoader().load(endGameMapPath);
        mapRenderer.setMap(tiledMap);
        collidableTiles.clear();

    }
}

