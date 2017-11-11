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

public class studLogin extends AppCompatActivity {
    EditText etLoginWalletID, etLoginPassword;
    private ProgressDialog pDialog;
    String checkPass;
    String walletID;
    Double balance;
    int loyaltyPoint;
    String loginPassword;
    String currentCard;
    String currentCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        etLoginWalletID = (EditText) findViewById(R.id.etLoginWalletID);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        pDialog = new ProgressDialog(this);
    }

    public void login(View view) {
        String LoginPassword = etLoginPassword.getText().toString();
        String WalletID = etLoginWalletID.getText().toString();
        if(WalletID.matches("")){
            Toast.makeText(this, "Please fill in username.", Toast.LENGTH_SHORT).show();
        }
        else if(LoginPassword.matches("")){
            Toast.makeText(this, "Please fill in password.", Toast.LENGTH_SHORT).show();
        }
        //checkUser(this, "https://gabriellb-wp14.000webhostapp.com/select_user.php", WalletID,LoginPassword);
        checkUser(this, "https://martpay.000webhostapp.com/gab_select_user.php", WalletID,LoginPassword);

    }

    public void checkUser(Context context, String url, final String WalletID, final String LoginPassword) {
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
                                    checkPass = jsonObject.getString("LoginPassword");
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                    if (checkPass.equals(LoginPassword)) {
                                        walletID = WalletID;
                                        balance = jsonObject.getDouble("Balance");
                                        loyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                        loginPassword = jsonObject.getString("LoginPassword");
                                        currentCard = jsonObject.getString("CurrentCard");
                                        currentCardType = jsonObject.getString("CurrentCardType");
                                        Toast.makeText(getApplicationContext(), "Welcome, "+walletID+".", Toast.LENGTH_LONG).show();
                                        goToMain();
                                    } else {
                                        err += "Password is incorrect.";
                                    }
                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                    params.put("WalletID", WalletID);
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

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("walletID",walletID);
        intent.putExtra("balance",balance);
        intent.putExtra("loyaltyPoint",loyaltyPoint);
        intent.putExtra("loginPassword",loginPassword);
        intent.putExtra("currentCard",currentCard);
        intent.putExtra("currentCardType",currentCardType);
        startActivity(intent);
    }

}
