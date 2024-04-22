package com.example.washit;

public class BankDetails {
    private int AccountNo;
    private String BankName;
    private int CardNumber;
    private int BranchCode;
    private int cvv;
    private int BankId;
    private int StudentNo;

    @Override
    public String toString() {
        return "Acc No:       "+ AccountNo + "\n"+
                "BankName:    " + BankName + "\n" +
                "CardNumber:  " + CardNumber + "\n"+
                "BranchCode:  " + BranchCode + "\n"+
                "CVV:         " + cvv;
    }

    public BankDetails(int accountNo, String bankName, int cardNumber, int branchCode, int cvv, int bankId, int studentNo) {
        AccountNo = accountNo;
        BankName = bankName;
        CardNumber = cardNumber;
        BranchCode = branchCode;
        this.cvv = cvv;
        BankId = bankId;
        StudentNo = studentNo;
    }

    public void setAccountNo(int accountNo) {
        AccountNo = accountNo;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public void setBranchCode(int branchCode) {
        BranchCode = branchCode;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public void setBankId(int bankId) {
        BankId = bankId;
    }

    public void setStudentNo(int studentNo) {
        StudentNo = studentNo;
    }

    public int getAccountNo() {
        return AccountNo;
    }

    public String getBankName() {
        return BankName;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public int getBranchCode() {
        return BranchCode;
    }

    public int getCvv() {
        return cvv;
    }

    public int getBankId() {
        return BankId;
    }

    public int getStudentNo() {
        return StudentNo;
    }
}
