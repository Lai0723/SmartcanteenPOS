package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 13/11/2017.
 */

public class Report {

    private String OrderID;
    private String ProdName;
    private int  OrderQuantity;
    private double PayAmount;
    private int PromotionApplied;
    private double PriceDifference;


    public Report(String OrderID,String ProdName,int  OrderQuantity,Double PayAmount,int PromotionApplied,Double PriceDifference){


        this.setOrderID(OrderID);
        this.setProdName(ProdName);
        this.setOrderQuantity(OrderQuantity);
        this.setPayAmount(PayAmount);
        this.setPromotionApplied(PromotionApplied);
        this.setPriceDifference(PriceDifference);



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

    public double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(double payAmount) {
        PayAmount = payAmount;
    }

    public int getPromotionApplied() {
        return PromotionApplied;
    }

    public void setPromotionApplied(int promotionApplied) {
        PromotionApplied = promotionApplied;
    }

    public double getPriceDifference() {
        return PriceDifference;
    }

    public void setPriceDifference(double priceDifference) {
        PriceDifference = priceDifference;
    }
}
