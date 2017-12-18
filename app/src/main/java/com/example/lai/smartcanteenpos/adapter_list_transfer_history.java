package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.database.Transfer;

import java.util.List;

/**
 * Created by Gabriel Lai Bihsyan
 */
public class adapter_list_transfer_history extends ArrayAdapter <Transfer> {

    public adapter_list_transfer_history(@NonNull Context context, int resource, @NonNull List<Transfer> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transfer transfer = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_list_transfer_history, parent, false);

        TextView tvTransferID, tvGiverID, tvReceiverID, tvTransferAmount, tvTransferDate;


        tvTransferID = rowView.findViewById(R.id.tvTransferID);
        tvGiverID = (TextView)rowView.findViewById(R.id.tvGiverID);
        tvReceiverID = (TextView)rowView.findViewById(R.id.tvReceiverID);
        tvTransferDate = (TextView)rowView.findViewById(R.id.tvTransferDate);
        tvTransferAmount = (TextView)rowView.findViewById(R.id.tvTransferAmount);

        tvTransferID.setText("Transfer ID: "+transfer.getTransferID());
        tvGiverID.setText("From: "+transfer.getGiverWalletID());
        tvReceiverID.setText("To: "+transfer.getReceiverWalletID());
        tvTransferDate.setText("Date: " + transfer.getTransferDate());
        tvTransferAmount.setText(String.format("RM %.2f",transfer.getTransferAmount()));

        ConstraintLayout layout = (ConstraintLayout) rowView.findViewById(R.id.cstTransferHistory);

        return rowView;
    }
}
