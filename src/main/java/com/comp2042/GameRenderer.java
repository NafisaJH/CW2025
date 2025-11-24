package com.comp2042;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GameRenderer {
    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane nextBrickPanel;
    private Rectangle[][] displayMatrix;
    private Rectangle[][] brickRectangles;
    private Rectangle[][] nextBrickRectangles;
    private static final int BRICK_SIZE = 20;

    public GameRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextBrickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.nextBrickPanel = nextBrickPanel;
    }

    public void initBoard(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE, Color.TRANSPARENT);
                displayMatrix[i][j] = rect;
                gamePanel.add(rect, j, i - 2);
            }
        }
    }

    private Paint getFillColor(int i) {
        switch (i) {
            case 0:  return Color.TRANSPARENT;
            case 1:  return Color.AQUA;
            case 2:  return Color.BLUEVIOLET;
            case 3:  return Color.DARKGREEN;
            case 4:  return Color.YELLOW;
            case 5:  return Color.RED;
            case 6:  return Color.BEIGE;
            case 7:  return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }

    public void renderBrick(ViewData brick) {
        if (brickRectangles == null) {
            brickRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    brickRectangles[i][j] = rect;
                    brickPanel.add(rect, j, i);
                }
            }
        }
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                brickRectangles[i][j].setFill(getFillColor(brick.getBrickData()[i][j]));
            }
        }
        updateBrickPosition(brick);
    }

    public void updateBrickPosition(ViewData brick) {
        brickPanel.setLayoutX(brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(brick.getyPosition() * BRICK_SIZE);
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                displayMatrix[i][j].setFill(getFillColor(board[i][j]));
                displayMatrix[i][j].setArcHeight(9);
                displayMatrix[i][j].setArcWidth(9);
            }
        }
    }


    public void showNextBrick(ViewData brick) {
        int[][] nextBrickData = brick.getNextBrickData();
        nextBrickPanel.getChildren().clear();
        nextBrickRectangles = new Rectangle[nextBrickData.length][nextBrickData[0].length];
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rect = new Rectangle(
                        BRICK_SIZE,
                        BRICK_SIZE,
                        getFillColor(nextBrickData[i][j]) // ✅ fixed
                );
                nextBrickRectangles[i][j] = rect;
                nextBrickPanel.add(rect, j, i);
            }
        }
    }

    public void updateAfterMove(DownData downData, Group notificationGroup) {
        renderBrick(downData.getViewData());
        showNextBrick(downData.getViewData());

        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel np = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            notificationGroup.getChildren().add(np);
            np.showScore(notificationGroup.getChildren());
        }
    }
}