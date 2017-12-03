package com.example.lai.smartcanteenpos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lai.smartcanteenpos.Obejct.Inventory;
import com.example.lai.smartcanteenpos.Obejct.Menu;
import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.Product;
import com.example.lai.smartcanteenpos.Obejct.Purchase_order;
import com.example.lai.smartcanteenpos.Obejct.Report;
import com.example.lai.smartcanteenpos.Obejct.Report_Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu_screen extends AppCompatActivity {
    public static List<Product> lList = null;
    public static List<Inventory> SList = null;
    public static List<Purchase_order>OList = null;
    public static List<Order>ORDERList = null;

    public static List<Menu> MList = null;
    public static List<Report> RList = null;
    public static List<Report_Transaction> RTList = null;

    static String Merc_WalletID;
    public static double balance;
    public static int loyaltyPoint;
    TextView tvMercBalance;
    String MercName;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_Wallet:
                    checkBalance(Menu_screen.this, "https://martpay.000webhostapp.com/gab_select_user.php");
                    fragmentMerc_wallet w = new fragmentMerc_wallet();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,w).commit();
                    break;

                case R.id.navigation_Menu:
                    MenuFragment m = new MenuFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,m).commit();
                    MenuFragment.allowRefresh = true;

                    Menu_screen.lList = null;
                    break;

                case R.id.navigation_Order:
                    OrderFragment o = new OrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,o).commit();
                    activity_payment.allowRefresh = true;

                    Menu_screen.MList = null;
                    break;

                case R.id.navigation_Inventory:
                    InventoryFragment i = new InventoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,i).commit();
                    InventoryFragment.allowRefresh = true;

                    Menu_screen.SList = null;
                    break;

                case R.id.navigation_Report:
                    choose_report r = new choose_report();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,r).commit();

                    break;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, "ERROR: NO EXTRAS FOUND!", Toast.LENGTH_SHORT).show();
            finish();
        }
        Merc_WalletID = extras.getString("WalletID");
        checkBalance(Menu_screen.this, "https://martpay.000webhostapp.com/gab_select_user.php");

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragmentMerc_wallet.newInstance());
        transaction.commit();

    }

    public void checkBalance(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

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

                                } else if (success == 1) {
                                    balance = jsonObject.getDouble("Balance");
                                    loyaltyPoint = jsonObject.getInt("LoyaltyPoint");
                                    //Toast.makeText(getApplicationContext(), "Balance loaded", Toast.LENGTH_LONG).show();
                                    tvMercBalance = (TextView) findViewById(R.id.tvMercBalance);
                                    if (tvMercBalance != null)
                                        tvMercBalance.setText(String.format("RM %.2f", Menu_screen.balance));
                                } else if (success == 2) {
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    err += "Wallet not found.";

                                } else {
                                    Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_SHORT).show();

                                }
                                //show error
                                if (err.length() > 0) {
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
                    params.put("WalletID", Merc_WalletID);
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




}
