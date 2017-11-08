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
 * Created by lai on 14/10/2017.
 */

public class InventoryAdapter extends ArrayAdapter<Inventory> {

    public InventoryAdapter(Activity context, int resource, List<Inventory> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Inventory list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.inventorylist, parent, false);

        TextView PID,PName,PQuantity,PSup;



        PID = (TextView) rowView.findViewById(R.id.PID);
        PName = (TextView) rowView.findViewById(R.id.PName);
        PQuantity = (TextView) rowView.findViewById(R.id.PQuantity);
        PSup = (TextView) rowView.findViewById(R.id.PSup);

        PID.setText(PID.getText()  + ""+ list.getProdID());
        PName.setText(PName.getText()  + "" + list.getProdName());
        PQuantity.setText(PQuantity.getText() + ""+ list.getProdQuantity());
        PSup.setText(PSup.getText()  + "" + list.getSupplierName());



        return rowView;
    }

}
