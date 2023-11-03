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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

public class ConfirmLaundryOrder implements Initializable {
    @FXML
    private TextField searchFld;
    @FXML
    private  Button searchBtn;
    @FXML
    private TableView orderTable;
    @FXML
    private Button PlaceBtn;
//    private CheckBox box;
    ArrayList<LaundryOrder> Order = new ArrayList<>();
    LaundryOrder orderSearched;

    private Stack<Scene> navigationStack = new Stack();

    @FXML
    public void onSearchBtnClicked(){
        try {
            if (!searchFld.getText().isEmpty()) {
                Order.clear();
                int wanted = Integer.parseInt(searchFld.getText().toString());
                searchOrder(wanted);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void searchOrder(int wanted) throws SQLException {
        Database Dba= new Database();
        Dba.connectToDB();
        Statement st =Dba.getStatement();
        String q ="SELECT ItemList.ItemName,LaundryBasket.Quantity FROM ItemList,LaundryBasket WHERE LaundryBasket.ItemID=ItemList.ItemID AND OrderNumber ="+wanted+";";
        ResultSet resultSet = st.executeQuery(q);
        while(resultSet.next()){
            String itemType = resultSet.getString("ItemName");
            int quantity =  resultSet.getInt("Quantity");
//            box = new CheckBox();
            orderSearched = new LaundryOrder(itemType,quantity);
            Order.add(orderSearched);
        }
        ObservableList<LaundryOrder> obsList = FXCollections.observableArrayList(Order);
        orderTable.setItems(obsList);
    }
    private void insertOnTable(){
        TableColumn column1 = new TableColumn("ItemType");
        column1.setCellValueFactory(
                new PropertyValueFactory<LaundryOrder,String>("item")
        );
        TableColumn column2 = new TableColumn("Quantity");
        column2.setCellValueFactory(
                new PropertyValueFactory<LaundryOrder,Integer>("quantity")
        );
//        TableColumn column3 = new TableColumn("Checked");
//        column3.setCellValueFactory(
//                new PropertyValueFactory<LaundryOrder,CheckBox>("match")
//        );
        orderTable.getColumns().addAll(column1,column2);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertOnTable();
    }

    public void OnPlaceOnQueueClicked(ActionEvent actionEvent) {

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

    public void onBackBtnClicked(ActionEvent actionEvent) {
        navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
    }
}