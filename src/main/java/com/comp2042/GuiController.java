package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.effect.Reflection;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Button playAgainButton;

    @FXML
    private Button pauseButton;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private Label scoreLabel;

    private InputEventListener eventListener;
    private MediaPlayer scoreSoundPlayer;
    private GameRenderer renderer;
    private GameStateManager stateManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        Media sound = new Media(getClass().getResource("/score_points.mp3").toExternalForm());
        scoreSoundPlayer = new MediaPlayer(sound);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        renderer = new GameRenderer(gamePanel, brickPanel, nextBrickPanel);

        stateManager = new GameStateManager();
        stateManager.gameOverProperty().addListener((obs, wasOver, isOver) -> {
            gameOverPanel.setVisible(isOver);
            playAgainButton.setVisible(isOver);
        });
        stateManager.pausedProperty().addListener((obs, old, paused) -> {
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        gamePanel.setOnKeyPressed(keyEvent -> {
            if (!stateManager.isPaused() && !stateManager.isGameOver() && eventListener != null) {
                switch (keyEvent.getCode()) {
                    case LEFT, A -> renderer.renderBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    case RIGHT, D -> renderer.renderBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    case UP, W -> renderer.renderBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    case DOWN, S -> moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                }
                keyEvent.consume();
            }

            if (keyEvent.getCode() == KeyCode.P) {
                pauseGame(null);
                keyEvent.consume();
                return;
            }

            if (keyEvent.getCode() == KeyCode.N) {
                newGame(null);
            }
        });

        gameOverPanel.setVisible(false);
        playAgainButton.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        renderer.initBoard(boardMatrix);
        renderer.renderBrick(brick);
        stateManager.startGameLoop(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        renderer.showNextBrick(brick);
    }

    public void refreshGameBackground(int[][] boardMatrix) {
        renderer.refreshGameBackground(boardMatrix);
    }

    public void moveDown(MoveEvent event) {
        if (stateManager.isPaused()) return;

        DownData downData = eventListener.onDownEvent(event);

        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            if (scoreSoundPlayer != null) {
                scoreSoundPlayer.stop(); // reset playback
                scoreSoundPlayer.play(); // play sound
            }
        }

        renderer.updateAfterMove(downData, groupNotification);
    }

    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }

    public void bindScore(IntegerProperty score) {
        scoreLabel.textProperty().bind(score.asString("Score: %d"));
    }

    public void gameOver() {
        stateManager.gameOver();
    }

    public void newGame(ActionEvent e) {
        stateManager.newGame(eventListener);
    }

    public void pauseGame(ActionEvent e) {
        stateManager.togglePause();
    }
}
