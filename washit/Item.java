package com.example.washit;

import java.io.Serializable;

public class Item implements Serializable {
    private int ItemId;
    private double ItemPrice;
    private String ItemName;
    private boolean isSelected;
    private String category;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCategory() {
        return category;
    }

    private int Quantity;

    public Item(int itemId, double itemPrice, String itemName, int quantity, String category, boolean isSelected) {
        String s1 = "";
        for(int i = 0; i< itemName.length(); i++){
            if(itemName.charAt(i)!=' '){
                s1 += itemName.charAt(i);
            }
        }
        String s2 = "";
        for(int i = 0; i< category.length(); i++){
            if(category.charAt(i)!=' '){
                s2 += category.charAt(i);
            }
        }
        ItemId = itemId;
        ItemPrice = itemPrice;
        ItemName = s1;
        Quantity = quantity;
        this.isSelected = isSelected;
        this.category = s2;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getItemId() {
        return ItemId;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public int getQuantity() {
        return Quantity;
    }

    @Override
    public String toString() {
        ItemName.trim();
        String s = "";
        for(int i = 0; i< ItemName.length(); i++){
            if(ItemName.charAt(i)!=' '){
                s+= ItemName.charAt(i);
            }
        }
        if(Quantity >1 && s.charAt(s.length()-1)!='s'){
            return Quantity +" "+ s+"s";
        }
        else{
            return Quantity +" "+ ItemName;
        }
    }
}
