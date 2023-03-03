package org.hvdw.fythwonekey.ADB;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Scanner;



public class AdbAdapter {
    // This implements the AdbBase64 interface required for AdbCrypto

    private AdbCrypto crypto;
    private String ip;
    private int port;
    private Context ctx;
    private String error = null;
    private String output = null;


    public static AdbBase64 getBase64Impl() {
        return new AdbBase64() {
            @Override
            public String encodeToString(byte[] arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Base64.getEncoder().encodeToString(arg0);
                }
                else {
                    return android.util.Base64.encodeToString(arg0, android.util.Base64.NO_WRAP);
                }
            }

        };
    }

    //constructor
    public AdbAdapter(Context ctx, String ip, int port) {
        this.ctx = ctx;
        this.ip = ip;
        this.port = port;
        try{
            this.crypto = setupCrypto();
        } catch (Exception e) {
            error(e.toString());
        }
    }


    // This function generates a new key pair if one does not exist
    private AdbCrypto setupCrypto()
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
    {
        AdbCrypto crypto = AdbUtils.readCryptoConfig(ctx.getFilesDir());
        if (crypto == null)
        {
            /* We need to make a new pair */
            log("Generating new key pair");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AdbCrypto crypto;

                    crypto = AdbUtils.writeNewCryptoConfig(ctx.getFilesDir());

                    if (crypto == null)
                    {
                        error("Failed to generate new key pair");
                        return;
                    }
                    log("Generated new key pair");
                }
            }).start();
        }
        else
        {
            log("Loaded existing key pair");
        }
    return crypto;
    }

    public void run(String cmd) throws IOException, InterruptedException {

        log("Starting ADB procedure...");
        AdbConnection adb;
        Socket sock;

        // Connect the socket to the remote host
        log("Socket connecting to " + ip + ":" + port);

        //On lance un socket avec un timeout de 5 secondes


        sock = new Socket(ip, port);


        log("Socket connected");

        // Construct the AdbConnection object
        try {
            adb = AdbConnection.create(sock, crypto);
        } catch (IOException e) {
            error("IO error: " + e.getMessage());
            return;
        }

        // Start the application layer connection process
        log("ADB connecting...");

        adb.connect();

        log("ADB connected");

        // Open the shell stream of ADB
        final AdbStream stream;
        try {
            log("sending command : "+cmd);
            stream = adb.open(cmd);
        } catch (UnsupportedEncodingException e) {
            error("Unsupported encoding: " + e.getMessage());
            return;
        } catch (IOException e) {
            error("IO error: " + e.getMessage());
            return;
        } catch (InterruptedException e) {
            error("Interrupted: " + e.getMessage());
            return;
        }

        // Start the receiving thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!stream.isClosed())
                    try {
                        // Print each thing we read from the shell stream
                        output = new String(stream.read(), "US-ASCII");
                        log("received: "+output);

                    } catch (UnsupportedEncodingException e) {
                        error("Unsupported encoding: " + e.getMessage());
                        return;
                    } catch (InterruptedException e) {
                        error("Interrupted: " + e.getMessage());
                        return;
                    } catch (IOException e) {
                        if (e.getMessage()!="Stream closed")
                            error("IO error: " + e.getMessage());
                        else
                            log("Stream closed");
                    }
            }
        }).start();

    }



    public String getLatestError() {
        return error;
    }

    public String getLatestOutput() {
        return output;
    }

    public void error(String msg) {
        Log.e("AdbAdapter", "\n\n"+msg+"\n\n");
        error=msg;
    }

    public void log(String msg) {
        Log.d("AdbAdapter", "\n\n"+msg+"\n\n");
    }
}
