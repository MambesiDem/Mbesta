package com.example.project;

public class StudentOrder {
    int OrderNumber;
    String StudName;
    String StudSurname;
    String status;

    public StudentOrder(int orderNumber, String studName, String studSurname) {
        OrderNumber = orderNumber;
        StudName = studName;
        StudSurname = studSurname;
    }

    public int getOrderNumber() {
        return OrderNumber;
    }

    public String getStudName() {
        return StudName;
    }

    public String getStudSurname() {
        return StudSurname;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    public void setStudName(String studName) {
        StudName = studName;
    }

    public void setStudSurname(String studSurname) {
        StudSurname = studSurname;
    }
}
