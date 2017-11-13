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
 * Created by lai on 7/11/2017.
 */

public class MAdapter extends ArrayAdapter<Menu> {

    public MAdapter(Activity context, int resource, List<Menu> list) {
        super(context, resource, list);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Menu list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.menulist, parent, false);

        TextView MID,MName,MPrice,MQuantity;



        MID = (TextView) rowView.findViewById(R.id.MID);
        MName = (TextView) rowView.findViewById(R.id.MName);
        MPrice = (TextView) rowView.findViewById(R.id.MPrice);
        MQuantity = (TextView) rowView.findViewById(R.id.MQuantity);





        MID.setText(MID.getText() + " :  "+ list.getProdID());
        MName.setText(MName.getText() + " : " + list.getProdName());
        MPrice.setText(MPrice.getText() + " : "+ list.getPrice());
        MQuantity.setText("Quantity left" + " : "+ list.getQuantity());




        return rowView;
    }

}
