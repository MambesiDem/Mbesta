package com.example.washit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomTableAdapter extends BaseAdapter implements Subscriber {
    private Context context;
    private ArrayList<Item> tableDataList;
    private ArrayAdapter adapter;
    private ArrayList<Item> selectedItems;
    private ListView list;
    private TextView totalPrice;
    int newQuantity = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CustomTableAdapter(Context context, ArrayList<Item> tableDataList, ArrayAdapter adapter, ListView list,
                              ArrayList<Item> selectedItems, TextView totalPrice) {
        this.context = context;
        this.tableDataList = tableDataList;
        this.adapter = adapter;
        this.list = list;
        this.selectedItems = selectedItems;
        this.totalPrice=totalPrice;

        context.getApplicationContext();

        Broker.subscribe(this,"Manipulate");
    }

    @Override
    public int getCount() {
        return tableDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_row_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemNameTextView = convertView.findViewById(R.id.itemName1);
            viewHolder.itemPriceTextView = convertView.findViewById(R.id.itemPrice1);
            viewHolder.quantityEditText = convertView.findViewById(R.id.itemQuantity1);
            viewHolder.selectCheckBox = convertView.findViewById(R.id.select1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(tableDataList.get(position).getQuantity()>0){
            viewHolder.selectCheckBox.setChecked(true);
        }

        Item rowData = tableDataList.get(position);
        viewHolder.itemNameTextView.setText(rowData.getItemName());
        viewHolder.itemPriceTextView.setText(String.format("%.2f",rowData.getItemPrice()));
        viewHolder.quantityEditText.setText(String.valueOf(rowData.getQuantity()));
        viewHolder.selectCheckBox.setChecked(rowData.isSelected());

        viewHolder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                    if(isChecked){
                        Map.Entry<String,Object> disableViews = new AbstractMap.SimpleEntry<>("disableViews",1);
                        Broker.publish("Interface",disableViews);

                        rowData.setSelected(true);
                        viewHolder.quantityEditText.setFocusable(true);
                        viewHolder.quantityEditText.setFocusableInTouchMode(true);
                        viewHolder.quantityEditText.setEnabled(true);
                        viewHolder.quantityEditText.setText("");
                        viewHolder.quantityEditText.requestFocus();

                        viewHolder.selectCheckBox.setEnabled(true);

                        selectedItems.add(tableDataList.get(position));

                        list.setAdapter(adapter);

                        viewHolder.quantityEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                try{
                                    viewHolder.quantityEditText.setTextColor(Color.WHITE);
                                    newQuantity = Integer.parseInt(charSequence.toString());
                                    tableDataList.get(position).setQuantity(newQuantity);
                                    if(newQuantity>0){
                                        Map.Entry<String,Object> enableViews = new AbstractMap.SimpleEntry<>("enableViews",1);
                                        Broker.publish("Interface",enableViews);
                                        list.setAdapter(adapter);
                                        calculateAndDisplayTotal(selectedItems,totalPrice);
                                        notifyDataSetChanged();
                                    }
                                    else{
                                        Map.Entry<String,Object> disableViews = new AbstractMap.SimpleEntry<>("disableViews",1);
                                        Broker.publish("Interface",disableViews);

                                        viewHolder.quantityEditText.setFocusable(true);
                                        viewHolder.quantityEditText.setFocusableInTouchMode(true);
                                        viewHolder.quantityEditText.setEnabled(true);
                                        viewHolder.quantityEditText.requestFocus();
                                        viewHolder.selectCheckBox.setEnabled(true);
                                    }
                                }
                                catch (Exception e){
                                    if(!viewHolder.quantityEditText.getText().toString().equals("0")){
                                        viewHolder.quantityEditText.setTextColor(Color.RED);
                                        Map.Entry<String,Object> disableViews = new AbstractMap.SimpleEntry<>("disableViews",1);
                                        Broker.publish("Interface",disableViews);
                                        viewHolder.quantityEditText.setFocusable(true);
                                        viewHolder.quantityEditText.setFocusableInTouchMode(true);
                                        viewHolder.quantityEditText.setEnabled(true);
                                        viewHolder.quantityEditText.requestFocus();
                                        viewHolder.selectCheckBox.setEnabled(true);
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });
                    }
                    else{
                        rowData.setSelected(false);
                        selectedItems.remove(tableDataList.get(position));
                        tableDataList.get(position).setQuantity(0);

                        viewHolder.quantityEditText.setText("0");
                        viewHolder.quantityEditText.setFocusable(false);
                        viewHolder.quantityEditText.setFocusableInTouchMode(false);

                        list.setAdapter(adapter);
                        calculateAndDisplayTotal(selectedItems,totalPrice);
                        Map.Entry<String,Object> enableViews = new AbstractMap.SimpleEntry<>("enableViews",1);
                        Broker.publish("Interface",enableViews);
                    }
                }
            });

        return convertView;
    }

    @Override
    public void readPublished(Map.Entry<String, Object>... published) {

    }

    static class ViewHolder {
        TextView itemNameTextView;
        TextView itemPriceTextView;
        EditText quantityEditText;
        CheckBox selectCheckBox;
    }
    public void addRowsToTableLayout(TableLayout tableLayout, ArrayList<Item> filteredData) {
        tableDataList = filteredData;
        for (int i = 0; i < filteredData.size(); i++) {
            //Item rowData = filteredData.get(i);

            View rowView = getView(i, null, null);
            TableRow tableRow = new TableRow(context);
            tableRow.addView(rowView);
            tableLayout.addView(tableRow);
        }
    }
    public void updateTableWithFilteredData(ArrayList<Item> filteredData, @NonNull TableLayout tableLayout) {
        tableLayout.removeAllViews();

        // Add new rows with the filtered data
        addRowsToTableLayout(tableLayout,filteredData);

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }
    public void calculateAndDisplayTotal(ArrayList<Item> items,TextView txtTotal){
        double total =0 ;
        for(Item i:items){
            total += i.getQuantity()*i.getItemPrice();
        }
        txtTotal.setText("R"+String.format("%.2f",total));
        Map.Entry<String,Object> totalPrice = new AbstractMap.SimpleEntry<>("totalPrice",total);
        Broker.publish("Interface",totalPrice);
        Broker.publish("Choose Payment Method",totalPrice);
    }
    public List<Item> getSelectedItems(){
        int size = selectedItems.size();
        for(int i=0;i<size;i++){
            if(selectedItems.get(i).getQuantity()==0)selectedItems.remove(i);
        }
        return selectedItems;
    }
}
