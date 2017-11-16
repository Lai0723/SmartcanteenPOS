package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;

import java.math.BigDecimal;

import cz.msebera.android.httpclient.entity.mime.Header;

public class add_fund extends AppCompatActivity {

    double topUpValue;
    TextView tvTopUpValue;
    SeekBar sbTopUpValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fund);

        sbTopUpValue = (SeekBar) findViewById(R.id.sbTopUpValue);
        tvTopUpValue = (TextView) findViewById(R.id.tvTopUpValue);
        tvTopUpValue.setText("RM " + String.valueOf(topUpValue));

        //pDialog = new ProgressDialog(this);

        sbTopUpValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (progress <= 10) {
                    topUpValue = 5;
                } else if (progress > 10 && progress <= 20) {
                    topUpValue = 10;
                } else if (progress > 20 && progress <= 40) {
                    topUpValue = 15;
                } else if (progress > 40 && progress <= 60) {
                    topUpValue = 20;
                } else if (progress > 60 && progress <= 80) {
                    topUpValue = 30;
                } else {
                    topUpValue = 50;
                }
                tvTopUpValue.setText("RM " + String.valueOf(topUpValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }





    public void onBuyPressed(View pressed) {


    }



}
