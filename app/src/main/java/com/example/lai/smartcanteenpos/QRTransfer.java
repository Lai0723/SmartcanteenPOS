package com.example.lai.smartcanteenpos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;
//import com.google.zxing.qrcode.encoder.QRCode;


public class QRTransfer extends AppCompatActivity {

    static String giverID;
    static double balanceToChk;
    TextView tvBalanceToChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtransfer);

        //get User info
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        giverID = extras.getString("giverID");
        balanceToChk = extras.getDouble("balanceToChk");

        tvBalanceToChk = (TextView) findViewById(R.id.tvBalanceToChk);
        tvBalanceToChk.setText(String.format("Balance: RM %.2f" ,QRTransfer.balanceToChk));

        //get date time
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        //convert to qr
        String info = giverID + "," + balanceToChk + "," + currentTime;
        Bitmap myBitmap = QRCode.from(info).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.ivQR);
        myImage.setImageBitmap(myBitmap);

    }

}
