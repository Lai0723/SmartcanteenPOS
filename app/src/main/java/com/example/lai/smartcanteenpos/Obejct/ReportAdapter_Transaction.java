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

public class ReportAdapter_Transaction extends ArrayAdapter<Report_Transaction> {
    public ReportAdapter_Transaction(Activity context, int resource, List<Report_Transaction> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Report_Transaction list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.reportlist_transaction, parent, false);

        TextView TransferID,GiverWalletID,TransferAmount;



        TransferID = (TextView) rowView.findViewById(R.id.txtTransferID);
        GiverWalletID= (TextView) rowView.findViewById(R.id.txtTransferGiver);
        TransferAmount = (TextView) rowView.findViewById(R.id.txtTransferAmount);




        TransferID.setText( TransferID.getText() + " : "+ list.getTransferID());
        GiverWalletID.setText( GiverWalletID.getText() + " : " + list.getGiverWalletID());
        TransferAmount.setText(TransferAmount.getText() + " : "+ list.getTransferAmount());




        return rowView;
    }
}
