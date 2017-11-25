package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.database.Orders;
import com.example.lai.smartcanteenpos.database.Transfer;

import java.util.List;

public class adapter_list_transaction_history extends ArrayAdapter<Orders> {

    public adapter_list_transaction_history(@NonNull Context context, int resource, @NonNull List<Orders> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Orders orders = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_list_transaction_history, parent, false);

        TextView tvOrderID, tvProdID, tvQuantity, tvOrderDate, tvPayDate, tvTransactionAmount;


        tvOrderID = rowView.findViewById(R.id.tvOrderID);
        tvProdID = (TextView)rowView.findViewById(R.id.tvProdID);
        tvQuantity = (TextView)rowView.findViewById(R.id.tvQuantity);
        tvOrderDate = (TextView)rowView.findViewById(R.id.tvOrderDate);
        tvPayDate = (TextView)rowView.findViewById(R.id.tvPayDate);
        tvTransactionAmount = (TextView)rowView.findViewById(R.id.tvTransactionAmount);

        tvOrderID.setText("Order ID: "+orders.getOrderID());
        tvProdID.setText("Product ID: "+orders.getProdID());
        tvQuantity.setText("Quantity: "+orders.getOrderQuantity());
        tvOrderDate.setText("Order Date: " + orders.getOrderDateTime());
        tvPayDate.setText("Pay Date: " + orders.getPayDateTime());
        tvTransactionAmount.setText(String.format("RM %.2f",orders.getPayAmount()));

        ConstraintLayout cstWalletTransactionHistory = (ConstraintLayout) rowView.findViewById(R.id.cstWalletTransactionHistory);

        return rowView;
    }
}
