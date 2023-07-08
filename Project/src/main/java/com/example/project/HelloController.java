package com.example.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    private Stage stage = new Stage();
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
    private Button checkLaundryOrdersBtn;

    @FXML
    protected void onLogoutBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Login Page");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onCustManagementBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CustomerManagement.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Customer Management");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onBusManagementBtnClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("BusinessManagement.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Business Management");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onLoginBtnClicked() throws IOException {
        if(txtUserName.getText().equals("12345") && txtPassword.getText().equals("1234")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 500);
            stage.setTitle("Menu Page");
            stage.setScene(scene);
            stage.show();
        }
        else{
            validityTxt.setText("Invalid username/password. Try Again!");
        }
    }
    @FXML
    protected void onUsernameTxtClicked(){

    }
    @FXML
    protected void onPasswordTxtClicked(){

    }
}