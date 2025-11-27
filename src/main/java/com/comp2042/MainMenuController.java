package com.comp2042;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainMenuController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void startGame() {
        try {
            URL gameLocation = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader gameLoader = new FXMLLoader(gameLocation);
            Parent gameRoot = gameLoader.load();
            GuiController guiController = gameLoader.getController();

            Scene gameScene = new Scene(gameRoot, 1000, 610);
            stage.setScene(gameScene);

            new GameController(guiController);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exitGame() {
        javafx.application.Platform.exit();
    }
}
