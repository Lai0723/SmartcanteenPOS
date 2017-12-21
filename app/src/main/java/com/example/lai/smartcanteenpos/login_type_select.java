/**
 * Created by Gabriel Lai Bihsyan, RSD, Year 2017
 */
package com.example.lai.smartcanteenpos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login_type_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type_select);
    }

    public void goStudLogin(View v){

        Intent intent = new Intent(this, studLogin.class);
        startActivity(intent);
    }

    public void goMercLogin(View v){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

}
