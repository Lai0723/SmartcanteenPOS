package com.example.lai.smartcanteenpos.database;

/**
 * Created by Gabb on 13/10/2017.
 */

public class Transfer {
    private int TransferID;
    private String TransferDate;
    private double TransferAmount;
    private String GiverWalletID;
    private String ReceiverWalletID;

    public Transfer (int TransferID, String TransferDate, double TransferAmount, String GiverWalletID, String ReceiverWalletID){
        this.setTransferID(TransferID);
        this.setTransferDate(TransferDate);
        this.setTransferAmount(TransferAmount);
        this.setGiverWalletID(GiverWalletID);
        this.setReceiverWalletID(ReceiverWalletID);

    }

    public int getTransferID() {
        return TransferID;
    }

    public void setTransferID(int transferID) {
        TransferID = transferID;
    }

    public String getTransferDate() {
        return TransferDate;
    }

    public void setTransferDate(String transferDate) {
        TransferDate = transferDate;
    }

    public double getTransferAmount() {
        return TransferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        TransferAmount = transferAmount;
    }

    public String getGiverWalletID() {
        return GiverWalletID;
    }

    public void setGiverWalletID(String giverWalletID) {
        GiverWalletID = giverWalletID;
    }

    public String getReceiverWalletID() {
        return ReceiverWalletID;
    }

    public void setReceiverWalletID(String receiverWalletID) {
        ReceiverWalletID = receiverWalletID;
    }
}
