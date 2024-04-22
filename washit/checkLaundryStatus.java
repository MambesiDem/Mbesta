package com.example.washit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class checkLaundryStatus extends AppCompatActivity implements Subscriber {

    Connection connection = null;

    Statement statement = null;

    int StudentNo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_laundry_status);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Broker.subscribe(this,"Make Online Payment");

        TextView txtStatus = findViewById(R.id.lblLaundryStatus);
        Button bckBtn = findViewById(R.id.btnEDBack);

        String result = null;
        connection = LoginActivity.connection;

        try {
            if (connection != null) {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);


                Map.Entry<String,Object> stOnlinePayment = new AbstractMap.SimpleEntry<>("stOnlinePayment",1);
                Broker.publish("Student Main Page",stOnlinePayment);

                ResultSet resultSet = statement.executeQuery("SELECT * " +
                        "FROM LaundryOrder " +
                        "WHERE StudentNo="+StudentNo);

                String res=null;
                while(resultSet.next()){

                    res += resultSet.getString("LaundryStatus");
                }

                txtStatus.setText(res);


                result = "Connection successful!";
            } else {
                result = "Connection failed!";
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        }
    @Override
    public void readPublished(Map.Entry<String, Object>... published) {
        for(Map.Entry<String, Object> msg: published) {
            if (msg.getKey().equals("studentNumber")) {
                StudentNo = (int) msg.getValue();
            }
        }
    }

    public void onVPBackClicked(View view) {
        Intent intent = new Intent(this,StudentMainPage.class);
        intent.putExtra("StudentNumber",StudentNo);
        startActivity(intent);
    }
}