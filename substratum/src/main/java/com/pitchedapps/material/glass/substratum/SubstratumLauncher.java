package com.pitchedapps.material.glass.substratum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import projekt.substrate.SubstratumLoader;

/**
 * Created by Allan Wang on 2016-08-03.
 */
public class SubstratumLauncher extends Activity {

    private static final String SUBSTRATUM_PACKAGE_NAME = "projekt.substratum";
    private static final int MINIMUM_SUBSTRATUM_VERSION = 510; // 510 is the final MM build
    private static final boolean THEME_READY_GOOGLE_APPS = false;

    private boolean isPackageInstalled(String package_name) {
        try {
            PackageManager pm = getPackageManager();
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
        } catch (Exception e) {
            return false;
        }
    }

    private void beginSubstratumLaunch() {
        // If Substratum is found, then launch it with specific parameters
        if (isPackageInstalled(SUBSTRATUM_PACKAGE_NAME)) {
            if (!isPackageEnabled(SUBSTRATUM_PACKAGE_NAME)) {
                Toast.makeText(this, getString(R.string.toast_substratum_frozen),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Substratum is found, launch it directly
            launchSubstratum();
        } else {
            getSubstratumFromPlayStore();
        }
    }

    private void getSubstratumFromPlayStore() {
        String playURL = "https://play.google.com/store/apps/details?id=projekt.substratum";
        Intent i = new Intent(Intent.ACTION_VIEW);
        Toast.makeText(this, getString(R.string.toast_substratum), Toast.LENGTH_SHORT).show();
        i.setData(Uri.parse(playURL));
        startActivity(i);
        finish();
    }

    private void showOutdatedSubstratumToast() {
        String parse = String.format(
                getString(R.string.outdated_substratum),
                getString(R.string.ThemeName),
                String.valueOf(MINIMUM_SUBSTRATUM_VERSION));
        Toast.makeText(this, parse, Toast.LENGTH_SHORT).show();
    }

    private void launchSubstratum() {
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager().getPackageInfo(SUBSTRATUM_PACKAGE_NAME, 0);
            if (packageInfo.versionCode >= MINIMUM_SUBSTRATUM_VERSION) {
                Intent intent = SubstratumLoader.launchThemeActivity(getApplicationContext(),
                        getIntent(), getString(R.string.ThemeName), getPackageName());
                startActivity(intent);
                finish();
            } else {
                showOutdatedSubstratumToast();
            }
        } catch (Exception e) {
            showOutdatedSubstratumToast();
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (THEME_READY_GOOGLE_APPS) {
            detectThemeReady();
        } else {
            launch();
        }
    }

    private void launch() {
        beginSubstratumLaunch();
    }

    private void detectThemeReady() {
        File addon = new File("/system/addon.d/80-ThemeReady.sh");
        if (addon.exists()) {
            ArrayList<String> apps = new ArrayList<>();
            boolean updated = false;
            boolean incomplete = false;
            PackageManager packageManager = this.getPackageManager();
            StringBuilder app_name = new StringBuilder();
            String[] packageNames = {"com.google.android.gm",
                    "com.google.android.googlequicksearchbox",
                    "com.android.vending",
                    "com.google.android.apps.plus",
                    "com.google.android.talk",
                    "com.google.android.youtube",
                    "com.google.android.apps.photos",
                    "com.google.android.inputmethod.latin"};
            String[] extraPackageNames = {"com.google.android.contacts",
                    "com.google.android.dialer"};

            for (String packageName : extraPackageNames) {
                try {
                    ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        incomplete = true;
                        apps.add(packageManager.getApplicationLabel(appInfo).toString());
                    }
                } catch (Exception e) {
                    // Package not found
                }
            }

            if (!incomplete) {
                for (String packageName : packageNames) {
                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                        if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                            updated = true;
                            apps.add(packageManager.getApplicationLabel(appInfo).toString());
                        }
                    } catch (Exception e) {
                        // Package not found
                    }
                }
                for (String packageName : extraPackageNames) {
                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                        if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                            updated = true;
                            apps.add(packageManager.getApplicationLabel(appInfo).toString());
                        }
                    } catch (Exception e) {
                        // Package not found
                    }
                }
            }

            for (int i = 0; i < apps.size(); i++) {
                app_name.append(apps.get(i));
                if (i <= apps.size() - 3) {
                    app_name.append(", ");
                } else if (i == apps.size() - 2) {
                    app_name.append(" ").append(getString(R.string.and)).append(" ");
                }
            }

            if (!updated && !incomplete) {
                launch();
            } else {
                int stringInt = incomplete ? R.string.theme_ready_incomplete :
                        R.string.theme_ready_updated;
                String parse = String.format(getString(stringInt),
                        app_name);

                new AlertDialog.Builder(this, R.style.DialogStyle)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(getString(R.string.ThemeName))
                        .setMessage(parse)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                launch();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                finish();
                            }
                        })
                        .show();
            }
        } else {
            new AlertDialog.Builder(this, R.style.DialogStyle)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getString(R.string.ThemeName))
                    .setMessage(getString(R.string.theme_ready_not_detected))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            launch();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            finish();
                        }
                    })
                    .show();
        }
    }

}