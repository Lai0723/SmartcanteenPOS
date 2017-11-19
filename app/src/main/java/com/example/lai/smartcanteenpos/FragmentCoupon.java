package com.example.lai.smartcanteenpos;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentCoupon extends Fragment implements View.OnClickListener{

    public static final String TAG = "com.example.user.myApp";
    private static String GET_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_reward.php";
    public static boolean allowRefresh;

    TextView tvRewardBalance;
    Button btn1;
    Button btn2;
    RequestQueue queue;

    public static FragmentCoupon newInstance() {
        FragmentCoupon fragment = new FragmentCoupon();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discount_coupon, container, false);
        allowRefresh = false;

        btn1 = (Button) rootView.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = (Button) rootView.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        return rootView;

    }

    public void insertRedeem(Context context, String url, final String id, final String username) {
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
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    onResume();
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
                            Toast.makeText(getActivity().getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("username", username);
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


    @Override
    public void onResume() {
        super.onResume();
        if (allowRefresh) {
            allowRefresh = false;
            tvRewardBalance.setText(String.format(Integer.toString(RedeemMainActivity.LoyaltyPoint)));
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onClick (View v) {
        Date timenow = new Date();

        switch (v.getId()) {
            case R.id.btn1:
                if (RedeemMainActivity.LoyaltyPoint >= 500){
                    RedeemMainActivity.LoyaltyPoint -= 500;
                    try {
                        String CouponCode = "5" + timenow.getTime() + RedeemMainActivity.WalletID;
                        String Description = "RM 5 discount";
                        update(getContext() , "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                        //tvRewardBalance.setText(String.format(Integer.toString(RedeemMainActivity.LoyaltyPoint)));
                        insert(getContext() , "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption.php",CouponCode,Description);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn2:
                if (RedeemMainActivity.LoyaltyPoint >= 1000) {
                    RedeemMainActivity.LoyaltyPoint -= 1000;
                    try {
                        String CouponCode = "10" + timenow.getTime() + RedeemMainActivity.WalletID;
                        String Description = "RM 10 discount";
                        update(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                        //tvRewardBalance.setText(String.format(Integer.toString(RedeemMainActivity.LoyaltyPoint)));
                        insert(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption.php", CouponCode, Description);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void update(Context context, String url) {
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

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("LoyaltyPoint", String.valueOf(RedeemMainActivity.LoyaltyPoint));
                    params.put("WalletID", String.valueOf(RedeemMainActivity.WalletID));
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

    public void insert(Context context, String url, final String CouponCode, final String Description) {

        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Description",Description);
                    params.put("CouponCode", CouponCode);
                    params.put("WalletID", RedeemMainActivity.WalletID);
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
