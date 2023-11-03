package com.example.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LaundryOrders {
    private IntegerProperty orderId = new SimpleIntegerProperty();

    public LaundryOrders(int ID){
        this.orderId.set(ID);
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public int getOrderId() {
        return orderId.get();
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }
}

