package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class BusManagementController {

    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private Button backBtn;
    @FXML
    protected void onBackBtnClicked(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        Stage oldstage = (Stage) backBtn.getScene().getWindow();
        oldstage.setTitle("Menu Page");
        oldstage.setScene(scene);
        oldstage.show();
    }

    public void onUpdatePriceListClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"price_view.fxml");
    }

    public void onAboutBtnClicked(ActionEvent actionEvent) {
    }
    private void navigateToNewScreen(ActionEvent event,String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene newScene = new Scene(loader.load(), 900, 500);
            navigationStack.push(((Node) event.getSource()).getScene()); // Push the current scene onto the stack
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
