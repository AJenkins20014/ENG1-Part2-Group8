package com.eng1.heslingtonhustle.helper;

import java.util.ArrayList;
import java.util.List;

import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.map.MapManager;

public class AchievementManager {
	
	/*
	 * Achievements:
	 * 
	 * Overachiever: Study more than once every day (+5)
	 * Glutton: Eat more than three times in one day (+2)
	 * Laid Back: Go a whole day without studing (+0)
	 * Dieting: Go a whole week without eating (+2)
	 * Sleeping Beauty: Go to sleep before 8pm at least twice (+5)
	 * Overworked: Go a whole week without relaxing (+2)
	 * Bookworm: Study in the library at least 6 times (+5)
	 * Technoholic: Study in the CS Building at least 6 times (+5)
	 */
	
	private boolean overAchiever;
	private boolean glutton;
	private boolean laidBack;
	private boolean dieting;
	private boolean sleepingBeauty;
	private boolean overworked;
	private boolean bookworm;
	private boolean technoholic;

	
	public AchievementManager() {

	}
	
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
	
	public boolean sleepingBeauty(List<Day> week) {
		int counter = 0;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).timeSlept < 20) {
				counter++;
			}
		}
		
		return (counter >= 2);
	}
	
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
	
	public boolean bookworm(List<Day> week) {
		int counter = 0;
		
		for(int i = 0; i < week.size(); i++) {
			if(week.get(i).placesStudied.contains(MapManager.libraryMapPath) && week.get(i).placesStudied.size() == 1) {
				counter++;
			}
		}
		
		return (counter >= 6);
	}
	
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
