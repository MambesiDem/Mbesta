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

public class CustManagementController {
    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private Button backBtn;
    @FXML
    protected void onBackBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        Stage oldstage = (Stage) backBtn.getScene().getWindow();
        oldstage.setTitle("Menu Page");
        oldstage.setScene(scene);
        oldstage.show();
    }


    public void onCheckLaundryOrdersClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"check_laundry_orders.fxml");
    }

    public void onCaptureWalkIn(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CaptureWalkIns.fxml");
    }

    public void onConfirmLaundryClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CLO_view.fxml");
    }

    public void onUpdateStatusClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"update_status.fxml");
    }

    public void onViewCommentsClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CommentsView.fxml");
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
