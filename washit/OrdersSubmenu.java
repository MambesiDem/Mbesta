package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OrdersSubmenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_submenu);
    }
    public void onPlaceLaundryBtnClicked(View view){
        Intent intent = new Intent(this,PlaceLaundryOrder.class);
        startActivity(intent);
    }
    public void onCheckStatusBtnClicked(View view){

    }
}