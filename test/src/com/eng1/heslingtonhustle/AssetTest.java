/**
 * This class contains unit tests to verify the existence of various assets used in the game.
 * These assets include background music, building information files, maps, UI elements, shaders, and resource textures.
 * Each test method checks whether the respective asset file exists.
 */
package com.eng1.heslingtonhustle;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.eng1.heslingtonhustle.building.BuildingManager;
import com.eng1.heslingtonhustle.graphics.GameUI;
import com.eng1.heslingtonhustle.graphics.RenderingManager;
import com.eng1.heslingtonhustle.helper.ResourceLoader;
import com.eng1.heslingtonhustle.map.MapManager;

@RunWith(GdxTestRunner.class)
public class AssetTest {
	
	/**
     * Verifies the existence of music assets.
     */
	@Test
	public void testMusicAssetsExist() {
		assertTrue("Background music asset found", Gdx.files.internal(Game.bgMusic).exists());
	}
	
	/**
     * Verifies the existence of the building information json file.
     */
	@Test
	public void testBuildingInfoExists() {
		assertTrue("Building info json file found", Gdx.files.internal(BuildingManager.buildingInfo).exists());
	}
	
	/**
     * Verifies the existence of various maps used in the game.
     */
	@Test
	public void testMapsExist() {
		assertTrue("Cafe map found", Gdx.files.internal(MapManager.cafeMapPath).exists());
		assertTrue("Campus East (main) map found", Gdx.files.internal(MapManager.defaultMapPath).exists());
		assertTrue("Cinema map found", Gdx.files.internal(MapManager.cinemaMapPath).exists());
		assertTrue("Comp Sci map found", Gdx.files.internal(MapManager.compSciMapPath).exists());
		assertTrue("End Game map found", Gdx.files.internal(MapManager.endGameMapPath).exists());
		assertTrue("Home map found", Gdx.files.internal(MapManager.homeMapPath).exists());
		assertTrue("Library map found", Gdx.files.internal(MapManager.libraryMapPath).exists());
	}
	
	/**
     * Verifies the existence of UI assets such as skins and textures.
     */
	@Test
	public void testUIAssetsExist() {
		assertTrue("Default UI Skin found", Gdx.files.internal(GameManager.uiSkin).exists());
		assertTrue("XP Background found", Gdx.files.internal(GameUI.xpBackgroundPath).exists());
		assertTrue("XP Fill found", Gdx.files.internal(GameUI.xpFillPath).exists());
		assertTrue("Craftacular UI Skin found", Gdx.files.internal(GameUI.skinPath).exists());
		assertTrue("Menu skin path found", Gdx.files.internal(Game.menuSkinPath).exists());
		assertTrue("Menu skin path found", Gdx.files.internal(Game.menuSkinPath).exists());
		assertTrue("Menu background path found", Gdx.files.internal(Game.backgroundPath).exists());
		assertTrue("Tutorial Image path found", Gdx.files.internal(Game.tutorialImage).exists());
	}
	
	/**
     * Verifies the existence of shader assets.
     */
	@Test
	public void testShaderAssetsExist() {
		assertTrue("Vertex Shader found", Gdx.files.internal(RenderingManager.vertexShaderPath).exists());
		assertTrue("Fragment Shader found", Gdx.files.internal(RenderingManager.fragmentShaderPath).exists());
	}
	
	/**
     * Verifies the existence of resource assets such as animation sheets and textures.
     */
	@Test
	public void testResourceAssetsExist() {
		assertTrue("Walk Animation Sheet 1 found", Gdx.files.internal(ResourceLoader.walkSheetPath1).exists());
		assertTrue("Walk Animation Sheet 2 found", Gdx.files.internal(ResourceLoader.walkSheetPath2).exists());
		assertTrue("Walk Animation Sheet 3 found", Gdx.files.internal(ResourceLoader.walkSheetPath3).exists());
		assertTrue("Building textures found", Gdx.files.internal(ResourceLoader.buildingsPath).exists());
		assertTrue("Debug textures found", Gdx.files.internal(ResourceLoader.debugPath).exists());
		assertTrue("Overlay textures found", Gdx.files.internal(ResourceLoader.overlayPath).exists());
	}
}
