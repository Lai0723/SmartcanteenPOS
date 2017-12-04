package com.example.lai.smartcanteenpos;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lai.smartcanteenpos.Obejct.Order;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    public static List<Order> listOrder = null;
    private static String orderID, walletID, productID, productName, orderStatus, orderDateTime, payDateTime;
    private static int orderQuantity;
    private static double walletBal, productPrice, payAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        init();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        walletID = extras.getString("walletID");
        walletBal = extras.getDouble("balance");
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameOrderHistory,orderHistoryFragment).commit();
    }

    public static String getwID() {
        return walletID;
    }

    public static String getProdID() {
        return productID;
    }

    public static void setProdID(String prodID) {
        productID = prodID;
    }

    public static String getPayDateTime() {
        return payDateTime;
    }

    public static void setPayDateTime(String payDT) {
        payDateTime = payDT;
    }

    public static String getProdName() {
        return productName;
    }

    public static void setProdName(String prodName) {
        productName = prodName;
    }

    public static double getOrderTotal() {
        return payAmount;
    }

    public static void setOrderTotal(double total) {
        payAmount = total;
    }

    public static String getOrderID() {
        return orderID;
    }

    public static void setOrderID(String oID) {
        orderID = oID;
    }

    public static String getOrderStatus() {
        return orderStatus;
    }

    public static void setOrderStatus(String orderStat) {
        orderStatus = orderStat;
    }

    public static String getOrderDateTime() {
        return orderDateTime;
    }

    public static void setOrderDateTime(String orderDT) {
        orderDateTime = orderDT;
    }

    public static int getOrderQuantity() {
        return orderQuantity;
    }

    public static void setOrderQuantity(int orderQty) {
        orderQuantity = orderQty;
    }

    public static double getWalletBal(){
        return walletBal;
    }

    public void init(){
        listOrder = null;
    }
}
