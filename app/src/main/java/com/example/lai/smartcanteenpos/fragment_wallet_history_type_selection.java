/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
package com.example.lai.smartcanteenpos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
//Default fragment when user enter student wallet history
public class fragment_wallet_history_type_selection extends Fragment {

    TextView tvSelectType;

    public static fragment_wallet_history_type_selection newInstance() {
        fragment_wallet_history_type_selection fragment = new fragment_wallet_history_type_selection();
        return fragment;
    }

    public fragment_wallet_history_type_selection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet_history_type_selection, container, false);

        tvSelectType = view.findViewById(R.id.tvSelectType);
        tvSelectType.setText("Please select history type from navigation bar below");

        return view;
    }

}
