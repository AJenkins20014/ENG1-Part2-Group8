/**
 * The Energy class represents the player's energy level.
 */
package com.eng1.heslingtonhustle.gameobjects;

public class Energy {
    private int energy = 100;

    /**
     * Constructs a new Energy instance with the initial energy level set to 100.
     */
    public Energy() {
    }

    /**
     * Retrieves the current energy level.
     * @return The current energy level
     */
    public int getEnergyLevel() {
        return energy;
    }

    /**
     * Reduces the energy level by the specified amount if possible.
     * @param energyUsed The amount of energy to be used
     */
    public void useEnergy(int energyUsed) {
        int min = 0;
        boolean canBeDone = energy - energyUsed >= min;
        if (canBeDone) {
            this.energy = energy - energyUsed;
        }
    }

    /**
     * Checks if it's possible to use the specified amount of energy.
     * @param energyUsed The amount of energy to be used
     * @return True if it's possible to use the energy, otherwise false
     */
    public boolean canUseEnergy(int energyUsed){
        final int MIN_ENERGY = 0;
        return (energy - energyUsed >= MIN_ENERGY);
    }

    /**
     * Resets the energy level back to 100.
     */
    public void reset() {
        this.energy = 100;
    }
}
