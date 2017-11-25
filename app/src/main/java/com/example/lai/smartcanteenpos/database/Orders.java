package com.example.lai.smartcanteenpos.database;

import com.example.lai.smartcanteenpos.Obejct.Order;

/**
 * Created by Gabb on 25/11/2017.
 */

public class Orders {

    private int OrderID;
    private int ProdID;
    private int OrderQuantity;
    private String OrderDateTime;
    private String PayDateTime;
    private Double PayAmount;

    public Orders (int OrderID, int ProdID, int OrderQuantity, String OrderDateTime, String PayDateTime, Double PayAmount){
        this.setOrderID(OrderID);
        this.setProdID(ProdID);
        this.setOrderQuantity(OrderQuantity);
        this.setOrderDateTime(OrderDateTime);
        this.setPayDateTime(PayDateTime);
        this.setPayAmount(PayAmount);

    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getProdID() {
        return ProdID;
    }

    public void setProdID(int prodID) {
        ProdID = prodID;
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

    public String getPayDateTime() {
        return PayDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        PayDateTime = payDateTime;
    }

    public Double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(Double payAmount) {
        PayAmount = payAmount;
    }
}
