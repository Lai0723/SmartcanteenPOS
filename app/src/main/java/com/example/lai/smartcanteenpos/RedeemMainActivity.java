package com.example.lai.smartcanteenpos;

import android.content.Context;
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
import com.example.lai.smartcanteenpos.database.History;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lim Boon Seng,RSD3 on 13/10/2017
 */

public class RedeemMainActivity extends AppCompatActivity {
    public static String WalletID;
    //    public static String newString;
    public static double balance;
    public static int LoyaltyPoint;

    public static List<History> hList = null;
    TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //get User info

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //newString = null;
                WalletID =null;
                Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
                //finish();
            } else {
                //newString = extras.getString("WalletID");
                WalletID = extras.getString("WalletID");
            }
        } else{
            Bundle extras = getIntent().getExtras();
            //newString = (String) savedInstanceState.getSerializable("WalletID");
            WalletID = extras.getString("WalletID");
            LoyaltyPoint = Integer.parseInt(extras.getString("LoyaltyPoint"));
            balance = Double.parseDouble(extras.getString("Balance"));
        }
        checkBalance(RedeemMainActivity.this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php");


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, FragmentHome.newInstance());
        transaction.commit();
        final View inflatedView;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        checkBalance(RedeemMainActivity.this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php");
                        fragment = FragmentHome.newInstance();
                        break;
                    case R.id.action_Coupon:
                        fragment = FragmentCoupon.newInstance();
                        break;
                    case R.id.action_giftCard:
                        fragment = FragmentItem.newInstance();
                        break;
                    case R.id.action_history:
                        checkBalance(RedeemMainActivity.this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_user.php");
                        fragment = FragmentHistory.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_main, fragment);
                transaction.commit();
                return true;
            }
        });


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
                                    LoyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                    Toast.makeText(getApplicationContext(), "Balance loaded", Toast.LENGTH_LONG).show();
                                    tvBalance = (TextView) findViewById(R.id.tvBalance1);
                                    if (tvBalance != null)
                                        tvBalance.setText(String.format(Integer.toString(RedeemMainActivity.LoyaltyPoint)));
                                } else if (success == 2) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "User not found.";
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
                    params.put("WalletID", WalletID);
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
