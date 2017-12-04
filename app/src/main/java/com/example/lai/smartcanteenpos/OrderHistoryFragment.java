/**
 * Created by Leow on 11/4/2017.
 * This is the Order History Fragement to display list view of orders made by logged in user
 */
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
import android.widget.ListView;
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

import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.OrderHistoryAdapter;


public class OrderHistoryFragment extends Fragment {
    public static boolean allowRefresh;
    public static final String TAG = "my.edu.tarc.order";

    ListView listViewOrderHistory;
    ProgressDialog progressDialog;
    RequestQueue queue;
    public String walletID = OrderHistoryActivity.getwID();


    public OrderHistoryFragment() {
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
        OrderHistoryActivity.listOrder = null;
        View v =  inflater.inflate(R.layout.fragment_order_history, container, false);
        allowRefresh = false;
        listViewOrderHistory = v.findViewById(R.id.listViewOrderHistory);
        progressDialog = new ProgressDialog(v.getContext());
        if (OrderHistoryActivity.listOrder == null) {
            OrderHistoryActivity.listOrder = new ArrayList<>();

            String type = "retrieveOrderHistory";
            BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  walletID);


        } else {
            loadListing();
        }

        listViewOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Order chosenOrder = (Order) parent.getItemAtPosition(position);
                OrderHistoryActivity.setOrderID(chosenOrder.getOrderID());
                OrderHistoryActivity.setProdID(chosenOrder.getProdID()+" ");
                OrderHistoryActivity.setProdName(chosenOrder.getProdName());
                OrderHistoryActivity.setOrderDateTime(chosenOrder.getOrderDateTime());
                OrderHistoryActivity.setOrderQuantity(chosenOrder.getOrderQuantity());
                OrderHistoryActivity.setOrderTotal(chosenOrder.getPayAmount());
                OrderHistoryActivity.setOrderStatus(chosenOrder.getOrderStatus());
                OrderHistoryActivity.setPayDateTime(chosenOrder.getPayDateTime());
                OrderDetailFragment nextFrag= new OrderDetailFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameOrderHistory, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    //Load the items into the list view from Orders table in the database
    private void loadListing() {
        final OrderHistoryAdapter adapter = new OrderHistoryAdapter(getActivity(), R.layout.fragment_order_menu, OrderHistoryActivity.listOrder);
        listViewOrderHistory.setAdapter(adapter);

    }

    //Async task for the loadListing to work with
    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        AlertDialog alertDialog;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String borrowURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getOrderHistory.php";

            // if the type of the task = retrieveBorrowHistory
            if (type == "retrieveOrderHistory") {
                String walletID = params[1];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(borrowURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("WalletID", "UTF-8") + "=" + URLEncoder.encode(walletID, "UTF-8");

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
            progressDialog.setMessage("Retrieving Order History");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    OrderHistoryActivity.listOrder.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse= (JSONObject) jsonArray.get(i);

                        String orderID = courseResponse.getString("OrderID");
                        int prodID = Integer.parseInt(courseResponse.getString("ProdID"));
                        String orderDateTime = courseResponse.getString("OrderDateTime");
                        int orderQuantity = Integer.parseInt(courseResponse.getString("OrderQuantity"));
                        String orderStatus = courseResponse.getString("OrderStatus");
                        double originalPrice = Double.parseDouble(courseResponse.getString("OrderPrice"));
                        double payAmount = Double.parseDouble(courseResponse.getString("PayAmount"));
                        double difference = Double.parseDouble(courseResponse.getString("PriceDifference"));
                        String promotionApplied = courseResponse.getString("PromotionApplied");
                        String payDateTime = courseResponse.getString("PayDateTime");
                        String prodName = courseResponse.getString("ProdName");

                        if (orderStatus == "Accepted" || orderStatus == "Cancelled"){
                            Order history = new Order(orderID, walletID, prodID, prodName, orderDateTime,
                                    orderQuantity, orderStatus, originalPrice, payAmount, difference,
                                    promotionApplied, null);
                            OrderHistoryActivity.listOrder.add(history);
                        }
                        else{
                            Order history = new Order(orderID, walletID, prodID, prodName, orderDateTime,
                                    orderQuantity, orderStatus, originalPrice, payAmount, difference,
                                    promotionApplied, payDateTime);
                            OrderHistoryActivity.listOrder.add(history);
                        }

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

    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}
