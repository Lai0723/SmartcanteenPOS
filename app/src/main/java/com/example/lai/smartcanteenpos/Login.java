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

public class Login extends AppCompatActivity {

    //declare all variables
    EditText txtUserName, txtPass;
    public  static  String LOGGED_IN_USER = "LOGGED_IN_USER";
    String username;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName =(EditText)findViewById(R.id.txtUserName);
        txtPass = (EditText)findViewById(R.id.txtPass);
    }

    public void ClickLogin (View v){

        username = txtUserName.getText().toString();
        String password = txtPass.getText().toString();
        String type = "login";


        if(txtUserName.getText().toString()==" " || txtPass.getText().toString()== " " )
        {
            Toast.makeText(getApplicationContext(), "Please fill in username and password" , Toast.LENGTH_LONG).show();

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

                Intent intent = new Intent(Login.this,Menu_screen.class);
                LOGGED_IN_USER = username;
                startActivity(intent);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}
