/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
package com.example.lai.smartcanteenpos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentWalletHistory extends Fragment {

    DatePicker dtpWalletHistory;
    Button btnChkWalletHistory;

    int day,month,year;
    String dateOfWalletHistory;

    public static fragmentWalletHistory newInstance() {
        fragmentWalletHistory fragment = new fragmentWalletHistory();
        return fragment;
    }

    public fragmentWalletHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView =inflater.inflate(R.layout.fragment_wallet_history, container, false);
        dtpWalletHistory = inflatedView.findViewById(R.id.dtpWalletHistory);
        dtpWalletHistory.setMaxDate(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        dtpWalletHistory.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker dtpWalletHistory, int yearSelected, int monthSelected, int daySelected) {
                day = dtpWalletHistory.getDayOfMonth();
                month = dtpWalletHistory.getMonth()+1;
                year = dtpWalletHistory.getYear();
            }
        });


        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        btnChkWalletHistory = inflatedView.findViewById(R.id.btnChkWalletHistory);

        //Pass date information to wallet history activity to retrieve history records of that day
        btnChkWalletHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOfWalletHistory = year+"-"+month+"-"+day;
                Intent intent = new Intent(v.getContext(), wallet_history.class);
                intent.putExtra("dateOfWalletHistory", dateOfWalletHistory);
                startActivity(intent);

            }
        });

        return inflatedView;


    }



}
