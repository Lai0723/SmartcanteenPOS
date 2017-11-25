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
public class fragment_wallet_transfer_history extends Fragment {
    ListView lvTransferHistory;
    List<Transfer> listTransfer;
    TextView tvTransferTotal;

    double transferTotal;

    private static String URL_Transfer_History = "https://martpay.000webhostapp.com/gab_select_transfer.php";

    public static fragment_wallet_transfer_history newInstance() {
        fragment_wallet_transfer_history fragment = new fragment_wallet_transfer_history();
        return fragment;
    }

    public fragment_wallet_transfer_history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listTransfer = new ArrayList<>();
        transferTotal =0;

        View view =inflater.inflate(R.layout.fragment_wallet_transfer_history, container, false);

        TextView tvlblTransferHistory = view.findViewById(R.id.tvlblTransferHistory);
        tvTransferTotal = view.findViewById(R.id.tvTransferTotal);
        //tvlblTransferHistory.setText(wallet_history.dateOfWalletHistory); //check date passed from date picker
        lvTransferHistory =view.findViewById(R.id.lvTransferHistory);
        if (!isConnected()) {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadTransfer(getContext(), URL_Transfer_History);

        return view;
    }

    public void downloadTransfer(Context context, String url) {
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
                                    listTransfer.clear();
                                    for (int i = 0; i < j.length(); i++) {
                                        JSONObject transferResponse = (JSONObject) j.get(i);
                                        int TransferID = transferResponse.getInt("TransferID");
                                        String GiverID = transferResponse.getString("GiverWalletID");
                                        String ReceiverID = transferResponse.getString("ReceiverWalletID");
                                        Double Amount = transferResponse.getDouble("TransferAmount");
                                        String TransferDate = transferResponse.getString("TransferDate");
                                        Transfer transfer = new Transfer(TransferID,TransferDate, Amount, GiverID, ReceiverID);
                                        listTransfer.add(transfer);
                                        transferTotal=transferTotal+Amount;
                                    }
                                    loadTransfer();

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

    private void loadTransfer() {
        final adapter_list_transfer_history adapter = new adapter_list_transfer_history(getContext(), R.layout.fragment_wallet_transfer_history, listTransfer);
        lvTransferHistory.setAdapter(adapter);
        tvTransferTotal.setText("Transfer out Total: "+ String.format("RM %.2f",transferTotal));
        //Toast.makeText(getApplicationContext(), "Count :" + TList.size(), Toast.LENGTH_LONG).show();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

}
