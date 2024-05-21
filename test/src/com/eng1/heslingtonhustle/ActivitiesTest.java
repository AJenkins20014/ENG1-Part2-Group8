package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.eng1.heslingtonhustle.activities.Eat;
import com.eng1.heslingtonhustle.activities.Relax;
import com.eng1.heslingtonhustle.activities.Sleep;
import com.eng1.heslingtonhustle.activities.Study;

/**
 * Tests the functionality of various player activities such as eating, relaxing, sleeping, and studying.
 */
@RunWith(GdxTestRunner.class)
public class ActivitiesTest {

    /**
     * Tests the behaviour and properties of the Eat activity.
     */
    @Test
    public void testEatActivity() {
        Eat eatActivity = new Eat(1, 10);

        // Check the initial state via toString
        String expectedDescription = "This will help you survive, please eat.\nIt will take 1 hours and use 10% of your energy.";
        assertEquals(expectedDescription, eatActivity.toString());
    }

    /**
     * Tests the behaviour and properties of the Relax activity.
     */
    @Test
    public void testRelaxActivity() {
        Relax relaxActivity = new Relax(2, 5);

        // Check the initial state via toString
        String expectedDescription = "This may help you relax.\nIt will take 2 hours and use 5% of your energy.";
        assertEquals(expectedDescription, relaxActivity.toString());
    }

    /**
     * Tests the behaviour and properties of the Sleep activity.
     */
    @Test
    public void testSleepActivity() {
        Sleep sleepActivity = new Sleep(8, 15);

        // Check the initial state via toString
        String expectedDescription = "This will make you skip to the next day.";
        assertEquals(expectedDescription, sleepActivity.toString());
    }

    /**
     * Tests the behaviour and properties of the Study activity.
     */
    @Test
    public void testStudyActivity() {
        Study studyActivity = new Study(3, 20);

        // Check the initial state via toString
        String expectedDescription = "This will help increase your grades.\nIt will take 3 hours and use 20% of your energy.";
        assertEquals(expectedDescription, studyActivity.toString());
    }
}
