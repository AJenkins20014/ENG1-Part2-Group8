/**
 * The Day class represents a day within the game.
 * It tracks the number of study sessions, meals eaten, and relaxation sessions during the day.
 */
package com.eng1.heslingtonhustle.gameobjects;

import java.util.ArrayList;

public class Day {
    public int studySessions;
    public int eaten;
    public int relaxed;
    public ArrayList<String> placesStudied = new ArrayList<String>();
    public ArrayList<String> placesRelaxed = new ArrayList<String>();
    
    /**
     * Constructs a new Day instance with default values.
     */
    public Day() {

    }

    /**
     * Increments the count of study sessions for the day.
     */
    public void studied() {
        studySessions++;
    }

    /**
     * Increments the count of meals eaten for the day.
     */
    public void eaten() {
        eaten++;
    }

    /**
     * Increments the count of relaxation sessions for the day.
     */
    public void relaxed() {
        relaxed++;
    }

    /**
     * Retrieves the number of study sessions during the day.
     * @return The number of study sessions
     */
    public int getStudySessions() {
        return studySessions;
    }

    /**
     * Retrieves the number of meals eaten during the day.
     * @return The number of meals eaten
     */
    public int getEaten() {
        return eaten;
    }

    /**
     * Retrieves the number of relaxation sessions during the day.
     * @return The number of relaxation sessions
     */
    public int getRelaxed() {
        return relaxed;
    }
}

