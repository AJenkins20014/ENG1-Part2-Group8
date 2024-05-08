package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.gameobjects.Energy;
import com.eng1.heslingtonhustle.gameobjects.Time;

public class GameObjectsTest {

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
        assertEquals(80, energy.getEnergyLevel()); // Should not change

        // Resetting energy
        energy.reset();
        assertEquals(100, energy.getEnergyLevel());
    }

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
        time.increaseTime(15); // This should not increase time beyond 24 hours
        assertEquals(13, time.getTime()); // Time does not exceed 24 hours limit

        // Moving to the next day
        time.nextDay();
        assertEquals("Tuesday", time.getDay());
        assertEquals(8, time.getTime()); // Resets to 8:00

        // Testing week completion
        for (int i = 0; i < 5; i++) {
            time.nextDay(); // Move through Wednesday, Thursday, Friday, Saturday, Sunday
        }
        assertEquals("Sunday", time.getDay());
        time.nextDay(); // Should indicate week over
        assertNull(time.getDay());
    }
}
