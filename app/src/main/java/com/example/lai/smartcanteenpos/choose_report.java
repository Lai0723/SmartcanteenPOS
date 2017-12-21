package com.example.lai.smartcanteenpos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Lai Wei Chun, RSD3, 2017
 */
public class choose_report extends Fragment {


    Button btnTransferReport,btnOnlineOrderReport;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choose_report, container, false);

        btnTransferReport = (Button)v.findViewById(R.id.btnTransferReport);
        btnOnlineOrderReport = (Button)v.findViewById(R.id.btnOnlineOrderReport);


        btnTransferReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report_transfer nextFrag= new report_transfer();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnOnlineOrderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report nextFrag= new report();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


        return v;
    }

}
