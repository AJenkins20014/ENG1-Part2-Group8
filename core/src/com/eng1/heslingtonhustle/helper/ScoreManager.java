/**
 * Utility class for managing high scores.
 */
package com.eng1.heslingtonhustle.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {
    private static final String HIGHSCORE_KEY = "highscore"; 

    /**
     * Loads the high score from preferences.
     * @return The loaded high score
     */
    public static int loadHighScore() {
        Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
        return prefs.getInteger(HIGHSCORE_KEY, 0);
    }

    /**
     * Saves the high score to preferences.
     * @param score The high score to be saved
     */
    public static void saveHighScore(int score) {
        Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
        prefs.putInteger(HIGHSCORE_KEY, score);
        prefs.flush();
    }
}