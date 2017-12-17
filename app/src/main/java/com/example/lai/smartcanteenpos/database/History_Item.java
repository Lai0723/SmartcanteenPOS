package com.example.lai.smartcanteenpos.database;

/**
 * Created by Lim Boon Seng,RSD3 on 13/10/2017
 */

public class History_Item {

    private  int RedeemItemID;
    private  String Description;
    private  String ItemCode;
    private  String CreateAt;
    private  String WalletID;
    private  String RedeemDate;

    public History_Item(int redeemItemID, String desc, String itemCode, String createAt, String walletID, String redeemDate) {

        RedeemItemID = redeemItemID;
        Description = desc;
        ItemCode = itemCode;
        CreateAt = createAt;
        WalletID = walletID;
        RedeemDate = redeemDate;
    }

    public int getRedeemItemID() {return RedeemItemID; }

    public void setRedeemItemID(int redeemItemID) { RedeemItemID = redeemItemID; }

    public String getWalletID() {return WalletID; }

    public void setWalletID(String walletID) { WalletID = walletID; }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String desc) {
        Description = desc;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getRedeemDate() {
        return RedeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        RedeemDate = redeemDate;
    }
}
