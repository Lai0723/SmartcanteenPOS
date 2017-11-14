package com.example.lai.smartcanteenpos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;
//import com.google.zxing.qrcode.encoder.QRCode;


public class QRTransfer extends AppCompatActivity {

    static String giverID;
    static double balanceToChk;
    TextView tvBalanceToChk, tvTimer;
    SeekBar sbTimer;

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
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        sbTimer = (SeekBar) findViewById(R.id.sbTimer);
        tvBalanceToChk.setText(String.format("Balance: RM %.2f" ,QRTransfer.balanceToChk));

        generateQR();

        int millisToChange=30000;
        final int millisToUpdate=100;
        sbTimer.setMax(millisToChange/millisToUpdate);
        sbTimer.setProgress(0);
        sbTimer.setEnabled(false);

        new CountDownTimer(millisToChange, millisToUpdate) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                sbTimer.setProgress((int)millisUntilFinished/millisToUpdate);
            }

            public void onFinish() {
                //tvTimer.setText("done!");
                this.start();
                generateQR();
            }
        }.start();

    }

    public void generateQR(){
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
