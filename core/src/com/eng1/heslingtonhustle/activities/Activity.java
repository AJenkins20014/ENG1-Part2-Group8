/**
 * The Activity class represents a generic activity that can be performed by a player.
 * It defines properties such as duration, energy usage, and provides methods for performing the activity.
 */
package com.eng1.heslingtonhustle.activities;

import com.eng1.heslingtonhustle.player.PlayerManager;

public abstract class Activity {
    public int durationHours;
    public int energyUsagePercent;
    protected String name;

    /**
     * Constructs a new Activity with the specified parameters.
     * @param name The name of the activity
     * @param durationHours The duration of the activity in hours
     * @param energyUsagePercent The percentage of energy used by the activity
     */
    public Activity(String name, int durationHours, int energyUsagePercent) {
        this.name = name;
        this.durationHours = durationHours;
        this.energyUsagePercent = energyUsagePercent;
    }

    /**
     * Performs the activity by consuming player's energy and calling the onPerform method.
     * @param playerManager The PlayerManager instance controlling the player
     * @return True if the activity was performed successfully, false otherwise
     */
    public boolean perform(PlayerManager playerManager) {
        if (!playerManager.performActivity(energyUsagePercent,durationHours)) {
            return false;
        }
        onPerform(playerManager);
        return true;
    }

    /**
     * Retrieves the name of the activity.
     * @return The name of the activity
     */
    public String getName() {
        return name;
    }
    
    /**
     * Abstract method to be implemented by subclasses for performing specific actions of the activity.
     * @param playerManager The PlayerManager instance controlling the player
     */
    public abstract void onPerform(PlayerManager playerManager);

    /**
     * Returns a string representation of the activity for tooltips, including duration and energy usage.
     * @return A string describing the activity's duration and energy usage
     */
    @Override
    public String toString() {
        return String.format("It will take %d hours and use %d%% of your energy.",durationHours,energyUsagePercent);
    }
}

