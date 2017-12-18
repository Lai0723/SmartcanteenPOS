/**
 * Created by Leow Wei Jian, RSD3 (September 2015 Intake) on 11/4/2017.
 * This is the Canteen Fragement to display selection of canteen for user. Currently is hardcoded.
 * Future work: made it dynamic with the use of Merchants table in database
 */
package com.example.lai.smartcanteenpos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class CanteenFragment extends Fragment {

    Button canteen1, canteen2, canteen3, canteen4;
    public static boolean allowRefresh;

    public CanteenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_canteen, container, false);
        allowRefresh = false;

        canteen1 = (Button)v.findViewById(R.id.buttonCanteen1);
        canteen2 = (Button)v.findViewById(R.id.buttonCanteen2);
        canteen3 = (Button)v.findViewById(R.id.buttonCanteen3);
        canteen4 = (Button)v.findViewById(R.id.buttonCanteen4);


        canteen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderMainActivity.setCanteen("Red Bricks Cafeteria");
                StallFragment nextFrag= new StallFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameOrderMain, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // buttonCanteen2, buttonCanteen3 and buttonCanteen4 are disabled
        // until function is added for them.
        canteen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature will be available on next update.",
                        Toast.LENGTH_LONG).show();
            }
        });

        canteen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature will be available on next update.",
                        Toast.LENGTH_LONG).show();
            }
        });

        canteen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This feature will be available on next update.",
                        Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    //Method for OrderMainActivity to call this fragment
    public static CanteenFragment newInstance() {
        CanteenFragment fragment = new CanteenFragment();
        return fragment;
    }

}
