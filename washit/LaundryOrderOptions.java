package com.example.washit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class LaundryOrderOptions extends Dialog {

    public LaundryOrderOptions(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.laundry_order_options, null);

        setContentView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }
    public void onPlaceLaundryOrderBtnClicked(View view){
    }
    public void onCheckLaundryOrderStatusBtnClicked(View view){

    }
}
