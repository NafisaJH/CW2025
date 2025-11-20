package com.comp2042;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        if (board.moveBrickDown()) {
            handleBrickMovedByUser(event);
            return new DownData(null, board.getViewData());
        } else {
            return handleBrickLocked();
        }
    }

    private void handleBrickMovedByUser(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().addPoints(1);
        }
    }

    private DownData handleBrickLocked() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        updateScore(clearRow);

        boolean gameOver = board.createNewBrick();
        if (gameOver) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    private void updateScore(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().addPoints(clearRow.getScoreBonus());
        }
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
