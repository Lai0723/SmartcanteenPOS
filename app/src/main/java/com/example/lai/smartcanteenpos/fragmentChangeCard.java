package com.example.lai.smartcanteenpos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.stripe.android.model.Card;
//import com.stripe.android.view.CardInputWidget;


/**
 * Created by Gabb on 12/10/2017.
 */

public class fragmentChangeCard extends Fragment {

    ProgressDialog mErrorDialogHandler;
    //Card cardToSave;

    public static fragmentChangeCard newInstance() {
        fragmentChangeCard fragment = new fragmentChangeCard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_changecard, container, false);
        //CardInputWidget mCardInputWidget = (CardInputWidget) view.findViewById(R.id.card_input_widget2);
        //cardToSave = mCardInputWidget.getCard();


        // Inflate the layout for this fragment
        return view;


    }


}
