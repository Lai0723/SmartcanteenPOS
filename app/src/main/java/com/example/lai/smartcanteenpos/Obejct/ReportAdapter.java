package com.example.lai.smartcanteenpos.Obejct;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.R;

import java.util.List;

/**
 * Created by lai on 13/11/2017.
 */

public class ReportAdapter extends ArrayAdapter<Report> {
    public ReportAdapter(Activity context, int resource, List<Report> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Report list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.reportlist, parent, false);

        TextView OrderID,ProdName,OrderQuantity,PayAmount;



        OrderID = (TextView) rowView.findViewById(R.id.ReportIName);
        ProdName = (TextView) rowView.findViewById(R.id.ReportIPN);
        OrderQuantity = (TextView) rowView.findViewById(R.id.ReportIOQ);
        PayAmount = (TextView)rowView.findViewById(R.id.ReportIPA);



        OrderID.setText( OrderID.getText() + " : "+ list.getOrderID());
        ProdName.setText( ProdName.getText() + " : " + list.getProdName());
        OrderQuantity.setText(OrderQuantity.getText() + " : "+ list.getOrderQuantity());

        PayAmount.setText(PayAmount.getText() + " : " + list.getPayAmount());



        return rowView;
    }
}
