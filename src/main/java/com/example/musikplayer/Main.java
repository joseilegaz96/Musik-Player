package com.example.musikplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    public static BorderPane mainScene;

    public static FXMLLoader saveFxml;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/mainView.fxml"));
        mainScene = fxmlLoader.load();
        saveFxml = new FXMLLoader(getClass().getResource("/tableViewView.fxml"));
        mainScene.setCenter(saveFxml.load());
        Scene scene = new Scene(mainScene);
        stage.setMinHeight(757);
        stage.setMinWidth(1266);
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("Musik");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}