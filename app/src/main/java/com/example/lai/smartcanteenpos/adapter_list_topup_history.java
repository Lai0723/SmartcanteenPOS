/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
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
import com.example.lai.smartcanteenpos.database.TopUp;

import java.util.List;


public class adapter_list_topup_history extends ArrayAdapter<TopUp> {

    public adapter_list_topup_history(@NonNull Context context, int resource, @NonNull List<TopUp> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopUp topup = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_list_topup_history, parent, false);

        TextView tvTopupDate, tvTopupAmount;


        tvTopupDate = rowView.findViewById(R.id.tvTopupDate);
        tvTopupAmount = (TextView)rowView.findViewById(R.id.tvTopupAmount);

        tvTopupDate.setText("Top Up Date: " + topup.getTopUpDateTime());
        tvTopupAmount.setText(String.format("RM %.2f",topup.getTopUpAmount()));

        ConstraintLayout cstTopupHistory = (ConstraintLayout) rowView.findViewById(R.id.cstTopupHistory);

        return rowView;
    }
}
