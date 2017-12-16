package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.lai.smartcanteenpos.Obejct.Report;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class report extends Fragment {


    public report() {
        // Required empty public constructor
    }

    DatePicker datePicker;
    private Button btnDailyReport, btnMonthlyReport, btnYearlyReport;
    int day,month,year;
    ProgressDialog progressDialog;
    Double total = 0.00;
    Double discounted_amount = 0.00;
    String date;


    String MercName = Login.LOGGED_IN_USER;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        progressDialog = new ProgressDialog(v.getContext());
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        datePicker.setMaxDate(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int yearSelected, int monthSelected, int daySelected) {
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth()+1;
                year = datePicker.getYear();
            }
        });

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        btnDailyReport = (Button) v.findViewById(R.id.btnDailyReport);
        btnMonthlyReport = (Button) v.findViewById(R.id.btnMonthlyReport);
        btnYearlyReport = (Button) v.findViewById(R.id.btnYearlyReport);

        btnDailyReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                total = 0.00;
                discounted_amount = 0.00;
                String dateOfRep= year+"-"+month+"-"+day;
                String type = "Generate Daily report";
                report.BackgroundWorker backgroundWorker = new report.BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, dateOfRep);
                date= year+"-"+month+"-"+day;


            }
        });

        btnMonthlyReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                total = 0.00;
                discounted_amount = 0.00;
                String MonthOfRep = month+"";
                String YearOfRep = year+"";
                String type = "Generate Monthly report";
                report.BackgroundWorker backgroundWorker = new report.BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, MonthOfRep,YearOfRep);

                date= year+"-"+month;
            }
        });

        btnYearlyReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                total = 0.00;
                discounted_amount = 0.00;
                String YearOfRep = year+"";
                String type = "Generate Yearly report";
                report.BackgroundWorker backgroundWorker = new report.BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, YearOfRep);
                date= year+"";
            }
        });





        return v;
    }

    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/DailyReport.php";
            String retrieveURL2 = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/MonthlyReport.php";
            String retrieveURL3 = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/Yearly_Report.php";


            if (type == "Generate Daily report") {
                String MercName = params[1];
                String  dateOfRep = params[2];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(retrieveURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PostData", "UTF-8") + "=" + URLEncoder.encode(MercName+";;;"+dateOfRep, "UTF-8");

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


            if (type == "Generate Monthly report") {
                String MercName = params[1];
                String  MonthOfRep = params[2];
                String  YearOfRep = params[3];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(retrieveURL2);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PostData", "UTF-8") + "=" + URLEncoder.encode(MercName+";;;"+MonthOfRep+";;;"+YearOfRep, "UTF-8");

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

            if (type == "Generate Yearly report") {
                String MercName = params[1];
                String  YearOfRep = params[2];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(retrieveURL3);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PostData", "UTF-8") + "=" + URLEncoder.encode(MercName+";;;"+YearOfRep, "UTF-8");

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

            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Gather record");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    Menu_screen.RList = new ArrayList<>();
                    Menu_screen.RList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        String OrderID = courseResponse.getString("OrderID");
                        String ProdName = courseResponse.getString("ProdName");
                        int OrderQuantity = Integer.parseInt(courseResponse.getString("OrderQuantity"));
                        Double OrderPrice = Double.parseDouble(courseResponse.getString("PayAmount"));
                        Double PayAmount = Double.parseDouble(courseResponse.getString("PayAmount"));
                        int PromotionApplied = Integer.parseInt(courseResponse.getString("PromotionApplied"));
                        Double PriceDifference = Double.parseDouble(courseResponse.getString("PriceDifference"));



                        Report listing = new Report(OrderID, ProdName, OrderQuantity,OrderPrice,PayAmount,PromotionApplied,PriceDifference);
                        Menu_screen.RList.add(listing);


                        total = total + PayAmount;
                        discounted_amount = discounted_amount + PriceDifference;
                    }


                    Intent intent = new Intent(getActivity(),reportdetails.class);
                    //String date= year+"-"+month+"-"+day;
                    Double profit = total;
                    Double discount = discounted_amount;

                    intent.putExtra("date",date);
                    intent.putExtra("total",profit);
                    intent.putExtra("discount",discount);
                    startActivity(intent);


                    if (progressDialog.isShowing())
                        progressDialog.dismiss();






                } catch (Exception e) {
                    Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getView().getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }

}
