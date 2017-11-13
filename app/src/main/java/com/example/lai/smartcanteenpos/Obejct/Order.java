package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 19/10/2017.
 */

public class Order {

    private String OrderID;
    private String ProdName;
    private int  OrderQuantity;
    private String OrderDateTime;
    private String OrderStatus;

public Order(String OrderID,String ProdName,int  OrderQuantity,String OrderDateTime,String OrderStatus){


        this.setOrderID(OrderID);
        this.setProdName(ProdName);
        this.setOrderQuantity(OrderQuantity);
        this.setOrderDateTime(OrderDateTime);
        this.setOrderStatus(OrderStatus);


    }







    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public int getOrderQuantity() {
        return OrderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        OrderQuantity = orderQuantity;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        OrderDateTime = orderDateTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
