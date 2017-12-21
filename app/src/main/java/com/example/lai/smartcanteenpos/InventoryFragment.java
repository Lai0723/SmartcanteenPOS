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

import com.android.volley.RequestQueue;
import com.example.lai.smartcanteenpos.Obejct.Inventory;
import com.example.lai.smartcanteenpos.Obejct.InventoryAdapter;

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
 * Created by Lai Wei Chun, RSD3, 2017
 */
public class InventoryFragment extends Fragment {

    public static boolean allowRefresh;
    public static final String TAG = "com.example.user.myApp";

    ListView InventoryList;
    RequestQueue queue;
    String MercName;
    ProgressDialog progressDialog;
    Button btnpurchase,btnview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        allowRefresh = false;
        InventoryList = (ListView)v.findViewById(R.id.InventoryList);



        btnpurchase = (Button)v.findViewById(R.id.btnPurchase);
        btnview = (Button)v.findViewById(R.id.btnView);


        progressDialog = new ProgressDialog(v.getContext());


        btnpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Make_Purchase_Order nextFrag= new Make_Purchase_Order ();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View_Purchase_Order nextFrag= new View_Purchase_Order();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });




        if (Menu_screen.SList == null) {
            Menu_screen.SList = new ArrayList<>();
            String type = "retrieveProductQuantity";
            MercName = Login.LOGGED_IN_USER;
            InventoryFragment.BackgroundWorker backgroundWorker = new InventoryFragment.BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);
            //downloadListing(getActivity().getApplicationContext(), GET_URL);
        } else {
            loadListing();
        }
        return v;
    }

    private void loadListing() {
        final InventoryAdapter adapter = new InventoryAdapter(getActivity(), R.layout.fragment_inventory, Menu_screen.SList);
        InventoryList.setAdapter(adapter);

    }


   private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getSupplier.php";


            if (type == "retrieveProductQuantity") {
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
            progressDialog.setMessage("Retrieving Product");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    Menu_screen.SList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        String ProdID = courseResponse.getString("ProdID");
                        String ProdName = courseResponse.getString("ProdName");
                        int ProdQuantity= Integer.parseInt(courseResponse.getString("ProdQuantity"));



                        Inventory listing = new Inventory(ProdID,ProdName,ProdQuantity);
                        Menu_screen.SList.add(listing);

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


}
