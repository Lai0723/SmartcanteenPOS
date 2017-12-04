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
import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.database.Orders;
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
public class fragment_wallet_transaction_history extends Fragment {

    ListView lvTransactionHistory;
    //List<Orders> listTransaction;
    TextView tvTransactionTotal;

    //double transactionTotal;

    private static String URL_Transaction_History = "https://martpay.000webhostapp.com/gab_select_transaction.php";

    public static fragment_wallet_transaction_history newInstance() {
        fragment_wallet_transaction_history fragment = new fragment_wallet_transaction_history();
        return fragment;
    }

    public fragment_wallet_transaction_history() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_wallet_transaction_history, container, false);

        tvTransactionTotal = view.findViewById(R.id.tvTransactionTotal);
        lvTransactionHistory = view.findViewById(R.id.lvTransactionHistory);
        if (!isConnected()) {
            Toast.makeText(getContext(), "No network", Toast.LENGTH_LONG).show();
        }
        if(wallet_history.listTransaction==null){
            wallet_history.listTransaction = new ArrayList<>();
            wallet_history.transactionTotal = 0;
            downloadTransaction(getContext(), URL_Transaction_History);
        }else{
            loadTransaction();
        }


        return view;
    }

    public void downloadTransaction(Context context, String url) {
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
                                    wallet_history.listTransaction.clear();
                                    for (int i = 0; i < j.length(); i++) {
                                        JSONObject transactionResponse = (JSONObject) j.get(i);
                                        /*int TransferID = transferResponse.getInt("TransferID");
                                        String GiverID = transferResponse.getString("GiverWalletID");
                                        String ReceiverID = transferResponse.getString("ReceiverWalletID");
                                        Double Amount = transferResponse.getDouble("TransferAmount");
                                        String TransferDate = transferResponse.getString("TransferDate");*/
                                        //Transfer transfer = new Transfer(TransferID,TransferDate, Amount, GiverID, ReceiverID);
                                        int OrderID = transactionResponse.getInt("OrderID");
                                        int ProdID = transactionResponse.getInt("ProdID");
                                        int OrderQuantity = transactionResponse.getInt("OrderQuantity");
                                        String OrderDateTime = transactionResponse.getString("OrderDateTime");
                                        String PayDateTime = transactionResponse.getString("PayDateTime");
                                        Double PayAmount = transactionResponse.getDouble("PayAmount");

                                        Orders orders = new Orders(OrderID, ProdID, OrderQuantity, OrderDateTime, PayDateTime,PayAmount );
                                        wallet_history.listTransaction.add(orders);
                                        wallet_history.transactionTotal = wallet_history.transactionTotal + PayAmount;
                                    }
                                    loadTransaction();

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

    private void loadTransaction() {
        final adapter_list_transaction_history adapter = new adapter_list_transaction_history(getContext(), R.layout.fragment_wallet_transaction_history, wallet_history.listTransaction);
        lvTransactionHistory.setAdapter(adapter);
        tvTransactionTotal.setText("Online Spent Total: " + String.format("RM %.2f", wallet_history.transactionTotal));
        //Toast.makeText(getApplicationContext(), "Count :" + TList.size(), Toast.LENGTH_LONG).show();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

}
