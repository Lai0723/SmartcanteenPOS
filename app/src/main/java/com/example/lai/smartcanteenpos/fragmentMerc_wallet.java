package com.example.lai.smartcanteenpos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lai wei chun
 */
/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentMerc_wallet extends Fragment {

    TextView tvBalance,tvTestWalletID;
    public static boolean allowRefresh;

    Button btnOrdRtvScanner;

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
        btnOrdRtvScanner = inflatedView.findViewById(R.id.btnOrdRtvScanner);
        tvTestWalletID = (TextView) inflatedView.findViewById(R.id.tvTestWalletID);
        tvTestWalletID.setText(Menu_screen.Merc_WalletID);
        allowRefresh=false;

        //button to scan qr code to perform online order retrieval
        btnOrdRtvScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), onlineOrderRetrievalScanner.class);
                startActivity(intent);

            }
        });

        return inflatedView;
    }

    public void onResume() {
        super.onResume();
        if (allowRefresh)
        {
            allowRefresh = false;
            tvBalance.setText(String.format("RM %.2f" ,Menu_screen.balance ));
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

}
