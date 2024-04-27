/**
 * The Eat class represents an activity where the player eats to replenish energy.
 * It extends the Activity class and implements specific behavior for eating.
 */
package com.eng1.heslingtonhustle.activities;

import com.eng1.heslingtonhustle.player.PlayerManager;

public class Eat extends Activity {
	
	/**
	 * Constructs a new Eat activity with the specified time and energy parameters.
	 * @param time The duration of eating activity in hours
	 * @param energy The energy percentage consumed by eating
	 */
    public Eat(int time, int energy) {
        super("eat",time, energy);
    }

    /**
     * Performs the eat activity by replenishing player's energy.
     * @param playerManager The PlayerManager instance controlling the player
     */
    @Override
    public void onPerform(PlayerManager playerManager) {
        playerManager.eat();
    }

    /**
     * Returns a string representation of the eat activity for tooltips, including a message to eat and activity details.
     * @return A string describing the eat activity and its details
     */
    @Override
    public String toString() {
        return String.format("This will help you survive, please eat.\n%s", super.toString());
    }
}
