/**
 * The GameUI class manages the user interface elements displayed during the game.
 * It includes functionalities to display energy level, progress bar, time, day, interaction messages, and score.
 */
package com.eng1.heslingtonhustle.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.eng1.heslingtonhustle.helper.AchievementManager;
import com.eng1.heslingtonhustle.helper.ScoreManager;
import com.eng1.heslingtonhustle.Game;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.gameobjects.Time;
import com.eng1.heslingtonhustle.player.PlayerManager;

import java.util.List;


public class GameUI {
	private RenderingManager renderingManager;
	private AchievementManager achievementManager;
    private final Stage uiStage;
    private final Texture xpBackground;
    private final Texture xpFill;
    public final ProgressBar progressBar;
    private Label timeLabel;
    private Label dayLabel;
    private final PlayerManager playerManager;
    private final Time time;
    private Table scoreTable;
    private Table achievementTable;
    private final Skin skin;
    private Label interactLabel;

    public static final String xpBackgroundPath = "../assets/skin/craftacular/raw/xp-bg.png";
    public static final String xpFillPath = "../assets/skin/craftacular/raw/xp.png";
    public static final String skinPath = "../assets/skin/craftacular/skin/craftacular-ui.json";

    /**
     * Constructs a new GameUI instance with the given Stage and PlayerManager.
     *
     * @param uiStage       The Stage instance for UI elements
     * @param playerManager The PlayerManager instance
     */
    public GameUI(Stage uiStage, PlayerManager playerManager, RenderingManager renderingManager) {
        this.uiStage = uiStage;
        this.playerManager = playerManager;
        this.achievementManager = new AchievementManager();
        this.time = playerManager.getTime();
        this.renderingManager = renderingManager;
        xpBackground = new Texture(Gdx.files.internal(xpBackgroundPath));
        xpFill = new Texture(Gdx.files.internal(xpFillPath));
        progressBar = new ProgressBar(0, 100, 0.01f, false, new ProgressBar.ProgressBarStyle());
        // https://ray3k.wordpress.com/craftacular-ui-skin-for-libgdx/
        skin = new Skin(Gdx.files.internal(skinPath));
        initUI();
    }

    /**
     * Initializes the UI elements.
     */
    private void initUI() {

        Label energyLabel = new Label("Energy: ", skin);
        dayLabel = new Label("Day: ", skin);
        timeLabel = new Label("Time: ", skin);
        interactLabel = new Label("PRESS E TO INTERACT", skin);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(xpBackground);
        backgroundDrawable.setMinWidth(400);
        backgroundDrawable.setMinHeight(50);

        TextureRegionDrawable fillDrawable = new TextureRegionDrawable(xpFill);
        fillDrawable.setMinWidth(400);
        fillDrawable.setMinHeight(50);

        progressBar.getStyle().background = backgroundDrawable;
        progressBar.getStyle().knobBefore = fillDrawable;

        updateProgressBar();


        Table interactTable = new Table();
        interactTable.setFillParent(true);
        interactLabel.setVisible(false);
        interactTable.bottom().add(interactLabel).padBottom(5).padRight(5);
        uiStage.addActor(interactTable);


        Table table = new Table();
        table.setFillParent(true);
        table.top().right();

        float padTop = 40f;
        float padRight = 30f;

        table.add(energyLabel).padTop(padTop).padRight(5);
        table.add(progressBar).width(400).height(50).padTop(padTop).padRight(padRight);
        table.row();
        table.add(dayLabel).padTop(padTop).padRight(5);
        table.row();
        table.add(timeLabel).padTop(padTop).padRight(5);
        table.row();
        uiStage.addActor(table);
    }

    /**
     * Updates the progress bar, time label, and day label.
     */
    public void updateProgressBar() {
        progressBar.setValue(playerManager.getEnergy().getEnergyLevel());
        timeLabel.setText("Time: " + time.toString());
        dayLabel.setText("Day: " + time.getDay());
    }

    /**
     * Shows the interaction message.
     */
    public void showInteractMessage() {
        interactLabel.setVisible(true);
    }

    /**
     * Hides the interaction message.
     */
    public void hideInteractMessage() {
        interactLabel.setVisible(false);
    }

    /**
     * Displays the final score.
     *
     * @param week The list of Day objects representing the week's activities
     */
    public void showScore(List<Day> week) {
        uiStage.clear();
        scoreTable = new Table();
        scoreTable.setFillParent(true);

        final int score = calculateScore(week);

        Label saveScoreLabel = new Label("Save Score:", skin);
        saveScoreLabel.setPosition(100, 200);
        
        TextField textField = new TextField("", skin);
        textField.setMessageText("Enter Username");
        textField.setPosition(100, 100);
        textField.setWidth(1000);
        textField.setMaxLength(20);
        
        uiStage.addActor(saveScoreLabel);	
    	uiStage.addActor(textField);
    	
        scoreTable.add(new Label("Day", skin)).expandX().center().bottom();
        scoreTable.add(new Label("Study", skin)).expandX().center().bottom();
        scoreTable.add(new Label("Eaten", skin)).expandX().center().bottom();
        scoreTable.add(new Label("Relaxed", skin)).expandX().center().bottom();
        int index = 0;
        for (Day day : week) {
            addDayStatsLabel(time.getDay(index), day.getStudySessions(), day.getEaten(), day.getRelaxed());
            index++;
        }
        
        String grade = "First!";
        if(score < 70f) grade = "2:1";
        if(score < 60f) grade = "2:2";
        if(score < 50f) grade = "3";
        if(score < 40f) grade = "Fail!";
        
        Label scoreLabel = new Label("Final Score: " + score + " - " + grade, skin);
        scoreLabel.setAlignment(Align.center);
        scoreTable.row().expandY().bottom();
        scoreTable.add(scoreLabel).expandX().center().colspan(4).padTop(20).bottom();
        scoreTable.row().pad(10).bottom();
        
        TextButton returnButton = new TextButton("Save and Exit", Game.menuSkin);
        returnButton.setTransform(true);
        returnButton.setSize(400, 100);
        returnButton.setScale(1f);
        returnButton.setPosition(500, 200);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if(textField.getText() != "") {
            		ScoreManager.saveHighScore(score, textField.getText());
            	}
            	
            	renderingManager.restartGame();
            }
        });
        uiStage.addActor(returnButton);
        
        TextButton hideDetailsButton = new TextButton("Hide Details", Game.menuSkin);
        TextButton detailsButton = new TextButton("View Details", Game.menuSkin);
        
        hideDetailsButton.setTransform(true);
        hideDetailsButton.setSize(400, 100);
        hideDetailsButton.setScale(1f);
        hideDetailsButton.setPosition(900, 200);
        hideDetailsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	scoreTable.remove();
            	uiStage.addActor(achievementTable);
            	hideDetailsButton.remove();
            	uiStage.addActor(detailsButton);
            }
        });
        
        detailsButton.setTransform(true);
        detailsButton.setSize(400, 100);
        detailsButton.setScale(1f);
        detailsButton.setPosition(900, 200);
        detailsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	achievementTable.remove();
            	uiStage.addActor(scoreTable);
            	detailsButton.remove();
            	uiStage.addActor(hideDetailsButton);
            }
        });
        uiStage.addActor(detailsButton);
        
        
        
        achievementTable = new Table();
        achievementTable.setFillParent(true);
        achievementTable.add(new Label("Achievement", skin)).expandX().center().bottom();
        achievementTable.add(new Label("Earned?", skin)).expandX().center().bottom();
        achievementTable.add(new Label("Description", skin)).expandX().center().bottom();
        achievementTable.add(new Label("Bonus", skin)).expandX().center().bottom();
        
        List<String> allAchievements = achievementManager.getAllAchievements();
        List<Boolean> achievementsEarned = achievementManager.getAchievementsEarned();
        
        for (int i = 0; i < allAchievements.size(); i++) {
        	String earned = "No";
        	String description = "???";
        	if(achievementsEarned.get(i)) earned = "Yes";
        	if(achievementsEarned.get(i)) description = achievementManager.getDescription(allAchievements.get(i));
            addAchievementsLabel(allAchievements.get(i), earned, description, achievementManager.getBonus(allAchievements.get(i)));
            index++;
        }
        Label scoreLabel2 = new Label("Final Score: " + score + " - " + grade, skin);
        achievementTable.row().expandY().bottom();
        achievementTable.add(scoreLabel2).expandX().center().colspan(4).padTop(20).bottom();
        achievementTable.row().pad(10).bottom();
        uiStage.addActor(achievementTable);
    }

    /**
     * Adds a row of statistics for a specific day to the score table.
     *
     * @param dayLabel      The label representing the day
     * @param studySessions The number of study sessions for the day
     * @param eaten         The number of times eaten for the day
     * @param relaxed       The number of times relaxed for the day
     */
    private void addDayStatsLabel(String dayLabel, int studySessions, int eaten, int relaxed) {
        scoreTable.row().pad(10);
        scoreTable.add(new Label(dayLabel, skin)).expandX().center().bottom();
        scoreTable.add(new Label(String.valueOf(studySessions), skin)).expandX().center().bottom();
        scoreTable.add(new Label(String.valueOf(eaten), skin)).expandX().center().bottom();
        scoreTable.add(new Label(String.valueOf(relaxed), skin)).expandX().center().bottom();
    }
    
    /**
     * Adds a row of statistics for a specific day to the score table.
     *
     * @param dayLabel      The label representing the day
     * @param studySessions The number of study sessions for the day
     * @param eaten         The number of times eaten for the day
     * @param relaxed       The number of times relaxed for the day
     */
    private void addAchievementsLabel(String achievement, String earned, String description, String bonus) {
    	achievementTable.row().pad(10);
    	achievementTable.add(new Label(achievement, skin)).expandX().center().bottom();
    	achievementTable.add(new Label(earned, skin)).expandX().center().bottom();
    	achievementTable.add(new Label(description, skin)).expandX().center().bottom();
    	achievementTable.add(new Label(bonus, skin)).expandX().center().bottom();
    }

    /**
     * Calculates the score based on the statistics of the week.
     *
     * @param week The list of Day objects representing the week's statistics
     * @return The calculated score
     */
    private int calculateScore(List<Day> week) {
        int studyCount = 0;
        int dayStudiedOnce = 0;
        int dayRelaxedOnce = 0;
        int dayEatenCount = 0;
        int maxScore = 100;
        int score = 0;
        boolean catchup = false;

        for (Day day : week) {
            studyCount += day.getStudySessions();
            if (day.getStudySessions() >= 1) {
                dayStudiedOnce++;
            }
            if(day.getStudySessions() > 1) {
            	catchup = true;
            }
            if (day.getEaten() >= 2) {
                dayEatenCount++;
            }
            if (day.getRelaxed() > 0) {
                dayRelaxedOnce++;
            }
            if (day.placesStudied.size() > 1) {
            	// Add bonus
            	score += 5;
            }
            if (day.placesRelaxed.size() > 2) {
            	// Add bonus
            	score += 5;
            }
        }

        score = studyCount * 10;
        
        // Cap the score at maxScore
        score = Math.min(score, maxScore);
        
        // Calculate achievement bonus
        achievementManager.calculateAchievements(week);
        score += achievementManager.getAchievementBonus();

        // Apply penalties
        if (dayStudiedOnce < 7 && !catchup) {
            score = Math.min(score, 39);
        }

        if (dayEatenCount < 7) {
            score -= 10; // Penalty for not eating enough
        }

        if (dayRelaxedOnce < 7) {
            score -= 10; // Penalty for not relaxing enough
        }   
        
        score = Math.max(score, 0); // Negative score resolved to 0
        return score;
    }

    public Stage getUiStage() {
        return uiStage;
    }
}