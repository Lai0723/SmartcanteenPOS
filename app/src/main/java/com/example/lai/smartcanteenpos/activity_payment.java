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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lai.smartcanteenpos.Obejct.MAdapter;
import com.example.lai.smartcanteenpos.Obejct.Menu;

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

import static com.example.lai.smartcanteenpos.R.id.menulistview;


/**
 * A simple {@link Fragment} subclass.
 */
public class activity_payment extends Fragment {
    Spinner menuspinner;
    private static final String TAG = "PaymentActivity";
    ListView Menulist;
    ProgressDialog progressDialog;
    public static boolean allowRefresh;
    String MercName, totalToPass,idToPass;
    static double itemInCart[] = new double[100];
    static int qtyOrdered = 0;
    Menu purchased[] = new Menu[100];
    Button btnTotal,btnSearchName,btnSearchCat;
    EditText menuProductName;
    TextView Total,ItemInCart;

    static int Cart = 0;
    String purchasedID="";

    String ttlPurchaseAmt,menuProdName,menuProdCat;


    @Override
    public void onResume() {
        super.onResume();
        Menu_screen.MList = null;
        Menu_screen.MList = new ArrayList<>();
        String type = "retrieveMenu";
        MercName = Login.LOGGED_IN_USER;
        activity_payment.BackgroundWorker backgroundWorker = new activity_payment.BackgroundWorker(getView().getContext());
        backgroundWorker.execute(type, MercName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_activity_payment, container, false);

        View v = inflater.inflate(R.layout.fragment_activity_payment, container, false);
        allowRefresh = false;


        Menulist = (ListView) v.findViewById(menulistview);
        btnTotal = (Button) v.findViewById(R.id.btnTotal);
        Total = (TextView) v.findViewById(R.id.TotalPrice);
        ItemInCart = (TextView) v.findViewById(R.id.txtCart);
        btnSearchName = (Button)v.findViewById(R.id.btnMenuSearch);
        btnSearchCat = (Button)v.findViewById(R.id.btnMenuSearchCat);
        menuspinner = (Spinner) v.findViewById(R.id.MenuSpinner);
        menuProductName = (EditText)v.findViewById(R.id.MenuProductName);


        progressDialog = new ProgressDialog(v.getContext());

        purchased = new Menu[100];
        qtyOrdered = 0;
        Cart=0;
        ItemInCart.setText(Integer.toString(Cart));
        Total.setText("RM 0");

        // set the info in to the product category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.foodCategory, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        menuspinner.setAdapter(adapter);

        menuspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set spinner selected item color
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //allow user to search the list after key in the name and press the button
        btnSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "retrieveMenuProductName";
                MercName = Login.LOGGED_IN_USER;
                menuProdName =  menuProductName.getText().toString();
                BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, menuProdName);
            }
        });

        //allow user to search the list by cateory and press the button
        btnSearchCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "retrieveMenuProductCat";
                MercName = Login.LOGGED_IN_USER;
                menuProdCat = menuspinner.getSelectedItem().toString();
                BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, menuProdCat);
            }
        });


       // add product into cart after press on the product in the list, calculate the total and update the database
        Menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu entry = (Menu) parent.getItemAtPosition(position);
                if (qtyOrdered < 99) {
                    if(entry.getQuantity()>0){
                        //update quantity (local, not db)
                        entry.setQuantity(entry.getQuantity()-1);
                        TextView MQuantity;
                        MQuantity = (TextView) view.findViewById(R.id.MQuantity);
                        MQuantity.setText("Quantity left" + " : "+ entry.getQuantity());

                        purchased[qtyOrdered] = entry;
                        qtyOrdered++;
                        Toast.makeText(getView().getContext(), "Add to cart succesful", Toast.LENGTH_LONG).show();
                        Cart++;
                        ItemInCart.setText(Integer.toString(Cart));
                        calcTotal();
                    }else{
                        Toast.makeText(getView().getContext(), "Item out of stock", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getView().getContext(), "Only can order 99 item at once", Toast.LENGTH_LONG).show();
                }
            }
        });

        // to scan QR code to perform payment
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double total = calcTotal();


                totalToPass = Double.toString(total);
                idToPass= purchasedID;

                //Passing total purchase amount to QR scanner
                ttlPurchaseAmt = totalToPass;
                Intent intent = new Intent(v.getContext(), onSpotTransferScanner.class);
                intent.putExtra("ttlPurchaseAmt", totalToPass);
                intent.putExtra("purchasedID", purchasedID);


                purchased = new Menu[100];
                qtyOrdered = 0;
                Cart=0;
                purchasedID="";
                ItemInCart.setText(Integer.toString(Cart));
                Total.setText("RM 0");
                startActivity(intent);
                //Menu_screen.startScan();

            }
        });



        return v;
    }

    //method to calculate product total price
    private double calcTotal(){

        int j = 0;
        double total = 0;
        purchasedID="";
        while (purchased[j] != null) {

            total += purchased[j].getPrice();
            purchasedID +=purchased[j].getProdID()+";;";
            j++;
        }
        purchasedID=(purchasedID.substring(0, purchasedID.length() - 1));

        Total.setText("RM  " + Double.toString(total));
        return total;

    }

    // to get the product info from database
    public class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String retrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getmenu.php";
            String RetrieveURL2 ="https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/Menu_SearchByName.php";
            String RetrieveURL3 ="https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/Menu_SearchByCategory.php";

            if (type == "retrieveMenu") {
                String MercName = params[1];

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
                    String post_data = URLEncoder.encode("MercName", "UTF-8") + "=" + URLEncoder.encode(MercName, "UTF-8");

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

            if (type == "retrieveMenuProductName") {
                String MercName = params[1];
                String  ProdName = params[2];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(RetrieveURL2);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PostData", "UTF-8") + "=" + URLEncoder.encode(MercName+";;;"+ProdName, "UTF-8");

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

            if (type == "retrieveMenuProductCat") {
                String MercName = params[1];
                String ProdCat = params[2];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(RetrieveURL3);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    //set output stream
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("PostData", "UTF-8") + "=" + URLEncoder.encode(MercName+";;;"+ProdCat, "UTF-8");

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
            progressDialog.setMessage("Retrieving order activities");
            progressDialog.show();


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {
                    Menu_screen.MList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse = (JSONObject) jsonArray.get(i);

                        String ProdID = courseResponse.getString("ProdID");
                        String ProdName = courseResponse.getString("ProdName");
                        double Price = Double.parseDouble(courseResponse.getString("ProdPrice"));
                        int Quantity = Integer.parseInt(courseResponse.getString("ProdQuantity"));


                        Menu listing = new Menu(ProdID, ProdName, Price,Quantity);


                        Menu_screen.MList.add(listing);
                    }

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    loadListing();


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

    //load product info into the adapter
    private void loadListing() {
        final MAdapter adapter = new MAdapter(getActivity(), R.layout.fragment_activity_payment, Menu_screen.MList);
        Menulist.setAdapter(adapter);

    }



}
