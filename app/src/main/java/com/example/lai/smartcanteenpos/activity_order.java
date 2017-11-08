package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.Order_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class activity_order extends Fragment {

    RequestQueue queue;
   // private static final String TAG = "OrderActivity";
    ListView orderlist;
    ProgressDialog progressDialog;
    Button btnOrderAccept,btnOrderDecline;
    EditText OrderID;
    public static boolean allowRefresh;
    public static final String TAG = "com.example.user.myApp";
    String id;

    String MercName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_activity_order, container, false);

        View v = inflater.inflate(R.layout.fragment_activity_order,container,false);
        allowRefresh = false;


        orderlist = (ListView)v.findViewById(R.id.orderlistview);
        btnOrderAccept = (Button)v.findViewById(R.id.btnOrderAccept);
        btnOrderDecline = (Button)v.findViewById(R.id.btnOrderDecline);
        OrderID = (EditText)v.findViewById(R.id.txtOrderid);

        progressDialog = new ProgressDialog(v.getContext());



        btnOrderAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = OrderID.getText().toString();
                //check is empty
                if (TextUtils.isEmpty(id)) {

                    Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                } else {
                    progressDialog = new ProgressDialog(getView().getContext());
                    String type = "Checking Stock";

                    activity_order.BackgroundWorker backgroundWorker2 = new activity_order.BackgroundWorker(v.getContext());
                    backgroundWorker2.execute(type, id);
                    OrderFragment.allowRefresh = true;

                    Menu_screen.ORDERList = null;
                }
            }
        });


        btnOrderDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 id = OrderID.getText().toString();
                //check is empty
                if (TextUtils.isEmpty(id)) {

                    Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                } else {
                    progressDialog = new ProgressDialog(getView().getContext());
                    DeclineOrder(getView().getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/declined%20order.php");
                    OrderFragment.allowRefresh = true;

                    Menu_screen.ORDERList = null;
                }
            }
        });





        if (Menu_screen.ORDERList == null) {
            Menu_screen.ORDERList = new ArrayList<>();

            String type = "retrieveOrder";
            MercName = Login.LOGGED_IN_USER;
            activity_order.BackgroundWorker backgroundWorker = new activity_order.BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);

        } else {
            loadListing();
        }
        return v;
    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getOrder.php";


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
                        String ODate = courseResponse.getString("OrderDateTime");
                        Date OrderDateTime = new SimpleDateFormat("dd-MM-yyyy").parse(ODate);
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



    private void loadListing() {
        final Order_Adapter adapter = new Order_Adapter(getActivity(), R.layout.fragment_activity_order, Menu_screen.ORDERList);
        orderlist.setAdapter(adapter);

    }




    public void DeclineOrder(Context context, String url) {
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
                            //response =string returned by server to the client
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 0) {

                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                } else {

                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getView().getContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("OrderID", OrderID.getText().toString());

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

}
