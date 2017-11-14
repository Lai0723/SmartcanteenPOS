package com.example.lai.smartcanteenpos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class add_product extends Fragment {

    public static final String UPLOAD_URL = "https://leowwj-wa15.000webhostapp.com/smart%20canteen%20system/newaddproduct.php";

    Spinner spinnerCat;
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    EditText txtProdName,  txtDesc, txtQuantity, txtPrice;
    boolean picChosen;
    private Button btnUpload, btnsubmit, btncancel;
    private ImageView imageView;


    public add_product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_product, container, false);
        txtProdName = (EditText) v.findViewById(R.id.txtProdName);
        spinnerCat = (Spinner) v.findViewById(R.id.spinnerCat);
        txtDesc = (EditText) v.findViewById(R.id.txtDesc);
        txtPrice = (EditText) v.findViewById(R.id.txtPrice);
        txtQuantity = (EditText) v.findViewById(R.id.txtQuantity);

        btnUpload = (Button) v.findViewById(R.id.btnUpload);
        btnsubmit = (Button) v.findViewById(R.id.btnsubmit);
        btncancel = (Button) v.findViewById(R.id.btncancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.foodCategory, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCat.setAdapter(adapter);

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set spinner selected item color
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView = (ImageView) v.findViewById(R.id.imageView);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showFileChooser();
            }

        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadClick();
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
        picChosen = false;
        return v;
    }


    public void uploadClick() {
        String ProdName = txtProdName.getText().toString();
        String ProdDesc = txtDesc.getText().toString();
        String ProdPrice = txtPrice.getText().toString();
        String ProdQuantity = txtQuantity.getText().toString();



       if (TextUtils.isEmpty(ProdName)) {
            txtProdName.setError("Field cannot be empty");
        }


        if (TextUtils.isEmpty(ProdDesc)) {
            txtDesc.setError("Field cannot be empty");
        }

        if (TextUtils.isEmpty(ProdPrice)) {
            txtPrice.setError("Field cannot be empty");
        }

        if (TextUtils.isEmpty(ProdQuantity)) {
            txtQuantity.setError("Field cannot be empty");
        }

        else {
          uploadImage();
            MenuFragment.allowRefresh = true;

            Menu_screen.lList = null;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            picChosen = true;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
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
            String ProdName = txtProdName.getText().toString();
            String ProdCat = spinnerCat.getSelectedItem().toString();
            String ProdDesc = txtDesc.getText().toString();
            String ProdPrice = txtPrice.getText().toString();
            String ProdQuantity = txtQuantity.getText().toString();


            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(add_product.this.getActivity(), "Uploading product", "Please wait...", true, true);
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


                data.put("ProdName", ProdName);
                data.put("ProdCat", ProdCat);
                data.put("ProdDesc", ProdDesc);
                data.put("ProdPrice", ProdPrice);
                data.put("ProdQuantity", ProdQuantity);
                data.put("ProdImage", ProdImage);
                data.put("MercName", Login.LOGGED_IN_USER);




                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);

    }

}
