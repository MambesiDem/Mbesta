package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class CheckLaundryOrdersController implements Initializable {
    private Stack<Scene> navigationStack = new Stack();

    private static List<Integer> keepIds = new ArrayList<>();
    @FXML
    private ListView tblOrders;
    private Button btnBack;
    @FXML
    private Button btnNotify;
    private ObservableList<Integer> Orders = FXCollections.observableArrayList();
    private void setupOrders() throws SQLException {
        Database Dba = new Database();
        Dba.connectToDB();
        Statement st = Dba.getStatement();
        String q = "Select OrderNumber From LaundryOrder;";
        ResultSet results = st.executeQuery(q);
        while(results.next()){
            int id = results.getInt("OrderNumber");
            //LaundryOrders order = new LaundryOrders();
            Orders.add(id);
            keepIds.add(id);
        }
    }
    private void createTable(){
        tblOrders.setItems(Orders);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setupOrders();
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void navigateToNewScreen(ActionEvent event, String fxmlFile) {
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

    public void onBackBtnClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
    }

    public void onNotifyBtnClicked(ActionEvent actionEvent) {
        Orders.clear();
        //new SendAddressToDriverController(keepIds);
        navigateToNewScreen(actionEvent,"SendAddressToDriver.fxml");
    }
    public static List<Integer> getKeepIds(){
        return keepIds;
    }
}