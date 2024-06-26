/**
 * The GameManager class controls the game logic and interactions between various game elements.
 * It manages player movements, interactions with buildings and activities, as well as the game's progression.
 */
package com.eng1.heslingtonhustle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.eng1.heslingtonhustle.building.Building;
import com.eng1.heslingtonhustle.building.BuildingManager;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.graphics.RenderingManager;
import com.eng1.heslingtonhustle.map.ActivityTile;
import com.eng1.heslingtonhustle.map.MapManager;
import com.eng1.heslingtonhustle.player.PlayerManager;
import com.eng1.heslingtonhustle.activities.Activity;
import com.eng1.heslingtonhustle.activities.Relax;

import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class GameManager {
    private final Stage stage;
    public final MapManager mapManager;
    public final PlayerManager playerManager;
    private final BuildingManager buildingManager;
    private final RenderingManager renderingManager;
    private Vector2 respawnLocation;
    private boolean playerInBuilding = false;
    private Building currentBuilding;
    
    
    public static final String uiSkin = "skin/default/uiskin.json";

    /**
     * Constructs a new GameManager with the specified parameters.
     * @param stage The stage where the game is rendered
     * @param mapManager Manages the game map
     * @param playerManager Manages the player
     * @param buildingManager Manages buildings in the game
     * @param renderingManager Manages rendering elements
     */
    public GameManager(Stage stage, MapManager mapManager, PlayerManager playerManager, BuildingManager buildingManager, RenderingManager renderManager) {
        this.stage = stage;
        this.mapManager = mapManager;
        this.playerManager = playerManager;
        this.buildingManager = buildingManager;
        Day day = new Day();
        playerManager.setCurrentDay(day);
        this.renderingManager = renderManager;
    }

    /**
     * Checks if any building is within the player's interaction range.
     * @return The building in range, or null if none.
     */
    public Building checkForBuildingInRange() {
        List<Building> buildings = buildingManager.getCampusBuildings();
        Vector2 position = playerManager.getPosition();
        for (Building building : buildings) {
            if (building.inRange(position)) {
                return building;
            }
        }
        return null;
    }

    /**
     * Interacts with the specified building if the player is in the INTERACTING state.
     * @param building The building to interact with
     */
    public void interactWithBuilding(Building building) {
        if (playerManager.getState().isINTERACTING()) {
            playerManager.getState().stopInteracting();
            enterBuilding(building);
        }
    }
    
    /**
     * Shows an error dialog indicating that the player can't perform an activity.
     */
    private void showErrorDialog() {
        Dialog dialog = createDialog();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dialog.show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.1f, Interpolation.fade), Actions.delay(.25f), Actions.fadeOut(0.1f, Interpolation.fade)));
            }
        }, 0f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                playerManager.getState().leftMenu();
            }
        }, .45f);
    }

    /**
     * Creates a dialog with an error message.
     * @return The created dialog.
     */
    public Dialog createDialog() {
        Skin skin = new Skin(Gdx.files.internal(uiSkin));
        Dialog dialog = new Dialog("Can't do activity.", skin);
        dialog.text("Can't perform activity.");
        dialog.setSize(200, 100);
        dialog.setPosition(playerManager.getPosition().x - 100, playerManager.getPosition().y + 50);
        return dialog;
    }

    /**
     * Enters the specified building, changes the map, and updates player state.
     * @param building The building to enter
     */
    private void enterBuilding(Building building) {
    	if(building.getName().equals("Pier") || building.getName().equals("Bus Stop")) {
    		playerManager.getState().interacting();
        	askToDoActivity(building.getActivity());
        	return;
        }
    	
        playerManager.getState().inMenu();
        String newMapPath = mapManager.getMapPath(building.getName());
        respawnLocation = new Vector2(playerManager.getPosition());
        playerInBuilding = true;
        currentBuilding = building;
        
        mapManager.changeMap(newMapPath);
        buildingManager.makeBuildingsDisappear();
        playerManager.movement.setPosition(new Vector2(400, 150));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                playerManager.getState().leftMenu();
            }
        }, .05f);
    }

    /**
     * Checks if the player is within an exit zone.
     * @param position The position to check
     * @return True if the player is within an exit zone, otherwise false
     */
    private boolean playerInExitZone(Vector2 position) {
        for (Rectangle exitZone : mapManager.getExitTiles()) {
            if (exitZone.contains(position.x, position.y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exits the current building if the player is in the INTERACTING state and within an exit zone.
     */
    private void exitBuilding() {
        if (playerManager.getState().isINTERACTING() && playerInExitZone(playerManager.getPosition())) {
            playerManager.getState().stopInteracting();
            playerInBuilding = false;
            currentBuilding = null;
            mapManager.changeMapToCampus();
            playerManager.movement.setPosition(respawnLocation);
            buildingManager.makeBuildingsAppear();
        }
    }

    /**
     * Checks if the player is within an activity zone.
     * @param position The position to check
     * @return The ActivityTile if the player is within an activity zone, otherwise null
     */
    private ActivityTile playerInActivityZone(Vector2 position) {
        for (ActivityTile activityZone : mapManager.getActivityTiles()) {
            if (activityZone.getRectangle().contains(position.x, position.y)) {
                return activityZone;
            }
        }
        return null;
    }

    /**
     * Handles the specified activity, showing an error dialog if it can't be performed.
     * @param activity The activity to handle
     */
    private void handleActivity(Activity activity) {
        boolean performed = activity.perform(playerManager);
        if (!performed) {
            showErrorDialog();
        } else {
            playerManager.getState().leftMenu();
            if (playerManager.gameOver()) {
                endGame();
            }
        }
    }

    /**
     * Ends the game by displaying the end map, hiding the player, and showing the final score.
     */
    public void endGame() {
        mapManager.displayEndMap();
        buildingManager.makeBuildingsDisappear();
        playerManager.movement.setPosition(new Vector2(900, 1900));
        mapManager.displayEndMap();
        renderingManager.hidePlayer();
        playerManager.getMovement().disableMovement();
        playerManager.getState().inMenu();
        renderingManager.getGameUI().showScore(playerManager.getWeek());
    }

    /**
     * Updates the game state, checking for player interactions with buildings and activities,
     * and updating the UI accordingly.
     */
    public void update() {
        boolean displayInteract = false;
        if (!playerInBuilding) {
            Building building = checkForBuildingInRange();
            if (checkForBuildingInRange() != null) {
                interactWithBuilding(building);
                displayInteract = true;
            }
        } else {
            ActivityTile activityTile = playerInActivityZone(playerManager.getPosition());
            if (playerInExitZone(playerManager.getPosition())) {
                displayInteract = true;
                exitBuilding();
            } else if (activityTile != null) {
                askToDoActivity(currentBuilding.getActivity());
                renderingManager.getGameUI().showInteractMessage();
                renderingManager.getGameUI().updateProgressBar();
                displayInteract = true;
            }
        }

        if (displayInteract) {
            renderingManager.getGameUI().showInteractMessage();
        }
        else {
            renderingManager.getGameUI().hideInteractMessage();
        }
    }

    /**
     * Asks the player if they want to perform the specified activity.
     * Handles the player's choice and proceeds accordingly.
     * @param activity The activity to ask about
     */
    private void askToDoActivity(Activity activity) {
        if (playerManager.getState().isINTERACTING()) {
            playerManager.getState().stopInteracting();
            playerManager.getState().inMenu();
            Vector2 playerPosition = playerManager.getPosition();
            Skin skin = new Skin(Gdx.files.internal(uiSkin));
            Dialog dialog = new Dialog("Activity", skin) {
                @Override
                protected void result(Object object) {
                    boolean choice = (Boolean) object;
                    if (!choice) {
                        playerManager.getState().leftMenu();
                        return;
                    }
                    handleActivity(activity);
                }
            };
            dialog.text(activity.toString());
            dialog.button("Yes", true);
            dialog.button("No", false);
            dialog.show(stage);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    dialog.show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
                    dialog.setPosition(playerPosition.x - 225, playerPosition.y + 50);
                    dialog.setSize(450, 100);
                }
            }, 0);
        }
    }
}
