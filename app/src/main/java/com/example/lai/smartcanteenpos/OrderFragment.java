package com.example.lai.smartcanteenpos;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    public static boolean allowRefresh;

    //DECLARE A View Pager
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_order, container, false);

        View v = inflater.inflate(R.layout.fragment_order, container, false);

        //Implement tabs
        mViewPager = (ViewPager) v.findViewById(R.id.container);


        setUpViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Order");
        tabLayout.getTabAt(1).setText("Payment");
        return v;
    }

    //setting up View Pager
    private void setUpViewPager(ViewPager viewPager) {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new activity_order(),"Order");
        adapter.addFragment(new activity_payment(),"Payment");
        viewPager.setAdapter(adapter);
    }




}
