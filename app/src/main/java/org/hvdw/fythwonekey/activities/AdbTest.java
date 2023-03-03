package org.hvdw.fythwonekey.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import org.hvdw.fythwonekey.ADB.AdbAdapter;
import org.hvdw.fythwonekey.ADB.AdbThread;


public class AdbTest extends Activity {
    public static final String TAG = "AdbTest";
    public  Context mContext;
    Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        Log.i(TAG, "Started AdbTest; in OnCreate void");
        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String ip = sharedprefs.getString("adb_ip_entry", "").replaceAll("\\s+","");

        int port = Integer.parseInt(sharedprefs.getString("adb_port_entry", "5555").replaceAll("\\s+",""));

        String command = "shell:cmd notification post -S bigtext -t 'It worked!' 'Tag' 'Sounds Good ! This notification was sent with ADB' && service call statusbar 1";

        AdbAdapter adb = new  AdbAdapter(mContext, ip, port);
        AdbThread adbThread =new AdbThread(adb, ip, port,command);
        Thread thread1 =new Thread(adbThread);
        thread1.start();

        //If the thread is running for more than 5 seconds, we interrupt it, timeout
        try {
            thread1.join(2000);
            if (thread1.isAlive()) {
                thread1.interrupt();
                Toast.makeText(mContext, "No response (timeout)", Toast.LENGTH_LONG).show();
                Log.e(TAG, "No response (timeout)");
            }
            String error=adbThread.getError();

            if (error != null) {
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }
}





