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
import com.example.lai.smartcanteenpos.Obejct.Product;
import com.example.lai.smartcanteenpos.Obejct.Purchase_order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class add_inventory extends Fragment {

    EditText txtInID,txtInQuantity,txtPOID,txtInFee;
    Button btnISubmit,btnIcancel;
    public ProgressDialog progressDialog;



    public add_inventory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add_inventory, container, false);

        txtInID = (EditText)v.findViewById(R.id.txtInID);
        txtInQuantity = (EditText)v.findViewById(R.id.txtInQuan);
        txtPOID = (EditText)v.findViewById(R.id.txtPOID);
        txtInFee = (EditText)v.findViewById(R.id.txtInFee) ;

        btnISubmit = (Button)v.findViewById(R.id.btnISubmit);


        btnISubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ProductID = txtInID.getText().toString();
                String ProductQuatity =  txtInQuantity.getText().toString();
                String POID = txtPOID.getText().toString();
                String Fee = txtInFee.getText().toString();


                if (TextUtils.isEmpty( ProductID)) {
                    txtInID.setError("Field cannot be empty");
                }


                if (TextUtils.isEmpty( ProductQuatity)) {
                    txtInQuantity.setError("Field cannot be empty");
                }

                if (TextUtils.isEmpty(POID)) {
                    txtPOID.setError("Field cannot be empty");
                }
                if (TextUtils.isEmpty(Fee)) {
                    txtInFee.setError("Field cannot be empty");
                }

                else {

                    InventoryFragment.allowRefresh = true;

                    Menu_screen.SList = null;

                    Purchase_order purchase = new Purchase_order();
                    Product product = new Product();


                    product.setProdID(txtInID.getText().toString());
                    product.setQuantity(Integer.parseInt(txtInQuantity.getText().toString()));

                    purchase.setPOID(txtPOID.getText().toString());

                    purchase.setFee(Double.parseDouble(txtInFee.getText().toString()));




                    progressDialog = new ProgressDialog(getView().getContext());

                    try {
                        makeServiceCall1(v.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/add_inventory.php", product);
                        makeServiceCall2(v.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_fee.php", purchase);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

        });

        btnIcancel = (Button)v.findViewById(R.id.btnIcancel);
        btnIcancel.setOnClickListener(new View.OnClickListener() {
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


    public void makeServiceCall1(Context context, String url, final  Product product) {
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
                    params.put("ProdID", product.getProdID());

                    params.put("PurchaseQuantity", Integer.toString(product.getQuantity()));

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

    public void makeServiceCall2(Context context, String url, final  Purchase_order purchase) {
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

                    params.put("POID", purchase.getPOID());
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
