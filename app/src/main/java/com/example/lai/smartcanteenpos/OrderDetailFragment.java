/**
 * Created by Leow on 11/4/2017.
 * This is the Order Detail Fragment that displays details of an Order History from Order History Fragment.
 */

package com.example.lai.smartcanteenpos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class OrderDetailFragment extends Fragment {

    Button buttonCancelOrder, buttonRedeem;
    TextView textViewOrderID, textViewProductName, textViewPayment, textViewPaymentStatus, textViewOrderDateTime;
    String orderID = OrderHistoryActivity.getOrderID();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_detail, container, false);
        String orderID = OrderHistoryActivity.getOrderID();
        String prodName = OrderHistoryActivity.getProdName();
        double total = OrderHistoryActivity.getOrderTotal(), walletBal = OrderHistoryActivity.getWalletBal();
        String status = OrderHistoryActivity.getOrderStatus();
        String orderDateTime = OrderHistoryActivity.getOrderDateTime();

        buttonCancelOrder = v.findViewById(R.id.buttonCancelOrder);
        buttonRedeem = v.findViewById(R.id.buttonRedeemOrder);
        textViewOrderID = v.findViewById(R.id.textViewOrder);
        textViewProductName = v.findViewById(R.id.textViewProductName);
        textViewPayment = v.findViewById(R.id.textViewPayment);
        textViewPaymentStatus = v.findViewById(R.id.textViewPaymentStatus);
        textViewOrderDateTime = v.findViewById(R.id.textViewOrderDateTime);

        textViewOrderID.setText(orderID + " ");
        textViewProductName.setText(prodName);
        textViewPayment.setText(total + " ");
        textViewOrderDateTime.setText(orderDateTime);
        textViewPaymentStatus.setText(status);
        if (status.matches("Accepted") && walletBal > total) {
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    buttonRedeem.setVisibility(View.VISIBLE);
                } });
        }
        else if (status.matches("Pending")){
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    buttonCancelOrder.setVisibility(View.VISIBLE);
                } });
        }

        //Cancel Order button programming which leads to the selected order's Order Status in Orders table in the database to change to "Cancelled"
        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.order_history_layout, null));
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Delete this order?");
                alertBuilder.setMessage("Press OK to continue, press anywhere around this box to cancel.");

                alertBuilder.setCancelable(true);
                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStatus(view.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/cancelOrder.php");
                    }
                });

                Dialog dialog = alertBuilder.create();
                dialog.show();
                OrderMenuFragment.allowRefresh = true;
                OrderHistoryActivity.listOrder = null;

            }
        });

        //Redeem Order button programming that calls the QR generator for the merchant to scan when the user get the item that he / she ordered
        buttonRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add this, your class into the bracket of the intent initialization
                Intent intent = new Intent(getContext(),QRretrieval.class);
                intent.putExtra("OrderID", OrderHistoryActivity.getOrderID());
                intent.putExtra("ProdID", OrderHistoryActivity.getProdID());
                intent.putExtra("WalletID",OrderHistoryActivity.getwID());
                intent.putExtra("ProdName",OrderHistoryActivity.getProdName());
                intent.putExtra("OrderDateTime",OrderHistoryActivity.getOrderDateTime());
                intent.putExtra("OrderQuantity",OrderHistoryActivity.getOrderQuantity());
                intent.putExtra("OrderStatus",OrderHistoryActivity.getOrderStatus());
                intent.putExtra("PayAmount", OrderHistoryActivity.getOrderTotal());
                intent.putExtra("PayDateTime",OrderHistoryActivity.getPayDateTime());
                startActivity(intent);
            }
        });

        return v;
    }

    // Volley to change the status of an order when Cancel Button is clicked
    public void updateStatus(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);
        textViewOrderID.setText(orderID + " ");
        //Send data
        try {

            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            //response =string returned by server to the client
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
                            Toast.makeText(getView().getContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("OrderID", textViewOrderID.getText().toString());
                    params.put("OrderStatus", "Cancelled");

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

    public static OrderDetailFragment newInstance() {
        OrderDetailFragment fragment = new OrderDetailFragment();
        return fragment;
    }
}
