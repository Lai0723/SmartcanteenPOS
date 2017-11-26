package com.example.lai.smartcanteenpos.Obejct;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import com.example.lai.smartcanteenpos.R;

/**
 * Created by Leow on 11/4/2017.
 */

public class OrderHistoryAdapter extends ArrayAdapter<Order> {
    DecimalFormat df2 = new DecimalFormat("0.00");

    public OrderHistoryAdapter(Activity context, int resource, List<Order> list){
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Order list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView = inflater.inflate(R.layout.order_history_layout, parent, false);

        TextView textViewProdName,textViewOrderDate, textViewAmount, textViewTotal, textViewStatus, textViewOrderID;

        textViewOrderID = listView.findViewById(R.id.textViewOrder);
        textViewProdName = listView.findViewById(R.id.textViewProdName);
        textViewOrderDate = listView.findViewById(R.id.textViewOrderDate);
        textViewAmount = listView.findViewById(R.id.textViewAmount);
        textViewTotal = listView.findViewById(R.id.textViewTotal);
        textViewStatus = listView.findViewById(R.id.textViewStatus);

        textViewOrderID.setText(textViewOrderID.getText() + ": " + list.getOrderID());
        textViewProdName.setText(textViewProdName.getText() + " " + list.getProdName());
        textViewOrderDate.setText(textViewOrderDate.getText() + " " + list.getOrderDateTime());
        textViewAmount.setText(textViewAmount.getText() + " " + list.getOrderQuantity());
        textViewTotal.setText(textViewTotal.getText() + " RM " + df2.format(list.getPayAmount()));
        textViewStatus.setText(textViewStatus.getText() + " " + list.getOrderStatus());

        return listView;
    }


}
