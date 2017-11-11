package com.example.lai.smartcanteenpos.database;

/**
 * Created by Gabb on 13/10/2017.
 */

public class TopUp {
    private String WalletID;
    private int CardNumber;
    private double TopUpAmount;
    private String TopUpDateTime;

    public TopUp (String WalletID, int CardNumber, double TopUpAmount, String TopUpDateTime){
        this.setWalletID(WalletID);
        this.setCardNumber(CardNumber);
        this.setTopUpAmount(TopUpAmount);
        this.setTopUpDateTime(TopUpDateTime);

    }

    public String getWalletID() {
        return WalletID;
    }

    public void setWalletID(String walletID) {
        WalletID = walletID;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public double getTopUpAmount() {
        return TopUpAmount;
    }

    public void setTopUpAmount(double topUpAmount) {
        TopUpAmount = topUpAmount;
    }

    public String getTopUpDateTime() {
        return TopUpDateTime;
    }

    public void setTopUpDateTime(String topUpDateTime) {
        TopUpDateTime = topUpDateTime;
    }
}
