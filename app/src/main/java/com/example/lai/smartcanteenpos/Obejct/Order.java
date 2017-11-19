package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by Leow and Lai on 11/1/2017.
 */

public class Order {
    private String orderID;
    private String walletID;
    private int prodID;
    private String prodName;
    private String orderDateTime;
    private int orderQuantity;
    private String orderStatus;
    private double payAmount;
    private String payDateTime;

    public Order(){

    }

    public Order(String orderID, String walletID, int prodID, String prodName, String orderDateTime,
                 int orderQuantity, String orderStatus, double payAmount, String payDateTime){
        this.setOrderID(orderID);
        this.setWalletID(walletID);
        this.setProdID(prodID);
        this.setProdName(prodName);
        this.setOrderDateTime(orderDateTime);
        this.setOrderQuantity(orderQuantity);
        this.setOrderStatus(orderStatus);
        this.setPayAmount(payAmount);
        this.setPayDateTime(payDateTime);
    }

    public Order(String orderID,String ProdName,int  OrderQuantity,String OrderDateTime,String OrderStatus) {


        this.setOrderID(orderID);
        this.setProdName(ProdName);
        this.setOrderQuantity(OrderQuantity);
        this.setOrderDateTime(OrderDateTime);
        this.setOrderStatus(OrderStatus);
    }


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getWalletID() {
        return walletID;
    }

    public void setWalletID(String walletID) {
        this.walletID = walletID;
    }

    public int getProdID() {
        return prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
}
