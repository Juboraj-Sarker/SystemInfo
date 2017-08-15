package com.juborajsarker.systeminfo.fragment.system;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juborajsarker.systeminfo.R;


public class SensorFragment extends Fragment {

    View view;


    Boolean accelerometer, compass, proximity, gyroscope, ambientLightSensor, barometer,
            relativeHumidity, heartRate, temperature, fingerprint;

    TextView accelerometerTV, compassTV, proximityTV, gyroscopeTV, ambientLightSensorTV, barometerTV,
            relativeHumidityTV, heartRateTV, temperatureTV, fingerprintTV;

    TextView SN_Accelerometer_TV, SN_Compass_TV, SN_Proximity_TV, SN_Gyroscope_TV, SN_Ambient_Light_Sensor_TV, SN_Barometer_TV,
            SN_Relative_Humidity_TV, SN_Heart_Rate_TV, SN_Temperature_TV, SN_Fingerprint_TV;

    public SensorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sensor, container, false);


        init();
        getSensorInfo();
        isFingerprintSensorAvialable();
        setText();


        return view;

    }







    private void init() {


        accelerometerTV = (TextView) view.findViewById(R.id.tv_sensor_accelerometer);
        compassTV = (TextView) view.findViewById(R.id.tv_sensor_compass);
        proximityTV = (TextView) view.findViewById(R.id.tv_sensor_proximity);
        gyroscopeTV = (TextView) view.findViewById(R.id.tv_sensor_gyro);
        ambientLightSensorTV = (TextView) view.findViewById(R.id.tv_sensor_light);
        barometerTV = (TextView) view.findViewById(R.id.tv_sensor_barometer);
        relativeHumidityTV = (TextView) view.findViewById(R.id.tv_sensor_relative_humidity);
        heartRateTV = (TextView) view.findViewById(R.id.tv_sensor_heart_rate);
        temperatureTV = (TextView) view.findViewById(R.id.tv_sensor_temperature);
        fingerprintTV = (TextView) view.findViewById(R.id.tv_sensor_fingerprint);

        SN_Accelerometer_TV = (TextView) view.findViewById(R.id.sn_accelerometer);
        SN_Compass_TV = (TextView) view.findViewById(R.id.sn_compass);
        SN_Proximity_TV = (TextView) view.findViewById(R.id.sn_proximity);
        SN_Gyroscope_TV = (TextView) view.findViewById(R.id.sn_gyro);
        SN_Ambient_Light_Sensor_TV = (TextView) view.findViewById(R.id.sn_light);
        SN_Barometer_TV = (TextView) view.findViewById(R.id.sn_barometer);
        SN_Relative_Humidity_TV = (TextView) view.findViewById(R.id.sn_relative_humidity);
        SN_Heart_Rate_TV = (TextView) view.findViewById(R.id.sn_heart_rate);
        SN_Temperature_TV = (TextView) view.findViewById(R.id.sn_temperature);
        SN_Fingerprint_TV = (TextView) view.findViewById(R.id.sn_fingerprint);


    }



    private void getSensorInfo() {



        PackageManager manager = getActivity().getPackageManager();

        accelerometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        compass = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        proximity = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
        gyroscope = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        ambientLightSensor = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
        barometer = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);

        relativeHumidity = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY);

        heartRate = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE);
        temperature = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
        fingerprint = manager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);





    }



    private boolean isFingerprintSensorAvialable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().getSystemService(FingerprintManager.class).isHardwareDetected();
        } else {
            return FingerprintManagerCompat.from(getContext()).isHardwareDetected();
        }
    }


    private void setText() {


        if (accelerometer){

            accelerometerTV.setText("YES");
            accelerometerTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Accelerometer_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            accelerometerTV.setText("NO");
            accelerometerTV.setTextColor(Color.RED);
            SN_Accelerometer_TV.setTextColor(Color.RED);
        }



        if (compass){

            compassTV.setText("YES");
            compassTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Compass_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            compassTV.setText("NO");
            compassTV.setTextColor(Color.RED);
            SN_Compass_TV.setTextColor(Color.RED);
        }



        if (proximity){

            proximityTV.setText("YES");
            proximityTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Proximity_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            proximityTV.setText("NO");
            proximityTV.setTextColor(Color.RED);
            SN_Proximity_TV.setTextColor(Color.RED);
        }



        if (gyroscope){

            gyroscopeTV.setText("YES");
            gyroscopeTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Gyroscope_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            gyroscopeTV.setText("NO");
            gyroscopeTV.setTextColor(Color.RED);
            SN_Gyroscope_TV.setTextColor(Color.RED);
        }



        if (ambientLightSensor){

            ambientLightSensorTV.setText("YES");
            ambientLightSensorTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Ambient_Light_Sensor_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            ambientLightSensorTV.setText("NO");
            ambientLightSensorTV.setTextColor(Color.RED);
            SN_Ambient_Light_Sensor_TV.setTextColor(Color.RED);
        }



        if (barometer){

            barometerTV.setText("YES");
            barometerTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Barometer_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            barometerTV.setText("NO");
            barometerTV.setTextColor(Color.RED);
            SN_Barometer_TV.setTextColor(Color.RED);
        }



        if (relativeHumidity){

            relativeHumidityTV.setText("YES");
            relativeHumidityTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Relative_Humidity_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            relativeHumidityTV.setText("NO");
            relativeHumidityTV.setTextColor(Color.RED);
            SN_Relative_Humidity_TV.setTextColor(Color.RED);
        }


        if (heartRate){

            heartRateTV.setText("YES");
            heartRateTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Heart_Rate_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            heartRateTV.setText("NO");
            heartRateTV.setTextColor(Color.RED);
            SN_Heart_Rate_TV.setTextColor(Color.RED);
        }


        if (temperature){

            temperatureTV.setText("YES");
            temperatureTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Temperature_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            temperatureTV.setText("NO");
            temperatureTV.setTextColor(Color.RED);
            SN_Temperature_TV.setTextColor(Color.RED);
        }


        if (isFingerprintSensorAvialable()){

            fingerprintTV.setText("YES");
            fingerprintTV.setTextColor(Color.parseColor("#0AB451"));
            SN_Fingerprint_TV.setTextColor(Color.parseColor("#0AB451"));

        }else {

            fingerprintTV.setText("NO");
            fingerprintTV.setTextColor(Color.RED);
            SN_Fingerprint_TV.setTextColor(Color.RED);
        }



    }



}
