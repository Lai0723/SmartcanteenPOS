package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.Obejct.Purchase_order;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by lai wei chun
 */
public class Make_Purchase_Order extends Fragment {


    EditText txtPID,txtPSup,txtPQ;
    Button btnPsubmit,btnPcancel;
    public ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_make__purchase__order2, container, false);

        txtPID = (EditText)v.findViewById(R.id.txtPID);
        txtPSup = (EditText)v.findViewById(R.id.txtPSup);
        txtPQ = (EditText)v.findViewById(R.id.txtPQ);
        btnPsubmit = (Button)v.findViewById(R.id.btnPsubmit);
        btnPcancel = (Button)v.findViewById(R.id.btnPcancel);

        //check the field to prevent it to be empty and send it to database
        btnPsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String PID = txtPID.getText().toString();
                String PSup =  txtPSup.getText().toString();
                String PQ = txtPQ.getText().toString();


                if (TextUtils.isEmpty(PID)) {
                    txtPID.setError("Field cannot be empty");
                }


                if (TextUtils.isEmpty( PSup)) {
                    txtPSup.setError("Field cannot be empty");
                }

                if (TextUtils.isEmpty(PQ)) {
                    txtPQ.setError("Field cannot be empty");
                }

                else {

                    View_Purchase_Order.allowRefresh = true;

                    Menu_screen.OList = null;

                    Purchase_order purchase = new Purchase_order();


                    purchase.setProdID(txtPID.getText().toString());
                    purchase.setMercName(Login.LOGGED_IN_USER);
                    purchase.setSupplierName(txtPSup.getText().toString());
                    Date requestDate = new Date();
                    purchase.setPurchaseDate(requestDate);
                    purchase.setPurchaseQuantity(txtPQ.getText().toString());
                    Double fee = 0.00;
                    purchase.setFee(fee);

                    progressDialog = new ProgressDialog(getView().getContext());

                    try {
                        makeServiceCall(v.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/purchase%20order.php", purchase);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

        });

       //go back to inventory screen
        btnPcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                InventoryFragment nextFrag= new InventoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

       return v;
}


    // send data to database to make purchase order record
    public void makeServiceCall(Context context, String url, final  Purchase_order purchase) {
        RequestQueue queue = Volley.newRequestQueue(context);


        //Send data
        try {
            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Loading");
            progressDialog.show();

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
                                if (success == 0) { // if failed
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getView().getContext(), message, Toast.LENGTH_LONG).show();



                                } else {
                                    // if success
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getView().getContext(), message, Toast.LENGTH_LONG).show();



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
                    params.put("ProdID", purchase.getProdID());
                    params.put("MercName", purchase.getMercName());
                    params.put("SupplierName", purchase.getSupplierName());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = dateFormat.format(purchase.getPurchaseDate());
                    params.put("PurchaseDate", dateString);
                    params.put("PurchaseQuantity", purchase.getPurchaseQuantity());
                    params.put("Fee",Double.toString(purchase.getFee()));
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
