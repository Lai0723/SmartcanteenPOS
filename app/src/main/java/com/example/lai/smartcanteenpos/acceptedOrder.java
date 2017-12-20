package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.Order_Adapter;

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
import java.util.ArrayList;


/**
 * Created by lai wei chun
 */
public class acceptedOrder extends Fragment {


    ListView acceptedOrder;
    ProgressDialog progressDialog;
    public static boolean allowRefresh;
    Button refreshButton;
    String MercName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accepted_order, container, false);
        allowRefresh = false;


        acceptedOrder = (ListView)v.findViewById(R.id.acceptedOrder);
        refreshButton = (Button)v.findViewById(R.id.refreshButton);
        progressDialog = new ProgressDialog(v.getContext());

        //check the list, if null reload the list
        if (Menu_screen.ORDERList == null) {
            Menu_screen.ORDERList = new ArrayList<>();

            String type = "retrieveOrder";
            MercName = Login.LOGGED_IN_USER;
            BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);

        } else {
            loadListing();
        }

        //refresh button to refresh the accepted order list
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity_order.allowRefresh = true;
                Menu_screen.ORDERList = null;

                Menu_screen.ORDERList = new ArrayList<>();

                String type = "retrieveOrder";
                MercName = Login.LOGGED_IN_USER;
                BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName);
            }
        });

        return v;
    }

    //to get accepted order info from the database
    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getAcceptedOrder.php";


            if (type == "retrieveOrder") {
                String MercName = params[1];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(retrieveURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("MercName", "UTF-8") + "=" + URLEncoder.encode(MercName, "UTF-8");

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
            progressDialog.setMessage("Retrieving Order");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    Menu_screen.ORDERList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        String OrderID = courseResponse.getString("OrderID");
                        String ProdName = courseResponse.getString("ProdName");
                        int OrderQuantity = Integer.parseInt(courseResponse.getString("OrderQuantity"));
                        String OrderDateTime = courseResponse.getString("OrderDateTime");
                        //Date OrderDateTime = new SimpleDateFormat("dd-MM-yyyy").parse(ODate);
                        String OrderStatus = courseResponse.getString("OrderStatus");



                        Order listing = new Order(OrderID, ProdName, OrderQuantity, OrderDateTime, OrderStatus);
                        Menu_screen.ORDERList.add(listing);
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    loadListing();




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


    //load the accepted order info to the adapter
    private void loadListing() {
        final Order_Adapter adapter = new Order_Adapter(getActivity(), R.layout.fragment_activity_order, Menu_screen.ORDERList);
        acceptedOrder.setAdapter(adapter);

    }






}
