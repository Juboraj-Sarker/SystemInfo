package com.juborajsarker.systeminfo.fragment.device;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.WIFI_SERVICE;
import static com.google.android.gms.internal.zzs.TAG;

public class NetworkFragment extends Fragment {


    Connectivity connectivity = new Connectivity();




    long txBytes = 0;
    long rxBytes = 0;

    String ssid = "";

    View view;


    public Handler mHandler = new Handler();

    TextView connectionType, dataType, sSID, networkTypeInfo, ipAddress, macAddress, bSSID,
            linkSpeed, downloadSpeed, uploadSpeed, networkTypeContainer;

    LinearLayout ssidLayout, bssidLayout, macAddressLayout, linkSpeedLayout, networkTypeLayout, ipAddressLayout;

    private long mStartRX = 0;
    private long mStartTX = 0;


    public NetworkFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network, container, false);

        connectionType = (TextView) view.findViewById(R.id.tv_connection_type);
        dataType = (TextView) view.findViewById(R.id.tv_data_type);
        sSID = (TextView) view.findViewById(R.id.tv_ssid);
        networkTypeInfo = (TextView) view.findViewById(R.id.tv_network_type);
        ipAddress = (TextView) view.findViewById(R.id.tv_ip_address);
        macAddress = (TextView) view.findViewById(R.id.tv_mac_address);
        bSSID = (TextView) view.findViewById(R.id.tv_bssid);
        linkSpeed = (TextView) view.findViewById(R.id.tv_link_speed);
        downloadSpeed = (TextView) view.findViewById(R.id.tv_download_speed);
        uploadSpeed = (TextView) view.findViewById(R.id.tv_upload_speed);

        networkTypeContainer = (TextView) view.findViewById(R.id.network_type_container);

        ssidLayout = (LinearLayout) view.findViewById(R.id.ssid_layout);
        bssidLayout = (LinearLayout) view.findViewById(R.id.bssid_layout);
        macAddressLayout = (LinearLayout) view.findViewById(R.id.mac_address_layout);
        linkSpeedLayout = (LinearLayout) view.findViewById(R.id.link_speed_layout);
        networkTypeLayout = (LinearLayout) view.findViewById(R.id.network_type_layout);
        ipAddressLayout = (LinearLayout) view.findViewById(R.id.ip_address_layout);


        StrictMode.enableDefaults();


        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        mHandler.postDelayed(mRunnable, 1000);
    }


    @Override
    public void onPause() {

        mHandler.removeCallbacks(mRunnable);
        super.onPause();

    }

    public static String getPublicIP() {
        String myip = "";


        try {
            Document doc = Jsoup.connect("http://www.checkip.org").get();
            myip = doc.getElementById("yourip").select("h1").first().select("span").text();


        } catch (IOException e) {
            e.printStackTrace();
        }


        return myip;
    }


    public String getCurrentIP(){

        String ip = "";

        URL whatismyip = null;
        try {
            whatismyip = new URL("http://icanhazip.com/");


            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        whatismyip.openStream()));


                ip = in.readLine(); //you get the IP as a String
                Log.i(TAG, "EXT IP: " + ip);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ip;
    }












    public static class Connectivity {


        public static NetworkInfo getNetworkInfo(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        }


        public boolean isConnected(Context context) {
            NetworkInfo info = Connectivity.getNetworkInfo(context);
            return (info != null && info.isConnected());
        }


        public boolean isConnectedWifi(Context context) {
            NetworkInfo info = Connectivity.getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
        }


        public boolean isConnectedMobile(Context context) {
            NetworkInfo info = Connectivity.getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
        }

    }


    public final Runnable mRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {

            TextView RX = (TextView) view.findViewById(R.id.tv_download_speed);
            TextView TX = (TextView) view.findViewById(R.id.tv_upload_speed);


            rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

            RX.setText(String.valueOf(rxBytes) + " bytes");

            if (rxBytes >= 1024) {

                long rxKb = rxBytes / 1024;
                RX.setText(String.valueOf(rxKb) + " KB/s");
                mStartRX = 0;
                mStartRX = TrafficStats.getTotalRxBytes();


            } else if (rxBytes >= 1048576) {

                long rxMB = rxBytes / 1048576;
                RX.setText(Long.toString(rxMB) + " MB/s");
                mStartRX = 0;
                mStartRX = TrafficStats.getTotalRxBytes();


            } else if (rxBytes >= 1073741824) {

                long rxGB = rxBytes / 1073741824;
                RX.setText(Long.toString(rxGB) + " GB/s");
                mStartRX = 0;
                mStartRX = TrafficStats.getTotalRxBytes();

            } else if (rxBytes < 1024) {

                RX.setText("0 KB/s");
                mStartRX = 0;
                mStartRX = TrafficStats.getTotalRxBytes();

            }


            txBytes = TrafficStats.getTotalTxBytes() - mStartTX;

            TX.setText(Long.toString(txBytes) + " bytes");

            if (txBytes >= 1024) {

                long txKb = txBytes / 1024;
                TX.setText(Long.toString(txKb) + " KB/s");
                mStartTX = 0;
                mStartTX = TrafficStats.getTotalTxBytes();


                if (txKb >= 1024) {

                    long txMB = txKb / 1024;
                    TX.setText(Long.toString(txMB) + " MB/s");
                    mStartTX = 0;
                    mStartTX = TrafficStats.getTotalTxBytes();


                    if (txMB >= 1024) {

                        long txGB = txMB / 1024;
                        TX.setText(Long.toString(txGB));
                        mStartTX = 0;
                        mStartTX = TrafficStats.getTotalTxBytes();

                    }
                }
            }


            if (connectivity.isConnected(getContext())) {

                if (connectivity.isConnectedWifi(getContext())) {

                    WifiManager manager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
                    WifiInfo wifiInfo = manager.getConnectionInfo();

                    dataType.setText("WiFi");
                    connectionType.setText("CONNECTED");
                    connectionType.setTextColor(Color.parseColor("#0AB451"));

                    bssidLayout.setVisibility(LinearLayout.VISIBLE);
                    macAddressLayout.setVisibility(LinearLayout.VISIBLE);
                    linkSpeedLayout.setVisibility(LinearLayout.VISIBLE);

                    String ip_address = getCurrentIP();
                    ipAddress.setText(ip_address);


                    if (wifiInfo != null) {

                        NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());

                        if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                            ssidLayout.setVisibility(LinearLayout.VISIBLE);
                            ssid = wifiInfo.getSSID();
                            sSID.setText(ssid);
                            macAddress.setText(wifiInfo.getMacAddress());
                            bSSID.setText(wifiInfo.getBSSID());
                            linkSpeed.setText(String.valueOf(wifiInfo.getLinkSpeed()) + " Mbps");
                            networkTypeContainer.setText("Frequency");
                            networkTypeInfo.setText(String.valueOf(wifiInfo.getFrequency()) + " MHz");





                        }
                    }


                } else if (connectivity.isConnectedMobile(getContext())) {

                    dataType.setText("Mobile Data");
                    connectionType.setText("CONNECTED");
                    connectionType.setTextColor(Color.parseColor("#0AB451"));
                    sSID.setText("Not connected on WIFI");
                    networkTypeContainer.setText("Network Type");
                    ipAddress.setText(Utils.getIPAddress(true));

                    TelephonyManager teleMan =
                            (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    int networkType = teleMan.getNetworkType();

                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            networkTypeInfo.setText("1xRTT");
                            break;
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                            networkTypeInfo.setText("CDMA");
                            break;
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            networkTypeInfo.setText("EDGE");
                            break;
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                            networkTypeInfo.setText("eHRPD");
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            networkTypeInfo.setText("EVDO rev. 0");
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            networkTypeInfo.setText("EVDO rev. A");
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            networkTypeInfo.setText("EVDO rev. B");
                            break;
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                            networkTypeInfo.setText("GPRS");
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                            networkTypeInfo.setText("HSDPA");
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                            networkTypeInfo.setText("HSPA");
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            networkTypeInfo.setText("HSPA+");
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                            networkTypeInfo.setText("HSUPA");
                            break;
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            networkTypeInfo.setText("iDen");
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            networkTypeInfo.setText("LTE");
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                            networkTypeInfo.setText("UMTS");
                            break;
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            networkTypeInfo.setText("Unknown");
                            break;
                    }


                    ssidLayout.setVisibility(LinearLayout.GONE);
                    bssidLayout.setVisibility(LinearLayout.GONE);
                    macAddressLayout.setVisibility(LinearLayout.GONE);
                    linkSpeedLayout.setVisibility(LinearLayout.GONE);
                    networkTypeLayout.setVisibility(LinearLayout.VISIBLE);




                }


            } else  {

                connectionType.setText("DISCONNECTED");
                connectionType.setTextColor(Color.RED);
                ipAddress.setText("Not Connected");
                dataType.setText("Not Connected");
                sSID.setText("Not connected");
                bSSID.setText("Not Connected");
                macAddress.setText("Not Connected");
                networkTypeContainer.setText("Network Type");
                networkTypeInfo.setText("Not Connected");
                linkSpeed.setText("Not Connected");

            }


            mHandler.postDelayed(mRunnable, 1000);

        }
    };




    }


