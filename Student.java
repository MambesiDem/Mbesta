package com.example.project;

import java.util.ArrayList;

public class Student {
    private String StudentName;
    private String StudentSurname;
    private int StudentNo;
    private String sResidentialAddress;
    private String sContact;
    private String Password;
    private int BankId;

    public String getStudentSurname() {
        return StudentSurname;
    }

    public Student(String studentName, int studentNo, String sResidentialAddress,
                   String sContact, String password, String studentSurname) {
        StudentName = studentName;
        StudentNo = studentNo;
        this.sResidentialAddress = sResidentialAddress;
        this.sContact = sContact;
        Password = password;
        this.StudentSurname = studentSurname;
    }

    public String getStudentName() {
        return StudentName;
    }

    public int getStudentNo() {
        return StudentNo;
    }

    public String getsResidentialAddress() {
        return sResidentialAddress;
    }

    public String getsContact() {
        return sContact;
    }

    public String getPassword() {
        return Password;
    }


    public int getBankId() {
        return BankId;
    }

    @Override
    public String toString() {
        ArrayList<String> separateRes = new ArrayList<>();
        String s="";
        String newRes="";
        for(int i=0;i<sResidentialAddress.length();i++){
            if(sResidentialAddress.charAt(i) == ','){
                separateRes.add(s);
                s="";
                i++;
            }
            s += sResidentialAddress.charAt(i);
        }
        for(int i=0;i<separateRes.size();i++){
            if(i>0){
                newRes += "\t\t\t\t "+separateRes.get(i) + "\n";
            }
            else{
                newRes += separateRes.get(i) + "\n";
            }
        }
        return "Name: " + StudentName + " \n" +
                "Surname: "+ StudentSurname+"\n"+
                "Student Number: " + StudentNo + " \n"+
                "Residential Address: " + String.format("%5s",newRes)  +
                "Contact Number: " + sContact;
    }
}
