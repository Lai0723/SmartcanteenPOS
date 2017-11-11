package com.example.lai.smartcanteenpos.database;

/**
 * Created by Gabb on 13/10/2017.
 */

public class PaymentCard {
    private String CardType;
    private int CardNumber;
    private int AccountID;
    private double AccountBalance;
    private String CardName;
    private String CardAddress;
    private int CardPostcode;
    private String CardState;

    public PaymentCard (String CardType, int CardNumber, int AccountID, double AccountBalance, String CardName, String CardAddress, int CardPostcode, String CardState){
        this.setCardType(CardType);
        this.setCardNumber(CardNumber);
        this.setAccountID(AccountID);
        this.setAccountBalance(AccountBalance);
        this.setCardName(CardName);
        this.setCardAddress(CardAddress);
        this.setCardPostcode(CardPostcode);
        this.setCardState(CardState);

    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public double getAccountBalance() {
        return AccountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        AccountBalance = accountBalance;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getCardAddress() {
        return CardAddress;
    }

    public void setCardAddress(String cardAddress) {
        CardAddress = cardAddress;
    }

    public int getCardPostcode() {
        return CardPostcode;
    }

    public void setCardPostcode(int cardPostcode) {
        CardPostcode = cardPostcode;
    }

    public String getCardState() {
        return CardState;
    }

    public void setCardState(String cardState) {
        CardState = cardState;
    }
}
