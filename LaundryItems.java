package com.example.project;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class LaundryItems {
    private Double price;
    private String Item_type;
    private int quantity;
    private int itemId;



    public LaundryItems(Double price, String item_type, int q,int ItemId) {
        this.price= price;
        this.Item_type= item_type;
        this.itemId=ItemId;
        this.quantity=q;
    }
    public int getQuantity() {
        return quantity;
    }
    public Double getPrice() {
        return price;
    }
    public String getItem_type() {
        return Item_type;
    }
    public int getItemId() {
        return itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "LaundryItems{" +
                "price=" + price +
                ", Item_type=" + Item_type +
                '}';
    }
}
