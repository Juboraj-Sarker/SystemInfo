package com.juborajsarker.systeminfo.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.inflator_fragment.AppsInflatorFragment;
import com.juborajsarker.systeminfo.inflator_fragment.DeviceInflatorFragment;
import com.juborajsarker.systeminfo.inflator_fragment.SystemInflatorFragment;

import java.io.File;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private MenuItem searchItem;
    private SearchView searchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contendor, new DeviceInflatorFragment()).commit();
        setTitle("Device Info");



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

       FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_device) {

            fragmentManager.beginTransaction().replace(R.id.contendor, new DeviceInflatorFragment()).commit();
            setTitle("Device Info");

        } else if (id == R.id.nav_system) {

            fragmentManager.beginTransaction().replace(R.id.contendor, new SystemInflatorFragment()).commit();
            setTitle("System Info");

        } else if (id == R.id.nav_apps) {

            fragmentManager.beginTransaction().replace(R.id.contendor, new AppsInflatorFragment()).commit();
            setTitle("Apps Info");

        }else if (id == R.id.nav_rateApp) {

            rateApp();



        }else if (id == R.id.nav_share) {

            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.createChooser(intent,"System Info");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "share System Info using"));


        }else if (id == R.id.nav_moreApps) {

            Intent intent;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6155570899607409709&hl"));
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }





}
