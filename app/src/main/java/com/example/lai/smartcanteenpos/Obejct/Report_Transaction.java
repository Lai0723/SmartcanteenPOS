package com.example.lai.smartcanteenpos.Obejct;

/**
 * Created by Lai Wei Chun, RSD3, 2017
 */
public class Report_Transaction {
    private String TransferID;
    private String GiverWalletID;
    private double TransferAmount;


    public Report_Transaction(String TransferID,String GiverWalletID,Double TransferAmount){


        this.setTransferID(TransferID);
        this.setGiverWalletID(GiverWalletID);
        this.setTransferAmount(TransferAmount);



    }

    public String getTransferID() {
        return TransferID;
    }

    public void setTransferID(String transferID) {
        TransferID = transferID;
    }

    public String getGiverWalletID() {
        return GiverWalletID;
    }

    public void setGiverWalletID(String giverWalletID) {
        GiverWalletID = giverWalletID;
    }

    public double getTransferAmount() {
        return TransferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        TransferAmount = transferAmount;
    }
}
