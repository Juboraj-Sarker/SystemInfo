package com.juborajsarker.systeminfo.fragment.system;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juborajsarker.systeminfo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static android.content.Context.MODE_PRIVATE;

public class ProcessorFragment extends Fragment {




    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    View view;

    public Handler mHandler = new Handler();

    String resultMax = "", resultMin= "";

    float maxResult, minResult;

    TextView tv_cpu_model, tv_clock_speed, tv_hardware, tv_cpu_board, tv_cores, tv_bootloader, tv_java_heap,
            tv_cpu_load, tv_cpu_governor, tv_kernel_version, tv_kernel_architecture;


    public ProcessorFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_processor, container, false);

        init();

        getCPUModel();
        getClockSpeed();
        getHardwareInfo();
        getCPUBoardInfo();
        getCores();
        getBootloader();
        getJavaHeap();
        getCPUGpvernor();
        getKernelVersion();
        getKernelArchitecture();


        sharedPreferences = getActivity().getSharedPreferences("processorPrefences", MODE_PRIVATE);
        editor = sharedPreferences.edit();





        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

      //  mHandler.postDelayed(mRunnable, 1000);

        tv_cpu_load.setText(getCPULoad());
    }

    @Override
    public void onPause() {
        super.onPause();

       // mHandler.removeCallbacks(mRunnable);
    }












    private void init() {


        tv_cpu_model = (TextView) view.findViewById(R.id.tv_cpu_model);
        tv_clock_speed = (TextView) view.findViewById(R.id.tv_clock_speed);
        tv_hardware = (TextView) view.findViewById(R.id.tv_hardware);
        tv_cpu_board = (TextView) view.findViewById(R.id.tv_cpu_board);
        tv_cores = (TextView) view.findViewById(R.id.tv_cores);
        tv_bootloader = (TextView) view.findViewById(R.id.tv_bootloader);
        tv_java_heap = (TextView) view.findViewById(R.id.tv_java_heap);
        tv_cpu_load = (TextView) view.findViewById(R.id.tv_cpu_load);
        tv_cpu_governor = (TextView) view.findViewById(R.id.tv_cpu_governor);
        tv_kernel_version = (TextView) view.findViewById(R.id.tv_kernel_version);
        tv_kernel_architecture = (TextView) view.findViewById(R.id.tv_kernel_architecture);




    }

    private void getCPUModel() {


        String arch = System.getProperty("os.arch");

        if (arch.contains("arm")){

            tv_cpu_model.setText("ARM");

        }else if (arch.contains("mip")){


            tv_cpu_model.setText("MIPS");


        }else if (arch.contains("x86")){


            tv_cpu_model.setText("X86");
        }



    }



    private void getClockSpeed() {


        countMin();
        countMax();
        tv_clock_speed.setText(resultMin + " - " + resultMax);





    }

    private void countMax() {


        try {

            File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            InputStream in = new FileInputStream(file);
            byte[] re = new byte[32768];
            int read = 0;
            while ( (read = in.read(re, 0, 32768)) != -1) {
                String string = new String(re, 0, read);
                Log.e(getClass().getSimpleName(), string);
                resultMax += string;


                if (resultMax.length() == 7){

                    maxResult = Float.parseFloat(resultMax);
                    maxResult = maxResult / 1000;
                    resultMax = String.valueOf(maxResult) + " MHz";

                }else if (resultMax.length() == 8){

                    maxResult = Float.parseFloat(resultMax);
                    maxResult = maxResult / 1000000;
                    resultMax = String.valueOf(maxResult) ;
                    resultMax = resultMax.substring(0,4);
                    resultMax = resultMax.concat( " GHz");

                }




            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private void countMin() {



        try {

            File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
            InputStream in = new FileInputStream(file);
            byte[] re = new byte[32768];
            int read = 0;
            while ( (read = in.read(re, 0, 32768)) != -1) {
                String string = new String(re, 0, read);
                Log.e(getClass().getSimpleName(), string);
                resultMin += string;

                if (resultMin.length() == 7){

                    minResult = Float.parseFloat(resultMin);
                    minResult = minResult / 1000;
                    resultMin = String.valueOf(minResult) + " MHz";

                }else if (resultMin.length() == 8){

                    minResult = Float.parseFloat(resultMin);
                    minResult = minResult / 10000;
                    resultMin = String.valueOf(minResult) + " GHz";

                }


            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    private void getHardwareInfo() {

        tv_hardware.setText(Build.HARDWARE);

    }


    private void getCPUBoardInfo() {

        tv_cpu_board.setText(Build.BOARD);

    }


    private void getCores() {


        int cores = Runtime.getRuntime().availableProcessors();
        tv_cores.setText(String.valueOf(cores));


    }

    private void getBootloader() {


        tv_bootloader.setText(Build.BOOTLOADER);


    }

    private void getJavaHeap() {


        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();

        int finalResult = (int) (maxMemory / 1048576);

        tv_java_heap.setText(String.valueOf(finalResult) + " MB");

    }

    private String getCPULoad() {

        int finalResult = 0;

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            float result =  (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

             finalResult = (int) (result * 100);

            tv_cpu_load.setText(String.valueOf(finalResult) + " %");
            view.invalidate();

        } catch (IOException ex) {
            ex.printStackTrace();
        }



        return String.valueOf(finalResult) + " %";

    }

    private void getCPUGpvernor() {


        StringBuffer sb = new StringBuffer();

        //String file = "/proc/cpuinfo";  // Gets most cpu info (but not the governor)
        String file = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";  // Gets governor

        if (new File(file).exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(file)));
                String aLine;
                while ((aLine = br.readLine()) != null)
                    sb.append(aLine );

                if (br != null)
                    br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result= sb.toString();
        tv_cpu_governor.setText(result);

    }

    private void getKernelVersion() {


        tv_kernel_version.setText(System.getProperty("os.name") + " " + System.getProperty("os.version"));


    }

    private void getKernelArchitecture() {

        String arch = System.getProperty("os.arch");
        tv_kernel_architecture.setText(arch);

    }




    public String getX86CPUName() {

        String aLine = "Intel";
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String strArray[] = new String[2];
                while ((aLine = br.readLine()) != null) {
                    if(aLine.contains("model name")){
                        br.close();
                        strArray = aLine.split(":", 2);
                        aLine = strArray[1];
                        tv_cpu_model.setText(aLine);
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLine;
    }




    public String getMIPSCPUName() {
        String aLine = "MIPS";
        if (new File("/proc/cpuinfo").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                String strArray[] = new String[2];
                while ((aLine = br.readLine()) != null) {
                    if(aLine.contains("cpu model")){
                        br.close();
                        strArray = aLine.split(":", 2);
                        aLine = strArray[1];
                        tv_cpu_model.setText(aLine);
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLine;
    }






    public static String getArmCPUName() {

        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }



//    public final Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//
//            tv_cpu_load.setText(getCPULoad());
//
//            editor.putString("processorFlag", "true");
//            editor.commit();
//
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//
//
//    };













}
