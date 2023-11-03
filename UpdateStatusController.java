package com.example.project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Stack;

public class UpdateStatusController implements Initializable {
    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private RadioButton on_queue;
    @FXML
    private RadioButton washing;
    @FXML
    private RadioButton finished;
    @FXML
    private TableView pendingOrders;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField searchFld;
    ToggleGroup radioGroup = new ToggleGroup();
    ObservableList<StudentOrder> orderIDs = FXCollections.observableArrayList();
    Database Dba = new Database();
    private void setUpR_Buttons(){
        on_queue.setToggleGroup(radioGroup);
        washing.setToggleGroup(radioGroup);
        finished.setToggleGroup(radioGroup);
    }
    private void setUpOrders() throws SQLException {
        Database Dba = new Database();
        Dba.connectToDB();
        Statement statement=Dba.getStatement();
        String q = "Select OrderNumber,StudentName,Surname,LaundryStatus From LaundryOrder La,Student Sd WHERE La.StudentNO = Sd.StudentNo;";
        ResultSet results = statement.executeQuery(q);
        while(results.next()){
            int ID = results.getInt("OrderNumber");
            String Name = results.getString("StudentName");
            String Surname = results.getString("Surname");
            String status = results.getString("LaundryStatus");
            StudentOrder studentOrder = new StudentOrder(ID,Name,Surname);
            studentOrder.setStatus(status);
            orderIDs.add(studentOrder);
        }
        pendingOrders.setItems(orderIDs);
    }
    private void searchOrder(int wanted) throws SQLException {
        Dba.connectToDB();
        Statement statement=Dba.getStatement();
        String q = "Select OrderNumber,StudentName,Surname,LaundryStatus From LaundryOrder LO,Student s WHERE s.StudentNo=LO.StudentNo AND LO.OrderNumber="+ wanted +";";
        ResultSet results = statement.executeQuery(q);
        orderIDs.clear();
        while(results.next()){

            int ID = results.getInt("OrderNumber");
            String Name = results.getString("StudentName");
            String Surname = results.getString("Surname");
            String status = results.getString("LaundryStatus");
            StudentOrder studentOrder = new StudentOrder(ID,Name,Surname);
            studentOrder.setStatus(status);
            orderIDs.add(studentOrder);
        }
        pendingOrders.setItems(orderIDs);
    }
    private void CreateTable(){
        TableColumn columnID = new TableColumn("OrderNumber");
        columnID.setCellValueFactory(
                new PropertyValueFactory<StudentOrder,Integer>("OrderNumber")
        );
        TableColumn columnName = new TableColumn("Name");
        columnName.setCellValueFactory(
                new PropertyValueFactory<StudentOrder,Integer>("StudName")
        );
        TableColumn columnSurname = new TableColumn("Surname");
        columnSurname.setCellValueFactory(
                new PropertyValueFactory<StudentOrder,Integer>("StudSurname")
        );
        pendingOrders.getColumns().addAll(columnID,columnName,columnSurname);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpR_Buttons();
        try {
            CreateTable();
            setUpOrders();
            on_queue.setDisable(true);
            washing.setDisable(true);
            finished.setDisable(true);
            pendingOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentOrder>() {
                @Override
                public void changed(ObservableValue<? extends StudentOrder> observable, StudentOrder oldValue, StudentOrder newValue) {
                    // Enable or disable the radio buttons based on selection
                    boolean isRowSelected = (newValue != null);
                    on_queue.setDisable(!isRowSelected);
                    washing.setDisable(!isRowSelected);
                    finished.setDisable(!isRowSelected);

                    if (isRowSelected) {
                        // You can also set the initial selection for the radio buttons
                        // based on the selected row's status, for example:
                        if (newValue.getStatus().equals("on_queue")) {
                            on_queue.setSelected(true);
                        } else if (newValue.getStatus().equals("washing")) {
                            washing.setSelected(true);
                        } else if (newValue.getStatus().equals("finished")) {
                            finished.setSelected(true);
                        }
                        if(radioGroup.getSelectedToggle().isSelected()){
//                            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
//                            if (selectedRadioButton != null) {
//                                String selectedStatus = selectedRadioButton.getText(); // Get the text of the selected radio button
//                                LaundryOrder selectedOrder = getSelectedLaundryOrder(); // Implement this method to get the selected LaundryOrder from the database
//                                if (selectedOrder != null) {
//                                    updateLaundryOrderStatus(selectedOrder, selectedStatus); // Implement this method to update the status in the database
//                                }
//                            }
                            StudentOrder selectedOrder = (StudentOrder)pendingOrders.getSelectionModel().getSelectedItem();
                            int id = selectedOrder.getOrderNumber();
                            RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();

                            String status = selectedRadioButton.getText();
                            Dba= new Database();
                            Dba.connectToDB();
                            Statement statement = Dba.getStatement();
                            String query= "UPDATE LaundryOrder SET LaundryStatus = '"+status+"' Where OrderNumber ="+id+";";
                            try {
                                statement.execute(query);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onButtonSearchClicked(){
        try {
            if (!searchFld.getText().isEmpty()) {
                int wanted = Integer.parseInt(searchFld.getText());
                searchOrder(wanted);
            }
        } catch(SQLException e) {
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
}