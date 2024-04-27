/**
 * The Relax class represents an activity where the player relaxes.
 * It extends the Activity class and implements specific behavior for relaxing.
 */
package com.eng1.heslingtonhustle.activities;

import com.eng1.heslingtonhustle.player.PlayerManager;

public class Relax extends Activity {
	/**
     * Constructs a new Relax activity with the specified time and energy parameters.
     * @param time The duration of relaxing activity in hours
     * @param energy The energy percentage consumed by relaxing
     */
    public Relax(int time, int energy) {
        super("relax",time, energy);
    }

    /**
     * Performs the relax activity.
     * @param playerManager The PlayerManager instance controlling the player
     */
    @Override
    public void onPerform(PlayerManager playerManager) {
        playerManager.relax();
    }

    /**
     * Returns a string representation of the relax activity for tooltips, including a message to relax and activity details.
     * @return A string describing the relax activity and its details
     */
    @Override
    public String toString() {
        return String.format("This may help you relax.\n%s", super.toString());
    }
}
