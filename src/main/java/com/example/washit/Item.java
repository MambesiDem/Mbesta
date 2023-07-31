package com.example.washit;

public class Item {
    private int ItemId;
    private double ItemPrice;
    private String ItemType;

    public Item(int itemId, double itemPrice, String itemType) {
        ItemId = itemId;
        ItemPrice = itemPrice;
        ItemType = itemType;
    }

    public int getItemId() {
        return ItemId;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public String getItemType() {
        return ItemType;
    }
}
