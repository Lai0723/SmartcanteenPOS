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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Boon Seng
 */

public class FragmentItem extends Fragment implements View.OnClickListener{

    public static final String TAG = "com.example.user.myApp";
    private static String GET_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/select_reward_item.php";
    public static boolean allowRefresh;

    Button btn3;
    Button btn4;
    TextView tvRewardBalance;
    ListView listViewReward;
    RequestQueue queue;

    public static FragmentItem newInstance() {
        FragmentItem  fragment = new FragmentItem ();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        allowRefresh = false;
        btn3 = (Button) rootView.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = (Button) rootView.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);


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
                                    //Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    onResume();
                                } else {
                                    //Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
            tvRewardBalance.setText("" + RedeemMainActivity.LoyaltyPoint);
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
    public void onClick(View v) {
        Date timenow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        final String currentDateandTime = sdf.format(new Date());

        switch (v.getId()) {
            case R.id.btn3:
                try {
                    final android.app.AlertDialog.Builder confirmation = new android.app.AlertDialog.Builder(getContext());
                    confirmation.setCancelable(false);
                    confirmation.setTitle("<!> Confirmation Message");
                    confirmation.setMessage("Do you want to redeem RM 10 Reload Card?");
                    confirmation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (RedeemMainActivity.LoyaltyPoint >= 1500) {
                                RedeemMainActivity.LoyaltyPoint -= 1500;

                                String itCode = "RC" + currentDateandTime + RedeemMainActivity.WalletID;
                                String desc = "RM 10 Reload Card";
                                update(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                                insert(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption_item.php", itCode, desc);
                                Toast.makeText(getContext(), "Success! RM 10 Reload Card is added into your EWallet", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    confirmation.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn4:
                try {
                    final android.app.AlertDialog.Builder confirmation = new android.app.AlertDialog.Builder(getContext());
                    confirmation.setCancelable(false);
                    confirmation.setTitle("<!> Confirmation Message");
                    confirmation.setMessage("Do you want to redeem RM 10 Gift Card?");
                    confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    confirmation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (RedeemMainActivity.LoyaltyPoint >= 2000) {
                                RedeemMainActivity.LoyaltyPoint -= 2000;
                                String itCode = "GC" + currentDateandTime + RedeemMainActivity.WalletID;
                                String desc = "RM 10 Gift Card";
                                update(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_point.php");
                                insert(getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_redemption_item.php", itCode, desc);
                                Toast.makeText(getContext(), "Success! RM 10 Gift Card is added into your EWallet", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getActivity(), "Not enough points", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    confirmation.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void insert(Context context, String url, final String itCode, final String desc) {

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
                    params.put("Description",desc);
                    params.put("ItemCode", itCode);
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
