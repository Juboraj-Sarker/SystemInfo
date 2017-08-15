package com.juborajsarker.systeminfo.fragment.device;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.systeminfo.R;

import static android.content.Context.BATTERY_SERVICE;


public class BatteryFragment extends Fragment {


    Intent i;

    View view;

    TextView b_level, b_health, b_status, b_capacity, b_voltage, b_temperature, b_technology, b_plugin_status;


    public BatteryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_battery, container, false);

        init();

        getBatteryCapacity(getContext());

        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-5809082953640465/9420368065");
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("2BA46C54FD47FD80CBBAD95AE0F70E1A").build();
        mAdView.loadAd(adRequest);

        return view;


    }







    private void init() {

        b_level = (TextView) view.findViewById(R.id.tv_battery_level);
        b_health = (TextView) view.findViewById(R.id.tv_battery_health);
        b_status = (TextView) view.findViewById(R.id.tv_battery_status);
        b_capacity = (TextView) view.findViewById(R.id.tv_battery_capacity);
        b_voltage = (TextView) view.findViewById(R.id.tv_battery_voltage);
        b_temperature = (TextView) view.findViewById(R.id.tv_battery_temperature);
        b_technology = (TextView) view.findViewById(R.id.tv_battery_technology);
        b_plugin_status = (TextView) view.findViewById(R.id.tv_battery_plugin_status);

        getContext().registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }









    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onReceive(Context context, Intent intent) {


            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);


            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            float  temperature= (float) ( intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f);
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
            int capacity = intent.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_CAPACITY), 0);


            String healthString = "";
            if(health == BatteryManager.BATTERY_HEALTH_COLD){
                healthString = "COLD";
            }else if (health == BatteryManager.BATTERY_HEALTH_DEAD){
                healthString = "DEAD";
            }else if (health == BatteryManager.BATTERY_HEALTH_GOOD){
                healthString = "GOOD";
            }else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT){
                healthString = "OVER HEAT";
            }else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
                healthString = "OVER VOLTAGE";
            }else if(health == BatteryManager.BATTERY_HEALTH_UNKNOWN){
                healthString = "UNKNOWN";
            }else if(health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){
                healthString = "UNSPECIFIED FAILURE";
            }



            String battery_status = "";
            if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                battery_status = "CHARGING";
            }else if (status == BatteryManager.BATTERY_STATUS_FULL){
                battery_status = "BATTERY FULL";
            }else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING){
                battery_status = "NOT CHARGING";
            }else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                battery_status = "NOT CHARGING";
            }else if (status == BatteryManager.BATTERY_STATUS_UNKNOWN){
                battery_status = "UNKNOWN";
            }



            String battery_pluged = "";
            if(plugged == BatteryManager.BATTERY_PLUGGED_USB){
                battery_pluged = "PLUGED IN";
            }else if (plugged == BatteryManager.BATTERY_PLUGGED_AC){
                battery_pluged = "PLUGED IN";
            }else if (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS){
                battery_pluged = "PLUGED IN";
            }else {
                battery_pluged = "NOT PLUGED IN";
            }


            b_technology.setText(technology);
            b_temperature.setText(String.valueOf(temperature) + "\u2103"); // \u2103 is for Celsius, \u2109 is for Fahrenheit, \u212A is for Kelvin, \u00B0R is for Romer
            b_voltage.setText(String.valueOf(voltage) + " mV");
            b_level.setText(String.valueOf(level) + "%");
            b_health.setText(healthString);
            b_status.setText(battery_status);
            b_plugin_status.setText(battery_pluged);





        }



    };



    public void getBatteryCapacity(Context mContext) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(mContext);//pass context if your code is not in activity else your code will work if it is in activity
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

            int capacity = (int) batteryCapacity;

           b_capacity.setText(String.valueOf(capacity) + " mAh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    }












