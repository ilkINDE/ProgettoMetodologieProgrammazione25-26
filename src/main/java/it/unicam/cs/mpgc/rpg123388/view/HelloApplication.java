package it.unicam.cs.mpgc.rpg123388.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Dungeon Crawler RPG");
        stage.setScene(scene);

        stage.setMinWidth(1600);
        stage.setMinHeight(900);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}