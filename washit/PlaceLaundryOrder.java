package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class PlaceLaundryOrder extends AppCompatActivity implements Subscriber {

    double totalPrice = 0;

    String studentNo;

    ArrayList<Item> ItemList = new ArrayList<>();

    ArrayList<Item> selectedItems = new ArrayList<>();

    ArrayAdapter adapter;

    ListView list;

    Connection connection = null;
    Statement statement = null;

    Spinner filterSpinner;
    CustomTableAdapter tableAdapter;

    TableLayout tableLayout;
    TextView total;

    LinearLayout wholeScreen;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_laundry_order);

        wholeScreen = findViewById(R.id.wholeScreen);

        try {
            Toast.makeText(this, populateTable(),Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Broker.subscribe(this, "Interface");

        list = findViewById(R.id.selectedItems);
        total = findViewById(R.id.txtTotalPrice);
        tableLayout = findViewById(R.id.tableLayout);

        filterSpinner = findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.filter_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    selectedItems);

        tableAdapter = new CustomTableAdapter(this,ItemList,adapter,list,selectedItems,total);
        tableAdapter.addRowsToTableLayout(tableLayout,ItemList);

        EditText txtSearch = findViewById(R.id.txtsearch);
        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Clear the text in the EditText
                    txtSearch.setText("");
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterTableData(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //filterTableData(editable.toString().toLowerCase());
            }
        });
    }

    private void filterTableData(String query) {
        ArrayList<Item> filteredData = new ArrayList<>();

        if (query.isEmpty()) {
            // If the search query is empty, show all data (reset to original state)
            filteredData.addAll(ItemList);
        } else {
            // Apply filtering logic based on the search query
            String selectedFilter = filterSpinner.getSelectedItem().toString();

            if (selectedFilter.equals("Filter by Category")) {
                // Filter by category logic here
                for (Item item : ItemList) {
                    if (item.getCategory().toLowerCase().contains(query)) {
                        filteredData.add(item);
                    }
                }
            } else if (selectedFilter.equals("Search by Item Name")) {
                // Search by item name logic here
                for (Item item : ItemList) {
                    if (item.getItemName().toLowerCase().contains(query)) {
                        filteredData.add(item);
                    }
                }
            }
        }
        // Update your table with the filtered data
        tableAdapter.updateTableWithFilteredData(filteredData,tableLayout);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSearchBtnClicked(View view){

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1,
                selectedItems);
        tableAdapter = new CustomTableAdapter(this,ItemList,adapter,list,selectedItems,total);
        tableAdapter.addRowsToTableLayout(tableLayout,ItemList);

    }
    public void onBtnPlaceOrderClicked(View view){
        if(!tableAdapter.getSelectedItems().isEmpty() && totalPrice>50){
            Intent intent = new Intent(this,ChoosePaymentMethod.class);
            intent.putExtra("SelectedItems",selectedItems);
            startActivity(intent);
        }
        else{
            if(tableAdapter.getSelectedItems().isEmpty()){
                Toast.makeText(this, R.string.noItemSelected, Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, R.string.priceLessThan50, Toast.LENGTH_SHORT).show();
            }
        }
    }
//    public void populateItemList(ResultSet resultSet) throws SQLException {
//        while(resultSet.next()){
//
//            int itemId = resultSet.getInt("ItemID");
//            double itemPrice = resultSet.getDouble("ItemPrice");
//            String itemName = resultSet.getString("ItemName");
//            String category = resultSet.getString("Category");
//
//            Item item = new Item(itemId,itemPrice, itemName,0,category,false);
//
//            ItemList.add(item);
//        }
//    }

    private void disableAllViewsExceptEditTextAndCheckBox(ViewGroup parent) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            child.setEnabled(false);

            // If the child is a ViewGroup, recursively disable its children
            if (child instanceof ViewGroup) {
                disableAllViewsExceptEditTextAndCheckBox((ViewGroup) child);
            }
        }
    }
    private void enableAllViewsExceptEditTextAndCheckBox(ViewGroup parent) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            child.setEnabled(true);

            // If the child is a ViewGroup, recursively disable its children
            if (child instanceof ViewGroup) {
                enableAllViewsExceptEditTextAndCheckBox((ViewGroup) child);
            }
        }
    }
    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published){
            if(msg.getKey().equals("disableViews")){
                disableAllViewsExceptEditTextAndCheckBox(wholeScreen);
            }
            else if(msg.getKey().equals("enableViews")){
                enableAllViewsExceptEditTextAndCheckBox(wholeScreen);
            }
            else if(msg.getKey().equals("totalPrice")){
                totalPrice = (double)msg.getValue();
            }
            else if(msg.getKey().equals("basketItems")){
                Map.Entry<String,Object> basketItems = new AbstractMap.SimpleEntry<>("basketItems",tableAdapter.getSelectedItems());
                Broker.publish("Choose Payment Method",basketItems);
            }
            else if(msg.getKey().equals("fromChooseBankingDetails")){
                Map.Entry<String,Object> basketItems = new AbstractMap.SimpleEntry<>("basketItems",tableAdapter.getSelectedItems());
                Broker.publish("Choose Banking Details",basketItems);
            }
            else if(msg.getKey().equals("totalPriceNeeded")){
                Map.Entry<String,Object> orderPrice = new AbstractMap.SimpleEntry<>("orderPrice",totalPrice);
                Broker.publish("Choose Payment Method",orderPrice);
            }
            else if(msg.getKey().equals("totalPriceNeededOnlinePayment")){
                Map.Entry<String,Object> orderPrice = new AbstractMap.SimpleEntry<>("totalPriceNeededOnlinePayment",totalPrice);
                Broker.publish("Make Online Payment",orderPrice);
            }
            else if(msg.getKey().equals("bankDetailsPrice")){
                Map.Entry<String,Object> orderPrice = new AbstractMap.SimpleEntry<>("orderPrice",totalPrice);
                Broker.publish("Choose Banking Details",orderPrice);
            }
            else if(msg.getKey().equals("selectedItemsOnlinePayment")){
                Map.Entry<String,Object> basketItems = new AbstractMap.SimpleEntry<>("basketItems",tableAdapter.getSelectedItems());
                Broker.publish("Make Online Payment",basketItems);
            }
            else if(msg.getKey().equals("MakeOnlinePaymentPrice")){
                Map.Entry<String,Object> orderPrice = new AbstractMap.SimpleEntry<>("orderPrice",totalPrice);
                Broker.publish("Make Online Payment",orderPrice);
            }
        }
    }

    private String populateTable() throws SQLException {
        String result = null;
        if (LoginActivity.connection != null) {
            statement = LoginActivity.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM ItemList");
            while(resultSet.next()){

                int itemId = resultSet.getInt("ItemID");
                double itemPrice = resultSet.getDouble("ItemPrice");
                String itemName = resultSet.getString("ItemName");
                String category = resultSet.getString("Category");

                Item item = new Item(itemId,itemPrice, itemName,0,category,false);

                ItemList.add(item);
            }

            result = "Connection successful!";
        } else {
            result = "Connection failed!";
        }
        statement.close();
        return result;
//        String result = null;
//        Log.d("database","debugging");
//        try {
//            // Connect to the database
//            connection = DatabaseManager.getConnection();
//
//            if (connection != null) {
//                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM ItemList");
//                while(resultSet.next()){
//
//                    int itemId = resultSet.getInt("ItemID");
//                    double itemPrice = resultSet.getDouble("ItemPrice");
//                    String itemName = resultSet.getString("ItemName");
//                    String category = resultSet.getString("Category");
//
//                    Item item = new Item(itemId,itemPrice, itemName,0,category,false);
//
//                    ItemList.add(item);
//                }
//
//                result = "Connection successful!";
//            } else {
//                result = "Connection failed!";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            result = "Connection error: " + e.getMessage();
//        } finally {
//            // Close the resources
//            try {
//                if (statement != null)
//                    statement.close();
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return result;
    }
}