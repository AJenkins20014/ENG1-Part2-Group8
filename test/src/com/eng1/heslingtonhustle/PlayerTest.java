package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Vector2;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.player.PlayerManager;

@RunWith(GdxTestRunner.class)
public class PlayerTest {

	private PlayerManager playerManager;
	
	@Before
	public void setUp() {
		playerManager = new PlayerManager(new Vector2(0, 0), 0);
	}
	
	@Test
	public void testDayChange() {
		Day day = new Day();
		playerManager.setCurrentDay(day);
		assertEquals(day, playerManager.currentDay);
		playerManager.sleep();
		assertNotEquals(playerManager.currentDay, day);
		assertTrue(playerManager.getWeek().contains(day));
	}
	
	@Test
	public void testActivities() {
		Day day = new Day();
		playerManager.setCurrentDay(day);
		playerManager.study();
		assertEquals(playerManager.currentDay.studySessions, 1);
		playerManager.eat();
		assertEquals(playerManager.currentDay.eaten, 1);
		playerManager.relax();
		assertEquals(playerManager.currentDay.relaxed, 1);
	}
	
}
