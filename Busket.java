package com.example.project;

import java.util.Objects;

public class Busket{
    private Double Total;
    private int quantity;
    private String Type;
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public Busket(Double total, int quantity, String type,int itemId) {
        Total = total;
        this.quantity = quantity;
        Type = type;
        this.itemId=itemId;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Busket otherItem = (Busket) obj;
        return Objects.equals(this.Type, otherItem.Type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Type);
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(String type) {
        Type = type;
    }

    public Double getTotal() {
        return Total;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return Type;
    }
}
