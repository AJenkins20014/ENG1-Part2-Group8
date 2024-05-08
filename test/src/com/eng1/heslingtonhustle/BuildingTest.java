package com.eng1.heslingtonhustle;

import static org.junit.Assert.*;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.activities.*;
import com.eng1.heslingtonhustle.building.Building;
import com.eng1.heslingtonhustle.building.BuildingInfo;

public class BuildingTest {

    @Test
    public void testBuildingInitialization() {
        // Simulate TextureRegion without mocks
        TextureRegion textureRegion = new TextureRegion();
        
        BuildingInfo info = new BuildingInfo();
        info.name = "Library";
        info.textureStartX = 0;
        info.textureStartY = 0;
        info.textureWidth = 4;
        info.textureHeight = 3;
        info.activityName = "Study";
        info.energy = 10;
        info.time = 2;

        Building building = new Building(info);
        
        assertEquals("Library", building.getName());
        assertNotNull(building.getTextureRegion()); // We can't compare textureRegion as it's created internally
        assertTrue(building.getActivity() instanceof Study);
        assertEquals(10, building.getActivity().energyUsagePercent);
        assertEquals(2, building.getActivity().durationHours);
    }

    @Test
    public void testInRange() {
        BuildingInfo info = new BuildingInfo();
        info.name = "Cafe";
        info.textureStartX = 0;
        info.textureStartY = 0;
        info.textureWidth = 4;
        info.textureHeight = 3;
        info.activityName = "Eat";
        info.energy = 5;
        info.time = 1;

        Building building = new Building(info);
        building.setPosition(new Vector2(100, 100));

        // Simulate checking range without mocking
        assertTrue(building.inRange(new Vector2(150, 95))); // Assume interaction range and check accordingly
        assertFalse(building.inRange(new Vector2(10, 10))); // Clearly out of range
    }

    @Test
    public void testVisibility() {
        BuildingInfo info = new BuildingInfo();
        info.name = "Gym";
        info.textureStartX = 0;
        info.textureStartY = 0;
        info.textureWidth = 4;
        info.textureHeight = 3;
        info.activityName = "Relax";
        info.energy = 8;
        info.time = 1;

        Building building = new Building(info);

        // Initial visibility
        assertTrue(building.isVisible());

        // Set visibility to false
        building.setVisible(false);
        assertFalse(building.isVisible());

        // Set visibility to true
        building.setVisible(true);
        assertTrue(building.isVisible());
    }

    @Test
    public void testGetActivity() {
        BuildingInfo info = new BuildingInfo();
        info.name = "Dormitory";
        info.textureStartX = 0;
        info.textureStartY = 0;
        info.textureWidth = 4;
        info.textureHeight = 3;
        info.activityName = "Sleep";
        info.energy = 15;
        info.time = 8;

        Building building = new Building(info);
        Activity activity = building.getActivity();

        assertNotNull(activity);
        assertTrue(activity instanceof Sleep);
        assertEquals(15, activity.energyUsagePercent);
        assertEquals(8, activity.durationHours);
    }
}
