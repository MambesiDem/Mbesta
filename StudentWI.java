package com.example.project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentWI {
   private StringProperty name;
   private StringProperty address;
   private StringProperty surname;
   private IntegerProperty studentNo;
   private StringProperty contact;

    public StudentWI(int studentN, String name, String Address, String surname, String contact) {
        this.studentNo = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.surname = new SimpleStringProperty();
        this.contact = new SimpleStringProperty();
        this.name.set(name);
        this.address.set(Address);
        this.surname.set(surname);
        this.studentNo.set(studentN);
        this.contact.set(contact);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public int getStudentNo() {
        return studentNo.get();
    }

    public IntegerProperty studentNoProperty() {
        return studentNo;
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }
}
