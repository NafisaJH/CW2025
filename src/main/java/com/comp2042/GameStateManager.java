package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;


public class GameStateManager {
    private final GameOverPanel gameOverPanel;
    private final BooleanProperty paused = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private Timeline timeline;

    public GameStateManager(GameOverPanel gameOverPanel) {
        this.gameOverPanel = gameOverPanel;
    }

    public void startGameLoop(Runnable tickAction) {
        timeline = new Timeline(new KeyFrame(Duration.millis(400), e -> tickAction.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public boolean isPaused() { return paused.get(); }
    public boolean isGameOver() { return gameOver.get(); }

    public void gameOver() {
        timeline.stop();
        gameOverPanel.setVisible(true);
        gameOver.set(true);
    }

    public void newGame(InputEventListener listener) {
        timeline.stop();
        gameOverPanel.setVisible(false);
        listener.createNewGame();
        timeline.play();
        paused.set(false);
        gameOver.set(false);
    }

    public void togglePause() {
        if (isGameOver()) return;
        if (isPaused()) {
            timeline.play();
            paused.set(false);
        } else {
            timeline.pause();
            paused.set(true);
        }
    }
}