package com.example.lai.smartcanteenpos;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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

import com.android.volley.RequestQueue;
import com.example.lai.smartcanteenpos.Obejct.Product;
import com.example.lai.smartcanteenpos.Obejct.ProductAdapter;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment   {
    Spinner Spinner;
    Button add, delete,update,NameSearch,CatSearch;
    public static boolean allowRefresh;
    public static final String TAG = "com.example.user.myApp";
    EditText searchProduct;

    ListView menulistview;
    String MercName;
    ProgressDialog progressDialog;
    RequestQueue queue;

    String ProdName,ProdCat;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        allowRefresh = false;
        menulistview = (ListView)v.findViewById(R.id.menulistview);
         add =(Button)v.findViewById(R.id.AddItem);
         delete = (Button)v.findViewById(R.id.DeleteItem);
         update = (Button)v.findViewById(R.id.UpdateItem);
         NameSearch = (Button)v.findViewById(R.id.NameSearch);
        CatSearch = (Button)v.findViewById(R.id.CatSearch);
        Spinner = (Spinner)v.findViewById(R.id.SearchSpinner);
        searchProduct = (EditText)v.findViewById(R.id.searchProduct);

        progressDialog = new ProgressDialog(v.getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.foodCategory, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner.setAdapter(adapter);

        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set spinner selected item color
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        NameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "retrieveProductName";
                MercName = Login.LOGGED_IN_USER;
                ProdName = searchProduct.getText().toString();
                BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, ProdName);
            }
        });

        CatSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "retrieveProductCat";
                MercName = Login.LOGGED_IN_USER;
                ProdCat = Spinner.getSelectedItem().toString();
                BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                backgroundWorker.execute(type,  MercName, ProdCat);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_product nextFrag= new add_product();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_menu nextFrag= new update_menu();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_product nextFrag= new delete_product();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (Menu_screen.lList == null) {
            Menu_screen.lList = new ArrayList<>();

            String type = "retrieveProduct";
            MercName = Login.LOGGED_IN_USER;
            BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
            backgroundWorker.execute(type,  MercName);



        } else {
            loadListing();
        }


        return v;


    }


    private void loadListing() {
        final ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.fragment_menu, Menu_screen.lList);
        menulistview.setAdapter(adapter);

    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;
        AlertDialog alertDialog;


        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String RetrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/getlist.php";
            String RetrieveURL2 ="https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/SearchByName.php";
            String RetrieveURL3 ="https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/SearchByCategory.php";

            if (type == "retrieveProduct") {
                String MercName = params[1];

                try {

                    //establish httpUrlConnection with POST method
                    URL url = new URL(RetrieveURL);
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

            if (type == "retrieveProductName") {
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

            if (type == "retrieveProductCat") {
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


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {

                    Menu_screen.lList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse= (JSONObject) jsonArray.get(i);



                        String ProdID = courseResponse.getString("ProdID");
                        String ProdName = courseResponse.getString("ProdName");
                        String ProdCat = courseResponse.getString("ProdCat");
                        String ProdDesc = courseResponse.getString("ProdDesc");
                        double ProdPrice = Double.parseDouble(courseResponse.getString("ProdPrice"));
                        String ImageURL = courseResponse.getString("url");


                        Product listing = new Product(ProdID, ProdName, ProdCat, ProdDesc, ProdPrice,ImageURL);
                        Menu_screen.lList.add(listing);

                    }



                    loadListing();
                    Toast.makeText(getView().getContext(), "", Toast.LENGTH_LONG).show();



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

    public void onDestroyView() {
        super.onDestroyView();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }






}
