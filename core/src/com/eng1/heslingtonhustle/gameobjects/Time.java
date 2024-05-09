/**
 * The Time class represents the ingame time and day.
 * It tracks the current time, day number, and provides methods to manipulate time and day.
 */
package com.eng1.heslingtonhustle.gameobjects;

public class Time {
    public int time;
    private int dayNumber;

    private final String[] DAYS = new String[]{"Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"};

    /**
     * Constructs a new Time instance with the initial time set to 08:00 and day number set to Monday (0).
     */
    public Time() {
        this.time = 8;
    }

    /**
     * Retrieves the current time.
     * @return The current time in hours
     */
    public int getTime() {
        return time;
    }

    /**
     * Converts the time to a formatted string representation (HH:MM).
     * @return The formatted string representation of the time
     */
    @Override
    public String toString() {
        if (time==24){
            return "00:00";
        }
        if (time<10){
            return "0"+time+":00";
        }
        return time+":00";
    }

    /**
     * Increases the time by the specified amount.
     * @param activityLength The length of the activity to be performed
     */
    public void increaseTime(int activityLength) {
    	if(!canIncreaseTime(activityLength)) return;
        this.time += activityLength;
    }

    /**
     * Checks if it's possible to increase the time by the specified amount without exceeding the maximum time (24 hours).
     * @param activityLength The length of the activity to be performed
     * @return True if it's possible to increase the time, otherwise false
     */
    public boolean canIncreaseTime(int activityLength){
        final int MAX_TIME = 24;
        return (time+activityLength <= MAX_TIME);
    }

    /**
     * Moves to the next day and resets the time to 08:00.
     */
    public void nextDay(){
        if (dayNumber < 6) {
            dayNumber++;
            reset();
        } else {
            dayNumber = 7;
        }
    }

    /**
     * Checks if the week is over (all days have passed).
     * @return True if the week is over, otherwise false
     */
    public boolean isWeekOver(){
        return dayNumber == 7;
    }

    /**
     * Resets the time to 08:00.
     */
    public void reset() {
        this.time = 8;
    }

    /**
     * Retrieves the name of the current day.
     * @return The name of the current day
     */
    public String getDay() {
        if (dayNumber < 7) {
            return DAYS[dayNumber];
        } else {
            return null;
        }
    }

    /**
     * Retrieves the name of the day based on the given day number.
     * @param i The day number
     * @return The name of the day
     */
    public String getDay(int i) {
        if (i < 7) {
            return DAYS[i];
        } else {
            return null;
        }
    }
}
