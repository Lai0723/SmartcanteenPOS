package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lai.smartcanteenpos.Obejct.PurchaseOrderAdapter;
import com.example.lai.smartcanteenpos.Obejct.Purchase_order;

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
 * A simple {@link Fragment} subclass.
 */
public class View_Purchase_Order extends Fragment {
    public static boolean allowRefresh;
    ListView POlist;
    String MercName;
    ProgressDialog progressDialog;
    Button btnPOcancel;
    public static String ProdName,PurchaseQuantity,PurchseOrderID;

    public View_Purchase_Order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view__purchase__order, container, false);

        allowRefresh = false;

        POlist = (ListView)v.findViewById(R.id.POlist);
        btnPOcancel = (Button)v.findViewById(R.id.btnPOcancel);
        progressDialog = new ProgressDialog(v.getContext());


        POlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Purchase_order entry = (Purchase_order) parent.getItemAtPosition(position);

                ProdName = entry.getProdName();
                PurchaseQuantity = entry.getPurchaseQuantity();
                PurchseOrderID = entry.getPOID();

                add_inventory nextFrag= new add_inventory();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });

        btnPOcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                InventoryFragment nextFrag= new InventoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });


        if (Menu_screen.OList == null) {
            Menu_screen.OList = new ArrayList<>();
            String type = "retrievePurchaseOrder";
            MercName = Login.LOGGED_IN_USER;
            View_Purchase_Order.BackgroundWorker backgroundWorker = new View_Purchase_Order.BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);

        } else {
            loadListing();
        }
        return v;
    }

    private void loadListing() {
        final PurchaseOrderAdapter adapter = new PurchaseOrderAdapter(getActivity(), R.layout.fragment_view__purchase__order, Menu_screen.OList);
        POlist.setAdapter(adapter);

    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getPurchaseOrder.php";


            if (type == "retrievePurchaseOrder") {
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
            progressDialog.setMessage("Retrieving Purchase Order");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    Menu_screen.OList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        String POID = courseResponse.getString("POID");
                        String ProdName = courseResponse.getString("ProdName");

                        String SupplierName = courseResponse.getString("SupplierName");
                        String PurchaseQuantity= courseResponse.getString("PurchaseQuantity");
                        Double Fee = Double.parseDouble(courseResponse.getString("Fee"));

                        String retrieveDate = courseResponse.getString("PurchaseDate");



                        Purchase_order listing = new Purchase_order(POID,ProdName,SupplierName,PurchaseQuantity,Fee,retrieveDate);
                        Menu_screen.OList.add(listing);

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
