/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class onlineOrderRetrievalScanner extends AppCompatActivity {

    TextView tvOrderID, tvWalletID, tvProdID, tvProdName, tvOrderDateTime, tvQuantity, tvStatus;


    String OrderID, ProdID, WalletID, ProdName, OrderDateTime,  OrderStatus, PayDateTime, PayAmount, OrderQuantity, retrievalScannedTime;
    String StatusCompleted="Completed";
    String cancel ="Cancelled";
    double doublePayAmount;
    int intOrderQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_order_retrieval_scanner);

        tvOrderID = findViewById(R.id.tvOrderID);
        tvWalletID = findViewById(R.id.tvWalletID);
        tvProdID = findViewById(R.id.tvProdID);
        tvProdName = findViewById(R.id.tvProdName);
        tvOrderDateTime = findViewById(R.id.tvOrderDateTime);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvStatus = findViewById(R.id.tvStatus);

        startRetrievalScan();

    }

    //Activate QR scanner
    public void startRetrievalScan() {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }

    //Decode result from scanned QR code
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                showRetrievalCancelled();
            } else {
                String[] arr = result.getContents().split(",");
                if (arr[0].equals("orderRetrieval")) {
                    OrderID = arr[1];
                    ProdID = arr[2];
                    WalletID = arr[3];
                    ProdName = arr[4];
                    OrderDateTime = arr[5];
                    OrderStatus = arr[6];
                    PayDateTime = arr[7];
                    PayAmount = arr[8];
                    OrderQuantity = arr[9];
                    retrievalScannedTime = arr[10];

                    //updateOrderStatus(this, "https://martpay.000webhostapp.com/gab_update_order_status.php", OrderID, StatusCompleted);

                    showRetrievalSuccess();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Button to confirm retrieval  completed and update order status
    public void btnRetrievalCompleteClick(View view){
        updateOrderStatus(this, "https://martpay.000webhostapp.com/gab_update_order_status.php", OrderID, StatusCompleted);
    }

    //Button to click when order cannot be retrieve yet (Does not update order status)
    public void btnProcessing (View view){

        finish();

    }

    //Show information of scanned retrieval QR code
    public void showRetrievalSuccess(){
        tvOrderID.setText("Order ID: "+OrderID);
        tvWalletID.setText("From: "+WalletID);
        tvProdID.setText("Product ID: "+ProdID);
        tvProdName.setText("Item: "+ProdName);
        tvQuantity.setText("Quantity: "+OrderQuantity);
        tvOrderDateTime.setText("Ordered on "+OrderDateTime);
        tvStatus.setText("Status: "+OrderStatus);
    }

    //Information to show when scanning cancelled by merchant
    public void showRetrievalCancelled(){
        tvOrderID.setText("Order ID: "+cancel);
        tvWalletID.setText("From: "+cancel);
        tvProdID.setText("Product ID: "+cancel);
        tvProdName.setText("Item: "+cancel);
        tvQuantity.setText("Quantity: "+cancel);
        tvOrderDateTime.setText("Ordered on "+cancel);
        tvStatus.setText("Status: "+cancel);
    }

    public void updateOrderStatus(Context context, String url, final String OrderID, final String StatusCompleted) {
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
                            JSONObject jsonObject;
                            try {
                                String err = "";
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else if (success == 1) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                    //setResult(Activity.RESULT_OK);
                                    //WalletFragment.allowRefresh = true;
                                    //finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }

                                if (err.length() > 0) {
                                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("OrderID", OrderID);
                    params.put("StatusCompleted", StatusCompleted);
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
