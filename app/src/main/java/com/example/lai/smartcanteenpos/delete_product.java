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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




/**
 * A simple {@link Fragment} subclass.
 */
public class delete_product extends Fragment {



    Button btndelete,btncancel;
    EditText ProdID;
    String ID;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_delete_product, container, false);

        btndelete = (Button) v.findViewById(R.id.btnDelete);
        btncancel = (Button) v.findViewById(R.id.btncancel);
        ProdID = (EditText)v.findViewById(R.id.txtdeleteName);

        //go back to the menu screen
        btncancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MenuFragment nextFrag= new MenuFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }


        });

        //check the field to prevent it to be empty and send the info delete product method
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                ID = ProdID.getText().toString();
                                //check is empty
                                if (TextUtils.isEmpty(ID)) {

                                    Toast.makeText(getView().getContext(), "Field cannot be empty", Toast.LENGTH_LONG).show();

                                } else {
                                    progressDialog = new ProgressDialog(getView().getContext());
                                    DeleteProduct(getView().getContext(), "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/delete_product.php");
                                    MenuFragment.allowRefresh = true;

                                    Menu_screen.lList = null;
                                }
                            }
        });

        return v;
    }

    //send the product id to the database to perform delete process
    public void DeleteProduct(Context context, String url) {
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

                    params.put("ProdID", ProdID.getText().toString());

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
