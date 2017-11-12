package com.example.lai.smartcanteenpos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    public void ClickLogin(View v) {

        username = txtUserName.getText().toString();
        String password = txtPass.getText().toString();
        String type = "login";


        if (txtUserName.getText().toString() == " " || txtPass.getText().toString() == " ") {
            Toast.makeText(getApplicationContext(), "Please fill in username and password", Toast.LENGTH_LONG).show();

        }

        progressDialog = new ProgressDialog(this);

        // execute backgroudWorker class to check whether the user is exist in database or not
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);

    }

    // when register button was clicked
    protected void ClickRegister(View v) {

        Intent registerActivityIntent = new Intent(this, Register.class);

        startActivity(registerActivityIntent);

    }

    // To check the login status
    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        AlertDialog alertDialog;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String loginURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/login_merchants.php";
            //String loginURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/test_merc_login.php";

            // if the type of the task = login
            if (type == "login") {

                String userName = params[1];
                String password = params[2];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(loginURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("MercName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    // read the data
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            // create an dialog and set it's title
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");

            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Logging in");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {
            // if login failed
            if (result.equals("login not success")) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                alertDialog.setMessage("Login failed. Please check your username and password");
                alertDialog.show();
            }
            //else allow user to login
            else {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();


                /*JSONObject jsonObject;
                jsonObject = new JSONObject();
                try {
                    WalletID = jsonObject.getString("WalletID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                Intent intent = new Intent(Login.this, Menu_screen.class);
                LOGGED_IN_USER = username;
                //intent.putExtra("WalletID",WalletID);
                startActivity(intent);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }



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
        //checkUser(this, "https://gabriellb-wp14.000webhostapp.com/select_user.php", WalletID,LoginPassword);
        //checkUser(this, "https://martpay.000webhostapp.com/gab_select_user.php", WalletID,LoginPassword);
        checkUser(this, "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/test_merc_login.php", MercName,password);

    }

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
