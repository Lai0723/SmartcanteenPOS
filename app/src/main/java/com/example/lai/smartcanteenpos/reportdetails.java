package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.Obejct.ReportAdapter;

public class reportdetails extends Activity {

    ListView reportList;
    TextView txtdate,txtTotal;
    String date;
    Double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportdetails);

        reportList = (ListView)this.findViewById(R.id.reportlist);

        final ReportAdapter adapter = new ReportAdapter(this, R.layout.activity_reportdetails, Menu_screen.RList);
        reportList.setAdapter(adapter);

        txtdate = (TextView) this.findViewById(R.id.txtdate);
        txtTotal = (TextView)this.findViewById(R.id.txtT);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            date = extras.getString("date");
            total = extras.getDouble("total");
        }

        txtdate.setText(date.toString());
        txtTotal.setText(Double.toString(total));
    }
}
