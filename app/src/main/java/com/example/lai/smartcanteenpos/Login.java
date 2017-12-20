package com.example.lai.smartcanteenpos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// Created by lai wei chun

//Login screen for merchants

public class Login extends AppCompatActivity {

    //declare all variables
    EditText txtUserName, txtPass;
    public static String LOGGED_IN_USER = "LOGGED_IN_USER";
    String  username, Merc_WalletID, checkPass, MercName;
    ProgressDialog progressDialog;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPass = (EditText) findViewById(R.id.txtPass);

        pDialog = new ProgressDialog(this);
    }


    // when register button was clicked
    protected void ClickRegister(View v) {

        Intent registerActivityIntent = new Intent(this, Register.class);

        startActivity(registerActivityIntent);

    }

    //Merchant login input validation
    public void test_merc_login(View view) {
        //String LoginPassword = etLoginPassword.getText().toString();
        //String WalletID = etLoginWalletID.getText().toString();

        MercName = txtUserName.getText().toString();
        String password = txtPass.getText().toString();

        if(MercName.matches("")){
            Toast.makeText(this, "Please fill in username.", Toast.LENGTH_SHORT).show();
        }
        else if(password.matches("")){
            Toast.makeText(this, "Please fill in password.", Toast.LENGTH_SHORT).show();
        }
        checkUser(this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/test_merc_login.php", MercName,password);

    }

    //Find merchant from database and retrieve relevant information
    public void checkUser(Context context, String url, final String MercName, final String password) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);


        if (!pDialog.isShowing()){
            pDialog.setMessage("Sync with server...");
            pDialog.show();
        }



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
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                } else if (success == 1) {
                                    checkPass = jsonObject.getString("password");
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    if (checkPass.equals(password)) {
                                        LOGGED_IN_USER = MercName;
                                        Merc_WalletID = jsonObject.getString("WalletID");
                                        Toast.makeText(getApplicationContext(), "Welcome, "+MercName+".", Toast.LENGTH_LONG).show();
                                        goToMenu();
                                    } else {
                                        err += "Password is incorrect.";
                                    }
                                } else if (success == 2) {
                                    err+="Wallet not found.";
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                } else{
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                }
                                //show error
                                if(err.length()>0){
                                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
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
                    params.put("MercName", MercName);
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

    public void goToMenu() {
        Intent intent = new Intent(Login.this, Menu_screen.class);
        LOGGED_IN_USER = MercName;
        intent.putExtra("WalletID",Merc_WalletID);
        startActivity(intent);
    }

}
