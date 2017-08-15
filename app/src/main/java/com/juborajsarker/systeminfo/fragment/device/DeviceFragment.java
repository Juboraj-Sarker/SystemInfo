package com.juborajsarker.systeminfo.fragment.device;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.juborajsarker.systeminfo.R;

import static android.content.Context.MODE_PRIVATE;

public class DeviceFragment extends Fragment {



    View view;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String flag;

    TextView tv_device_model, tv_manufacturer, tv_brand, tv_serial_id, tv_IMEL, tv_screen_size,
            tv_screen_resolution, tv_screen_density, tv_screen_refresh_rate, tv_camera_front, tv_camera_back, tv_flash;

    String man, model , brand,  deviceID;

    String screenSizeInInch;


    public DeviceFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_device, container, false);

        init();

        sharedPreferences = getActivity().getSharedPreferences("deviceSharedPrefence", MODE_PRIVATE);
        editor = sharedPreferences.edit();




        flag = sharedPreferences.getString("status", "");

        if (flag.equals("true")){

            tv_manufacturer.setText(sharedPreferences.getString("dMan",""));
            tv_IMEL.setText(sharedPreferences.getString("dIMEI", ""));
            tv_device_model.setText(sharedPreferences.getString("dModel", ""));
            tv_brand.setText(sharedPreferences.getString("dBrand", ""));
            tv_serial_id.setText(sharedPreferences.getString("dDeviceID", ""));

            tv_screen_size.setText(sharedPreferences.getString("dScreenSize", ""));
            tv_screen_resolution.setText(sharedPreferences.getString("resolution", ""));
            tv_screen_density.setText(sharedPreferences.getString("density", ""));
            tv_screen_refresh_rate.setText(sharedPreferences.getString("screenRefresh", ""));

            tv_camera_front.setText(sharedPreferences.getString("cameraFront", ""));
            tv_camera_back.setText(sharedPreferences.getString("cameraBack", ""));
            tv_flash.setText(sharedPreferences.getString("flash", ""));




        }else {

            getDeviceInfo();
            getScreenInfo();
            getCameraInfo();

        }










        return view;
    }


    private void init() {


        tv_device_model = (TextView) view.findViewById(R.id.tv_device_model);
        tv_manufacturer = (TextView) view.findViewById(R.id.tv_manufacturer);
        tv_brand = (TextView) view.findViewById(R.id.tv_brand);
        tv_serial_id = (TextView) view.findViewById(R.id.tv_serial_id);
        tv_IMEL = (TextView) view.findViewById(R.id.tv_imei);
        tv_screen_size = (TextView) view.findViewById(R.id.tv_screen_size);
        tv_screen_resolution = (TextView) view.findViewById(R.id.tv_screen_resolution);
        tv_screen_density = (TextView) view.findViewById(R.id.tv_screen_density);
        tv_screen_refresh_rate = (TextView) view.findViewById(R.id.tv_screen_refresh_rate);
        tv_camera_front = (TextView) view.findViewById(R.id.tv_camera_front);
        tv_camera_back = (TextView) view.findViewById(R.id.tv_camera_back);
        tv_flash = (TextView) view.findViewById(R.id.tv_flash);


    }



    private void getDeviceInfo() {

        TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);


        editor.putString("status", "true");

         man = Build.MANUFACTURER;
         model = Build.MODEL;
         brand = Build.BRAND;
         deviceID = Build.SERIAL;


        editor.putString("dMan", man);
        editor.putString("dIMEI", telephonyManager.getDeviceId());
        editor.putString("dModel", model);
        editor.putString("dBrand", brand);
        editor.putString("dDeviceID", deviceID);








        tv_manufacturer.setText(man);
        tv_IMEL.setText(telephonyManager.getDeviceId());
        tv_device_model.setText(model);
        tv_brand.setText(brand);
        tv_serial_id.setText(deviceID);
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
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);

        String temp = String.valueOf(screenInches);

        screenSizeInInch =   temp.substring(0, 3);
        tv_screen_size.setText(screenSizeInInch + " inch");

        editor.putString("dScreenSize", screenSizeInInch + " inch");
    }



    private void getScreenResolution() {

        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getActivity().getWindowManager();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }


        tv_screen_resolution.setText(String.valueOf(Measuredwidth) + " x " + String.valueOf(Measuredheight) + " pixels");

        editor.putString("resolution", String.valueOf(Measuredwidth) + " x " + String.valueOf(Measuredheight) + " pixels");


    }




    private void getScreenDensity() {

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        String dpi = "";

        float den = getContext().getResources().getDisplayMetrics().density;
        if (den >= 4.0) {
            dpi= "xxxhdpi";
        }
       else if (den >= 3.0) {
            dpi= "xxhdpi";
        }
        else if (den >= 2.0) {
            dpi= "xhdpi";
        }
        else if (den >= 1.5) {
            dpi= "hdpi";
        }
        else  if (den >= 1.0) {
            dpi= "mdpi";

        }else if (den < 1){

            dpi= "ldpi";
        }


        tv_screen_density.setText(String.valueOf(densityDpi) + " dpi" + " (" + dpi + ")");

        editor.putString("density", String.valueOf(densityDpi) + " dpi" + " (" + dpi + ")");


    }



    private void getScreenRefreshRate() {



        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float refreshRating = display.getRefreshRate();

        tv_screen_refresh_rate.setText(String.valueOf(refreshRating) + " Hz");
        editor.putString("screenRefresh", String.valueOf(refreshRating) + " Hz");


    }




    private void getCameraInfo() {



        getBackCameraInfo();
        getFrontCameraInfo();
        getFlashInfo();


    }


    private void getBackCameraInfo() {

        Camera camera = Camera.open(0);

        android.hardware.Camera.Parameters parameters = camera.getParameters();
        android.hardware.Camera.Size size = parameters.getPictureSize();


        double height = size.height;
        double width = size.width;
        float mgBack = (float) ((height * width) / 1024000);
        tv_camera_back.setText(String.valueOf((String.format("%.0f", mgBack))) + " MP");
        editor.putString("cameraBack", String.valueOf((String.format("%.0f", mgBack))) + " MP");
        camera.release();
    }



    private void getFrontCameraInfo() {

        Camera camera = Camera.open(1);

        android.hardware.Camera.Parameters parameters = camera.getParameters();
        android.hardware.Camera.Size size = parameters.getPictureSize();


        double height = size.height;
        double width = size.width;
        float mgFront = (float) ((height * width) / 1024000);
        tv_camera_front.setText(String.valueOf((String.format("%.0f", mgFront))) + " MP");
        editor.putString("cameraFront",String.valueOf((String.format("%.0f", mgFront))) + " MP");
        camera.release();


    }






    private void getFlashInfo() {



     boolean flashAvailable =   getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (flashAvailable){

            tv_flash.setText("YES");
            editor.putString("flash", "YES");
            editor.commit();

        }else {

            tv_flash.setText("NO");
            editor.putString("flash", "NO");
            editor.commit();

        }



    }


}
