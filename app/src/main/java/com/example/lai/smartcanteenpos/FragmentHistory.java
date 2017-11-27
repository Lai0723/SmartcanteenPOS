package com.example.lai.smartcanteenpos;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.database.History;
import com.example.lai.smartcanteenpos.database.HistoryAdapter;
import com.example.lai.smartcanteenpos.database.History_Item;
import com.example.lai.smartcanteenpos.database.History_ItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Boon Seng
 */

public class FragmentHistory extends Fragment {
    public static boolean allowRefresh;
    public static final String TAG = "com.example.a45vd.SmartCanteen";
    private static String GET_HISTORY_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history.php";
    private static String GET_HISTORY_URL_item = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history_item.php";

    ListView lvHistory;
    List<History> HistoryList;
    ListView lvHistory_item;
    List<History_Item> HistoryList2;

    RequestQueue queue;

    public static FragmentHistory newInstance() {
        FragmentHistory fragment = new FragmentHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        allowRefresh = false;
        lvHistory = (ListView) rootView.findViewById(R.id.lvHistory);
        HistoryList = new ArrayList<>();
        lvHistory_item = (ListView) rootView.findViewById(R.id.lvHistory_item);
        HistoryList2 = new ArrayList<>();
        downloadListing(getActivity().getApplicationContext(), GET_HISTORY_URL);
        downloadListing_item(getActivity().getApplicationContext(), GET_HISTORY_URL_item);

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                History entry = (History) parent.getItemAtPosition(position);
                downloadListing(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history.php");
                ClipboardManager myClipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                String text = entry.getCouponCode();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getActivity(),"Code copied",Toast.LENGTH_LONG).show();
            }
        });

        lvHistory_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                History_Item entry2 = (History_Item) parent.getItemAtPosition(position);
                downloadListing_item(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history_item.php");
                ClipboardManager myClipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                String text = entry2.getItemCode();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getActivity(),"Code copied",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    public void downloadListing(Context context, String url) {
        queue = Volley.newRequestQueue(context);
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            HistoryList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject historyResponse = (JSONObject) response.get(i);
                                int redeemCodeID = Integer.parseInt(historyResponse.getString("RedeemCodeID"));
                                String desc = historyResponse.getString("Description");
                                String couponCode = historyResponse.getString("CouponCode");
                                String createAt = historyResponse.getString("createAt");
                                String walletID = historyResponse.getString("WalletID");
                                String redeemDate = historyResponse.getString("RedeemDate");
                                History history = new History(redeemCodeID, desc, couponCode, createAt, walletID, redeemDate);
                                HistoryList.add(history);
                            }
                            loadListing();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Error:" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    public void downloadListing_item(Context context, String url) {
        queue = Volley.newRequestQueue(context);
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            HistoryList2.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject historyResponse = (JSONObject) response.get(i);
                                int redeemItemID = Integer.parseInt(historyResponse.getString("RedeemItemID"));
                                String desc = historyResponse.getString("Description");
                                String itemCode = historyResponse.getString("ItemCode");
                                String createAt = historyResponse.getString("createAt");
                                String walletID = historyResponse.getString("WalletID");
                                String redeemDate = historyResponse.getString("RedeemDate");
                                History_Item history_item = new History_Item(redeemItemID, desc, itemCode, createAt, walletID, redeemDate);
                                HistoryList2.add(history_item);
                            }
                            loadListing();

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Error:" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }


    private void loadListing() {
        final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), R.layout.fragment_history, HistoryList);
        lvHistory.setAdapter(historyAdapter);
        final History_ItemAdapter history_itemAdapter = new History_ItemAdapter(getActivity(), R.layout.fragment_history, HistoryList2);
        lvHistory_item.setAdapter(history_itemAdapter);
        //Toast.makeText(getApplicationContext(), "Count :" + TransactionList.size(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (allowRefresh) {
            allowRefresh = false;
            if (RedeemMainActivity.hList == null) {
                RedeemMainActivity.hList = new ArrayList<>();
                downloadListing(getActivity().getApplicationContext(), GET_HISTORY_URL);
                downloadListing_item(getActivity().getApplicationContext(), GET_HISTORY_URL_item);
            } else {
                loadListing();
            }
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}

