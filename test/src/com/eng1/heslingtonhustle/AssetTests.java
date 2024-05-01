package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

@RunWith(GdxTestRunner.class)
public class AssetTests {
	
	@Test
	public void testMusicAssetsExist() {
		assertTrue("All music assets found", Gdx.files.internal("images/debug.png").exists());
	}
}
