package com.juborajsarker.systeminfo.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juborajsarker.systeminfo.AppInfo;
import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.SystemInfoManager;
import com.juborajsarker.systeminfo.async.ExtractFileInBackground;
import com.juborajsarker.systeminfo.utils.AppPreferences;
import com.juborajsarker.systeminfo.utils.UtilsApp;
import com.juborajsarker.systeminfo.utils.UtilsDialog;
import com.juborajsarker.systeminfo.utils.UtilsUI;

public class AppActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    // Load Settings
    private AppPreferences appPreferences;

    // General variables
    private AppInfo appInfo;

    // Configuration variables
    private int UNINSTALL_REQUEST_CODE = 1;
    private Context context;
    private Activity activity;
    private MenuItem item_favorite;

    // UI variables
    private FloatingActionsMenu fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        this.context = this;
        this.activity = (Activity) context;
        this.appPreferences = SystemInfoManager.getAppPreferences();

        getInitialConfiguration();
        setInitialConfiguration();
        setScreenElements();


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
        mInterstitialAd.loadAd(adRequest);



        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });



    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }



    private void setInitialConfiguration() {

  //      setSupportActionBar(toolbar);
        if (getSupportActionBar() != null ) {

            getSupportActionBar().setTitle(getIntent().getStringExtra("app_name"));
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);


        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));

            if (!appPreferences.getNavigationBlackPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }
    }

    private void setScreenElements() {
        TextView header = (TextView) findViewById(R.id.header);
        ImageView icon = (ImageView) findViewById(R.id.app_icon);
        ImageView icon_googleplay = (ImageView) findViewById(R.id.app_googleplay);
        TextView name = (TextView) findViewById(R.id.app_name);
        TextView version = (TextView) findViewById(R.id.app_version);
        TextView apk = (TextView) findViewById(R.id.app_apk);
        CardView googleplay = (CardView) findViewById(R.id.id_card);
        CardView start = (CardView) findViewById(R.id.start_card);
        CardView extract = (CardView) findViewById(R.id.extract_card);
        CardView uninstall = (CardView) findViewById(R.id.uninstall_card);
        CardView cache = (CardView) findViewById(R.id.cache_card);
        CardView clearData = (CardView) findViewById(R.id.clear_data_card);
        fab = (FloatingActionsMenu) findViewById(R.id.fab);
        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_a);


        icon.setImageDrawable(appInfo.getIcon());
        name.setText(appInfo.getName());
        apk.setText(appInfo.getAPK());
        version.setText(appInfo.getVersion());

        // Configure Colors
        header.setBackgroundColor(appPreferences.getPrimaryColorPref());
        fab_share.setColorNormal(appPreferences.getFABColorPref());
        fab_share.setColorPressed(UtilsUI.darker(appPreferences.getFABColorPref(), 0.8));


        // CardView
        if (appInfo.isSystem()) {
            icon_googleplay.setVisibility(View.GONE);
            start.setVisibility(View.GONE);
        } else {
            googleplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilsApp.goToGooglePlay(context, appInfo.getAPK());
                }
            });

            googleplay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData clipData;

                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipData = ClipData.newPlainText("text", appInfo.getAPK());
                    clipboardManager.setPrimaryClip(clipData);
                    UtilsDialog.showSnackbar(activity, context.getResources().getString(R.string.copied_clipboard), null, null, 2).show();

                    return false;
                }
            });

            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(appInfo.getAPK());
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        UtilsDialog.showSnackbar(activity, String.format(getResources().getString(R.string.dialog_cannot_open), appInfo.getName()), null, null, 2).show();
                    }
                }
            });

            uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                    intent.setData(Uri.parse("package:" + appInfo.getAPK()));
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                    startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
                }
            });
        }
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog dialog = UtilsDialog.showTitleContentWithProgress(context
                        , String.format(getResources().getString(R.string.dialog_saving), appInfo.getName())
                        , getResources().getString(R.string.dialog_saving_description));
                new ExtractFileInBackground(context, dialog, appInfo).execute();
            }
        });

         if (appInfo.isSystem()) {
            uninstall.setVisibility(View.GONE);
            uninstall.setForeground(null);
        }

        // FAB (Share)
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsApp.copyFile(appInfo);
                Intent shareIntent = UtilsApp.getShareIntent(UtilsApp.getOutputFilename(appInfo));
                startActivity(Intent.createChooser(shareIntent, String.format(getResources().getString(R.string.send_to), appInfo.getName())));
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.i("App", "OK");
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("App", "CANCEL");
            }
        }
    }

    private void getInitialConfiguration() {
        String appName = getIntent().getStringExtra("app_name");
        String appApk = getIntent().getStringExtra("app_apk");
        String appVersion = getIntent().getStringExtra("app_version");
        String appSource = getIntent().getStringExtra("app_source");
        String appData = getIntent().getStringExtra("app_data");
		
        Bitmap bitmap = getIntent().getParcelableExtra("app_icon");
        Drawable appIcon = new BitmapDrawable(getResources(), bitmap);
		
        Boolean appIsSystem = getIntent().getExtras().getBoolean("app_isSystem");

        appInfo = new AppInfo(appName, appApk, appVersion, appSource, appData, appIcon, appIsSystem);

    }

    @Override
    public void onBackPressed() {
        if (fab.isExpanded()) {
            fab.collapse();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }



    public void aboutMe(MenuItem item) {

        startActivity(new Intent(AppActivity.this, AboutActivity.class));
    }



}