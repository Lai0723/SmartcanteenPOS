package com.example.lai.smartcanteenpos.database;

public class History {

    private  int RedeemCodeID;
    private  String Description;
    private  String CouponCode;
    private  String CreateAt;
    private  String WalletID;
    private  String RedeemDate;

    public History(int redeemCodeID, String desc, String couponCode, String createAt, String walletID, String redeemDate) {

        RedeemCodeID = redeemCodeID;
        Description = desc;
        CouponCode = couponCode;
        CreateAt = createAt;
        WalletID = walletID;
        RedeemDate = redeemDate;
    }

    public int getRedeemCodeID() {return RedeemCodeID; }

    public void setRedeemCodeID(int redeemCodeID) { RedeemCodeID = redeemCodeID; }

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

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getRedeemDate() {
        return RedeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        RedeemDate = redeemDate;
    }
}
