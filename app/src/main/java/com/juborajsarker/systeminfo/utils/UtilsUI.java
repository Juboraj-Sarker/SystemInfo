package com.juborajsarker.systeminfo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.SystemInfoManager;
import com.juborajsarker.systeminfo.adapters.AppAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import java.util.Calendar;

public class UtilsUI {

    public static int darker (int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }

    public static Drawer setNavigationDrawer (Activity activity, final Context context, Toolbar toolbar,
                                              final AppAdapter appAdapter, final AppAdapter appSystemAdapter,
                                              final AppAdapter appFavoriteAdapter, final AppAdapter appHiddenAdapter,
                                              final RecyclerView recyclerView) {
        final String loadingLabel = "...";
        int header;
        AppPreferences appPreferences = SystemInfoManager.getAppPreferences();
        String apps, systemApps, favoriteApps, hiddenApps;

        if (getDayOrNight() == 1) {
            header = R.drawable.header_day;
        } else {
            header = R.drawable.header_night;
        }

        if (appAdapter != null) {
            apps = Integer.toString(appAdapter.getItemCount());
        } else {
            apps = loadingLabel;
        }
        if (appSystemAdapter != null) {
            systemApps = Integer.toString(appSystemAdapter.getItemCount());
        } else {
            systemApps = loadingLabel;
        }
        if (appFavoriteAdapter != null) {
            favoriteApps = Integer.toString(appFavoriteAdapter.getItemCount());
        } else {
            favoriteApps = loadingLabel;
        }
        if (appHiddenAdapter != null) {
            hiddenApps = Integer.toString(appHiddenAdapter.getItemCount());
        } else {
            hiddenApps = loadingLabel;
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();

        Integer badgeColor = ContextCompat.getColor(context, R.color.divider);
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor, badgeColor).withTextColor(Color.GRAY);

        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //      drawerBuilder.withToolbar(toolbar);


        }
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));


            drawerBuilder.addDrawerItems(
                    new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_apps)).withIcon(GoogleMaterial.Icon.gmd_phone_android).
                            withBadge(apps).withBadgeStyle(badgeStyle).withIdentifier(1),
                    new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_system_apps)).withIcon(GoogleMaterial.Icon.gmd_android).
                            withBadge(systemApps).withBadgeStyle(badgeStyle).withIdentifier(2),
                    new DividerDrawerItem(),
                    new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_favorites)).withIcon(GoogleMaterial.Icon.gmd_star).withBadge(favoriteApps).withBadgeStyle(badgeStyle).withIdentifier(3),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withSelectable(false).withIdentifier(6),
                    new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_about)).withIcon(GoogleMaterial.Icon.gmd_info).withSelectable(false).withIdentifier(7));




        return drawerBuilder.build();
    }

    public static int getDayOrNight() {
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (actualHour >= 8 && actualHour < 19) {
            return 1;
        } else {
            return 0;
        }
    }

}