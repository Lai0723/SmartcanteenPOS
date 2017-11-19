package com.example.lai.smartcanteenpos;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

public class QRretrieval extends AppCompatActivity {

    String OrderID, ProdID, WalletID, ProdName, OrderDateTime,  OrderStatus, PayDateTime;
    double PayAmount;
    int OrderQuantity;

    ImageView ivQRretrieval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrretrieval);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        OrderID = extras.getString("OrderID");
        ProdID = extras.getString("ProdID");
        WalletID = extras.getString("WalletID");
        ProdName = extras.getString("ProdName");
        OrderDateTime = extras.getString("OrderDateTime");
        OrderStatus = extras.getString("OrderStatus");
        PayDateTime = extras.getString("PayDateTime");

        PayAmount = extras.getDouble("PayAmount");
        OrderQuantity = extras.getInt("OrderQuantity");

        generateQRretrieval();

    }

    public void generateQRretrieval(){
        //get date time
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        //convert to qr
        String info = "orderRetrieval" + "," + OrderID + "," + ProdID + "," + WalletID + "," + ProdName + "," + OrderDateTime + "," + OrderStatus + "," + PayDateTime + "," + PayAmount + "," + OrderQuantity + "," + currentTime;
        Bitmap myBitmap = QRCode.from(info).bitmap();
        ivQRretrieval = (ImageView) findViewById(R.id.ivQRretrieval);
        ivQRretrieval.setImageBitmap(myBitmap);

    }

}
