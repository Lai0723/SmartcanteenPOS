/**
 * Created by Leow on 11/4/2017.
 * This is the Canteen Fragement to display selection of canteen stalls for user based on user's canteen selection. Currently is hardcoded.
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


public class StallFragment extends Fragment {

    Button buttonStall1, buttonStall2, buttonStall3, buttonStall4, buttonStall5, buttonStall6, buttonStall7, buttonStall8, buttonStall9;
    public static boolean allowRefresh;
    String canteenChoice = OrderMainActivity.getCanteen();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_stall, container, false);
        allowRefresh = false;

        buttonStall1 = v.findViewById(R.id.buttonStall1);
        buttonStall2 = v.findViewById(R.id.buttonStall2);
        buttonStall3 = v.findViewById(R.id.buttonStall3);
        buttonStall4 = v.findViewById(R.id.buttonStall4);
        buttonStall5 = v.findViewById(R.id.buttonStall5);
        buttonStall6 = v.findViewById(R.id.buttonStall6);
        buttonStall7 = v.findViewById(R.id.buttonStall7);
        buttonStall8 = v.findViewById(R.id.buttonStall8);
        buttonStall9 = v.findViewById(R.id.buttonStall9);

        switch (canteenChoice){
            case "Red Bricks Cafeteria":
                buttonStall1.setText(R.string.convenienceStore);
                buttonStall2.setText(R.string.mixedRice);
                buttonStall3.setText(R.string.masakanMalaysia);
                buttonStall4.setText(R.string.noodles);
                buttonStall5.setText(R.string.fresh);
                buttonStall6.setText(R.string.indoDeli);
                buttonStall7.setText(R.string.vegetarianFood);
                buttonStall8.setText(R.string.monsterCafe);
                buttonStall9.setText(R.string.mercTest);
                /*buttonStall9.setVisibility(View.INVISIBLE);
                buttonStall9.setClickable(false);*/

                buttonStall9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderMainActivity.setStall("lai");
                        OrderMenuFragment nextFrag= new OrderMenuFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameOrderMain, nextFrag,"findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });

                buttonStall1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                buttonStall8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "This feature will be available on next update.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                break;

                //add in cases for other canteen and set the buttons' text
                // to provide service for the stalls at other canteen.
        }

        return v;
    }


    /*@Override
    public void onAttach(Context context) {
       super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            stallIdentifier = (DataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
