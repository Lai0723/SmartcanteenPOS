package com.example.lai.smartcanteenpos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
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
import com.example.lai.smartcanteenpos.Obejct.Product;
import com.example.lai.smartcanteenpos.database.History;
import com.example.lai.smartcanteenpos.database.HistoryAdapter;
import com.example.lai.smartcanteenpos.database.History_Item;
import com.example.lai.smartcanteenpos.database.History_ItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Lim Boon Seng,RSD3 on 20/10/2017
 * Comment: This fragment display history lists for discount code history and item history
 *          For Discount Code History List, this is where you can find the discount code
 *          and by tapping once, the code is copied
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
    String RedeemCodeID;
    String RedeemItemID;
    ProgressDialog progressDialog;
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
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        allowRefresh = false;
        progressDialog = new ProgressDialog(rootView.getContext());
        if (!progressDialog.isShowing()) ;
        progressDialog.setMessage("Retrieving Redeem History");
        progressDialog.show();
        lvHistory = (ListView) rootView.findViewById(R.id.lvHistory);
        HistoryList = new ArrayList<>();
        lvHistory_item = (ListView) rootView.findViewById(R.id.lvHistory_item);
        HistoryList2 = new ArrayList<>();
        downloadListing(getActivity().getApplicationContext(), GET_HISTORY_URL);
        downloadListing_item(getActivity().getApplicationContext(), GET_HISTORY_URL_item);

        if (HistoryList == null) {
            HistoryList = new ArrayList<>();
            String type = "retrieveRedeemCodeHistory";
/*            BackgroundWorker backgroundWorker = new BackgroundWorker(rootView.getContext());
            backgroundWorker.execute(type,  RedeemCodeID);*/

        } else {
            loadListing();
        }

        if (HistoryList2 == null) {
            HistoryList2 = new ArrayList<>();
            String type = "retrieveRedeemItemHistory";
/*            BackgroundWorker backgroundWorker = new BackgroundWorker(rootView.getContext());
            backgroundWorker.execute(type,  RedeemItemID);*/

        } else {

            loadListing();
        }

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
                String type = "retrieveRedeemCodeHistory";
/*                BackgroundWorker backgroundWorker = new BackgroundWorker(rootView.getContext());
                backgroundWorker.execute(type, RedeemCodeID);*/
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
                String type = "retrieveRedeemItemHistory";
/*                BackgroundWorker2 backgroundWorker2 = new BackgroundWorker2(rootView.getContext());
                backgroundWorker2.execute(type, RedeemItemID);*/
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
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        final HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), R.layout.fragment_history, HistoryList);
        lvHistory.setAdapter(historyAdapter);
        final History_ItemAdapter history_itemAdapter = new History_ItemAdapter(getActivity(), R.layout.fragment_history, HistoryList2);
        lvHistory_item.setAdapter(history_itemAdapter);
        //Toast.makeText(getApplicationContext(), "Count :" + TransactionList.size(), Toast.LENGTH_LONG).show();
    }

/*    private class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        public BackgroundWorker(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String RetrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history.php";

            if (type == "retrieveRedeemCodeHistory") {
                String redeemCodeID = params[1];

                try {
                    //establish httpUrlConnection with POST method
                    URL url = new URL(RetrieveURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("RedeemCodeID", "UTF-8") + "=" + URLEncoder.encode(redeemCodeID, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    // read the data
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Retrieving Redeem History");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    HistoryList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        int redeemCodeID = Integer.parseInt(courseResponse.getString("RedeemCodeID"));
                        String desc = courseResponse.getString("Description");
                        String couponCode = courseResponse.getString("CouponCode");
                        String createAt = courseResponse.getString("createAt");
                        String walletID = courseResponse.getString("WalletID");
                        String redeemDate = courseResponse.getString("RedeemDate");
                        History history = new History(redeemCodeID, desc, couponCode, createAt, walletID, redeemDate);
                        HistoryList.add(history);
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    loadListing();
                    Toast.makeText(getView().getContext(), "", Toast.LENGTH_LONG).show();



                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    private class BackgroundWorker2 extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        public BackgroundWorker2(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String RetrieveURL2 ="https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/get_history_item.php";

            if (type == "retrieveRedeemItemHistory") {
                String redeemItemID = params[1];

                try {
                    //establish httpUrlConnection with POST method
                    URL url = new URL(RetrieveURL2);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("RedeemItemID", "UTF-8") + "=" + URLEncoder.encode(redeemItemID, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    // read the data
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Retrieving Redeem History");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    HistoryList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse2= (JSONObject) jsonArray.get(i);

                        int redeemItemID = Integer.parseInt(courseResponse2.getString("RedeemItemID"));
                        String desc2 = courseResponse2.getString("Description");
                        String itemCode = courseResponse2.getString("ItemCode");
                        String createAt2 = courseResponse2.getString("createAt");
                        String walletID2 = courseResponse2.getString("WalletID");
                        String redeemDate2 = courseResponse2.getString("RedeemDate");
                        History_Item history_item = new History_Item(redeemItemID, desc2, itemCode, createAt2, walletID2, redeemDate2);
                        HistoryList2.add(history_item);
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    loadListing();
                    Toast.makeText(getView().getContext(), "", Toast.LENGTH_LONG).show();



                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }*/

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

