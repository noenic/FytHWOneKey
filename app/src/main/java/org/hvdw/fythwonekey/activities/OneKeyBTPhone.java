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


public class OneKeyBTPhone extends Activity {
    public static final String TAG = "OneKeyBTPhone";
    public static Context mContext;
    private static PackageManager pm;
    Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        Log.i(TAG, "Started OneKeyBTPHONE; in OnCreate void");

        String packagename_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.BTPHONE_PACKAGENAME_ENTRY, "");
        String intent_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.BTPHONE_INTENT_ENTRY, "");
        String sys_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.BTPHONE_SYSCALL_ENTRY, "");

        Utils myUtils = new Utils();
        myUtils.checkAndRunOptions(mContext, packagename_call);

        finish();
    }

}
