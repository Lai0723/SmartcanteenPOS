package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 12/10/2017.
 */

public class Product {

    private String ProdID;
    private String ProdName;
    private String ProdCat;
    private String ProdDesc;
    private double Price;
    private int  Quantity;
    private String ImageURL;
    private String MercName;




    public Product(){

    }




    public Product (String ProdID, String ProdName,String ProdCat,String ProdDesc,Double Price, int prodQty, String ImageURL){
        this.setProdName(ProdName);
        this.setProdCat(ProdCat);
        this.setProdDesc(ProdDesc);
        this.setPrice(Price);
        this.setQuantity(prodQty);
        this.setImageURL(ImageURL);
        this.setProdID(ProdID);


    }

    public Product (String ProdID, String ProdName,Double Price){
        this.setProdName(ProdName);

        this.setPrice(Price);

        this.setProdID(ProdID);


    }


    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getProdCat() {
        return ProdCat;
    }

    public void setProdCat(String prodCat) {
        ProdCat = prodCat;
    }

    public String getProdDesc() {
        return ProdDesc;
    }

    public void setProdDesc(String prodDesc) {
        ProdDesc = prodDesc;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public String getMercName() {
        return MercName;
    }

    public void setMercName(String mercName) {
        MercName = mercName;
    }


}
