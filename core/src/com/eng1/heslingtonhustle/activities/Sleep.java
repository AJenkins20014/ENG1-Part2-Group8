/**
 * The Sleep class represents an activity where the player sleeps to advance to the next day.
 * It extends the Activity class and implements specific behavior for sleeping.
 */
package com.eng1.heslingtonhustle.activities;

import com.eng1.heslingtonhustle.player.PlayerManager;

public class Sleep extends Activity {
	
	/**
     * Constructs a new Sleep activity with the specified time and energy parameters.
     * @param time The duration of sleeping activity in hours
     * @param energy The energy percentage consumed by sleeping
     */
    public Sleep(int time, int energy) {
        super("sleep",time, energy);
    }

    /**
     * Performs the sleep activity by advancing to the next day.
     * @param playerManager The PlayerManager instance controlling the player
     */
    @Override
    public void onPerform(PlayerManager playerManager) {
        playerManager.sleep();
    }

    /**
     * Returns a string representation of the sleep activity for tooltips, indicating that it advances to the next day.
     * @return A string describing the sleep activity
     */
    @Override
    public String toString() {
        return "This will make you skip to the next day.";
    }
}
