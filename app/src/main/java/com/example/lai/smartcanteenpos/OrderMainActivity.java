package com.example.lai.smartcanteenpos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.Product;

public class OrderMainActivity extends AppCompatActivity {

    private static String orderID = null, canteenName = null, stallName = null, walletID = null, productID = null, productName = null, productDesc = null,
            orderStatus = null, orderDateTime = null, payDateTime = null;
    private static int orderAmount = 0;
    private static double walletBal = 0, productPrice = 0, payAmount = 0;
    public static List<Product> listMenu = null;
    public static List<Order> listOrder = null;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_order:
                    CanteenFragment canteenFragment = new CanteenFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameOrderMain,canteenFragment).commit();
                    break;
                case R.id.navigation_orderHistory:
                    OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameOrderMain,orderHistoryFragment).commit();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        walletID = extras.getString("walletID");
        walletBal = extras.getDouble("balance");
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameOrderMain, CanteenFragment.newInstance());
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    public static String getCanteen() {
        return canteenName;
    }

    public static void setCanteen(String cName) {
        canteenName = cName;
    }

    public static String getStall() {
        return stallName;
    }

    public static void setStall(String nameOfStall) {
        stallName = nameOfStall;
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

    public static String getProdDesc() {
        return productDesc;
    }

    public static void setProdDesc(String prodDesc) {
        productDesc = prodDesc;
    }

    public static double getProdPrice() {
        return productPrice;
    }

    public static void setProdPrice(double prodPrice) {
        productPrice = prodPrice;
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

    public static int getOrderAmount() {
        return orderAmount;
    }

    public static void setOrderAmount(int orderQuantity) {
        orderAmount = orderQuantity;
    }

    public static double getWalletBal(){
        return walletBal;
    }

}
