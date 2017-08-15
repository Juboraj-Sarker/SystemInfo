package com.juborajsarker.systeminfo;

import android.app.Application;

import com.juborajsarker.systeminfo.utils.AppPreferences;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;

public class SystemInfoManager extends Application {
    public static AppPreferences sAppPreferences;

    @Override
    public void onCreate() {
        super.onCreate();


        sAppPreferences = new AppPreferences(this);




        Iconics.registerFont(new GoogleMaterial());
    }

    public static AppPreferences getAppPreferences() {
        return sAppPreferences;
    }


}
