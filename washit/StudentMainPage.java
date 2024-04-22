package com.example.washit;

//import static android.os.Build.VERSION_CODES.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Map;

public class StudentMainPage extends AppCompatActivity implements Subscriber {

    static int studentNo;

    Bundle extras;

    Connection connection;
    Statement statement;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");

        try {
            connection = LoginActivity.connection;
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent1 = getIntent();
        extras=intent1.getExtras();
        if(extras!=null){
            studentNo = extras.getInt("StudentNumber");
        }
        else{
            Toast.makeText(this, "StudentNumber is not sent", Toast.LENGTH_SHORT).show();
        }
        Broker.subscribe(this,"Student Main Page");

        LinearLayout linearLayout = findViewById(R.id.driverMainPageNavigationView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.driverBottomNavigationView);
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

                case R.id.menu_laundry_orders:
                    createLaundryOrderOptionsLayout(linearLayout);
                    return true;

                case R.id.menu_logout:
                    createLogoutLayout(linearLayout);
                    return true;

                case R.id.menu_comment_rate:
                    linearLayout.removeAllViews();
                    new CommentSection(this,linearLayout);
//                    createCommentRateLayout(linearLayout);
//                    Intent intent = new Intent(this,CommentAndRate.class);
//                    startActivity(intent);
                    return true;
            }

            return false;
        });

    }
    public void createCommentRateLayout(LinearLayout linearLayout){
        linearLayout.removeAllViews();
        //TextView commRateTxt = findViewById(R.id.txtCommRate);
        TextInputLayout txtLayout = new TextInputLayout(this);
        Button submitBtn = new Button(this);
        //linearLayout.addView(commRateTxt);
        linearLayout.addView(txtLayout);
        linearLayout.addView(submitBtn);
    }
    public void createLaundryOrderOptionsLayout(LinearLayout linearLayout){
        linearLayout.removeAllViews();

        Button button1 = new Button(this);
        button1.setText("Place Laundry Order");
        button1.setBackgroundResource(R.drawable.rounded_button);
        button1.setGravity(Gravity.CENTER);
        button1.setTextColor(Color.WHITE);
        onBtnPlaceLandryOrderClicked(button1);

        Button button2 = new Button(this);
        button2.setText("Check Laundry Status");
        button2.setBackgroundResource(R.drawable.rounded_button);
        button2.setGravity(Gravity.CENTER);
        button2.setTextColor(Color.WHITE);
        onBtnCheckLandryStatusClicked(button2);

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
    public void onBtnLogoutClicked(Button btn){

        btn.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Logout Confirmation");
            alertDialog.setMessage("Are you sure you want to to logout?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(this,LoginActivity.class);
                        startActivity(intent);
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                    (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
            alertDialog.show();
        });
    }
    public void onBtnPlaceLandryOrderClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,PlaceLaundryOrder.class);
            intent.putExtra("StudentNumber",studentNo);
            startActivity(intent);
        });
    }
    public void onBtnCheckLandryStatusClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,checkLaundryStatus.class);
            intent.putExtra("StudentNumber",studentNo);
            startActivity(intent);
        });
    }
    public void onBtnViewProfileClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,ViewProfile.class);
            intent.putExtra("StudentNumber",studentNo);
            startActivity(intent);
        });
    }
    public void onBtnEditProfileClicked(Button btn){
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,EditProfile.class);
            startActivity(intent);
        });
    }

    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published){
            if(msg.getKey().equals("studentNumberNeeded")){
                Map.Entry<String,Object> studentNumber = new AbstractMap.SimpleEntry<>("studentNumber",studentNo);
                Broker.publish("Choose Payment Method",studentNumber);
            }
            else if(msg.getKey().equals("stOnlinePayment")){
                Map.Entry<String,Object> studentNumber = new AbstractMap.SimpleEntry<>("studentNumber",studentNo);
                Broker.publish("Make Online Payment",studentNumber);
            }
            else if(msg.getKey().equals("stNumberNeeded")){
                Map.Entry<String,Object> studentNumber = new AbstractMap.SimpleEntry<>("studentNumber",studentNo);
                Broker.publish("Comment and rate",studentNumber);
            }
        }
    }
}