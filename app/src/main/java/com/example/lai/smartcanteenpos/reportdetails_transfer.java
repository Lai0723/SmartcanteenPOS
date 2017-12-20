package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.Obejct.ReportAdapter_Transaction;

//Created by lai wei chun

//display transaction report info
public class reportdetails_transfer extends Activity {

    ListView TransactionReportList;
    TextView TransactionTime,TransactionTotal;
    String date;
    Double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportdetails_transfer);

        TransactionReportList = (ListView)findViewById(R.id.TransactionReportList);
        TransactionTime = (TextView) findViewById(R.id.TransactionTime);
        TransactionTotal = (TextView) findViewById(R.id.TransactionTotal);

        final ReportAdapter_Transaction adapter = new ReportAdapter_Transaction(this, R.layout.activity_reportdetails_transfer, Menu_screen.RTList);
        TransactionReportList.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            date = extras.getString("date");
            total = extras.getDouble("total");
        }

        TransactionTime.setText(date.toString());
        TransactionTotal.setText(Double.toString(total));
    }

}
