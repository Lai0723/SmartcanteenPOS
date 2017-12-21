package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.Obejct.ReportAdapter;

/**
 * Created by Lai Wei Chun, RSD3, 2017
 */

//display online order report info
public class reportdetails extends Activity {

    ListView reportList;
    TextView txtdate,txtTotal,txtDiscount;
    String date;
    Double total,discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportdetails);

        reportList = (ListView)this.findViewById(R.id.reportlist);
        txtdate = (TextView) this.findViewById(R.id.txtdate);
        txtTotal = (TextView)this.findViewById(R.id.txtTotal_profit);
        txtDiscount = (TextView)this.findViewById(R.id.txtDiscountAmount);

        final ReportAdapter adapter = new ReportAdapter(this, R.layout.activity_reportdetails, Menu_screen.RList);
        reportList.setAdapter(adapter);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            date = extras.getString("date");
            total = extras.getDouble("total");
            discount = extras.getDouble("discount");
        }

        txtdate.setText(date.toString());
        txtTotal.setText(Double.toString(total));
        txtDiscount.setText(Double.toString(discount));
    }
}
