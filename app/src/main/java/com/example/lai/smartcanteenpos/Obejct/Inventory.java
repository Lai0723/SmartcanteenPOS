package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by lai on 14/10/2017.
 */

public class Inventory {
    private String ProdID;
    private String ProdName;
    private int ProdQuantity;
    private String SupplierName;


    public Inventory (String ProdID, String ProdName, int ProdQuantity,String SupplierName){
        this.setProdID(ProdID);
        this.setProdName(ProdName);
        this.setProdQuantity(ProdQuantity);
        this.setSupplierName(SupplierName);


    }

    public Inventory (String ProdID, String ProdName, int ProdQuantity){
        this.setProdID(ProdID);
        this.setProdName(ProdName);
        this.setProdQuantity(ProdQuantity);



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

    public int getProdQuantity() {
        return ProdQuantity;
    }

    public void setProdQuantity(int prodQuantity) {
        ProdQuantity = prodQuantity;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }
}
