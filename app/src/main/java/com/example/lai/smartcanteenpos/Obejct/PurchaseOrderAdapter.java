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
 * Created by lai on 18/10/2017.
 */

public class PurchaseOrderAdapter extends ArrayAdapter<Purchase_order> {
    public PurchaseOrderAdapter(Activity context, int resource, List<Purchase_order> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Purchase_order list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.purchase_order_list, parent, false);

        TextView POID,Name,Supplier,PurchaseQuan,PurchaseFee,PurchaseDate;



        POID = (TextView) rowView.findViewById(R.id.POID);
        Name = (TextView) rowView.findViewById(R.id.Name);
        Supplier = (TextView) rowView.findViewById(R.id.Supplier);
        PurchaseQuan = (TextView) rowView.findViewById(R.id.PurchaseQuan);
        PurchaseFee = (TextView) rowView.findViewById(R.id.PurchaseFee);
        PurchaseDate = (TextView) rowView.findViewById(R.id.PurchaseDate);


        POID.setText( POID.getText() + ""+ list.getPOID());
        Name.setText( Name.getText() + "" + list.getProdID());
        Supplier.setText(Supplier.getText() + ""+ list.getSupplierName());
        PurchaseQuan.setText(PurchaseQuan.getText() + "" + list.getPurchaseQuantity());
        PurchaseFee.setText(PurchaseFee.getText() + "" + list.getFee());
        PurchaseDate.setText(PurchaseDate.getText() + "" + list.getRetrieveDate());



        return rowView;
    }

}
