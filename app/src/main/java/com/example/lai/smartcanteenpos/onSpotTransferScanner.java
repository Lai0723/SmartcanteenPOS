package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class onSpotTransferScanner extends AppCompatActivity {

    String ttlPurchaseAmt;
    double balanceToCheck, balance;

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

        startScan();

    }

    public void startScan() {
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == TOP_UP_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        } else { */
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String[] arr = result.getContents().split(",");
                if (!arr[0].equals("orderRetrieval")) {
                    String giverID = arr[0];
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
                    } else if (diffSeconds>60) {
                        Toast.makeText(this, "Code expired, generate code again.", Toast.LENGTH_SHORT).show();
                    } else {
                        //String combined = "Sender: " + giver + "\nAmount: RM" + amount + "\n Time: " + date+"\n Receiver " + receiver;
                        //Toast.makeText(this, "Scanned: " + combined, Toast.LENGTH_LONG).show();

                        //double balanceToCheck, ttlPurchaseAmt, transferAmount;
                        balanceToCheck = Double.parseDouble(balanceToChk);
                        Double ttlPurchaseAmount = Double.parseDouble(ttlPurchaseAmt);
                        if (balanceToCheck > ttlPurchaseAmount) {
                            //Double.toString(ttlPurchaseAmt);
                            try {
                                fragmentWallet.allowRefresh = true;
                                insertTransfer(this, "https://martpay.000webhostapp.com/gab_insert_transfer.php", giverID, ttlPurchaseAmt, date, receiverID);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else if (balanceToCheck < ttlPurchaseAmount) {
                            Toast.makeText(this, "Insufficient balance.", Toast.LENGTH_SHORT).show();

                        }
                    }
                } /*else {
                    String listID = arr[2];
                    String date = arr[1];
                    String seller = arr[4];
                    double price = Double.parseDouble(arr[3]);
                    String buyer = username;
                    if (seller.equals(buyer)) {
                        Toast.makeText(this, "You cannot buy your own item.", Toast.LENGTH_SHORT).show();
                    } else if (price > balance) {
                        Toast.makeText(this, "You cannot afford it.", Toast.LENGTH_SHORT).show();
                    } else {

                        //String combined = "Sender: " + giver + "\nAmount: RM" + amount + "\n Time: " + date+"\n Receiver " + receiver;
                        String combined = result.getContents();
                        Toast.makeText(this, "Scanned: " + combined, Toast.LENGTH_LONG).show();
                        try {
                            WalletFragment.allowRefresh = true;
                            insertTransaction(this, "https://martpay.000webhostapp.com/insert_transaction.php", date, buyer, listID);

                            //end need change de part
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                }   */

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        //closing of ELSE }

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
                                    transaction.replace(R.id.frame_main, fragmentWallet.newInstance());
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
}
