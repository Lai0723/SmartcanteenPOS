package com.example.lai.smartcanteenpos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OrderingFragment extends Fragment {
    public static final String UPLOAD_URL = "https://leowwj-wa15.000webhostapp.com/add_order.php";
    String walletID = OrderMainActivity.getwID(), prodID = OrderMainActivity.getProdID();
    String disCode;
    TextView textViewProductName, textViewProductDesc, textViewPrice, textViewTotal;
    EditText editTextAmount, editTextDiscount;
    int orderQty = 0;
    double productPrice, total;
    Button buttonOrder, buttonApply;
    static boolean ticketApplied;
    RequestQueue queue;



    public OrderingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ordering, container, false);
        textViewProductName = v.findViewById(R.id.textViewProductName);
        textViewProductDesc = v.findViewById(R.id.textViewProductDesc);
        textViewPrice = v.findViewById(R.id.textViewPrice);
        textViewTotal = v.findViewById(R.id.textViewTotalPrice);
        textViewProductName.setText(OrderMainActivity.getProdName());
        textViewProductDesc.setText(OrderMainActivity.getProdDesc());
        textViewPrice.setText(OrderMainActivity.getProdPrice() + "");
        editTextAmount = v.findViewById(R.id.editTextAmount);
        editTextDiscount = v.findViewById(R.id.editTextDiscountCode);
        productPrice = OrderMainActivity.getProdPrice();
        total = 0;

        buttonOrder = v.findViewById(R.id.buttonOrder);
        buttonApply = v.findViewById(R.id.buttonApplyCode);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    orderQty = Integer.parseInt(editTextAmount.getText().toString());
                }
                catch(Exception e){
                    editTextAmount.setError("Only integer values are allowed.");
                }
                if (TextUtils.isEmpty(editTextAmount.getText().toString())) {
                    editTextAmount.setError("Field cannot be empty");
                }
                else if (orderQty <= 0){
                    editTextAmount.setError("Minimum 1 order shall be made to proceed.");
                }
                else{
                    textViewTotal.setText(R.string.total);
                    textViewTotal.setText( textViewTotal.getText().toString() + " " + total);
                    if(OrderMainActivity.getWalletBal() >= total){
                        final AlertDialog.Builder confirmation = new AlertDialog.Builder(getActivity());
                        confirmation.setCancelable(false);
                        confirmation.setTitle("Amount To Be Paid = RM " + total);
                        confirmation.setMessage("Redeem your order at the canteen stall.");
                        confirmation.setPositiveButton("Pay",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(ticketApplied)
                                    updateRedeemDate(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insertRedeemDate.php", disCode);
                                makeOrder(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insertOrder.php",
                                        walletID, prodID, orderQty+ "", total + "");
                                OrderMainActivity.listOrder = null;
                                dialog.dismiss();
                                Intent intent = new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);


                            }
                        });
                        confirmation.setNegativeButton("Back to Ordering",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        confirmation.show();
                    }
                    else{
                        editTextAmount.setError("Please top up your wallet for your order.");
                    }
                }
                }
            }
        );

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDiscount.setError(null);
                try{
                    orderQty = Integer.parseInt(editTextAmount.getText().toString());
                }
                catch(Exception e){
                    editTextAmount.setError("Only integer values are allowed.");
                }
                if (editTextDiscount.getText().toString().matches("")) {
                    editTextDiscount.setError("Field cannot be empty");
                    ticketApplied = false;
                }
                else if(!ticketApplied && orderQty == 0){
                    editTextDiscount.setError("Please check your order amount first.");
                }
                else {
                    editTextDiscount.setError(null);
                    checkEligibility(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/checkDiscountCode.php", editTextDiscount.getText().toString(), walletID);
                    disCode = editTextDiscount.getText().toString();
                    if (ticketApplied && disCode.matches("^10.*")){
                        total = total - 10;
                        if (total <= 0.00)
                            total = 0.00;
                        textViewTotal.setText(R.string.total + " " + total);
                    }
                    else if (ticketApplied && disCode.matches("^5.*")){
                        total = total - 5;
                        if (total <= 0.00)
                            total = 0.00;
                        textViewTotal.setText(R.string.total + " " + total);
                    }
                }
            }
        });

        return v;
    }

    public void updateRedeemDate(Context context, String url, final String couponCode ) {
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
                                int success = Integer.parseInt(jsonObject.getString("success"));
                                String message = jsonObject.getString("message");
                                if (success == 1) {
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
                            Toast.makeText(getActivity().getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("CouponCode", couponCode);
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

    public void makeOrder(Context context, String url, final String walletID, final String productID, final String OrderQuantity, final String PayAmount ) {
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
                            Toast.makeText(getActivity().getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("WalletID", walletID);
                    params.put("ProductID", productID);
                    params.put("OrderQuantity", OrderQuantity);
                    params.put("PayAmount", PayAmount);
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

    public void checkEligibility(Context context, String url, final String disCode, final String wID){
        //mPostCommentResponse.requestStarted();
        queue = Volley.newRequestQueue(context);
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
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    ticketApplied = true;

                                } else if (success == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        ticketApplied = false;
                                }
                                else {
                                    Toast.makeText(getActivity().getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                                    ticketApplied = false;
                                }
                                //show error
                                if (err.length() > 0) {
                                    Toast.makeText(getActivity().getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                    ticketApplied = false;
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
                    params.put("WalletID", wID);
                    params.put("CouponCode",disCode);
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