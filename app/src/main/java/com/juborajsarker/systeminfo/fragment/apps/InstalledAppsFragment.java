package com.juborajsarker.systeminfo.fragment.apps;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juborajsarker.systeminfo.AppInfo;
import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.SystemInfoManager;
import com.juborajsarker.systeminfo.adapters.AppAdapter;
import com.juborajsarker.systeminfo.utils.AppPreferences;
import com.juborajsarker.systeminfo.utils.UtilsApp;
import com.juborajsarker.systeminfo.utils.UtilsDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


public class InstalledAppsFragment extends Fragment implements SearchView.OnQueryTextListener {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_READ = 1;

    View view;

    // Load Settings
    private AppPreferences appPreferences;

    private List<AppInfo> appList;
    private List<AppInfo> appSystemList;
    private List<AppInfo> appHiddenList;

    private AppAdapter appAdapter;
    private AppAdapter appSystemAdapter;
    private AppAdapter appFavoriteAdapter;
    private AppAdapter appHiddenAdapter;

    // Configuration variables
    private Boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private Activity activity;
    private Context context;
    private RecyclerView recyclerView;
    private PullToRefreshView pullToRefreshView;
    private ProgressWheel progressWheel;
    private Drawer drawer;
    private MenuItem searchItem;
    private SearchView searchView;
    private static VerticalRecyclerViewFastScroller fastScroller;
    private static LinearLayout noResults;


    public InstalledAppsFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_installed_apps, container, false);

        this.appPreferences = SystemInfoManager.getAppPreferences();

       setInitialConfiguration();

        setHasOptionsMenu(true);




      //  checkAndAddPermissions(activity);


      setAppDir();

        recyclerView = (RecyclerView) view.findViewById(R.id.appList);
        pullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        fastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress);
        noResults = (LinearLayout) view.findViewById(R.id.noResults);

        pullToRefreshView.setEnabled(false);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressWheel.setBarColor(Color.BLUE);
        progressWheel.setVisibility(View.VISIBLE);

        new getInstalledApps().execute();


        return view;
    }







    class getInstalledApps extends AsyncTask<Void, String, Void> {
        private Integer totalApps;
        private Integer actualApps;

        public getInstalledApps() {
            actualApps = 0;

            appList = new ArrayList<>();
            appSystemList = new ArrayList<>();
            appHiddenList = new ArrayList<>();
        }


        protected Void doInBackground(Void... params) {
            final PackageManager packageManager = getActivity().getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

            totalApps = packages.size() ;
            // Get Sort Mode



            switch (appPreferences.getSortMode()) {
                default:
                    // Comparator by Name (default)
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return packageManager.getApplicationLabel(p1.applicationInfo).toString().toLowerCase().compareTo(packageManager.getApplicationLabel(p2.applicationInfo).toString().toLowerCase());
                        }
                    });
                    break;
                case "2":
                    // Comparator by Size
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            Long size1 = new File(p1.applicationInfo.sourceDir).length();
                            Long size2 = new File(p2.applicationInfo.sourceDir).length();
                            return size2.compareTo(size1);
                        }
                    });
                    break;
                case "3":
                    // Comparator by Installation Date (default)
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return Long.toString(p2.firstInstallTime).compareTo(Long.toString(p1.firstInstallTime));
                        }
                    });
                    break;
                case "4":
                    // Comparator by Last Update
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return Long.toString(p2.lastUpdateTime).compareTo(Long.toString(p1.lastUpdateTime));
                        }
                    });
                    break;
            }






            // Installed & System Apps
            for (PackageInfo packageInfo : packages) {
                if (!(packageManager.getApplicationLabel(packageInfo.applicationInfo).equals("") ||
                        packageInfo.packageName.equals(""))) {

                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        try {
                            // Non System Apps
                            AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                    packageInfo.packageName, packageInfo.versionName, packageInfo.applicationInfo.sourceDir,
                                    packageInfo.applicationInfo.dataDir, packageManager.getApplicationIcon(packageInfo.applicationInfo),
                                    false);

                            appList.add(tempApp);

                        } catch (OutOfMemoryError e) {

                            //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                            AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                    packageInfo.packageName, packageInfo.versionName, packageInfo.applicationInfo.sourceDir,
                                    packageInfo.applicationInfo.dataDir, getResources().getDrawable(R.drawable.ic_android),
                                    false);

                            appList.add(tempApp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            // System Apps
                            AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                    packageInfo.packageName, packageInfo.versionName,
                                    packageInfo.applicationInfo.sourceDir, packageInfo.applicationInfo.dataDir,
                                    packageManager.getApplicationIcon(packageInfo.applicationInfo),
                                    true);

                            appSystemList.add(tempApp);

                        } catch (OutOfMemoryError e) {

                            //TODO Workaround to avoid FC on some devices (OutOfMemoryError). Drawable should be cached before.
                            AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                    packageInfo.packageName, packageInfo.versionName, packageInfo.applicationInfo.sourceDir,
                                    packageInfo.applicationInfo.dataDir, getResources().getDrawable(R.drawable.ic_android),
                                    false);

                            appSystemList.add(tempApp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                actualApps++;
                publishProgress(Double.toString((actualApps * 100) / totalApps));
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
            progressWheel.setProgress(Float.parseFloat(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            appAdapter = new AppAdapter(appList, context);
            appSystemAdapter = new AppAdapter(appSystemList, context);



            fastScroller.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(appAdapter);
            pullToRefreshView.setEnabled(true);
            progressWheel.setVisibility(View.GONE);


            fastScroller.setRecyclerView(recyclerView);
            recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

            setPullToRefreshView(pullToRefreshView);
           // searchItem.setVisible(true);


        }

    }


    private void setPullToRefreshView(final PullToRefreshView pullToRefreshView) {
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                appAdapter.clear();
                recyclerView.setAdapter(null);
                new getInstalledApps().execute();

                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }





    private void setAppDir() {
        File appDir = UtilsApp.getAppFolder();
        if(!appDir.exists()) {
            appDir.mkdir();
        }
    }






    @Override
    public boolean onQueryTextChange(String search) {
        if (search.isEmpty()) {
            ((AppAdapter) recyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((AppAdapter) recyclerView.getAdapter()).getFilter().filter(search.toLowerCase());
        }

        return false;
    }


    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
            fastScroller.setVisibility(View.GONE);
        } else {
            noResults.setVisibility(View.GONE);
            fastScroller.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }








    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_READ: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    UtilsDialog.showTitleContent(context, getResources().getString(R.string.dialog_permissions), getResources().getString(R.string.dialog_permissions_description));
                }
            }
        }
    }






    private void setInitialConfiguration() {
        //toolbar = (Toolbar) view.findViewById(R.id.toolbar);
      //  ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Apps Info");
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

    }
}
