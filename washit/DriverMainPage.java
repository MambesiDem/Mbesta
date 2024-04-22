package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class DriverMainPage extends AppCompatActivity implements Subscriber {
    String newOrders = "";

    BottomNavigationView bottomNavigationView;

    int empId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main_page);

        Broker.subscribe(this,"Driver Main Page");

        Client client = new Client("172.20.10.6",
                value -> runOnUiThread(()->severMessageDisplay(value)));
        client.start();

        LinearLayout linearLayout = findViewById(R.id.driverMainPageNavigationView);

        Intent newIntent = getIntent();

        Bundle extras = newIntent.getExtras();
        if(extras!=null){
            empId = extras.getInt("EmployeeID");
        }
        else{
            Toast.makeText(this, "Employee ID not sent", Toast.LENGTH_SHORT).show();
        }

        bottomNavigationView = findViewById(R.id.driverBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            switch (itemId) {
                case R.id.menu_home:
                    // Handle Home option
                    // Implement your logic here
                    return true;

                case R.id.menu_profile:
                    createProfileOptionsLayout(linearLayout);
                    return true;

                case R.id.menu_deliveries:
                    createDeliveryOptionsLayout(linearLayout);
                    return true;

                case R.id.menu_logout:
                    createLogoutLayout(linearLayout);
                    return true;

                case R.id.menu_about:
                    // Handle About option
                    // Implement your logic here
                    return true;
            }

            return false;
        });
    }
    public void severMessageDisplay(String msg){
        MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.menu_deliveries);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.green_dot);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(layoutParams);
        menuItem.setActionView(imageView);

        newOrders = msg;
    }

    public void createDeliveryOptionsLayout(LinearLayout linearLayout){
        linearLayout.removeAllViews();
        TextView orders = new TextView(this);
        orders.setBackgroundColor(Color.WHITE);
        if(newOrders.equals("")){
            orders.setText("No new landry orders available");
            linearLayout.addView(orders);
        }
        else{
            orders.setText(newOrders);
            linearLayout.addView(orders);
        }
    }
    public void createLogoutLayout(LinearLayout linearLayout){
        linearLayout.removeAllViews();

        Button button1 = new Button(this);
        button1.setText("Logout");
        button1.setBackgroundResource(R.drawable.rounded_button);
        button1.setGravity(Gravity.CENTER);
        button1.setTextColor(Color.WHITE);
        onBtnLogoutClicked(button1);


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        int marginInDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        int marginInPixels = (int) (marginInDp * scale + 0.5f);

        buttonParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        buttonParams.gravity = Gravity.CENTER;

        linearLayout.addView(button1, buttonParams);


    }
    public void onBtnLogoutClicked(Button btn){

        btn.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Logout Confirmation");
            alertDialog.setMessage("Are you sure you want to to logout?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
            alertDialog.show();
        });
    }
    public void createProfileOptionsLayout(LinearLayout linearLayout){
        linearLayout.removeAllViews();

        Button button1 = new Button(this);
        button1.setText("View Profile");
        button1.setBackgroundResource(R.drawable.rounded_button);
        button1.setGravity(Gravity.CENTER);
        button1.setTextColor(Color.WHITE);

        Button button2 = new Button(this);
        button2.setText("Edit Profile");
        button2.setBackgroundResource(R.drawable.rounded_button);
        button2.setGravity(Gravity.CENTER);
        button2.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        int marginInDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        int marginInPixels = (int) (marginInDp * scale + 0.5f);

        buttonParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        buttonParams.gravity = Gravity.CENTER;

        linearLayout.addView(button1, buttonParams);
        linearLayout.addView(button2, buttonParams);

        onBtnViewProfileClicked(button1);
        onBtnEditProfileClicked(button2);

    }
    public void onBtnViewProfileClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,viewProfileDriver.class);
            intent.putExtra("EmployeeID",empId);
            startActivity(intent);
        });
    }
    public void onBtnEditProfileClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,editDriverProfile.class);
            startActivity(intent);
        });
    }
    @Override
    public void readPublished(Map.Entry<String, Object>... published) {

    }
}