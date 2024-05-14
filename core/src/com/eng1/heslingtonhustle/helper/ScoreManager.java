/**
 * Utility class for managing high scores.
 */
package com.eng1.heslingtonhustle.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {
    public static final String HIGHSCORE_TESTING_KEY = "HIGHSCORE_TEST";

    /**
     * Loads the high score from preferences.
     * @return The loaded high score
     */
    public static int loadHighScore(String username) {
        Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
        return prefs.getInteger(username, 0);
    }

    /**
     * Saves the high score to preferences.
     * @param score The high score to be saved
     */
    public static void saveHighScore(int score, String username) {
    	username.replaceAll(",","");
    	
        Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
        if(loadHighScore(username) > score || score == 0) {
        	return;
        }
        prefs.putInteger(username, score);
        String allUsers = prefs.getString("allUsers", "");
        String[] usersArray = allUsers.split(",");
        boolean userExists = false;
        for (String user : usersArray) {
            if (user.equals(username)) {
                userExists = true;
                break;
            }
        }
        if(!userExists) {
        	if(allUsers == "") {
        		allUsers = username;
        	}
        	else{
        		allUsers = allUsers + "," + username;
        	}
        	prefs.putString("allUsers", allUsers);
        }
        
        prefs.flush();
    }
    
    public static String getAllUsers() {
    	Preferences prefs = Gdx.app.getPreferences("HeslingtonHustleData");
    	String allUsers = prefs.getString("allUsers", "");
        return(allUsers);
    }
}