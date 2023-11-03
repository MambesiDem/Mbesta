package com.example.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Stack;

public class UpdatePriceListController implements Initializable {



    @FXML
    private CheckBox Edit;
    @FXML
    private Button newItem;
    @FXML
    private Button SaveBtn;
    @FXML
    private TableView<ItemList> priceList;
    @FXML
    protected void onEdiButtonClicked() {
        priceList.setEditable(true);
    }
    private Stack<Scene> navigationStack = new Stack();
    ObservableList<ItemList> ItemPriceList= FXCollections.observableArrayList();;
    TableColumn column1;
    TableColumn column2;
//    private ObservableList<ItemList> sharedItemPriceList;

    @FXML
    protected void onBackBtnClicked(ActionEvent actionEvent) throws IOException {

        navigateToNewScreen(actionEvent,"BusinessManagement.fxml");
    }

    public void setSharedItemPriceList(ObservableList<ItemList> sharedItemPriceList) {
        this.ItemPriceList = sharedItemPriceList;
    }
    private void setupTable(){
        column1 = new TableColumn("ItemName");
        column1.setCellValueFactory(
                new PropertyValueFactory<ItemList,String>("itemName")
        );
        column2 = new TableColumn("ItemPrice");
        column2.setCellValueFactory(
                new PropertyValueFactory<ItemList,Double>("price")
        );
        priceList.getColumns().addAll(column1,column2);
        priceList.setItems(ItemPriceList);
    }
    Database Dba;
    private void setupItemList() throws SQLException {
        Dba= new Database();
        Dba.connectToDB();
        Statement statement = Dba.getStatement();
        String query ="Select ItemName,ItemPrice From ItemList";
        ResultSet results = statement.executeQuery(query);
        while (results.next()){
            String itemName = results.getString("ItemName");
            Double Price = results.getDouble("ItemPrice");
            ItemList item = new ItemList(itemName,Price);
            ItemPriceList.add(item);
        }

    }
    private void makeEditable(){
        column2.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column2.setOnEditCommit(event -> {
            // Typecast event into something more usable.
            TableColumn.CellEditEvent<ItemList, Double> e = (TableColumn.CellEditEvent<ItemList, Double>) event;

            // Get specific contact that was being edited.
            ItemList selectedItem = e.getTableView().getSelectionModel().getSelectedItem();
            // Change its quantity based on the checkbox selection (0 or 1).
            selectedItem.setPrice(e.getNewValue());
            Double price = selectedItem.getPrice();
            String name = selectedItem.getItemName();
            Dba= new Database();
            Dba.connectToDB();
            Statement statement = Dba.getStatement();
            String query= "UPDATE ItemList SET ItemPrice = "+price+" Where ItemName ='"+name+"';";
            try {
                statement.execute(query);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setupItemList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (priceList != null) {
            setupTable();
            makeEditable();
        }

    }

//    public void OnButtonDoneClicked(ActionEvent actionEvent) {
//        if(Name_Fld!=null && Price_Fld!=null){
//
//            String itemName = Name_Fld.getText();
//            double price = Double.parseDouble(Price_Fld.getText());
//            ItemList newItem= new ItemList(itemName,price);
//            ItemPriceList.add(newItem);
//            Stage stage = (Stage) doneBtn.getScene().getWindow();
//            stage.close();
//        }
//
//    }
    public void onAddItemClicked(ActionEvent actionEvent) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewItem.fxml"));
//        Parent root = loader.load();
//        Stage childStage = new Stage();
//        childStage.initStyle(StageStyle.UTILITY);
//        childStage.setResizable(false);
//
//        childStage.initModality(Modality.APPLICATION_MODAL);
//        childStage.initOwner(newItem.getScene().getWindow()); // Set the parent stage as the owner
//        childStage.setScene(new Scene(root));
//
//        childStage.showAndWait();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewItem.fxml"));
        Parent root = loader.load();

        // Pass the shared ObservableList to the secondary stage controller
        newItemController newItemController = loader.getController();
        newItemController.setSharedItemPriceList(ItemPriceList);

        Stage childStage = new Stage();
        childStage.initStyle(StageStyle.UTILITY);
        childStage.setResizable(false);
        childStage.initModality(Modality.APPLICATION_MODAL);
        childStage.initOwner(newItem.getScene().getWindow()); // Set the parent stage as the owner
        childStage.setScene(new Scene(root));
        childStage.showAndWait();

    }

    public void onCheckBoxClicked(ActionEvent actionEvent) {
        if(Edit.isSelected()){
            priceList.setEditable(true);
        }
        else{
            priceList.setEditable(false);
        }
    }

    public void OnSaveButtonClicked(ActionEvent actionEvent) {
        //adding to database
        navigateToNewScreen(actionEvent,"BusinessManagement.fxml");
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