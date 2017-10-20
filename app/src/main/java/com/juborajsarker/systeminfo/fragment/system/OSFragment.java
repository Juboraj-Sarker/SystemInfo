package com.juborajsarker.systeminfo.fragment.system;


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
import com.juborajsarker.systeminfo.SystemInfoManager;

import java.io.File;


public class OSFragment extends Fragment {

    private static final int ROOT_STATUS_NOT_CHECKED = 0;
    private static final int ROOT_STATUS_ROOTED = 1;
    private static final int ROOT_STATUS_NOT_ROOTED = 2;


    View view;

    TextView tv_android_version, tv_version_name, tv_api_level, tv_root_access, tv_build_id, tv_fingerprint, tv_version_code;


    public OSFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_os, container, false);

        init();


        getOSInfo();



        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_home_footer_1) );
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);




        return view;
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





        String version = Build.VERSION.RELEASE;
        int api_level = Build.VERSION.SDK_INT;
        String fingerprint = Build.FINGERPRINT;
        String build_id = Build.ID;

        isRooted();

        if (isRooted()){

            tv_root_access.setText("ROOTED");


        }else {

            tv_root_access.setText("NOT ROOTED");

        }



         int os_version  = android.os.Build.VERSION.SDK_INT;

        if (os_version == Build.VERSION_CODES.JELLY_BEAN || os_version == Build.VERSION_CODES.JELLY_BEAN_MR1 || os_version == Build.VERSION_CODES.JELLY_BEAN_MR2){

            tv_version_name.setText("JELLY BEAN");


        }else if (os_version == Build.VERSION_CODES.KITKAT || os_version == Build.VERSION_CODES.KITKAT_WATCH){

            tv_version_name.setText("KITKAT");


        }else if (os_version == Build.VERSION_CODES.LOLLIPOP || os_version == Build.VERSION_CODES.LOLLIPOP_MR1){

            tv_version_name.setText("LOLLIPOP");


        }else if (os_version == Build.VERSION_CODES.M ){

            tv_version_name.setText("MARSHMALLOW");


        }else if (os_version == Build.VERSION_CODES.N || os_version == Build.VERSION_CODES.N_MR1){

            tv_version_name.setText("NOUGAT");


        }

        tv_android_version.setText(version);


        tv_api_level.setText(String.valueOf(api_level));


        tv_fingerprint.setText(fingerprint);


        tv_build_id.setText(build_id);


        tv_version_code.setText(Build.VERSION.CODENAME);



    }

//    private boolean getRootInfo() {
//
//
//        // get from build info
//        String buildTags = android.os.Build.TAGS;
//        if (buildTags != null && buildTags.contains("test-keys")) {
//            return true;
//        }
//
//        // check if /system/app/Superuser.apk is present
//        try {
//            File file = new File("/system/app/Superuser.apk");
//            if (file.exists()) {
//                return true;
//            }
//        } catch (Exception e1) {
//            // ignore
//        }
//
//        // try executing commands
//        return canExecuteCommand("/system/xbin/which su")
//                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
//    }
//
//    // executes a command on the system
//    private static boolean canExecuteCommand(String command) {
//        boolean executedSuccessfully;
//        try {
//            Runtime.getRuntime().exec(command);
//            executedSuccessfully = true;
//        } catch (Exception e) {
//            executedSuccessfully = false;
//        }
//
//        return executedSuccessfully;
//
//
//    }











    public static boolean isRooted() {
        int rootStatus = SystemInfoManager.getAppPreferences().getRootStatus();
        boolean isRooted = false;
        if (rootStatus == ROOT_STATUS_NOT_CHECKED) {
            isRooted = isRootByBuildTag() || isRootedByFileSU() || isRootedByExecutingCommand();
            SystemInfoManager.getAppPreferences().setRootStatus(isRooted ? ROOT_STATUS_ROOTED : ROOT_STATUS_NOT_ROOTED);
        } else if (rootStatus == ROOT_STATUS_ROOTED) {
            isRooted = true;
        }
        return isRooted;
    }

    public static boolean isRootByBuildTag() {
        String buildTags = Build.TAGS;
        return ((buildTags != null && buildTags.contains("test-keys")));
    }

    public static boolean isRootedByFileSU() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
        }
        return false;
    }

    public static boolean isRootedByExecutingCommand() {
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su");
    }


    private static boolean canExecuteCommand(String command) {
        boolean isExecuted;
        try {
            Runtime.getRuntime().exec(command);
            isExecuted = true;
        } catch (Exception e) {
            isExecuted = false;
        }

        return isExecuted;
    }













}
