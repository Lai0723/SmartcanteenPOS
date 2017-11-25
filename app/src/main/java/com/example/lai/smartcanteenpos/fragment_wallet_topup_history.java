package com.example.lai.smartcanteenpos;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_wallet_topup_history extends Fragment {

    ListView lvTopupHistory;
    List<TopUp> listTopup;
    TextView tvTopupTotal;

    double topupTotal;

    private static String URL_Topup_History = "https://martpay.000webhostapp.com/gab_select_topup.php";

    public static fragment_wallet_topup_history newInstance() {
        fragment_wallet_topup_history fragment = new fragment_wallet_topup_history();
        return fragment;
    }

    public fragment_wallet_topup_history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listTopup = new ArrayList<>();
        topupTotal =0;
        View view = inflater.inflate(R.layout.fragment_wallet_topup_history, container, false);

        tvTopupTotal = view.findViewById(R.id.tvTopupTotal);
        lvTopupHistory = view.findViewById(R.id.lvTopupHistory);
        if (!isConnected()) {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadTopup(getContext(), URL_Topup_History);

        return view;
    }

    public void downloadTopup(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray j = new JSONArray(response);
                                try {
                                    listTopup.clear();
                                    for (int i = 0; i < j.length(); i++) {
                                        JSONObject topupResponse = (JSONObject) j.get(i);
                                        String WalletID = topupResponse.getString("WalletID");
                                        //String CardNumber = topupResponse.getString("CardNumber");
                                        Double TopUpAmount = topupResponse.getDouble("TopUpAmount");
                                        String TopUpDateTime = topupResponse.getString("TopUpDateTime");

                                        TopUp topup = new TopUp(WalletID, TopUpAmount, TopUpDateTime);
                                        listTopup.add(topup);
                                        topupTotal = topupTotal + TopUpAmount;
                                    }
                                    loadTopup();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("walletID", MainActivity.walletID);
                    params.put("dateOfWalletHistory", wallet_history.dateOfWalletHistory);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTopup() {
        final adapter_list_topup_history adapter = new adapter_list_topup_history(getContext(), R.layout.fragment_wallet_topup_history, listTopup);
        lvTopupHistory.setAdapter(adapter);
        tvTopupTotal.setText("Top Up Total: " + String.format("RM %.2f", topupTotal));
        //Toast.makeText(getApplicationContext(), "Count :" + TList.size(), Toast.LENGTH_LONG).show();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

}
