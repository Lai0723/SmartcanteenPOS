/**
 * Created by Leow Wei Jian, RSD3 (September 2015 Intake) on 11/4/2017.
 * This is the Order Activity that is started when user clicked on the Online Menu button in Main Activity
 */
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

    private static String orderID, canteenName, stallName, walletID, productID, productName, productDesc;
    private static int orderQuantity;
    private static double walletBal, productPrice;
    public static List<Product> listMenu = null;


    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
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
        CanteenFragment canteenFragment = new CanteenFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameOrderMain,canteenFragment).commit();
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

    public static String getOrderID() {
        return orderID;
    }

    public static void setOrderID(String oID) {
        orderID = oID;
    }

    public static int getOrderQuantity() {
        return orderQuantity;
    }

    public static double getWalletBal(){
        return walletBal;
    }

    public void init(){
        orderID = null;
        canteenName = null;
        stallName = null;
        walletID = null;
        productID = null;
        productName = null;
        productDesc = null;
        orderQuantity = 0;
        walletBal = 0;
        productPrice = 0;
        listMenu = null;
    }

}
