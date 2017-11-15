package com.example.lai.smartcanteenpos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class update_menu extends Fragment {

    public static final String Image_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_image.php";
    Button btnName,btnCat,btnPrice,btnDesc,btncancel,btnSearch,btnImage,btnUpload;
    EditText txtProdID;
    String newName,newCat,newDesc,newPrice,ProdID;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    boolean picChosen;
    private Bitmap bitmap;
    ImageView imageView2,imageView3;
    String MercName;
    ProgressDialog progressDialog;
    TextView editName,editPrice,editDesc,editCat;


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
        btnUpload = (Button)v.findViewById(R.id.btnUpload);
        btnSearch = (Button)v.findViewById(R.id.btnSearch);
        btnImage = (Button)v.findViewById(R.id.btnImage);
       imageView2 = (ImageView)v.findViewById(R.id.imageView2);
        editName = (TextView)v.findViewById(R.id.editName);
        editPrice = (TextView)v.findViewById(R.id.editPrice);
        editDesc = (TextView)v.findViewById(R.id.editDesc);
        editCat = (TextView)v.findViewById(R.id.editCat);
        imageView3 = (ImageView)v.findViewById(R.id.imageView3);



        ProdID = txtProdID.getText().toString();

        if(TextUtils.isEmpty(ProdID)){
            txtProdID.setError("Must field in product id first");
        }

            btnName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_text, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Name");
                    final EditText editTextNew = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newName = editTextNew.getText().toString();


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

                    final View view = (LayoutInflater.from(v.getContext()).inflate(R.layout.edit_spinner, null));
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setView(view);
                    alertBuilder.setTitle("Edit Product Category");
                    final Spinner spinnerCat2 = (Spinner) view.findViewById(R.id.spinnerCat2);

                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.foodCategory, android.R.layout.simple_spinner_item);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerCat2.setAdapter(adapter);

                    spinnerCat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //set spinner selected item color
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newCat = spinnerCat2.getSelectedItem().toString();


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
                    final EditText editTextNew = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newDesc = editTextNew.getText().toString();


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
                    final EditText editTextNew = (EditText) view.findViewById(R.id.changeText);

                    alertBuilder.setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    newPrice = editTextNew.getText().toString();


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

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


                    picChosen = false;
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;

                }
            });

            btnImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    uploadImage();
                }


            });


            btnSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String ProdID = txtProdID.getText().toString();
                    String type = "retrieveProductInfo";

                    BackgroundWorker backgroundWorker = new BackgroundWorker(v.getContext());
                    backgroundWorker.execute(type, ProdID);
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
        editName.setText(newName.toString());
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
        editCat.setText(newCat.toString());
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
        editDesc.setText(newDesc.toString());
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
        editPrice.setText(newPrice.toString());
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            picChosen = true;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView3.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {


            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_menu.this.getActivity(), "Uploading image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String ProdImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();


                data.put("ProdID", txtProdID.getText().toString());
                data.put("ProdImage", ProdImage);



                String result = rh.sendPostRequest(Image_URL, data);

                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);

    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {

        Context context;



        public BackgroundWorker(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String RetrieveURL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/update_get_info.php";

            // if the type of the task = retrieveBorrowHistory
            if (type == "retrieveProductInfo") {
                String ProdID = params[1];


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
                    String post_data = URLEncoder.encode("ProdID", "UTF-8") + "=" + URLEncoder.encode(ProdID, "UTF-8");

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
/*
            if (!progressDialog.isShowing()) ;
            progressDialog.setMessage("Retrieving Product");
            progressDialog.show();*/


        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);

                try {


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject courseResponse= (JSONObject) jsonArray.get(i);




                        String ProdName = courseResponse.getString("ProdName");
                        String ProdCat = courseResponse.getString("ProdCat");
                        String ProdDesc = courseResponse.getString("ProdDesc");
                        double ProdPrice = Double.parseDouble(courseResponse.getString("ProdPrice"));
                        String ImageURL = courseResponse.getString("url");



                        editName.setText(ProdName.toString());
                        editCat.setText(ProdCat.toString());
                        editDesc.setText(ProdDesc.toString());
                        editPrice.setText(Double.toString(ProdPrice));
                        getImage(ImageURL, imageView2);


                    }

                    /*if (progressDialog.isShowing())
                        progressDialog.dismiss();*/


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

    private void getImage(String urlToImage, final ImageView ivImage) {
        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;

                String urlToImage = params[0];
                try {
                    url = new URL(urlToImage);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "Downloading Image...", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                ivImage.setImageBitmap(bitmap);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }


}
