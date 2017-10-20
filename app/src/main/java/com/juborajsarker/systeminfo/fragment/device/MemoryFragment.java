package com.juborajsarker.systeminfo.fragment.device;


import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.juborajsarker.systeminfo.R;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

public class MemoryFragment extends Fragment {


    private static final String LOG_TAG = "";
    View view;
    TextView total_ram, avialable_ram, total_rom, avialable_rom, external_present, total_external, avialable_external;

    public static String sss = "";


    public Handler mHandler = new Handler();


    public MemoryFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_memory, container, false);

        init();

        getTotalRAM();
        getAvailableRAM();

        getAvailableInternalMemorySize();
        getTotalInternalMemorySize();


        externalMemoryAvailable();
        getAvailableExternalMemorySize();
        getTotalExternalMemorySize();


        total_rom.setText(getTotalInternalMemorySize());
        avialable_rom.setText(getAvailableInternalMemorySize());


        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.banner_home_footer_1));
        AdView mAdView = (AdView) view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mAdView.loadAd(adRequest);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        //   mHandler.postDelayed(mRunnable, 1000);

        avialable_ram.setText(getAvailableRAM());
    }

    @Override
    public void onPause() {
        super.onPause();
        // mHandler.removeCallbacks(mRunnable);
    }


    private void init() {


        total_ram = (TextView) view.findViewById(R.id.tv_total_ram);
        avialable_ram = (TextView) view.findViewById(R.id.tv_avialable_ram);
        total_rom = (TextView) view.findViewById(R.id.tv_total_rom);
        avialable_rom = (TextView) view.findViewById(R.id.tv_avialable_rom);
        external_present = (TextView) view.findViewById(R.id.tv_external_memory);
        total_external = (TextView) view.findViewById(R.id.tv_external_memory_size);
        avialable_external = (TextView) view.findViewById(R.id.tv_free_external_memory_space);


    }


    public void getTotalRAM() {

        double mb = 0, gb = 0, tb;

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();


            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);

            }
            reader.close();

            totRam = Double.parseDouble(value);


            mb = totRam / 1024.0;
            gb = totRam / 1048576.0;
            tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

        }

        total_ram.setText(String.valueOf(lastValue));
    }


    private String getAvailableRAM() {


        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableRam = mi.availMem / 0x100000L;


        double percentAvail = (mi.availMem / (double) mi.totalMem) * 100;

        String availableInPercent = String.format("%.2f", percentAvail);

        if (availableRam < 1024) {

            //avialable_ram.setText(String.valueOf(availableRam) + " MB (" + availableInPercent + "%)");

            return String.valueOf(availableRam) + " MB (" + availableInPercent + "%)";

        } else {


            float avialable = (float) (availableRam / 1024);
            //avialable_ram.setText(String.valueOf(avialable) + " GB (" + availableInPercent + "%)");

            return String.valueOf(avialable) + " GB (" + availableInPercent + "%)";


        }


    }


//    public final Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//
//
//
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//
//
//    };


    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }


    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }


    public static boolean externalMemoryAvailable() {


        File file = new File("df /mnt/sdcard");
        try {
            File list[] = file.listFiles();
            return true;
        } catch (NullPointerException o) {
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getAvailableExternalMemorySize() {


        File storage = new File("/storage");
        String external_storage_path = "";
        String size = "";

        if (storage.exists()) {
            File[] files = storage.listFiles();

            for (File file : files) {
                if (file.exists()) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {

                            external_storage_path = file.getAbsolutePath();
                            break;
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            }
        }

        if (!external_storage_path.isEmpty()) {
            File external_storage = new File(external_storage_path);
            if (external_storage.exists()) {
                size = availableSize(external_storage);

                if (size.equals("0.00")){
                    external_present.setText("NO");
                    avialable_external.setText("No SD Card");

                }else {

                    external_present.setText("YES");
                    avialable_external.setText(size);
                }
            }
        }
        return size;

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  String getTotalExternalMemorySize() {

        File storage = new File("/storage");
        String external_storage_path = "";
        String size = "";

        if (storage.exists()) {
            File[] files = storage.listFiles();

            for (File file : files) {
                if (file.exists()) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {

                            external_storage_path = file.getAbsolutePath();
                            break;
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.toString());
                    }
                }
            }
        }

        if (!external_storage_path.isEmpty()) {
            File external_storage = new File(external_storage_path);
            if (external_storage.exists()) {
                size = totalSize(external_storage);

                if (size.equals("0.00")){

                    external_present.setText("NO");
                    total_external.setText("No SD Card");

                }else {

                    external_present.setText("YES");
                    total_external.setText(size);
                }
            }
        }
        return size;

    }


    public static String totalSize(File file) {
        StatFs stat = new StatFs(file.getPath());
        long blockSize, totalBlocks;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        } else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();

        }

        return formatSize(totalBlocks * blockSize);
    }







    public static String availableSize(File file) {
        StatFs stat = new StatFs(file.getPath());
        long blockSize, availableBlock;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlock = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlock = stat.getAvailableBlocks();

        }

        return formatSize( availableBlock * blockSize   );
    }












    public static String formatSize(float size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = " GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(String.format("%.2f", size));


        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


}




