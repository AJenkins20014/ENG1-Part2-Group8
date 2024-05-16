/**
 * The AchievementManager class is responsible for managing and calculating
 * the various achievements that a player can earn based on their actions 
 * throughout the game week.
 * 
 * Achievements include:
 * - Overachiever: Study more than once every day (+5)
 * - Glutton: Eat more than three times in one day (+2)
 * - Laid Back: Go a whole day without studying (+0)
 * - Dieting: Go a whole week without eating (+2)
 * - Sleeping Beauty: Go to sleep before 8pm at least twice (+5)
 * - Overworked: Go a whole week without relaxing (+2)
 * - Bookworm: Study in the library at least 6 times (+5)
 * - Technoholic: Study in the CS Building at least 6 times (+5)
 * 
 * Each achievement has an associated bonus score which is added to the player's 
 * total score if the achievement is earned.
 */
package com.eng1.heslingtonhustle.helper;

import java.util.ArrayList;
import java.util.List;

import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.map.MapManager;

public class AchievementManager {
	
	private boolean overAchiever;
	private boolean glutton;
	private boolean laidBack;
	private boolean dieting;
	private boolean sleepingBeauty;
	private boolean overworked;
	private boolean bookworm;
	private boolean technoholic;

	/**
	 * Constructs a new instance of the AchievementManager class.
	 */
	public AchievementManager() {

	}

	/**
     * Calculates and updates the status of all achievements based on the given week data.
     * @param week The list of all Day objects in the week.
     */
	public void calculateAchievements(List<Day> week) {
		overAchiever = overAchiever(week);
		glutton = glutton(week);
		laidBack = laidBack(week);
		dieting = dieting(week);
		sleepingBeauty = sleepingBeauty(week);
		overworked = overworked(week);
		bookworm = bookworm(week);
		technoholic = technoholic(week);
	}
	
	/**
     * Calculates the total achievement bonus score based on earned achievements.
     * @return The total bonus score.
     */
	public int getAchievementBonus() {
		int bonus = 0;
		if(overAchiever) bonus += 5;
		if(glutton) bonus += 2;
		if(laidBack) bonus += 0;
		if(dieting) bonus += 2;
		if(sleepingBeauty) bonus += 5;
		if(overworked) bonus += 2;
		if(bookworm) bonus += 5;
		if(technoholic) bonus += 5;
		
		return(bonus);
	}
	
	/**
     * Returns a list of booleans indicating which achievements have been earned.
     * @return A list of booleans representing the status of each achievement.
     */
	public List<Boolean> getAchievementsEarned() {
		List<Boolean> achievementList = new ArrayList<>();
		achievementList.add(overAchiever);
		achievementList.add(glutton);
		achievementList.add(laidBack);
		achievementList.add(dieting);
		achievementList.add(sleepingBeauty);
		achievementList.add(overworked);
		achievementList.add(bookworm);
		achievementList.add(technoholic);
		
		return(achievementList);
	}
	
	/**
     * Returns a list of all possible achievements.
     * @return A list of strings representing all achievements.
     */
	public List<String> getAllAchievements() {
		List<String> achievementList = new ArrayList<>();
		achievementList.add("Over Achiever");
		achievementList.add("Glutton");
		achievementList.add("Laid Back");
		achievementList.add("Dieting");
		achievementList.add("Sleeping Beauty");
		achievementList.add("Over Worked");
		achievementList.add("Bookworm");
		achievementList.add("Technoholic");
		
		return(achievementList);
	}
	
	/**
     * Returns the description of a specific achievement.
     * @param achievement The name of the achievement.
     * @return The description of the achievement.
     */
	public String getDescription(String achievement) {
		if(achievement == "Over Achiever") {
			return("Study more than once every day");
		}
		else if(achievement == "Glutton") {
			return("Eat more than three times in one day");
		}
		else if(achievement == "Laid Back") {
			return("Go a whole day without studing");
		}
		else if(achievement == "Dieting") {
			return("Go a whole week without eating");
		}
		else if(achievement == "Sleeping Beauty") {
			return("Go to sleep before 8pm at least twice");
		}
		else if(achievement == "Over Worked") {
			return("Go a whole week without relaxing");
		}
		else if(achievement == "Bookworm") {
			return("Study in the library at least 6 times");
		}
		else if(achievement == "Technoholic") {
			return("Study in the CS Building at least 6 times");
		}
		else {
			return("Achievement Not Recognised");
		}
	}
	
	/**
     * Returns the bonus score of a specific achievement.
     * @param achievement The name of the achievement.
     * @return The bonus score of the achievement.
     */
	public String getBonus(String achievement) {
		if(achievement == "Over Achiever") {
			return("5");
		}
		else if(achievement == "Glutton") {
			return("2");
		}
		else if(achievement == "Laid Back") {
			return("0");
		}
		else if(achievement == "Dieting") {
			return("2");
		}
		else if(achievement == "Sleeping Beauty") {
			return("5");
		}
		else if(achievement == "Over Worked") {
			return("2");
		}
		else if(achievement == "Bookworm") {
			return("5");
		}
		else if(achievement == "Technoholic") {
			return("5");
		}
		else {
			return("0");
		}
	}
	
	/**
     * Checks if the Overachiever achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player studied more than once every day, false otherwise.
     */
	public boolean overAchiever(List<Day> week){
		boolean achieved = true;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).studySessions < 2) {
				achieved = false;
				break;
			}
		}
		return achieved;
	}
	
	/**
     * Checks if the Glutton achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player ate more than three times in one day, false otherwise.
     */
	public boolean glutton(List<Day> week) {
		boolean achieved = false;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).eaten > 3) {
				achieved = true;
				break;
			}
		}
		return achieved;
	}
	
	/**
     * Checks if the Laid Back achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player went a whole day without studying, false otherwise.
     */
	public boolean laidBack(List<Day> week) {
		boolean achieved = false;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).studySessions == 0) {
				achieved = true;
				break;
			}
		}
		return achieved;
	}
	
	/**
     * Checks if the Dieting achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player went a whole week without eating, false otherwise.
     */
	public boolean dieting(List<Day> week) {
		boolean achieved = false;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).eaten == 0) {
				achieved = true;
				break;
			}
		}
		return achieved;
	}
	
	/**
     * Checks if the Sleeping Beauty achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player went to sleep before 8pm at least twice, false otherwise.
     */
	public boolean sleepingBeauty(List<Day> week) {
		int counter = 0;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).timeSlept < 20) {
				counter++;
			}
		}
		
		return (counter >= 2);
	}
	
	/**
     * Checks if the Overworked achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player went a whole week without relaxing, false otherwise.
     */
	public boolean overworked(List<Day> week) {
		boolean achieved = true;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).relaxed > 0) {
				achieved = false;
				break;
			}
		}
		return achieved;
	}
	
	/**
     * Checks if the Bookworm achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player studied in the library at least 6 times, false otherwise.
     */
	public boolean bookworm(List<Day> week) {
		int counter = 0;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).placesStudied.contains(MapManager.libraryMapPath) && week.get(i).placesStudied.size() == 1) {
				counter++;
			}
		}
		
		return (counter >= 6);
	}
	
	/**
     * Checks if the Technoholic achievement is earned.
     * @param week The list of all Day objects in the week.
     * @return True if the player studied in the CS Building at least 6 times, false otherwise.
     */
	public boolean technoholic(List<Day> week) {
		int counter = 0;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).placesStudied.contains(MapManager.compSciMapPath) && week.get(i).placesStudied.size() == 1) {
				counter++;
			}
		}
		
		return (counter >= 6);
	}
	
	
}
