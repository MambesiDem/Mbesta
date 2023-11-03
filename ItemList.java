package com.example.project;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemList {
    private StringProperty itemName;
    private DoubleProperty price;

    public ItemList(String itemName, Double price) {
        this.itemName = new SimpleStringProperty(itemName);
        this.price = new SimpleDoubleProperty(price);
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}