package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class HelloController {

    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label validityTxt;
    @FXML
    private Button loginBtn;

    @FXML
    private Button logoutBtn;
    @FXML
    private Button custManagementBtn;

    @FXML
    private Button busManagementBtn;


    private Stage getCurrentStage(){
        return (Stage) loginBtn.getScene().getWindow();
    }

    public void firstScreen() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onLogoutBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        Stage oldstage = (Stage)logoutBtn.getScene().getWindow();
        oldstage.setTitle("Login Page");
        oldstage.setScene(scene);
        oldstage.show();
    }
    @FXML
    protected void onCustManagementBtnClicked(ActionEvent actionEvent) throws IOException {
        navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
    }
    @FXML
    public void onBusManagementBtnClicked(ActionEvent actionEvent) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("BusinessManagement.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
//        Stage oldstage = (Stage)busManagementBtn.getScene().getWindow();
//        oldstage.setTitle("Business Management");
//        oldstage.setScene(scene);
//        oldstage.show();
        navigateToNewScreen(actionEvent,"BusinessManagement.fxml");
    }
    @FXML
    public void onLoginBtnClicked(ActionEvent actionEvent) throws IOException {
        if(txtUserName.getText().equals("12345") && txtPassword.getText().equals("1234")){
            navigateToNewScreen(actionEvent,"MenuPage.fxml");
        }
        else{
            validityTxt.setText("Invalid username/password. Try Again!");
        }
    }
    @FXML
    protected void onPasswordTxtClicked(){

    }
    @FXML
    protected void onUsernameTxtClicked(){

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