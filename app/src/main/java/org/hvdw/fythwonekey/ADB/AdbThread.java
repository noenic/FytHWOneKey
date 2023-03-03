package org.hvdw.fythwonekey.ADB;

import android.content.Context;
import android.util.Log;


public class AdbThread implements Runnable{
    private static final String TAG = "AdbThread";

    private final AdbAdapter adb;
    private final String ip;
    private final int port;
    private Context mContext;
    private String command;
    private volatile String output;
    private volatile String error;


    public AdbThread(AdbAdapter adb,String ip, int port, String command){
        this.adb = adb;
        this.ip = ip;
        this.port = port;
        this.command = command;

    }
    public void run(){
        try {
            adb.run(command);
        } catch (Exception e) {
            Log.e(TAG, "Error in AdbThread: " + e.getMessage());
            error = e.getMessage();
        }
    }

    public String getError() {
        return error;
    }

}