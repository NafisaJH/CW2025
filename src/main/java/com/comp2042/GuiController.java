package com.comp2042;

import com.comp2042.commands.*;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

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

    private Rectangle[][] nextBrickRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private GameStateManager stateManager;

    private Map<KeyCode, Command> commandMap = new HashMap<>();

    private void initCommands() {
        commandMap.put(KeyCode.LEFT, new MoveLeftCommand(eventListener, this::refreshBrick));
        commandMap.put(KeyCode.A,    new MoveLeftCommand(eventListener, this::refreshBrick));

        commandMap.put(KeyCode.RIGHT, new MoveRightCommand(eventListener, this::refreshBrick));
        commandMap.put(KeyCode.D,     new MoveRightCommand(eventListener, this::refreshBrick));

        commandMap.put(KeyCode.UP, new RotateCommand(eventListener, this::refreshBrick));
        commandMap.put(KeyCode.W,  new RotateCommand(eventListener, this::refreshBrick));

        commandMap.put(KeyCode.DOWN, new MoveDownCommand(this::moveDown));
        commandMap.put(KeyCode.S,    new MoveDownCommand(this::moveDown));

        commandMap.put(KeyCode.P, new PauseCommand(() -> pauseGame(null)));
        commandMap.put(KeyCode.N, new NewGameCommand(() -> newGame(null)));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        stateManager = new GameStateManager();
        stateManager.gameOverProperty().addListener((obs, wasOver, isOver) -> {
            gameOverPanel.setVisible(isOver);
            playAgainButton.setVisible(isOver);
        });
        stateManager.pausedProperty().addListener((obs, old, paused) -> {
            pauseButton.setText(paused ? "Resume" : "Pause");
        });

        gamePanel.setOnKeyPressed(event -> {
            if (!stateManager.isPaused() && !stateManager.isGameOver()) {
                Command command = commandMap.get(event.getCode());
                if (command != null) {
                    command.execute();
                    event.consume();
                }
            } else {
                if (event.getCode() == KeyCode.P || event.getCode() == KeyCode.N) {
                    Command command = commandMap.get(event.getCode());
                    if (command != null) {
                        command.execute();
                        event.consume();
                    }
                }
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
        stateManager.startGameLoop(() -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        showNextBrick(brick);

    }


    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void refreshBrick(ViewData brick) {
        if (!stateManager.isPaused()) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

        }
    }

    private void showNextBrick(ViewData brick) {
        int[][] nextBrickData = brick.getNextBrickData();

        nextBrickPanel.getChildren().clear();

        nextBrickRectangles = new Rectangle[nextBrickData.length][nextBrickData[0].length];

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextBrickData[i][j]));
                nextBrickRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (!stateManager.isPaused() && !stateManager.isGameOver()) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
            showNextBrick(downData.getViewData());
        }
        gamePanel.requestFocus();

    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        initCommands();
    }

    public void bindScore(IntegerProperty integerProperty) {
    }

    public void gameOver() {
        stateManager.gameOver();
    }
    public void newGame(ActionEvent e) {
        stateManager.newGame(eventListener);
    }
    public void pauseGame(ActionEvent e) { stateManager.togglePause(); }
}
