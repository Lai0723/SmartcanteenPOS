package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.stripe.android.model.Card;
//import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String walletID;
    public static double balance;
    public static int loyaltyPoint;
    public static String loginPassword;
    public static String currentCard;
    public static String currentCardType;
    String updateCurrentCard;
    String updateCurrentCardType;

    double balanceToCheck, transferAmount;
    String ttlPurchaseAmt = "5.0";
    public ProgressDialog pDialog;
    //Card cardToSave;
    java.util.Date dt;
    java.text.SimpleDateFormat sdf;
    String topUpDateTime;

    TextView tvBalance;

    BraintreeFragment mBraintreeFragment;
    String mAuthorization;
    final int BT_REQUEST_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //get User info
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        walletID = extras.getString("walletID");
        balance = extras.getDouble("balance");
        loyaltyPoint = extras.getInt("loyaltyPoint");
        loginPassword = extras.getString("loginPassword");
        currentCard = extras.getString("currentCard");
        currentCardType = extras.getString("currentCardType");


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragmentWallet.newInstance());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_wallet:
                        //checkBalance(MainActivity.this, "https://gabriellb-wp14.000webhostapp.com/select_user.php");
                        checkBalance(MainActivity.this, "https://martpay.000webhostapp.com/gab_select_user.php");
                        fragment = fragmentWallet.newInstance();
                        break;
                    case R.id.action_fund:
                        //checkCard(MainActivity.this, "https://gabriellb-wp14.000webhostapp.com/select_user.php");
                        checkCard(MainActivity.this, "https://martpay.000webhostapp.com/gab_select_user.php");
                        fragment = fragmentFund.newInstance();
                        break;
                    /*case R.id.action_transfer:
                        fragment = TransferFragment.newInstance();
                        break;
                    case R.id.action_reward:
                        checkBalance(MainActivity.this, "https://martpay.000webhostapp.com/select_user.php");
                        fragment = RewardFragment.newInstance();
                        break;*/
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_main, fragment);
                transaction.commit();
                return true;
            }
        });

    }

    public void goOnlineMenu(View v){
        Intent intent = new Intent(this, OrderMainActivity.class);
        intent.putExtra("walletID",walletID);
        intent.putExtra("balance",balance);
        startActivity(intent);
    }

    public void goRedeemCoupon(View v){
        Intent intent = new Intent(this,RedeemMainActivity.class);
        intent.putExtra("WalletID",walletID);
        intent.putExtra("LoyaltyPoint",loyaltyPoint);
        startActivity(intent);
    }

    public void goChangeCard(View v) {
        Intent intent = new Intent(this, changecard.class);
        startActivity(intent);
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragmentChangeCard.newInstance());
        transaction.commit();*/
    }

    //Confirm bind/change card
    public void onConfirmClick2(View v) {
        //View view =  inflater.inflate(R.layout.fragment_changecard, container, false);
        //CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget2);
        //cardToSave = mCardInputWidget.getCard();

        /*if (cardToSave != null) {
            //updateCurrentCard = cardToSave.getNumber();
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText(updateCurrentCard);
        }*/


    }

    //For Cancel button in change card UI
    public void goAddFund(View v) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragmentFund.newInstance());
        transaction.commit();
    }

    /*

        Transfer QR Code part

    */

    public void goQRPassword(View v) {

        Intent intent = new Intent(this, QRPassword.class);
        intent.putExtra("giverID", walletID);
        intent.putExtra("balanceToChk", balance);
        intent.putExtra("QRpw", loginPassword);
        startActivity(intent);
    }

    public void startScan(View view) {
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
                    String receiverID = walletID;

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
        if (requestCode == BT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult resultBT = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Toast.makeText(this, "Card Accepted.", Toast.LENGTH_SHORT).show();
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Toast.makeText(this, "You have cancelled the order.", Toast.LENGTH_SHORT).show();
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Toast.makeText(this, "Error"+error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*

        Add fund part

    */
    public void clickAddFund(View v) {

        dt = new java.util.Date();
        sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        topUpDateTime = sdf.format(dt);

        try {
            insertTopUp(this, "https://martpay.000webhostapp.com/gab_insert_topup.php"); //insert record to topup table in database
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        onBraintreeSubmit(v);
    }

    public void insertTopUp(Context context, String url) {
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
                                    //balance += Double.parseDouble(ttlPurchaseAmt);
                                    fragmentWallet.allowRefresh =true;
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
                    params.put("WalletID", MainActivity.walletID);
                    params.put("TopUpAmount", String.valueOf(fragmentFund.tuValue));
                    params.put("TopUpDateTime", topUpDateTime);
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


    /*

        Misc

    */

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


    public void checkBalance(Context context, String url) {
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
                                    balance = jsonObject.getDouble("Balance");
                                    loyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                    //Toast.makeText(getApplicationContext(), "Balance loaded", Toast.LENGTH_LONG).show();
                                    tvBalance = (TextView) findViewById(R.id.tvMercBalance);
                                    if (tvBalance != null)
                                        tvBalance.setText(String.format("RM %.2f", MainActivity.balance));
                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "Wallet not found.";

                                } else {
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                                //show error
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
                    params.put("WalletID", walletID);
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

    public void checkCard(Context context, String url) {
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
                                    //balance = jsonObject.getDouble("Balance");
                                    //loyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                    //Toast.makeText(getApplicationContext(), "Balance loaded", Toast.LENGTH_LONG).show();
                                    tvBalance = (TextView) findViewById(R.id.tvMercBalance);
                                    TextView tvCardType = (TextView) findViewById(R.id.tvCardType);
                                    TextView tvCardEnding = (TextView) findViewById(R.id.tvCardEnding);
                                    //if (tvBalance != null)
                                    //tvBalance.setText(String.format("RM %.2f", MainActivity.balance));
                                    if (tvCardType != null)
                                        tvCardType.setText(tvCardType.getText().toString() + MainActivity.currentCardType);
                                    if (tvCardEnding != null)
                                        tvCardEnding.setText(tvCardEnding.getText().toString() + MainActivity.currentCard);
                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "Wallet not found.";

                                } else {
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                                //show error
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
                    params.put("WalletID", walletID);
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

        if (currentCard.matches("TBD")) {
            Toast.makeText(this, "Please bind a card to wallet.", Toast.LENGTH_LONG).show();
            /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_main, fragmentChangeCard.newInstance());
            transaction.commit();*/
        }

    }

    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIxZjYxNjBhYjMwNjkwZDhhYjBhNzY3ZGFjOTI1ZjM4MzRlNGUzMmFiNmZmOWVkZDY3Yzg2OTA0NDAwYWI0NmJjfGNyZWF0ZWRfYXQ9MjAxNy0xMS0xNlQwNzoxMjo0Ni43ODA1MTk0OTMrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(dropInRequest.getIntent(this), BT_REQUEST_CODE);
    }


}
