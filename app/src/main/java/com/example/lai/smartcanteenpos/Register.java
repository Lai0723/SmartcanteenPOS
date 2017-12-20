package com.example.lai.smartcanteenpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.Obejct.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//Created by lai wei chun

//register screen for merchants

public class Register extends AppCompatActivity {

    EditText txtWid,txtCName,txtpass,txtcontact,txtaddr,txtssm;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickRegister(View v){
        txtWid = (EditText) findViewById(R.id.txtWalletID);
        txtCName = (EditText) findViewById(R.id.txtCompanyName);
        txtpass = (EditText) findViewById(R.id.txtpass);
        txtcontact = (EditText) findViewById(R.id.txtContact);
        txtaddr = (EditText) findViewById(R.id.txtAddr);
        txtssm = (EditText) findViewById(R.id.txtSSM);

        final String wid = txtWid.getText().toString();
        final String CompanyName = txtCName.getText().toString();
        final String pass = txtpass.getText().toString();
        final String contact = txtcontact.getText().toString();
        final String addr = txtaddr.getText().toString();
        final String ssm = txtssm.getText().toString();

        //prevent the field to be empty
        if(TextUtils.isEmpty(wid)){
            txtWid.setError("Field cannot be empty");
        }


        if(TextUtils.isEmpty(CompanyName)){
            txtCName.setError("Field cannot be empty");
        }

        if(TextUtils.isEmpty(pass)){
            txtpass.setError("Field cannot be empty");
        }

        if(TextUtils.isEmpty(contact)){
            txtcontact.setError("Field cannot be empty");
        }

        if(TextUtils.isEmpty(addr)){
            txtaddr.setError("Field cannot be empty");
        }

        if(TextUtils.isEmpty(ssm)){
            txtssm.setError("Field cannot be empty");
        }

        //set the info into object
        if(!TextUtils.isEmpty(wid) && !TextUtils.isEmpty(CompanyName) && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(addr) && !TextUtils.isEmpty(ssm)){

            User user = new User();

            user.setWid(txtWid.getText().toString());
            user.setName(txtCName.getText().toString());
            user.setPassword(txtpass.getText().toString());
            user.setContact(txtcontact.getText().toString());
            user.setAddr(txtaddr.getText().toString());
            user.setSsm(txtssm.getText().toString());

            progressDialog = new ProgressDialog(this);




            //create a new user in database
            try {
                makeServiceCall(this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/insert_merchant.php", user);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }



        }


    }

    // create new user in database
    public void makeServiceCall(Context context, String url, final User user) {

        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {


            //setting up progress dialog

            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Creating Account");
            progressDialog.show();

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
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                } else {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
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

                    // put the parameters with specific values
                    params.put("WalletID", user.getWid());
                    params.put("MercName", user.getName());
                    params.put("password", user.getPassword());
                    params.put("MercContact", user.getContact());
                    params.put("MercAddr", user.getAddr());
                    params.put("MercSSM", user.getSsm());

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
