package com.example.lai.smartcanteenpos.Obejct;

import java.util.Date;

/**
 * Created by lai on 17/10/2017.
 */

public class Purchase_order {
    private String POID;
    private String ProdID;
    private String ProdName;
    private String PurchaseQuantity;
    private String SupplierName;
    private double fee;
    private Date purchaseDate;
    private String MercName;
    private String retrieveDate;

public Purchase_order(){

}

    public Purchase_order (String POID,String ProdID, String SupplierName,String PurchaseQuantity,double fee,Date purchaseDate){

        this.setPOID(POID);
        this.setProdName(ProdID);
        this.setSupplierName(SupplierName);
        this.setPurchaseQuantity(PurchaseQuantity);
        this.setFee(fee);
        this.setPurchaseDate(purchaseDate);


    }

    public Purchase_order (String POID,String ProdName, String SupplierName,String PurchaseQuantity,double fee,String retrieveDate){

        this.setPOID(POID);
        this.setProdName(ProdName);
        this.setSupplierName(SupplierName);
        this.setPurchaseQuantity(PurchaseQuantity);
        this.setFee(fee);
        this.setRetrieveDate(retrieveDate);


    }


    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodID) {
        ProdName = prodID;
    }

    public String getPurchaseQuantity() {
        return PurchaseQuantity;
    }

    public void setPurchaseQuantity(String purchaseQuantity) {
        PurchaseQuantity = purchaseQuantity;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getMercName() {
        return MercName;
    }

    public void setMercName(String mercName) {
        MercName = mercName;
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public String getRetrieveDate() {
        return retrieveDate;
    }

    public void setRetrieveDate(String retrieveDate) {
        this.retrieveDate = retrieveDate;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }
}
