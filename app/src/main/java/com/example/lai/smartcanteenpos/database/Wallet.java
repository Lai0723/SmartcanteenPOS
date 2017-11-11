package com.example.lai.smartcanteenpos.database;

/**
 * Created by Gabb on 13/10/2017.
 */

public class Wallet {
    private String WalletID;
    private double Balance;
    private int LoyaltyPoint;
    private String LoginPassword;
    private String CurrentCard;
    private String CurrentCardType;

    public Wallet (String WalletID, double Balance, int LoyaltyPoint, String LoginPassword, String CurrentCard, String CurrentCardType){
        this.setWalletID(WalletID);
        this.setBalance(Balance);
        this.setLoyaltyPoint(LoyaltyPoint);
        this.setLoginPassword(LoginPassword);
        this.setCurrentCard(CurrentCard);
        this.setCurrentCardType(CurrentCardType);
    }

    public String getWalletID() {
        return WalletID;
    }

    public void setWalletID(String walletID) {
        WalletID = walletID;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public int getLoyaltyPoint() {
        return LoyaltyPoint;
    }

    public void setLoyaltyPoint(int loyaltyPoint) {
        LoyaltyPoint = loyaltyPoint;
    }

    public String getLoginPassword() {
        return LoginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        LoginPassword = loginPassword;
    }

    public String getCurrentCard() {
        return CurrentCard;
    }

    public void setCurrentCard(String currentCard) {
        CurrentCard = currentCard;
    }

    public String getCurrentCardType() {
        return CurrentCardType;
    }

    public void setCurrentCardType(String currentCardType) {
        CurrentCardType = currentCardType;
    }
}
