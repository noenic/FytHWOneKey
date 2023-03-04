package org.hvdw.fythwonekey;

import android.preference.Preference;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.os.Handler;
import android.widget.ProgressBar;
import android.util.AttributeSet;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    Context mContext;
    AttributeSet attrs;

    private ProgressBar pb;
    static Runnable myRunnable;
    private static Handler myHandler;
    private boolean zygote_reboot;

    private BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter = new IntentFilter();

    public static final String TAG = "FHWOK-prefs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Log.i(TAG, "onCreate: in Sofia 6.0.1 sdk23");
            //Running on Sofia 6.0.1 sdk23
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.preferences);
        } else {
            Log.i(TAG, "onCreate: Running on Android 8.0.0 sdk26");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
        }

        //We need to update the summary of the preference with the current value for the ABD section
        Preference adb_ip_entry = findPreference("adb_ip_entry");
        adb_ip_entry.setSummary(getPreferenceManager().getSharedPreferences().getString("adb_ip_entry", ""));
        Preference adb_port_entry = findPreference("adb_port_entry");
        adb_port_entry.setSummary(getPreferenceManager().getSharedPreferences().getString("adb_port_entry", ""));


        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent intent = new Intent();
        String toastText = "";
        String additionalText = "";

        switch (key) {

            case MySettings.AV_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_AV_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_AV_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.AV_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_AV_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_AV_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BAND_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_BAND_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BTAV_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BTAV_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BTAV_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BTAV_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_BTAV_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BTAV_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BTPHONE_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BTPHONE_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BTPHONE_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BTPHONE_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_BTPHONE_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BTPHONE_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_DVD_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_DVD_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_EQ_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_EQ_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_MEDIA_KEY_OPTION:
                intent.setAction(MySettings.ACTION_EQ_MEDIA_KEY_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_MEDIA_KEY_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_MEDIA_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.VIDEO_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_VIDEO_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_VIDEO_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.VIDEO_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_VIDEO_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_VIDEO_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.VOICE_KEY_CALL_OPTION:
                intent.setAction(MySettings.ACTION_VOICE_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_VOICE_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.VOICE_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_VOICE_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_VOICE_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACCON_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACCON_INTENT_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_INTENT_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_INTENT_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACCON_SYSCALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_SYSCALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_SYSCALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            /* USB ON settings. This is when ACC_ON is generated. Available usb devices are reconnected, which gives the
            usb device attached broadcast. We can't use ACC_ON on Android >=7.0
             */
            case MySettings.USBON_ACTIONSTRING_ENTRY:
                intent.setAction(MySettings.ACTION_USBON_ACTIONSTRING_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_USBON_ACTIONSTRING_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.USBON_INTENT_ENTRY:
                intent.setAction(MySettings.ACTION_USBON_INTENT_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_USBON_INTENT_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.USBON_SYSCALL_ENTRY:
                intent.setAction(MySettings.ACTION_USBON_SYSCALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_USBON_SYSCALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.SWITCH_WIFI_ON:
                intent.setAction(MySettings.ACTION_SWITCH_WIFI_ON_CHANGED);
                intent.putExtra(MySettings.EXTRA_SWITCH_WIFI_ON_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.RESTART_PLAYER:
                intent.setAction(MySettings.ACTION_RESTART_PLAYER_CHANGED);
                intent.putExtra(MySettings.EXTRA_RESTART_PLAYER_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;

                /* ACCOFF settings */
            case MySettings.SWITCH_WIFI_OFF:
                intent.setAction(MySettings.ACTION_SWITCH_WIFI_OFF_CHANGED);
                intent.putExtra(MySettings.EXTRA_SWITCH_WIFI_OFF_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PAUSE_PLAYER:
                intent.setAction(MySettings.ACTION_PAUSE_PLAYER_CHANGED);
                intent.putExtra(MySettings.EXTRA_PAUSE_PLAYER_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.ACCOFF_SYSCALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACCOFF_SYSCALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCOFF_SYSCALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;


            case MySettings.ADB_PORT_ENTRY:
            case MySettings.ADB_IP_ENTRY:
                Preference pref = findPreference(key);
                pref.setSummary(sharedPreferences.getString(key, ""));
                break;


            default:
                Log.i(TAG, "Invalid setting encountered");
                break;
       }

        Log.i(TAG, "updated key is " + key);
        if (toastText.equals("BOOLEAN_KEY")) {
            toastText = "You updated boolean key \"" + (String) key + "\" to \"" + String.valueOf(sharedPreferences.getBoolean(key, false)) + "\"";
        } else {
            Log.i(TAG, "updated string is " + sharedPreferences.getString(key, ""));
            toastText = "You updated key \"" + key + "\" to \"" + sharedPreferences.getString(key, "") + "\"";
        }
        if (additionalText != "") {
            toastText = toastText + additionalText;
        }
        Toast mToast = Toast.makeText(mContext, toastText, Toast.LENGTH_LONG);
        mToast.show();

        if (intent.getAction() != null) {
            getActivity().sendBroadcast(intent);
        }


    }

}
