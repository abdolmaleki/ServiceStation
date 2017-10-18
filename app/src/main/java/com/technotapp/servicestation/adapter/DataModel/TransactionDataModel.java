package com.technotapp.servicestation.adapter.DataModel;


public class TransactionDataModel {
    private  String panNumber;
    private  String amount;
    private  String dateTimeShaparak;
    private  String backTransactionID;
    private  String responseCode;
    private  String terminalID;
    private  String MAC;

    public  void setPanNumber(String panNumber1) {
        panNumber = "";
        panNumber = panNumber1;
    }

    public  void setAmount(String amount1) {
        amount = "";
        amount = amount1;
    }

    public  void setDateTimeShaparak(String dateTimeShaparak1) {
        dateTimeShaparak = "";
        dateTimeShaparak = dateTimeShaparak1;
    }

    public  void setResponseCode(String responseCode1) {

        responseCode = "";
        responseCode = responseCode1;
    }

    public  void setMAC(String MAC1) {
        MAC = "";
        MAC = MAC1;
    }

    public  void setTerminalID(String terminalID1) {
        terminalID = "";
        terminalID = terminalID1;
    }

    public  void setBackTransactionID(String backTransactionID1) {
        backTransactionID = "";
        backTransactionID = backTransactionID1;
    }

    public  String getPanNumber() {
        return panNumber;
    }

    public  String getAmount() {
        return amount;
    }

    public  String getDateTimeShaparak() {
        return dateTimeShaparak;
    }

    public  String getResponseCode() {
        return responseCode;
    }

    public  String getMAC() {
        return MAC;
    }

    public  String getTerminalID() {
        return terminalID;
    }

    public  String getBackTransactionID() {
        return backTransactionID;
    }
}
