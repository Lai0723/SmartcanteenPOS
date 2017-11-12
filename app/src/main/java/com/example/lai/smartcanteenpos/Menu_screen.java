package com.example.lai.smartcanteenpos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.lai.smartcanteenpos.Obejct.Inventory;
import com.example.lai.smartcanteenpos.Obejct.Menu;
import com.example.lai.smartcanteenpos.Obejct.Order;
import com.example.lai.smartcanteenpos.Obejct.Product;
import com.example.lai.smartcanteenpos.Obejct.Purchase_order;

import java.util.List;

public class Menu_screen extends AppCompatActivity {
    public static List<Product> lList = null;
    public static List<Inventory> SList = null;
    public static List<Purchase_order>OList = null;
    public static List<Order>ORDERList = null;
    public static List<Menu> MList = null;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_Wallet:
                    fragmentMerc_wallet w = new fragmentMerc_wallet();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,w).commit();
                    break;

                case R.id.navigation_Menu:
                    MenuFragment m = new MenuFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,m).commit();
                    break;

                case R.id.navigation_Order:
                    OrderFragment o = new OrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,o).commit();
                    break;

                case R.id.navigation_Inventory:
                    InventoryFragment i = new InventoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,i).commit();
                    break;

                case R.id.navigation_Report:
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

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragmentMerc_wallet.newInstance());
        transaction.commit();

    }


}
