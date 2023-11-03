package com.example.project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(),900,500);
//        stage.setTitle("Send Address to Driver");
//        stage.setScene(scene);

//        SendAddressToDriver controller = new SendAddressToDriver();
//        controller.sendAddressScreen();
        HelloController controller = new HelloController();
        controller.firstScreen();

//        Platform.runLater(() -> {
//            Server server = new Server();
//            server.start();
//        });
    }
    public static void main(String[] args) {
        launch();
    }
}