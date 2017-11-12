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
public class fragmentMerc_wallet extends Fragment {

    TextView tvBalance,tvTestWalletID;
    public static boolean allowRefresh;

    public static fragmentMerc_wallet newInstance() {
        fragmentMerc_wallet fragment = new fragmentMerc_wallet();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_merc_wallet, container, false);

        tvBalance = (TextView) inflatedView.findViewById(R.id.tvMercBalance);
        tvTestWalletID = (TextView) inflatedView.findViewById(R.id.tvTestWalletID);
        tvTestWalletID.setText(Menu_screen.Merc_WalletID);
        allowRefresh=false;

        return inflatedView;
    }

    public void onResume() {
        super.onResume();
        /*if (allowRefresh)
        {
            allowRefresh = false;
            tvBalance.setText(String.format("RM %.2f" ,MainActivity.balance ));
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }*/
    }

}
