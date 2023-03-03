//package org.hvdw.fythwonekey.ADB;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //On utilise AdbTest
//        AdbTest adbTest = new AdbTest();
//        AdbCrypto crypto = AdbUtils.readCryptoConfig(getFilesDir());
//        if (crypto == null)
//        {
//            /* We need to make a new pair */
//            Toast.makeText(this, "Generating new key pair", Toast.LENGTH_LONG).show();
//
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    AdbCrypto crypto;
//
//                    crypto = AdbUtils.writeNewCryptoConfig(getFilesDir());
//
//                    if (crypto == null)
//                    {
//                        Toast.makeText(MainActivity.this, "Failed to generate new key pair", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    Toast.makeText(MainActivity.this, "Generated new key pair", Toast.LENGTH_LONG).show();
//                }
//            }).start();
//        }
//        else
//        {
//            Toast.makeText(this, "Loaded existing key pair", Toast.LENGTH_LONG).show();
//        }
//
//        //On lance le test dans un thread
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                AdbTest.main(MainActivity.this, crypto,"10.0.2.2", 5555);
//                }catch (Exception e){
//                    Log.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", e.toString());
//                }
//            }
//        }).start();
//
//
//    }
//}
