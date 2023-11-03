package com.example.project;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class newItemController implements Initializable {
    @FXML
    private TextField Name_Fld;
    @FXML
    private TextField Price_Fld;
    @FXML
    private TextField cat_ID;
    Database Dba;
    private ObservableList<ItemList> sharedItemPriceList;

    // Setter method to set the shared ObservableList
    public void setSharedItemPriceList(ObservableList<ItemList> sharedItemPriceList) {
        this.sharedItemPriceList = sharedItemPriceList;
    }

    public void OnButtonDoneClicked(ActionEvent actionEvent) throws SQLException {
        if (Name_Fld != null && Price_Fld != null) {
            String itemName = Name_Fld.getText();
            double price = Double.parseDouble(Price_Fld.getText());
            ItemList newItem = new ItemList(itemName,price);
            String category = cat_ID.getText();
            // Add the new item to the shared ObservableList
            sharedItemPriceList.add(newItem);
            Dba= new Database();
            Dba.connectToDB();
            Statement statement = Dba.getStatement();
            String query ="INSERT INTO ItemList (ItemName, ItemPrice,Category) VALUES ('"+itemName+"',"+ price+",'"+category+"');";
            statement.execute(query);
            // Close the secondary stage
            Stage stage = (Stage) Name_Fld.getScene().getWindow();
            stage.close();
        }
        else{
            Stage stage = (Stage) Name_Fld.getScene().getWindow();
            stage.close();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }
}
