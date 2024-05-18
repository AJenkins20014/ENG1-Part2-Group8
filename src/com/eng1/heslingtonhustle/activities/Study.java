/**
 * The Study class represents an activity where the player studies to increase grades.
 * It extends the Activity class and implements specific behavior for studying.
 */
package com.eng1.heslingtonhustle.activities;

import com.eng1.heslingtonhustle.player.PlayerManager;

public class Study extends Activity {
	
	/**
     * Constructs a new Study activity with the specified time and energy parameters.
     * @param time The duration of studying activity in hours
     * @param energy The energy percentage consumed by studying
     */
    public Study(int time, int energy) {
        super("study",time, energy);
    }

    /**
     * Performs the study activity.
     * @param playerManager The PlayerManager instance controlling the player
     */
    @Override
    public void onPerform(PlayerManager playerManager){
        playerManager.study();
    }

    /**
     * Returns a string representation of the study activity for tooltips, including a message to study and activity details.
     * @return A string describing the study activity and its details
     */
    @Override
    public String toString() {
        return String.format("This will help increase your grades.\n%s", super.toString());
    }
}
