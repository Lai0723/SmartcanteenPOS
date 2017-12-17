package com.example.lai.smartcanteenpos.database;

import android.app.Activity;
import android.content.Context;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.R;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/*
 * Created by Lim Boon Seng,RSD3 on 13/10/2017
 */

public class HistoryAdapter extends ArrayAdapter<History>{
    public HistoryAdapter(Activity context, int resource, List<History> list) {
        super(context, resource, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History history = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.history_record, parent, false);

        TextView tvRedeemID, tvDesc, tvRedeemDate, tvWalletID,tvCouponCode,tvCreateAt;


        tvRedeemID = (TextView) rowView.findViewById(R.id.tvRedeemID);
        tvDesc = (TextView) rowView.findViewById(R.id.tvDesc);
        tvCouponCode = (TextView) rowView.findViewById(R.id.tvCouponCode);
        tvRedeemDate = (TextView) rowView.findViewById(R.id.tvRedeemDate);
        tvWalletID = (TextView)rowView.findViewById(R.id.tvWalletID);
        tvCreateAt = (TextView)rowView.findViewById(R.id.tvCreateAt);

        tvRedeemID.setText(tvRedeemID.getText() + ":" + history.getRedeemCodeID());
        tvDesc.setText(tvDesc.getText() + ":" + history.getDescription());
        tvCouponCode.setText(tvCouponCode.getText() + ":" + history.getCouponCode());
        tvWalletID.setText(tvWalletID.getText() + ":" + history.getWalletID());
        tvRedeemDate.setText(tvRedeemDate.getText() + ":" + history.getRedeemDate());
        tvCreateAt.setText(tvCreateAt.getText() + ":" + history.getCreateAt());

        return rowView;
    }

}
