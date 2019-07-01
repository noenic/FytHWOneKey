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
import org.hvdw.fythwonekey.Utils;


public class OneKeyAV extends Activity {
    public static final String TAG = "OneKeyAV";
    public static Context mContext;
    private static PackageManager pm;
    Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        Log.i(TAG, "Started OneKeyAV; in OnCreate void");

        String packagename_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.AV_PACKAGENAME_ENTRY, "");
        //String intent_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.AV_INTENT_ENTRY, "");
        //String sys_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.AV_SYSCALL_ENTRY, "");

        Utils myUtils = new Utils();

        if ("".equals(packagename_call)) {
            //packagename_call unknown, start setup
            Log.i(TAG, getString(R.string.pkg_notconfigured_short));
            mToast = Toast.makeText(OneKeyAV.this, getString(R.string.pkg_notconfigured_long), Toast.LENGTH_SHORT);
            mToast.show();
            myUtils.startActivityByPackageName(mContext, "org.hvdw.fythwonekey");
        } else {
            Log.i(TAG, getString(R.string.pkg_configured_short) + " " + packagename_call);
            //Start AV app or whatever app the user has configured
            myUtils.startActivityByPackageName(mContext, packagename_call);
        }

        finish();
    }

}
