package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChoosePaymentMethod extends AppCompatActivity implements Subscriber {

    ArrayList<Item> selectedItems = new ArrayList<>();
    private Bundle extras;

    Connection connection = null;
    Statement statement = null;

    int StudentNo =0;

    ArrayAdapter adapter;

    double currentOrderPrice;

    List<Item> listSelectedItems = new ArrayList<>();

    ListView lstOfItems;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_method);

        Broker.subscribe(this,"Choose Payment Method");

        Intent intent = getIntent();
        if(intent !=null){
            extras = intent.getExtras();

            if(extras!=null){
                selectedItems = (ArrayList<Item>) extras.getSerializable("SelectedItems");
            }else{
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBtnNextClicked(View view){
        RadioButton online = findViewById(R.id.online);

        RadioButton onDelivery = findViewById(R.id.onDelivery);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId == R.id.online) {
            Map.Entry<String,Object> studentNumberNeeded = new AbstractMap.SimpleEntry<>("studentNumberNeeded",1);
            Broker.publish("Student Main Page",studentNumberNeeded);
            Intent intent = new Intent(this,ChooseBankDetails.class);
            intent.putExtra("StudentNumber",StudentNo);
            startActivity(intent);
        } else if (selectedRadioButtonId == R.id.onDelivery) {
            Map.Entry<String,Object> basketItems = new AbstractMap.SimpleEntry<>("basketItems",1);
            Broker.publish("Interface",basketItems);

            ScrollablePopupDialog popupDialog = new ScrollablePopupDialog(this);
            popupDialog.show();

            lstOfItems = popupDialog.findViewById(R.id.listOfSelectedItems);

            Map.Entry<String,Object> totalPriceNeeded = new AbstractMap.SimpleEntry<>("totalPriceNeeded",1);
            Broker.publish("Interface",totalPriceNeeded);

            TextView displayTotalPrice = popupDialog.findViewById(R.id.popupTotalPriceConf);
            displayTotalPrice.setText("Total: "+"R"+String.format("%.2f",currentOrderPrice));

            adapter = new ArrayAdapter<>(this, R.layout.custom_list_item,
                    R.id.textView1,
                    listSelectedItems);

            lstOfItems.setAdapter(adapter);

            Button placeBtn = popupDialog.findViewById(R.id.checkLaundryStatusBtn);
            Button cancelBtn = popupDialog.findViewById(R.id.placeLaundryOrderBtn);

            cancelBtn.setOnClickListener(x->{
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Confirm Cancellation");
                alertDialog.setMessage("Are you sure you want to cancel the process?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            Map.Entry<String,Object> studentNumberNeeded = new AbstractMap.SimpleEntry<>("studentNumberNeeded",1);
                            Broker.publish("Student Main Page",studentNumberNeeded);
                            popupDialog.cancel();
                            Intent intent = new Intent(this,StudentMainPage.class);
                            intent.putExtra("StudentNumber",StudentNo);
                            startActivity(intent);
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });
                alertDialog.show();
            });


            placeBtn.setOnClickListener(x->{
                try {
                    connectDB();
                    createDeliveryRecord();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                popupDialog.cancel();

                    AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
                    alertDialog1.setTitle("Laundry Placement Complete");
                    alertDialog1.setMessage(getString(R.string.successMessage));
                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                        (dialogInterface1, i2) -> {
                            Intent intent = new Intent(this,StudentMainPage.class);
                            intent.putExtra("StudentNumber",StudentNo);
                            startActivity(intent);
                        });
                    alertDialog1.show();
            });

        }
        else{
            Toast.makeText(this, "Choose Payment Method.", Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createLaundryBasketRecord(int OrderNumber) throws SQLException {

        double Total = 0;
        int Quantity = 0;
        int ItemID = 0;
        for(Item item:selectedItems){
            ItemID = item.getItemId();
            Quantity = item.getQuantity();
            Total = Quantity*item.getItemPrice();
            String query = "INSERT INTO LaundryBasket(Quantity,TotalPrice,ItemID,OrderNumber)"+"\n"
                    +"VALUES('"+Quantity+"','"+Total+"','"+ItemID+"','"+OrderNumber+"');";
            statement.execute(query);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createLaundryOrderRecord(int DeliveryID) throws SQLException {

        LocalDate currentDate = LocalDate.now();
        String DataPlaced = currentDate.toString();
        String DateTaken = "not Taken";

        Map.Entry<String,Object> studentNumberNeeded = new AbstractMap.SimpleEntry<>("studentNumberNeeded",1);
        Broker.publish("Student Main Page",studentNumberNeeded);

        int EmployeeID = 24521365;
        String LaundryStatus = "Not arrived yet.";

        String query = "INSERT INTO LaundryOrder(DatePlaced,DateTaken,StudentNo,DeliveryID,EmployeeID,LaundryStatus,OrderPrice)"+"\n"
                +"VALUES('"+DataPlaced+"','"+DateTaken+"','"+StudentNo+"','"+DeliveryID+"','"+EmployeeID+"','"+LaundryStatus+"','"+currentOrderPrice+"');";
        statement.execute(query,Statement.RETURN_GENERATED_KEYS);

        ResultSet set = statement.getGeneratedKeys();
        set.next();
        int OrderNumber = set.getInt(1);
        createLaundryBasketRecord(OrderNumber);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createDeliveryRecord() throws SQLException {
        int empId = 1;
        double cost = 0;
        String query = "INSERT INTO Delivery(EmployeeID,Cost)"+"\n"
                +"VALUES('1','0');";

        statement.execute(query,Statement.RETURN_GENERATED_KEYS);

        ResultSet set = statement.getGeneratedKeys();
        set.next();
        int DeliveryID = set.getInt(1);
        createLaundryOrderRecord(DeliveryID);

    }
    private String connectDB() throws SQLException {
        String result = null;
        Log.d("database","debugging");
        try {
            // Connect to the database
            connection = LoginActivity.connection;

            if (connection != null) {
                statement = LoginActivity.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                result = "Connection successful!";
            } else {
                result = "Connection failed!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "Connection error: " + e.getMessage();
        } finally {
            // Close the resources

        }

        return result;
    }


    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published){
            if(msg.getKey().equals("basketItems")){
                listSelectedItems = (List<Item>) msg.getValue();
            }
            else if(msg.getKey().equals("orderPrice")){
                currentOrderPrice = (double)msg.getValue();
            }
            else if(msg.getKey().equals("studentNumber")){
                StudentNo = (int)msg.getValue();
            }
        }
    }
}