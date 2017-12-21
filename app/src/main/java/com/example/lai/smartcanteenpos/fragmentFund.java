/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017 on 11/10/2017.
 */
package com.example.lai.smartcanteenpos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;



public class fragmentFund extends Fragment {

    SeekBar sbTopUpValue;
    TextView tvTopUpValue;
    public static int tuValue = 5;

    private ProgressDialog pDialog;

    public static fragmentFund newInstance() {
        fragmentFund fragment = new fragmentFund();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_fund, container, false);

        sbTopUpValue = (SeekBar) view.findViewById(R.id.sbTopUpValue);
        tvTopUpValue = (TextView) view.findViewById(R.id.tvTopUpValue);
        tuValue = 5;
        tvTopUpValue.setText("RM " + String.valueOf(tuValue));



        //Get top up value from seekbar
        sbTopUpValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (progress <= 10) {
                    tuValue = 5;
                } else if (progress > 10 && progress <= 20) {
                    tuValue = 10;
                } else if (progress > 20 && progress <= 40) {
                    tuValue = 15;
                } else if (progress > 40 && progress <= 60) {
                    tuValue = 20;
                } else if (progress > 60 && progress <= 80) {
                    tuValue = 30;
                } else {
                    tuValue = 50;
                }
                tvTopUpValue.setText("RM " + String.valueOf(tuValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {

            }


        });

        return view;
    }



}
