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
 * Created by Lai Wei Chun, RSD3, 2017
 */

public class Order_Adapter extends ArrayAdapter<Order> {

    public Order_Adapter(Activity context, int resource, List<Order> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Order list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.orderlist, parent, false);

        TextView OrderID,ProdName,OrderQuantity,OrderDateTime,OrderStatus;



        OrderID = (TextView) rowView.findViewById(R.id.OID);
        ProdName = (TextView) rowView.findViewById(R.id.OProd);
        OrderQuantity = (TextView) rowView.findViewById(R.id.OQuantity);
        OrderDateTime = (TextView) rowView.findViewById(R.id.OTime);
        OrderStatus = (TextView) rowView.findViewById(R.id.OStatus);



        OrderID.setText( OrderID.getText() + ""+ list.getOrderID());
        ProdName.setText( ProdName.getText() + "" + list.getProdName());
        OrderQuantity.setText(OrderQuantity.getText() + ""+ list.getOrderQuantity());
        OrderDateTime.setText(OrderDateTime.getText() + "" + list.getOrderDateTime());
        OrderStatus.setText(OrderStatus.getText() + "" + list.getOrderStatus());



        return rowView;
    }
}
