package com.juborajsarker.systeminfo.inflator_fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.fragment.system.OSFragment;
import com.juborajsarker.systeminfo.fragment.system.ProcessorFragment;
import com.juborajsarker.systeminfo.fragment.system.SensorFragment;


public class SystemInflatorFragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;


    public SystemInflatorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_system_inflator, container, false);


        View contendor = (View) container.getParent();
        appBar = (AppBarLayout) contendor.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        appBar.addView(tabs);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);


        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter (FragmentManager fragmentManager){

            super(fragmentManager);
        }

        String[]  titles = {"OS","CPU", "Sensor"};

        @Override
        public Fragment getItem(int position) {

            switch (position){

                case 0: return new OSFragment();
                case 1: return new ProcessorFragment();
                case 2: return new SensorFragment();





            }
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }



}
