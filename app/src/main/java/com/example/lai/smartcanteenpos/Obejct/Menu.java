package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 7/11/2017.
 */

public class Menu {

    private String ProdID;
    private String ProdName;
    private double Price;
    private int  Quantity;



    public Menu (String ProdID, String ProdName, double Price, int  Quantity){
        this.setProdName(ProdName);

        this.setPrice(Price);

        this.setProdID(ProdID);

        this.setQuantity(Quantity);

    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
