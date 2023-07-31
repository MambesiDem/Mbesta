package com.example.washit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First get the number of records from table ItemList
        //Create x tableRows where x is the number of records in ItemList
        //When creating each tableRow also edit the columns values based on Item attributes

        //The following array contains records of ItemList table

        ArrayList<Item> ItemList = new ArrayList<>();
        Item item1 = new Item(001,20.00,"Jacket");
        ItemList.add(item1);
        Item item2 = new Item(002,7.00,"T-Shirt");
        ItemList.add(item2);
        Item item3 = new Item(003,15.00,"Jean");
        ItemList.add(item3);
        Item item4 = new Item(004,30.00,"Blanket");
        ItemList.add(item4);
        Item item5 = new Item(005,0.50,"Socks");
        ItemList.add(item5);
        Item item6 = new Item(006,30.00,"Curtains");
        ItemList.add(item6);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView itemName = new TextView(this);
        TextView itemPrice = new TextView(this);
        EditText quantity = new EditText(this);
        TableRow tableRow = new TableRow(this);


        for(int i=0;i<ItemList.size();i++){
            if(i==0){
                TextView textView1 = findViewById(R.id.itemName1);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice1);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
            else if(i==1){
                TextView textView1 = findViewById(R.id.itemName2);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice2);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
            else if(i==2){
                TextView textView1 = findViewById(R.id.itemName3);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice3);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
            else if(i==3){
                TextView textView1 = findViewById(R.id.itemName4);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice4);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
            else if(i==4){
                TextView textView1 = findViewById(R.id.itemName5);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice5);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
            else{
                TextView textView1 = findViewById(R.id.itemName6);
                textView1.setText(ItemList.get(i).getItemType());
                TextView textView2 = findViewById(R.id.itemPrice6);
                textView2.setText(Double.toString(ItemList.get(i).getItemPrice()));
            }
        }
    }
    public void onCheckBoxClicked(View view){
        CheckBox select1 = findViewById(R.id.select1);
    }
}