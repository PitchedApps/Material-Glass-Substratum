package com.pitchedapps.material.glass.substratum.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Allan Wang on 2016-08-03.
 */
public class SubstratumLauncher extends AppCompatActivity {



    /**
     * Other variables/methods; do not modify
     */

    private static final String SUBSTRATUM_PACKAGE_NAME = "projekt.substratum";

    private boolean isPackageInstalled(String package_name) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(package_name, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPackageEnabled(String package_name) {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(package_name, 0);
            return ai.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void beginSubstratumLaunch() {
        // If Substratum is found, then launch it with specific parameters
        if (isPackageInstalled(SUBSTRATUM_PACKAGE_NAME)) {
            if (!isPackageEnabled(SUBSTRATUM_PACKAGE_NAME)) {
                Toast toast = Toast.makeText(this, getString(R.string.toast_substratum_frozen),
                        Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            // Substratum is found, launch it directly
            launchSubstratum();
        } else {
            getSubstratumFromPlayStore();
        }
    }

    private void getSubstratumFromPlayStore() {
        String playURL =
                "https://play.google.com/store/apps/details?" +
                        "id=projekt.substratum&hl=en";
        Intent i = new Intent(Intent.ACTION_VIEW);
        Toast toast = Toast.makeText(this, getString(R.string.toast_substratum),
                Toast.LENGTH_SHORT);
        toast.show();
        i.setData(Uri.parse(playURL));
        startActivity(i);
        finish();
    }

    private void launchSubstratum() {
        Intent currentIntent = getIntent();
        String theme_mode = currentIntent.getStringExtra("theme_mode");
        if (theme_mode == null) theme_mode = "";
        final boolean theme_legacy = currentIntent.getBooleanExtra("theme_legacy", false);
        final boolean refresh_mode = currentIntent.getBooleanExtra("refresh_mode", false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(ComponentName.unflattenFromString(
                "projekt.substratum/projekt.substratum.InformationActivity"));
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("theme_name", getString(R.string.ThemeName));
        intent.putExtra("theme_pid", getPackageName());
        intent.putExtra("theme_legacy", theme_legacy);
        intent.putExtra("theme_mode", theme_mode);
        intent.putExtra("refresh_mode", refresh_mode);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            beginSubstratumLaunch();
    }
}