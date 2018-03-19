package com.juborajsarker.systeminfo.fragment.device;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.systeminfo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceFragment extends Fragment {


    View view;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    TextView tv_device_model, tv_manufacturer, tv_brand, tv_serial_id, tv_IMEI, tv_screen_size,
            tv_screen_resolution, tv_screen_density, tv_screen_refresh_rate, tv_camera_front, tv_camera_back, tv_flash;

    String man, model, brand, deviceID;

    String screenSizeInInch;


    public DeviceFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_device, container, false);

        init();


        getDeviceInfo();
        getScreenInfo();

        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiplePermissions();
        }



        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);


        return view;
    }





    @Override
    public void onResume() {
        super.onResume();
        getCameraInfo();
        getIMEI();
    }

    private void init() {


        tv_device_model = (TextView) view.findViewById(R.id.tv_device_model);
        tv_manufacturer = (TextView) view.findViewById(R.id.tv_manufacturer);
        tv_brand = (TextView) view.findViewById(R.id.tv_brand);
        tv_serial_id = (TextView) view.findViewById(R.id.tv_serial_id);
        tv_IMEI = (TextView) view.findViewById(R.id.tv_imei);
        tv_screen_size = (TextView) view.findViewById(R.id.tv_screen_size);
        tv_screen_resolution = (TextView) view.findViewById(R.id.tv_screen_resolution);
        tv_screen_density = (TextView) view.findViewById(R.id.tv_screen_density);
        tv_screen_refresh_rate = (TextView) view.findViewById(R.id.tv_screen_refresh_rate);
        tv_camera_front = (TextView) view.findViewById(R.id.tv_camera_front);
        tv_camera_back = (TextView) view.findViewById(R.id.tv_camera_back);
        tv_flash = (TextView) view.findViewById(R.id.tv_flash);


    }


    private void getDeviceInfo() {



        man = Build.MANUFACTURER;
        model = Build.MODEL;
        brand = Build.BRAND;
        deviceID = Build.SERIAL;


        tv_manufacturer.setText(man);



        tv_device_model.setText(model);
        tv_brand.setText(brand);
        tv_serial_id.setText(deviceID);
    }


    private void getIMEI() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            tv_IMEI.setText(imei);


        }
    }


    private void getScreenInfo() {

        getScreenSize();
        getScreenResolution();
        getScreenDensity();
        getScreenRefreshRate();


    }


    private void getScreenSize() {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);

        String temp = String.valueOf(screenInches);

        screenSizeInInch = temp.substring(0, 3);
        tv_screen_size.setText(screenSizeInInch + " inch");


    }


    private void getScreenResolution() {

        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }


        tv_screen_resolution.setText(String.valueOf(Measuredwidth) + " x " + String.valueOf(Measuredheight) + " pixels");


    }


    private void getScreenDensity() {

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        String dpi = "";

        float den = getContext().getResources().getDisplayMetrics().density;
        if (den >= 4.0) {
            dpi = "xxxhdpi";
        } else if (den >= 3.0) {
            dpi = "xxhdpi";
        } else if (den >= 2.0) {
            dpi = "xhdpi";
        } else if (den >= 1.5) {
            dpi = "hdpi";
        } else if (den >= 1.0) {
            dpi = "mdpi";

        } else if (den < 1) {

            dpi = "ldpi";
        }


        tv_screen_density.setText(String.valueOf(densityDpi) + " dpi" + " (" + dpi + ")");


    }


    private void getScreenRefreshRate() {


        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float refreshRating = display.getRefreshRate();

        tv_screen_refresh_rate.setText(String.valueOf(refreshRating) + " Hz");


    }


    private void getCameraInfo() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

           // getBackCameraInfo();
          //  getFrontCameraInfo();

            getBackCameraResolutionInMp();
            getFrontCameraResolutionInMp();
        }


        getFlashInfo();


    }



    public float getBackCameraResolutionInMp() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0;i < noOfCameras;i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Camera camera = Camera.open(i);;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount)
                    {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
                        tv_camera_back.setText(String.valueOf((String.format("%.0f", maxResolution))) + " MP");


                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }





    public float getFrontCameraResolutionInMp() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0;i < noOfCameras;i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                Camera camera = Camera.open(i);;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount)
                    {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
                        tv_camera_front.setText(String.valueOf((String.format("%.0f", maxResolution))) + " MP");



                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }





//    private void getBackCameraInfo() {
//
//        Camera camera = Camera.open(0);
//
//        android.hardware.Camera.Parameters parameters = camera.getParameters();
//        android.hardware.Camera.Size size = parameters.getPictureSize();
//
//
//        double height = size.height;
//        double width = size.width;
//        float mgBack = (float) ((height * width) / 1024000);
//        tv_camera_back.setText(String.valueOf((String.format("%.0f", mgBack))) + " MP");
//
//        camera.release();
//    }


//    private void getFrontCameraInfo() {
//
//        Camera camera = Camera.open(1);
//
//        android.hardware.Camera.Parameters parameters = camera.getParameters();
//        android.hardware.Camera.Size size = parameters.getPictureSize();
//
//
//        double height = size.height;
//        double width = size.width;
//        float mgFront = (float) ((height * width) / 1024000);
//        tv_camera_front.setText(String.valueOf((String.format("%.0f", mgFront))) + " MP");
//
//        camera.release();
//
//
//    }


    private void getFlashInfo() {


        boolean flashAvailable = getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (flashAvailable) {

            tv_flash.setText("YES");


        } else {

            tv_flash.setText("NO");


        }


    }


    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, android.Manifest.permission.READ_PHONE_STATE)) {
                permissionsNeeded.add("Phone State");
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getContext(),
                                "My App cannot run without Camera and Phone State " +
                                        "Permissions.\nRelaunch My App or allow permissions" +
                                        " in Applications Settings",
                                Toast.LENGTH_LONG).show();


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

                           tv_camera_back.setText("Permission not given");
                           tv_camera_front.setText("Permission not given");
                        }


                        if (perms.get(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){

                            tv_IMEI.setText("Permission not given");
                        }

                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}
