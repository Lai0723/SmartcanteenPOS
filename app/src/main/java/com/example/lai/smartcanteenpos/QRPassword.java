package com.example.lai.smartcanteenpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class QRPassword extends AppCompatActivity {

    static String giverID, passedQRpw, inputQRpw;
    static double balanceToChk;
    EditText etQRpw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrpassword);



        etQRpw = (EditText)findViewById(R.id.etQRpw);

        //get User info
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        giverID = extras.getString("giverID");
        balanceToChk = extras.getDouble("balanceToChk");
        passedQRpw = extras.getString("QRpw");

    }

    public void onProceedClick(View v){

        inputQRpw = etQRpw.getText().toString();
        if (inputQRpw.equals(passedQRpw) ){

            //Toast.makeText(this, "Pw match", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, QRTransfer.class);
            intent.putExtra("giverID",giverID);
            intent.putExtra("balanceToChk",balanceToChk);
            startActivity(intent);
        }
        else if(!inputQRpw.equals(passedQRpw)) {
            Toast.makeText(this, "Error: Pw NOT match", Toast.LENGTH_SHORT).show();

        }

    }

}
