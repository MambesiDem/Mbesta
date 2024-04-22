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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class MakeOnlinePayment extends AppCompatActivity implements Subscriber {

    List<Item> selectedItems = new ArrayList<>();

    Connection connection = null;
    Statement statement = null;
    ImageView correctDetails;
    double currentOrderPrice =0;

    Boolean isValidBankingDetails = false;


    int StudentNo;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_online_payment);

        Broker.subscribe(this,"Make Online Payment");

        correctDetails = findViewById(R.id.correctDetails);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBtnPayClicked(View view) throws SQLException {
        if(isValidBankingDetails){
            createPopup();
        }
        else{
            Toast.makeText(this, "Provide valid banking ", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPopup(){
        Map.Entry<String,Object> selectedItemsOnlinePayment = new AbstractMap.SimpleEntry<>("selectedItemsOnlinePayment",1);
        Broker.publish("Interface",selectedItemsOnlinePayment);

        ScrollablePopupDialog popupDialog = new ScrollablePopupDialog(this);
        popupDialog.show();

        ListView lstOfItems = popupDialog.findViewById(R.id.listOfSelectedItems);

        Map.Entry<String,Object> MakeOnlinePaymentPrice = new AbstractMap.SimpleEntry<>("MakeOnlinePaymentPrice",1);
        Broker.publish("Interface",MakeOnlinePaymentPrice);

        TextView displayTotalPrice = popupDialog.findViewById(R.id.popupTotalPriceConf);
        displayTotalPrice.setText("Total: "+"R"+String.format("%.2f",currentOrderPrice));

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.custom_list_item,
                R.id.textView1,
                selectedItems);

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
                        popupDialog.cancel();
                        Intent intent = new Intent(this,StudentMainPage.class);
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
    public void onBtnSaveClicked(View view) throws SQLException {

        EditText bankName = findViewById(R.id.txtBankName);
        EditText accNumber = findViewById(R.id.txtAccountNumber);
        EditText branchCode = findViewById(R.id.txtBranchCode);
        EditText cardNumber = findViewById(R.id.txtCardNumber);
        EditText cvvNo = findViewById(R.id.txtCvvNumber);

        String bName = bankName.getText().toString();
        String aNumber = accNumber.getText().toString();
        String bCode = branchCode.getText().toString();
        String cNumber = cardNumber.getText().toString();
        String cvv = cvvNo.getText().toString();

        try{
            int intAccNo = Integer.parseInt(aNumber);
            int intBCode = Integer.parseInt(bCode);
            int intCNo = Integer.parseInt(cNumber);
            int intCVV = Integer.parseInt(bCode);

            int studentNo = StudentMainPage.studentNo;

            String result = connectDB();
            String query = "INSERT INTO BankDetails (AccountNo, BankName, CardNumber, BranchCode, CVV,StudentNo)\n" +
                    "VALUES ('"+ aNumber + "','" +bName+"', '"+cNumber +"', '"+ bCode+"', '"+cvv +"','"+studentNo+"');";
            statement.execute(query);
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            view.setVisibility(View.GONE);
            correctDetails.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Banking details successful.", Toast.LENGTH_SHORT).show();
            isValidBankingDetails = true;
        }catch (Exception e){
            isValidBankingDetails = false;
            Toast.makeText(this, "An invalid number is entered.", Toast.LENGTH_SHORT).show();
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
        Map.Entry<String,Object> stOnlinePayment = new AbstractMap.SimpleEntry<>("stOnlinePayment",1);
        Broker.publish("Student Main Page",stOnlinePayment);

        Map.Entry<String,Object> totalPriceNeeded = new AbstractMap.SimpleEntry<>("totalPriceNeededOnlinePayment",1);
        Broker.publish("Interface",totalPriceNeeded);

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
            connection = DatabaseManager.getConnection();

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
            if(msg.getKey().equals("totalPriceNeededOnlinePayment")){
                currentOrderPrice = (double)msg.getValue();
            }
            else if(msg.getKey().equals("studentNumber")){
                StudentNo = (int)msg.getValue();
            }
            else if(msg.getKey().equals("basketItems")){
                selectedItems = (List<Item>) msg.getValue();
            }
            else if(msg.getKey().equals("orderPrice")){
                currentOrderPrice = (double) msg.getValue();
            }
        }
    }
}