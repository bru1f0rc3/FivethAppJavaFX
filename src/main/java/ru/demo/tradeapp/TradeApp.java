package ru.demo.tradeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.demo.tradeapp.model.User;
import ru.demo.tradeapp.util.Manager;

import java.io.IOException;


public class TradeApp extends Application {

    public User currentUser;

    public static void main(String[] args) {
        launch();


    }

    @Override
    public void start(Stage stage) throws IOException {

        stage.getIcons().add(new Image(TradeApp.class.getResourceAsStream("pen.png")));
        stage.setScene(getNewScene());
        stage.setTitle("Авторизация!");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            Manager.ShowPopup();
        });
        stage.setOnShown(windowEvent -> {
            try {
                stage.setScene(getNewScene());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Manager.mainStage = stage;
        stage.show();
    }

    Scene getNewScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TradeApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("base-styles.css");
        return scene;
    }
}