package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getClassLoader().getResource("MainMenu.fxml"));
        Parent menuRoot = menuLoader.load();

        MainMenuController controller = menuLoader.getController();
        controller.setStage(primaryStage); // pass stage

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(new Scene(menuRoot, 1000, 610));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
