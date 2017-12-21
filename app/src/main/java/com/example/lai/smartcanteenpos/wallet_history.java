/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.database.Orders;
import com.example.lai.smartcanteenpos.database.TopUp;
import com.example.lai.smartcanteenpos.database.Transfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class wallet_history extends AppCompatActivity {

    String walletID = MainActivity.walletID;
    static String dateOfWalletHistory;

    public static List<Transfer> listTransfer;
    public static List<Orders> listTransaction;
    public static List<TopUp> listTopup;

    public static double topupTotal;
    public static double transactionTotal;
    public static double transferTotal;


    private static String URL_Transfer_History = "https://martpay.000webhostapp.com/gab_select_transfer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.walletHistoryNavi);

        //initiate Lists
        listTransfer=null;
        listTransaction=null;
        listTopup=null;



        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        dateOfWalletHistory = extras.getString("dateOfWalletHistory");

        //downloadTransfer(getApplicationContext(), URL_Transfer_History);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_wallet_history, fragment_wallet_history_type_selection.newInstance());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_wallet_transfer_history:

                        //downloadTransfer(getApplicationContext(), URL_Transfer_History);
                        fragment = fragment_wallet_transfer_history.newInstance();
                        break;
                    case R.id.action_wallet_transaction_history:
                        //checkCard(MainActivity.this, "https://gabriellb-wp14.000webhostapp.com/select_user.php");
                        //checkCard(MainActivity.this, "https://martpay.000webhostapp.com/gab_select_user.php");
                        fragment = fragment_wallet_transaction_history.newInstance();
                        break;
                    case R.id.action_wallet_topup_history:
                        fragment = fragment_wallet_topup_history.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_main_wallet_history, fragment);
                transaction.commit();
                return true;
            }
        });

    }



}
