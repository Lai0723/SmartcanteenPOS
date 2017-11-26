package com.example.lai.smartcanteenpos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

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

import com.example.lai.smartcanteenpos.Obejct.MenuAdapter;
import com.example.lai.smartcanteenpos.Obejct.Product;

public class OrderMenuFragment extends Fragment {

    public String MercName = OrderMainActivity.getStall();
    public static boolean allowRefresh;
    public static final String TAG = "my.edu.tarc.order";

    GridView gridViewMenu;
    ProgressDialog progressDialog;
    RequestQueue queue;

    public OrderMenuFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_menu, container, false);
        allowRefresh = false;
        gridViewMenu = v.findViewById(R.id.gridViewMenu);

        progressDialog = new ProgressDialog(v.getContext());

        if(OrderMainActivity.listMenu == null){
            OrderMainActivity.listMenu = new ArrayList<>();
            String type = "retrieveProduct";
            BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);
        }
        else {
            loadListing();
        }
        gridViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Product chosenProd = (Product) parent.getItemAtPosition(position);
                OrderMainActivity.setProdID(chosenProd.getProdID());
                OrderMainActivity.setProdName(chosenProd.getProdName());
                OrderMainActivity.setProdDesc(chosenProd.getProdDesc());
                OrderMainActivity.setProdPrice(chosenProd.getPrice());
                OrderingFragment nextFrag= new OrderingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameOrderMain, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
            return v;
    }

    private void loadListing() {
        final MenuAdapter adapter = new MenuAdapter(getActivity(), R.layout.fragment_order_menu, OrderMainActivity.listMenu);
        gridViewMenu.setAdapter(adapter);

    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        AlertDialog alertDialog;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String RetrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getlist.php";

            // if the type of the task = retrieveBorrowHistory
            if (type == "retrieveProduct") {
                String MercName = params[1];

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
/*            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Retrieving Menu Item");
            progressDialog.show();*/


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {

                    OrderMainActivity.listMenu.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse= (JSONObject) jsonArray.get(i);



                        String ProdID = courseResponse.getString("ProdID");
                        String ProdName = courseResponse.getString("ProdName");
                        String ProdCat = courseResponse.getString("ProdCat");
                        String ProdDesc = courseResponse.getString("ProdDesc");
                        double ProdPrice = Double.parseDouble(courseResponse.getString("ProdPrice"));
                        int ProdQty = Integer.parseInt(courseResponse.getString("ProdQty"));
                        String ImageURL = courseResponse.getString("url");


                        Product listing = new Product(ProdID, ProdName, ProdCat, ProdDesc, ProdPrice, ProdQty, ImageURL);
                        OrderMainActivity.listMenu.add(listing);

                    }

                    /*if (progressDialog.isShowing())
                        progressDialog.dismiss();*/

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

    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
        OrderMainActivity.listMenu = null;
    }
}


