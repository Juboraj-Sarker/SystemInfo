package com.juborajsarker.systeminfo.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.afollestad.materialdialogs.MaterialDialog;
import com.juborajsarker.systeminfo.AppInfo;
import com.juborajsarker.systeminfo.R;
import com.juborajsarker.systeminfo.utils.UtilsApp;
import com.juborajsarker.systeminfo.utils.UtilsDialog;
public class ExtractFileInBackground extends AsyncTask<Void, String, Boolean> {
    private Context context;
    private Activity activity;
    private MaterialDialog dialog;
    private AppInfo appInfo;

    public ExtractFileInBackground(Context context, MaterialDialog dialog, AppInfo appInfo) {
        this.activity = (Activity) context;
        this.context = context;
        this.dialog = dialog;
        this.appInfo = appInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean status = false;



        return status;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        dialog.dismiss();
        if (status) {
            UtilsDialog.showSnackbar(activity, String.format(context.getResources().getString(R.string.dialog_saved_description), appInfo.getName(), UtilsApp.getAPKFilename(appInfo)), context.getResources().getString(R.string.button_undo), UtilsApp.getOutputFilename(appInfo), 1).show();
        } else {
            UtilsDialog.showTitleContent(context, context.getResources().getString(R.string.dialog_extract_fail), context.getResources().getString(R.string.dialog_extract_fail_description));
        }
    }
}