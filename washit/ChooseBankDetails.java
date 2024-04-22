package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ChooseBankDetails extends AppCompatActivity implements Subscriber {

    Connection connection = null;
    Statement statement = null;
    ArrayList<BankDetails> bankDetailsList = new ArrayList<>();
    double currentOrderPrice = 0;
    List<Item> listSelectedItems = new ArrayList<>();

    int studentNo;

    Bundle extras;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank_details);

        Broker.subscribe(this, "Choose Banking Details");

        Intent newIntent = getIntent();
        extras = newIntent.getExtras();
        if(extras!=null){
            studentNo = extras.getInt("StudentNumber");
        }

        try {
            Toast.makeText(this, connectDB(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT * FROM BankDetails " +
                "WHERE StudentNo="+StudentMainPage.studentNo+ ";";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                int AccountNo = resultSet.getInt("AccountNo");
                String BankName = resultSet.getString("BankName");
                int CardNumber = resultSet.getInt("CardNumber");
                int BranchCode = resultSet.getInt("BranchCode");
                int cvv = resultSet.getInt("CVV");
                int BankID = resultSet.getInt("BankID");
                int StudentNo = resultSet.getInt("StudentNo");
                BankDetails bankDetails = new BankDetails(AccountNo,BankName,CardNumber,BranchCode,cvv,BankID,StudentNo);
                bankDetailsList.add(bankDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayAdapter<BankDetails> adapter = new ArrayAdapter<>(this, R.layout.custom_list_item,
                R.id.textView1,
                bankDetailsList);

        ListView lstOfBanks = findViewById(R.id.listOfBankingDetails);
        TextView txtNoBanks = findViewById(R.id.txtResults);
        TextView selectStatement = findViewById(R.id.textView9);
        if(adapter.getCount()<1){
            selectStatement.setTextColor(Color.TRANSPARENT);
            txtNoBanks.setText(R.string.noBanks);
        }
        else{
            lstOfBanks.setAdapter(adapter);
            lstOfBanks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BankDetails selected = adapter.getItem(i);
                    AlertDialog alertDialog1 = new AlertDialog.Builder(ChooseBankDetails.this).create();
                    alertDialog1.setTitle("Confirm banking details.");
                    alertDialog1.setMessage(selected.getBankName()+" "+selected.getCardNumber());
                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                            (dialogInterface1, i2) -> {
                                dialogInterface1.dismiss();
                                createPopup();
                            });
                    alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            (dialogInterface1, i2) -> {
                                dialogInterface1.dismiss();
                            });
                    alertDialog1.show();
                }
            });
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPopup(){
        Map.Entry<String,Object> basketItems = new AbstractMap.SimpleEntry<>("fromChooseBankingDetails",1);
        Broker.publish("Interface",basketItems);

        ScrollablePopupDialog popupDialog = new ScrollablePopupDialog(this);
        popupDialog.show();

        ListView lstOfItems = popupDialog.findViewById(R.id.listOfSelectedItems);

        Map.Entry<String,Object> bankDetailsPrice = new AbstractMap.SimpleEntry<>("bankDetailsPrice",1);
        Broker.publish("Interface",bankDetailsPrice);

        TextView displayTotalPrice = popupDialog.findViewById(R.id.popupTotalPriceConf);
        displayTotalPrice.setText("Total: "+"R"+String.format("%.2f",currentOrderPrice));

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.custom_list_item,
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
                        popupDialog.cancel();
                        Intent intent = new Intent(this,StudentMainPage.class);
                        intent.putExtra("StudentNumber",studentNo);
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
                        intent.putExtra("StudentNumber",studentNo);
                        startActivity(intent);
                    });
            alertDialog1.show();
        });
    }
    public void onBtnAddNewClicked(View view){

        Intent intent = new Intent(this,MakeOnlinePayment.class);

        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createLaundryBasketRecord(int OrderNumber) throws SQLException {

        double Total = 0;
        int Quantity = 0;
        int ItemID = 0;
        for(Item item:listSelectedItems){
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

        Map.Entry<String,Object> totalPriceNeeded = new AbstractMap.SimpleEntry<>("totalPriceNeeded",1);
        Broker.publish("Interface",totalPriceNeeded);

        int EmployeeID = 24521365;
        String LaundryStatus = "Not arrived yet.";

        String query = "INSERT INTO LaundryOrder(DatePlaced,DateTaken,StudentNo,DeliveryID,EmployeeID,LaundryStatus,OrderPrice)"+"\n"
                +"VALUES('"+DataPlaced+"','"+DateTaken+"','"+studentNo+"','"+DeliveryID+"','"+EmployeeID+"','"+LaundryStatus+"','"+currentOrderPrice+"');";
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
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

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
        }
    }
}