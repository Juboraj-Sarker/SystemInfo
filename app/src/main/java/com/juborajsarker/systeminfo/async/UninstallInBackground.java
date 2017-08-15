package com.juborajsarker.systeminfo.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.afollestad.materialdialogs.MaterialDialog;
import com.juborajsarker.systeminfo.AppInfo;
import com.juborajsarker.systeminfo.activities.MainActivity;
import com.juborajsarker.systeminfo.utils.UtilsApp;
import com.juborajsarker.systeminfo.utils.UtilsDialog;
import com.juborajsarker.systeminfo.utils.UtilsRoot;
import com.juborajsarker.systeminfo.R;

public class UninstallInBackground extends AsyncTask<Void, String, Boolean> {
    private Context context;
    private Activity activity;
    private MaterialDialog dialog;
    private AppInfo appInfo;

    public UninstallInBackground(Context context, MaterialDialog dialog, AppInfo appInfo) {
        this.context = context;
        this.activity = (Activity) context;
        this.dialog = dialog;
        this.appInfo = appInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean status = false;

        if (UtilsApp.checkPermissions(activity)) {
            status = UtilsRoot.uninstallWithRootPermission(appInfo.getSource());
        }

        return status;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        dialog.dismiss();
        if (status) {
            MaterialDialog.Builder materialDialog = UtilsDialog.showUninstalled(context, appInfo);
            materialDialog.callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    UtilsRoot.rebootSystem();
                    dialog.dismiss();
                }
            });
            materialDialog.callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onNegative(MaterialDialog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.finish();
                    context.startActivity(intent);
                }
            });
            materialDialog.show();
        } else {
            UtilsDialog.showTitleContent(context, context.getResources().getString(R.string.dialog_root_required), context.getResources().getString(R.string.dialog_root_required_description));
        }
    }
}