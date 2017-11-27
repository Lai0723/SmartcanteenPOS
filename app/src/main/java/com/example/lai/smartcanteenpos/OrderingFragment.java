/**
 * Created by Leow on 11/4/2017.
 * This is the Ordering Fragement to display the Order Page for user to make online order
 */
package com.example.lai.smartcanteenpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderingFragment extends Fragment {
    public static final String UPLOAD_URL = "https://leowwj-wa15.000webhostapp.com/add_order.php";
    String walletID = OrderMainActivity.getwID(), prodID = OrderMainActivity.getProdID();
    String disCode;
    TextView textViewProductName, textViewProductDesc, textViewPrice, textViewTotal;
    Spinner spinnerOrderQuantity;
    EditText editTextAmount, editTextDiscount;
    int orderQty;
    double productPrice, discountedTotal, originalTotal, difference, totalPayable, discountAmount;
    Button buttonOrder, buttonApply, buttonRemove;
    public static boolean ticketApplied;
    RequestQueue queue;
    DecimalFormat df2 = new DecimalFormat("0.00");



    public OrderingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init();
        View v = inflater.inflate(R.layout.fragment_ordering, container, false);
        textViewProductName = v.findViewById(R.id.textViewProductName);
        textViewProductDesc = v.findViewById(R.id.textViewProductDesc);
        textViewPrice = v.findViewById(R.id.textViewPrice);
        textViewTotal = v.findViewById(R.id.textViewTotalPrice);
        textViewProductName.setText(OrderMainActivity.getProdName());
        textViewProductDesc.setText(OrderMainActivity.getProdDesc());
        textViewPrice.setText(df2.format(OrderMainActivity.getProdPrice()) + "");
        editTextDiscount = v.findViewById(R.id.editTextDiscountCode);
        spinnerOrderQuantity = v.findViewById(R.id.spinnerOrderQty);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.qtyOfOrder, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerOrderQuantity.setAdapter(adapter);
        productPrice = OrderMainActivity.getProdPrice();


        buttonOrder = v.findViewById(R.id.buttonOrder);
        buttonApply = v.findViewById(R.id.buttonApplyCode);
        buttonRemove = v.findViewById(R.id.buttonRemoveCode);

        //Order button that casues discount to be applied and orders to be made
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ticketApplied && disCode.matches("^10.*")){
                    discountAmount = 10;
                    discountedTotal = originalTotal - discountAmount;
                    if (discountedTotal <= 0.00)
                        discountedTotal = 0.00;
                    difference = originalTotal - discountedTotal;
                    textViewTotal.setText(R.string.total);
                    textViewTotal.setText(textViewTotal.getText().toString() + " " + df2.format(discountedTotal));
                }
                else if (ticketApplied && disCode.matches("^5.*")){
                    discountAmount = 5;
                    discountedTotal = originalTotal - discountAmount;
                    if (discountedTotal <= 0.00)
                        discountedTotal = 0.00;
                    difference = originalTotal - discountedTotal;
                    textViewTotal.setText(R.string.total);
                    textViewTotal.setText(textViewTotal.getText().toString() + " " + df2.format(discountedTotal));
                }
                textViewTotal.setText(R.string.total);
                if (difference == 0)
                    totalPayable = originalTotal;
                else
                    totalPayable = discountedTotal;
                textViewTotal.setText(R.string.total);
                textViewTotal.setText(textViewTotal.getText().toString() + " " + df2.format(totalPayable));
                if(OrderMainActivity.getWalletBal() >= totalPayable){
                    final AlertDialog.Builder confirmation = new AlertDialog.Builder(getContext());
                    confirmation.setCancelable(false);
                    confirmation.setTitle("Amount To Be Paid = RM " + df2.format(totalPayable));
                    confirmation.setMessage("Redeem your order at the canteen stall.");
                    confirmation.setPositiveButton("Pay",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OrderHistoryActivity.listOrder = null;
                            dialog.dismiss();
                            if(ticketApplied){
                                updateRedeemDate(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insertRedeemDate.php", disCode);
                                makeOrder(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insertOrder.php", walletID, prodID, orderQty+ "", originalTotal+"", totalPayable + "", difference+"", "true");
                            }
                            else
                                makeOrder(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insertOrder.php", walletID, prodID, orderQty+ "", originalTotal+"", totalPayable + "", difference+"", "false");
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
                    buttonOrder.setText(R.string.topup);
                    Toast.makeText(getActivity(),"You do not have enough balance to make te order. Please top up your wallet first", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }}
        );

        //Apply Code button that verifies the code validity and set the condition ticket applied to justify whether the discount will be applied when the user is making the order
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDiscount.setError(null);
                checkEligibility(getActivity(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/checkDiscountCode.php", editTextDiscount.getText().toString(), walletID);
                disCode = editTextDiscount.getText().toString();
            }
        });

        //Remove code button that resets the code applied and its status so that user can use another code
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketApplied = false;
                disCode = null;
                editTextDiscount.clearComposingText();
                textViewTotal.setText(R.string.total);
                textViewTotal.setText(textViewTotal.getText().toString() + " " + df2.format(originalTotal));
            }
        });

        //Spinner that allows user to set the order quantity and the TextView will show Original Total Price only
        spinnerOrderQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderQty = Integer.parseInt(parent.getItemAtPosition(position).toString());
                originalTotal = orderQty * productPrice;
                textViewTotal.setText(R.string.total);
                textViewTotal.setText(textViewTotal.getText().toString() + " " + df2.format(originalTotal));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    //This will be run when button Order is clicked to apply the valid promotion code used by user to update redeem date in RedeemCode table in database currently.
    //Future work: used it in Redeem Item also
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

    //Inserts an order into the Orders table in the database
    public void makeOrder(Context context, String url, final String walletID, final String productID,
                          final String OrderQuantity, final String orderPrice, final String payAmount,
                          final String priceDifference, final String promoApplication) {
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
                                    getActivity().finish();
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
                    params.put("OrderPrice",orderPrice);
                    params.put("PayAmount", payAmount);
                    params.put("PriceDifference",priceDifference);
                    params.put("PromoApplication",promoApplication);
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

    //Check code validity and set the code applied status
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
                                    OrderingFragment.ticketApplied = true;

                                } else if (success == 1) {
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    OrderingFragment.ticketApplied = false;
                                }
                                else {
                                    Toast.makeText(getActivity().getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                                    OrderingFragment.ticketApplied = false;
                                }
                                //show error
                                if (err.length() > 0) {
                                    Toast.makeText(getActivity().getApplicationContext(), err, Toast.LENGTH_LONG).show();
                                    OrderingFragment.ticketApplied = false;
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

    //initialise view
    public void init(){
        orderQty = 0;
        discountedTotal = 0;
        originalTotal = 0;
        totalPayable = 0;
        difference = 0;
        discountAmount = 0;
        disCode = null;
        ticketApplied = false;
    }
}