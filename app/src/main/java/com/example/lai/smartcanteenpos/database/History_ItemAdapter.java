package com.example.lai.smartcanteenpos.database;

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
 * Created by Lim Boon Seng,RSD3 on 13/10/2017
 */

public class History_ItemAdapter extends ArrayAdapter<History_Item> {
    public History_ItemAdapter(Activity context, int resource, List<History_Item> list) {
        super(context, resource, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History_Item history_item = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.history_item_record, parent, false);

        TextView tvRedeemItemID, tvDesc, tvRedeemDate, tvWalletID,tvItemCode,tvCreateAt;


        tvRedeemItemID = (TextView) rowView.findViewById(R.id.tvRedeemItemID);
        tvDesc = (TextView) rowView.findViewById(R.id.tvDesc);
        tvItemCode = (TextView) rowView.findViewById(R.id.tvItemCode);
        tvRedeemDate = (TextView) rowView.findViewById(R.id.tvRedeemDate);
        tvWalletID = (TextView)rowView.findViewById(R.id.tvWalletID);
        tvCreateAt = (TextView)rowView.findViewById(R.id.tvCreateAt);

        tvRedeemItemID.setText(tvRedeemItemID.getText() + ":" + history_item.getRedeemItemID());
        tvDesc.setText(tvDesc.getText() + ":" + history_item.getDescription());
        tvItemCode.setText(tvItemCode.getText() + ":" + history_item.getItemCode());
        tvWalletID.setText(tvWalletID.getText() + ":" + history_item.getWalletID());
        tvRedeemDate.setText(tvRedeemDate.getText() + ":" + history_item.getRedeemDate());
        tvCreateAt.setText(tvCreateAt.getText() + ":" + history_item.getCreateAt());

        return rowView;
    }

}
