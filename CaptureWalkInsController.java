package com.example.project;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Stack;

public class CaptureWalkInsController implements Initializable {
    private Stack<Scene> navigationStack = new Stack();
    @FXML
    private CheckBox makeTbl_additable;
    @FXML
    private TextField textS;
    @FXML
    private VBox vBoxDetails;
    @FXML
    private TableView table1;
    @FXML
    private TableView table2;
    @FXML
    private Button searchB;
    @FXML
    private Button BackBtn;
    @FXML
    private Label totalCost;

    private TableView<LaundryItems> tblOrders;
    private TableColumn column1;
    private TableColumn column2;
//    private TableColumn column3;
    private TableColumn column4;
    private TableColumn colItem;
    private TableColumn colQuantity;
    private CheckBox box;
    int quantity= 0;
    double overRollTotal;
    ObservableList <Busket> busket = FXCollections.observableArrayList();
    Database Dba;
    StudentWI studentSearched;
    private void createTable() throws SQLException {
        column1 = new TableColumn("LaundryItems");
        column1.setCellValueFactory(
                new PropertyValueFactory<LaundryItems,String>("Item_type")
        );
        column2 = new TableColumn("ItemPrice");
        column2.setCellValueFactory(
                new PropertyValueFactory<LaundryItems,Double>("price")
        );
        column4 = new TableColumn("Quantity");
        column4.setCellValueFactory(
                new PropertyValueFactory<LaundryItems,Integer>("quantity")
        );
//        column4.setEditable(false);
        table1.getColumns().addAll(column1,column2,column4);
        ArrayList<LaundryItems> ldItems = new ArrayList<>();
        setupOrders(ldItems);

        ObservableList<LaundryItems> obsList =FXCollections.observableArrayList(ldItems);

        table1.setItems(obsList);
    }
    private void setupOrders(ArrayList<LaundryItems> list) throws SQLException {
//        Dba = new Database();
//        Dba.connectToDB();
//        Statement statement = Dba.getStatement();
//        String Query = "Select ItemName,ItemPrice From ItemList;";
//        ResultSet results = statement.executeQuery(Query);
//        while(results.next()){
//            Double item_price = results.getDouble("ItemPrice");
//            String item_type = results.getString("ItemName");
//            //box = new CheckBox();
//            LaundryItems Data = new LaundryItems(item_price,item_type,quantity);
//            list.add(Data);
//        }
        Dba = new Database();
        Dba.connectToDB();
        Statement statement = Dba.getStatement();
        String Query = "Select ItemName,ItemPrice,ItemID From ItemList;";
        ResultSet results = statement.executeQuery(Query);
        while(results.next()){
            Double item_price = results.getDouble("ItemPrice");
            String item_type = results.getString("ItemName");
            int id = results.getInt("ItemID");
            LaundryItems Data = new LaundryItems(item_price,item_type,quantity,id);
            list.add(Data);
        }
    }
    private void searchStudent(int wanted) throws SQLException,NumberFormatException{
        Statement st =Dba.getStatement();
        String q ="Select * From Student Where StudentNo=" + wanted + ";";
        ResultSet resultSet = st.executeQuery(q);
        if(resultSet.next()){
            String name = resultSet.getString("StudentName");
            String Surname =  resultSet.getString("Surname");
            String contact = resultSet.getString("sContactNo");
            String Address = resultSet.getString("sResidentialAddress");
            int studentNo = resultSet.getInt("StudentNo");
            studentSearched = new StudentWI(studentNo,name,Address,Surname,contact);
        }
    }
    private void attachEventHandlers() throws NumberFormatException {

//        placeOrder.setOnAction(actionEvent -> {
//            try {
//                AddNewOrder();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        });  // Adding new order
        searchB.setOnAction(actionEvent -> {
            vBoxDetails.getChildren().clear();
            try {
                if (!textS.getText().isEmpty()) {
                    int wanted = Integer.parseInt(textS.getText().toString());
                    searchStudent(wanted);

                    if (studentSearched != null) {
                        Label lblName = new Label("Name    :" + studentSearched.getName());
                        Label Surname = new Label("Surname :" + studentSearched.getSurname());
                        Label contact = new Label("Contact :" + studentSearched.getContact());
                        Label Address = new Label(studentSearched.getAddress());
                        vBoxDetails.getChildren().addAll(
                                lblName,
                                Surname,
                                contact,
                                Address
                        );
                    } else {
                        vBoxDetails.getChildren().addAll(
                                new Label("No results found!")
                        );
                    }
                } else  {
                    vBoxDetails.getChildren().addAll(
                            new Label("Please use the search function to display information on this area")
                    );
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void CreateSecondTable() throws SQLException{
        colItem = new TableColumn("LaundryItems");
        colItem.setCellValueFactory(
                new PropertyValueFactory<Busket,String>("Type")
        );
       // column1.setPrefWidth(150);
        colQuantity = new TableColumn("Quantity");
        colQuantity.setCellValueFactory(
                new PropertyValueFactory<Busket,Integer>("quantity")
        );
        table2.getColumns().addAll(colItem,colQuantity);
        table2.setItems(busket);
    }

    private void makeEditable(){
        column4.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column4.setOnEditCommit(event -> {
            // Typecast event into something more usable.
            TableColumn.CellEditEvent<LaundryItems, Integer> e = (TableColumn.CellEditEvent<LaundryItems, Integer>) event;

            // Get specific contact that was being edited.
            LaundryItems selectedItem = e.getTableView().getSelectionModel().getSelectedItem();
            // Change its quantity based on the checkbox selection (0 or 1).
            selectedItem.setQuantity(e.getNewValue());
            String itemName = selectedItem.getItem_type();
            int quantity = selectedItem.getQuantity();

            Busket existingItem = null;
            for (Busket busketItem : busket) {
                if (busketItem.getType().equals(itemName)) {
                    // Item already exists, update the quantity
                    existingItem = busketItem;
                    break;
                }
            }
            // If the item doesn't exist in the busket and the quantity is greater than 0, add it
            if (existingItem != null) {
                // Update the quantity for the existing item
                if(quantity>0){
                    double total = quantity * selectedItem.getPrice();
                    existingItem.setQuantity(quantity);
                    existingItem.setTotal(total);
                }
                else{
                    busket.remove(existingItem);
                }

            } else if (quantity > 0) {
                double total = quantity * selectedItem.getPrice();
                int id = selectedItem.getItemId();
                Busket newitem = new Busket(total, quantity, itemName,id);
                busket.add(newitem);
            }
            overRollTotal=0;
            for (Busket busketItem : busket) {
                System.out.println("Item: " + busketItem.getType() + " | Quantity: " + busketItem.getQuantity() + " | Total: " + busketItem.getTotal());
                overRollTotal += busketItem.getTotal();
                totalCost.setText("Total Cost : "+ overRollTotal);
            }
        });

    }
//    private void AddNewOrder() throws SQLException {
//        Dba.connectToDB();
//        Statement statement= Dba.getStatement();
//        Date date1 = java.sql.Date.valueOf("12-04-2023") ;
//        Date dateT = java.sql.Date.valueOf("12-04-2023");
//        int studentN = Integer.parseInt(textS.getText());
//        String status = "On Queue";
//        String query = "INSERT INTO LaundryOrder ( DatePlaced, DateTaken, StudentNo, DeliveryID, EmployeeID, LaundryStatus) " +
//                "VALUES ("+date1+","+dateT+","+studentN+",0,24521365,"+status+");";
//        statement.executeQuery(query);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createTable();
            CreateSecondTable();
            attachEventHandlers();
            makeEditable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnCheckBoxClicked(ActionEvent actionEvent) {
        if(makeTbl_additable.isSelected()){
            table1.setEditable(true);
        }
        else{
            table1.setEditable(false);
        }
    }

    public void OnAddButtonClicked(ActionEvent actionEvent) throws SQLException {
        if(!textS.getText().isEmpty() && busket.size()>0){
            Dba.connectToDB();
            Statement statement= Dba.getStatement();
            int studentN = Integer.parseInt(textS.getText());
            Date date1 = java.sql.Date.valueOf("2023-10-29") ;
            Date dateT = java.sql.Date.valueOf("2023-10-29");
            String query = "INSERT INTO LaundryOrder (DatePlaced, DateTaken, StudentNo, DeliveryID, EmployeeID, LaundryStatus, OrderPrice)\n" +
                    "VALUES ('"+date1+"','"+dateT+"', '"+studentN+"','46','24521365','On Queue', '"+overRollTotal+"')";
            statement.executeUpdate(query);
            String query2 = "SELECT TOP 1 OrderNumber\n" +
                    "FROM LaundryOrder\n" +
                    "ORDER BY OrderNumber DESC;";
            ResultSet result = statement.executeQuery(query2);
            int Id = result.getInt("OrderNumber");
            for (Busket busketItem : busket){
                double price = busketItem.getTotal();
                int quantity = busketItem.getQuantity();
                int itemId = busketItem.getItemId();
                String query3 = "INSERT INTO LaundryBasket (Quantity,TotalPrice,ItemID,OrderNumber) VALUES("+quantity+","+price+","+itemId+","+Id+";)";
                statement.executeUpdate(query3);
            }
                navigateToNewScreen(actionEvent,"CustomerManagement.fxml");
        }

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