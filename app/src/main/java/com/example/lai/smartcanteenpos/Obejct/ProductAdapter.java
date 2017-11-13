package com.example.lai.smartcanteenpos.Obejct;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lai.smartcanteenpos.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by lai on 14/10/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Activity context, int resource, List<Product> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Product list = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listlayout, parent, false);

        TextView PID,PName,PCat,PDesc,PPrice;
        ImageView ivImage;


        PID = (TextView) rowView.findViewById(R.id.PID);
        PName = (TextView) rowView.findViewById(R.id.PName);
        PCat = (TextView) rowView.findViewById(R.id.PCat);
        PDesc = (TextView) rowView.findViewById(R.id.PDesc);
        PPrice = (TextView) rowView.findViewById(R.id.PPrice);
        ivImage = (ImageView) rowView.findViewById(R.id.ivImage);

        PID.setText(PID.getText() + ":  " + list.getProdID());
        PName.setText(PName.getText() + ":  " + list.getProdName());
        PCat.setText(PCat.getText() + ":  " + list.getProdCat());
        PDesc.setText(PDesc.getText() + ":  " + list.getProdDesc());
        PPrice.setText(PPrice.getText() + ":  " + list.getPrice());
        getImage(list.getImageURL(), ivImage);

        return rowView;
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
