/**
 * Manages the player character, including movement, time, energy, and activities.
 */
package com.eng1.heslingtonhustle.player;

import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.gameobjects.Energy;
import com.eng1.heslingtonhustle.gameobjects.Time;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    public final Movement movement;
    public final Time time = new Time();
    public final Energy energy = new Energy();
    private final List<Day> week = new ArrayList<>();

    public Day currentDay;
    private Game game;

    /**
     * Constructs a PlayerManager with the specified position and speed.
     * @param position The initial position of the player.
     * @param speed The speed of the player's movement.
     */
    public PlayerManager(Vector2 position, float speed, Game game) {
        movement = new Movement(position, speed);
        this.game = game;
    }

    /**
     * Sets the current day.
     * @param currentDay The current day.
     */
    public void setCurrentDay(Day currentDay) {
        this.currentDay = currentDay;
    }

    /**
     * Retrieves the state of the player's movement.
     * @return The state of the player's movement.
     */
    public State getState() {
        return movement.getPlayerState();
    }

    /**
     * Retrieves the position of the player.
     * @return The position of the player.
     */
    public Vector2 getPosition() {
        return movement.getPosition();
    }

    /**
     * Retrieves the movement component of the player.
     * @return The movement component of the player.
     */
    public Movement getMovement() {
        return movement;
    }

    /**
     * Initiates a study action for the player.
     */
    public void study() {
        currentDay.studied();
        if(game.mapManager == null) {
        	return;
        }
        if(!currentDay.placesStudied.contains(game.mapManager.currentMapPath)) {
        	currentDay.placesStudied.add(game.mapManager.currentMapPath);
        }
    }
    
    /**
     * Initiates an eat action for the player.
     */
    public void eat() {
        currentDay.eaten();
    }

    /**
     * Initiates a relax action for the player.
     */
    public void relax() {
        currentDay.relaxed();
        if(game.mapManager == null) {
        	return;
        }
        if(!currentDay.placesRelaxed.contains(game.mapManager.currentMapPath)) {
        	currentDay.placesRelaxed.add(game.mapManager.currentMapPath);
        }
    }

    /**
     * Initiates a sleep action for the player, marking the end of the current day.
     * Resets energy, progresses to the next day, and starts a new day.
     */
    public void sleep() {
    	currentDay.timeSlept = time.getTime();
        week.add(currentDay);
        currentDay = new Day();
        energy.reset();
        time.nextDay();
    }

    /**
     * Performs an activity that consumes energy and time.
     * @param energyCost The energy cost of the activity.
     * @param timeUsed The time consumed by the activity.
     * @return True if the activity was performed successfully, false otherwise.
     */
    public boolean performActivity(int energyCost, int timeUsed) {
        if (!energy.canUseEnergy(energyCost)) {
            System.out.println("not enough energy");
            return false; //not enough energy
        }
        if (!time.canIncreaseTime(timeUsed)) {
            System.out.println("not enough time");
            return false; //not enough time
        }
        energy.useEnergy(energyCost);
        time.increaseTime(timeUsed);
        return true;
    }

    /**
     * Retrieves the energy component of the player.
     * @return The energy component of the player.
     */
    public Energy getEnergy() {
        return energy;
    }

    /**
     * Retrieves the time component of the player.
     * @return The time component of the player.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Checks if the game is over (end of the week).
     * @return True if the game is over, false otherwise.
     */
    public boolean gameOver() {
        return time.isWeekOver();
    }

    /**
     * Retrieves the list of day objects in the week.
     * @return The list of day objects in the week.
     */
    public List<Day> getWeek() {
        return week;
    }
}
