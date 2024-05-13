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
import com.eng1.heslingtonhustle.helper.ScoreManager;
import com.eng1.heslingtonhustle.gameobjects.Day;
import com.eng1.heslingtonhustle.gameobjects.Time;
import com.eng1.heslingtonhustle.player.PlayerManager;

import java.util.List;


public class GameUI {
    private final Stage uiStage;
    private final Texture xpBackground;
    private final Texture xpFill;
    public final ProgressBar progressBar;
    private Label timeLabel;
    private Label dayLabel;
    private final PlayerManager playerManager;
    private final Time time;
    private Table scoreTable;
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
    public GameUI(Stage uiStage, PlayerManager playerManager) {
        this.uiStage = uiStage;
        this.playerManager = playerManager;
        this.time = playerManager.getTime();
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
        uiStage.addActor(scoreTable);

        int score = calculateScore(week);
        int highScore = ScoreManager.loadHighScore();

        if (score > highScore) {
            highScore = score;
            ScoreManager.saveHighScore(highScore);
        }

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
        Label highScoreLabel = new Label("High Score: " + highScore, skin);
        highScoreLabel.setAlignment(Align.center);
        scoreTable.add(highScoreLabel).expandX().center().colspan(4).padTop(10).bottom();

        scoreTable.row().pad(10).bottom();
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
        score = Math.min(score, maxScore);

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

        // Cap the score at maxScore

        score = Math.max(score, 0);
        return score;
    }

    public Stage getUiStage() {
        return uiStage;
    }
}