package com.juborajsarker.systeminfo.fragment.system;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.systeminfo.R;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;


public class OSFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String flag = "";

    View view;

    TextView tv_android_version, tv_version_name, tv_api_level, tv_root_access, tv_build_id, tv_fingerprint, tv_version_code;


    public OSFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_os, container, false);

        init();

        sharedPreferences = getActivity().getSharedPreferences("osPrefences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        flag = sharedPreferences.getString("osStatus", "");

        if (flag.equals("true")){


            tv_android_version.setText(sharedPreferences.getString("osVersion", ""));
            tv_version_name.setText(sharedPreferences.getString("versionName", ""));
            tv_api_level.setText(sharedPreferences.getString("apiLevel", ""));
            tv_root_access.setText(sharedPreferences.getString("rootAccess", ""));
            tv_build_id.setText(sharedPreferences.getString("buildID", ""));
            tv_version_code.setText(sharedPreferences.getString("codeName", ""));
            tv_fingerprint.setText(sharedPreferences.getString("fingerprint", ""));



        }else {


            getOSInfo();
        }



        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-5809082953640465/9420368065");
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("2BA46C54FD47FD80CBBAD95AE0F70E1A").build();
        mAdView.loadAd(adRequest);




        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        editor.putString("osStatus", "false");
    }

    private void init() {

        tv_android_version = (TextView) view.findViewById(R.id.tv_android_version);
        tv_version_name = (TextView) view.findViewById(R.id.tv_android_version_name);
        tv_api_level = (TextView) view.findViewById(R.id.tv_api_level);
        tv_root_access = (TextView) view.findViewById(R.id.tv_root_access);
        tv_build_id = (TextView) view.findViewById(R.id.tv_os_build_id);
        tv_fingerprint = (TextView) view.findViewById(R.id.tv_fingerprint);
        tv_version_code = (TextView) view.findViewById(R.id.tv_os_version_code);
    }

    private void getOSInfo() {


        editor.putString("osStatus", "true");


        String version = Build.VERSION.RELEASE;
        int api_level = Build.VERSION.SDK_INT;
        String fingerprint = Build.FINGERPRINT;
        String build_id = Build.ID;

        getRootInfo();
        if (getRootInfo()){

            tv_root_access.setText("ROOTED");
            editor.putString("rootAccess", "ROOTED");

        }else {

            tv_root_access.setText("NOT ROOTED");
            editor.putString("rootAccess", "NOT ROOTED");
        }



         int os_version  = android.os.Build.VERSION.SDK_INT;

        if (os_version == Build.VERSION_CODES.JELLY_BEAN || os_version == Build.VERSION_CODES.JELLY_BEAN_MR1 || os_version == Build.VERSION_CODES.JELLY_BEAN_MR2){

            tv_version_name.setText("JELLY BEAN");
            editor.putString("versionName", "JELLY BEAN");

        }else if (os_version == Build.VERSION_CODES.KITKAT || os_version == Build.VERSION_CODES.KITKAT_WATCH){

            tv_version_name.setText("KITKAT");
            editor.putString("versionName", "KITKAT");

        }else if (os_version == Build.VERSION_CODES.LOLLIPOP || os_version == Build.VERSION_CODES.LOLLIPOP_MR1){

            tv_version_name.setText("LOLLIPOP");
            editor.putString("versionName", "LOLLIPOP");

        }else if (os_version == Build.VERSION_CODES.M ){

            tv_version_name.setText("MARSHMALLOW");
            editor.putString("versionName", "MARSHMALLOW");

        }else if (os_version == Build.VERSION_CODES.N || os_version == Build.VERSION_CODES.N_MR1){

            tv_version_name.setText("NOUGAT");
            editor.putString("versionName", "NOUGAT");

        }

        tv_android_version.setText(version);
        editor.putString("osVersion", version);

        tv_api_level.setText(String.valueOf(api_level));
        editor.putString("apiLevel", String.valueOf(api_level));

        tv_fingerprint.setText(fingerprint);
        editor.putString("fingerprint", fingerprint);

        tv_build_id.setText(build_id);
        editor.putString("buildID", build_id);

        tv_version_code.setText(Build.VERSION.CODENAME);
        editor.putString("codeName", Build.VERSION.CODENAME);

        editor.commit();





    }

    private boolean getRootInfo() {


        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccessfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccessfully = true;
        } catch (Exception e) {
            executedSuccessfully = false;
        }

        return executedSuccessfully;


    }


}
