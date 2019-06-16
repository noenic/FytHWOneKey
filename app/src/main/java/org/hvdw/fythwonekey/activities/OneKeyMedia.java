package org.hvdw.fythwonekey.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.hvdw.fythwonekey.MySettings;
import org.hvdw.fythwonekey.R;
import org.hvdw.fythwonekey.utils.AppStartUtils;

import static org.hvdw.fythwonekey.utils.ShellUtils.shellExec;


public class OneKeyMedia extends Activity {
    public static final String TAG = "OneKeyMedia";
    public Context mContext;
    private static PackageManager pm;
    private String packagename_call;
    private String intent_call;
    private String sys_call;
    Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        Log.d(TAG, "Started OneKeyMedia; in OnCreate void");

        packagename_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.MEDIA_PACKAGENAME_ENTRY, "");
        intent_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.MEDIA_INTENT_ENTRY, "");
        sys_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.MEDIA_SYSCALL_ENTRY, "");

        //mToast = Toast.makeText(OneKeyMedia.this, "In On Create", Toast.LENGTH_LONG);
        //mToast.show();
        AppStartUtils myAppUtils = new AppStartUtils();

        if ("".equals(packagename_call)) {
            //packagename_call unknown, start setup
            Log.d(TAG, getString(R.string.pkg_notconfigured_short));
            mToast = Toast.makeText(OneKeyMedia.this, getString(R.string.pkg_notconfigured_long), Toast.LENGTH_SHORT);
            mToast.show();
            myAppUtils.startActivityByPackageName(mContext, "org.hvdw.fythwonekey");
        } else {
            Log.d(TAG, getString(R.string.pkg_configured_short) + " " + packagename_call);
            //Start music app or whatever app the user has configured
            myAppUtils.startActivityByPackageName(mContext, packagename_call);
        }

        if (sys_call != "") {
            Log.d(TAG, "do a system call");
            shellExec(sys_call);
        }

        finish();
    }

}
