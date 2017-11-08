package com.example.lai.smartcanteenpos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class update_menu extends Fragment {


    Button btnName,btnCat,btnPrice,btnDesc,btncancel;
    EditText txtProdID;
    String newName,newCat,newDesc,newPrice,ProdID;



    public update_menu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_product, container, false);

        btnName = (Button)v.findViewById(R.id.btnName);
        btnCat = (Button)v.findViewById(R.id.btnCat);
        btnPrice = (Button)v.findViewById(R.id.btnPrice);
        btnDesc = (Button)v.findViewById(R.id.btnDesc);
        txtProdID = (EditText)v.findViewById(R.id.txtProductID);
        btncancel = (Button)v.findViewById(R.id.btncancel);

        ProdID = txtProdID.getText().toString();

        /*if(TextUtils.isEmpty(ProdID)){
            txtProdID.setError("Must field in product id first");
        }
        else if (!TextUtils.isEmpty(ProdID)){*/
            btnName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_text, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Name");
                    final EditText editTextNewName = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newName = editTextNewName.getText().toString();


                                    //check is empty
                                    if (TextUtils.isEmpty(newName)) {

                                        Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                                    } else {

                                        updateName(view.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_name.php");
                                    }
                                }
                            });

                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;

                }
            });

            btnCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_text, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Category");
                    final EditText editTextNewName = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newCat = editTextNewName.getText().toString();


                                    //check is empty
                                    if (TextUtils.isEmpty(newCat)) {

                                        Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                                    } else {

                                        updateCat(view.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_cat.php");
                                    }
                                }
                            });

                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;

                }
            });

            btnDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_text, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Description");
                    final EditText editTextNewName = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newDesc = editTextNewName.getText().toString();


                                    //check is empty
                                    if (TextUtils.isEmpty(newDesc)) {

                                        Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                                    } else {

                                        updateDesc(view.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_desc.php");
                                    }
                                }
                            });

                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;

                }
            });

            btnPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_text, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Description");
                    final EditText editTextNewName = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newPrice = editTextNewName.getText().toString();


                                    //check is empty
                                    if (TextUtils.isEmpty(newPrice)) {

                                        Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                                    } else {

                                        updatePrice(view.getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_price.php");
                                    }
                                }
                            });

                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;

                }
            });



        btncancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MenuFragment nextFrag= new MenuFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }


        });
        return v;
    }

    // perform updates accordingly
    public void updateName(Context context, String url) {
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
                    params.put("ProdID", txtProdID.getText().toString());
                    params.put("ProdName", newName);

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

    public void updateCat(Context context, String url) {
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
                    params.put("ProdID", txtProdID.getText().toString());
                    params.put("ProdCat", newCat);

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

    public void updateDesc(Context context, String url) {
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
                    params.put("ProdID", txtProdID.getText().toString());
                    params.put("ProdDesc", newDesc);

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

    public void updatePrice(Context context, String url) {
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
                    params.put("ProdID", txtProdID.getText().toString());
                    params.put("ProdPrice", newPrice);

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
