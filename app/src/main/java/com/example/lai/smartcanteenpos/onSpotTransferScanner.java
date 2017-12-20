package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by lai wei chun
 */
public class onSpotTransferScanner extends AppCompatActivity {

    String ttlPurchaseAmt, giverID, purchasedIDGet;
    String[] purchasedID;
    double balanceToCheck, balance, showTtl;
    TextView tvTransferStatus, tvTransferFrom, tvTransferTo, tvTransferAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_spot_transfer_scanner);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        ttlPurchaseAmt = extras.getString("ttlPurchaseAmt");
        purchasedIDGet = extras.getString("purchasedID");
        purchasedID = purchasedIDGet.split(";;");

        showTtl = Double.parseDouble(ttlPurchaseAmt);

        tvTransferStatus = (TextView) findViewById(R.id.tvTransferStatus);
        tvTransferFrom = (TextView) findViewById(R.id.tvTransferFrom);
        tvTransferTo = (TextView) findViewById(R.id.tvTransferTo);
        tvTransferAmount = (TextView) findViewById(R.id.tvTransferAmount);

        startScan();

    }

    //Activate on spot transfer QR scanner
    public void startScan() {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }

    // Decode information from scanned QR code and insert new transfer record and show transfer result on screen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                showStatusCancelled();
            } else {
                String[] arr = result.getContents().split(",");
                if (!arr[0].equals("orderRetrieval")) {
                    giverID = arr[0];
                    String balanceToChk = arr[1];
                    String date = arr[2];
                    String receiverID = Menu_screen.Merc_WalletID;

                    //get current date time
                    java.util.Date dt = new java.util.Date();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timeNow = sdf.format(dt);

                    java.util.Date d1 = null;
                    java.util.Date d2 = null;

                    try {
                        d1 = sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        d2 = sdf.parse(timeNow);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Calculate time diff
                    long diff = d2.getTime() - d1.getTime();
                    long diffSeconds = diff / 1000;

                    if (giverID.equals(receiverID)) {
                        Toast.makeText(this, "You cannot transfer money to yourself.", Toast.LENGTH_SHORT).show();
                    } else if (diffSeconds > 30) {
                        Toast.makeText(this, "Code expired, generate code again.", Toast.LENGTH_SHORT).show();
                    } else {
                        balanceToCheck = Double.parseDouble(balanceToChk);
                        Double ttlPurchaseAmount = Double.parseDouble(ttlPurchaseAmt);
                        if (balanceToCheck > ttlPurchaseAmount) {
                            try {
                                insertTransfer(this, "https://martpay.000webhostapp.com/gab_insert_transfer.php", giverID, ttlPurchaseAmt, date, receiverID);
                                fragmentMerc_wallet.allowRefresh = true;
                                for (String productID : purchasedID) {
                                    updateInventory(this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/payment_update_inventory.php", productID);
                                }
                                showStatusSuccess();
                                InventoryFragment.allowRefresh = true;

                                Menu_screen.SList = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else if (balanceToCheck < ttlPurchaseAmount) {
                            Toast.makeText(this, "Insufficient balance.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void insertTransfer(Context context, String url, final String giverID, final String ttlPurchaseAmt, final String date, final String receiverID) {
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
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 1) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    //balance += Double.parseDouble(ttlPurchaseAmt);
                                    balance += Double.parseDouble(ttlPurchaseAmt);
                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content, fragmentMerc_wallet.newInstance());
                                    transaction.commit();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    params.put("giverID", giverID);
                    params.put("amount", ttlPurchaseAmt);
                    params.put("date", date);
                    params.put("receiverID", receiverID);
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

    public void updateInventory(Context context, String url, final String productID) {
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
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success == 1) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    //balance += Double.parseDouble(ttlPurchaseAmt);

                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    params.put("ProdID", productID);
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

    //Show success transfer result
    public void showStatusSuccess() {
        tvTransferStatus.setText("Transfer Successful!");
        tvTransferFrom.setText("From: " + giverID);
        tvTransferTo.setText("To: " + Menu_screen.Merc_WalletID);
        tvTransferAmount.setText("Amount: RM" + showTtl);
        tvTransferAmount.setText("Amount: " + String.format("RM %.2f", showTtl));

    }

    //Show cancelled transfer information
    public void showStatusCancelled() {
        tvTransferStatus.setText("Transfer Cancelled!");
        tvTransferFrom.setText("From: NONE");
        tvTransferTo.setText("To: NONE");
        tvTransferAmount.setText("Amount: NONE");
    }
}