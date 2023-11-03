package com.example.project;

public class LaundryOrder {
    private String item;
    private int quantity;
//    CheckBox match;
//    public LaundryOrder(String item, int quantity, CheckBox match) {
//        this.item = item;
//        this.quantity = quantity;
//        this.match = match;
//    }

    public LaundryOrder(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

//    public CheckBox getMatch() {
//        return match;
//    }
}
