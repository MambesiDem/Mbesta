package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Stack;

public class CommentsController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private TableView comments;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    ObservableList<CommentsAndRatings> commentList= FXCollections.observableArrayList();;
    private Stack<Scene> navigationStack = new Stack();
    Database Dba;
    public void createTable(){

        TableColumn column1 = new TableColumn("Comment");
        column1.setCellValueFactory(
                new PropertyValueFactory<CommentsAndRatings,String>("comment")
        );
        TableColumn column2 = new TableColumn("Rating");
        column2.setCellValueFactory(
                new PropertyValueFactory<CommentsAndRatings,String>("rating")
        );
        comments.getColumns().addAll(column1,column2);
        comments.setItems(commentList);
    }
    private void setUptable() throws SQLException {
        Dba= new Database();
        Dba.connectToDB();
        Statement statement = Dba.getStatement();
        String query ="Select commentText,Rating From StudentFeedback";
        ResultSet results = statement.executeQuery(query);
        while (results.next()){
            String comment = results.getString("commentText");
            String rating = results.getString("Rating");
            CommentsAndRatings item = new CommentsAndRatings(comment,rating);
            commentList.add(item);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTable();
        try {
            setUptable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onBackBtnClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
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