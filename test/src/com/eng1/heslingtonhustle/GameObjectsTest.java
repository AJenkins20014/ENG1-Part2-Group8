package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.gameobjects.Energy;
import com.eng1.heslingtonhustle.gameobjects.Time;

/**
 * Tests the functionality and integrity of the game objects: Day, Energy, and Time.
 */
public class GameObjectsTest {

    /**
     * Tests the Day class for its functionality of tracking study sessions, meals, and relaxation activities.
     */
    @Test
    public void testDayClass() {
        Day day = new Day();
        
        // Initial state
        assertEquals(0, day.getStudySessions());
        assertEquals(0, day.getEaten());
        assertEquals(0, day.getRelaxed());

        // Incrementing each session
        day.studied();
        assertEquals(1, day.getStudySessions());
        
        day.eaten();
        assertEquals(1, day.getEaten());
        
        day.relaxed();
        assertEquals(1, day.getRelaxed());
    }

    /**
     * Tests the Energy class for managing energy levels through usage and reset operations.
     */
    @Test
    public void testEnergyClass() {
        Energy energy = new Energy();
        
        // Initial state
        assertEquals(100, energy.getEnergyLevel());
        
        // Using energy
        energy.useEnergy(20);
        assertEquals(80, energy.getEnergyLevel());
        
        // Trying to use more energy than available
        assertFalse(energy.canUseEnergy(85));
        energy.useEnergy(85);
        assertEquals(80, energy.getEnergyLevel());

        // Resetting energy
        energy.reset();
        assertEquals(100, energy.getEnergyLevel());
    }

    /**
     * Tests the Time class for managing time and day transitions within the game.
     */
    @Test
    public void testTimeClass() {
        Time time = new Time();
        
        // Initial state
        assertEquals(8, time.getTime());
        assertEquals("Monday", time.getDay());

        // Increase time within the same day
        time.increaseTime(5);
        assertEquals(13, time.getTime());

        // Increase time beyond the limit of a day
        assertFalse(time.canIncreaseTime(15));
        time.increaseTime(15); 
        assertEquals(13, time.getTime());

        // Moving to the next day
        time.nextDay();
        assertEquals("Tuesday", time.getDay());
        assertEquals(8, time.getTime());

        // Testing week completion
        for (int i = 0; i < 5; i++) {
            time.nextDay();
        }
        assertEquals("Sunday", time.getDay());
        time.nextDay();
        assertNull(time.getDay());
    }
}
