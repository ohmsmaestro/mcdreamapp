package com.stetis.mcdream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private long exitTime = 0;
    private PowerManager.WakeLock wakeLock;

    private Timer startTimer;
    private TimerTask startTask;
    Handler startHandler;
    private Context context;
    // private PolicyManager policyManager;

    @SuppressLint({"NewApi", "SetJavaScriptEnabled", "InvalidWakeLockTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);


            //if(Build.)
            context = this;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
            // StatusBarDisable(true);
            //setNavigationBarState(true);
        } catch (Exception ex) {

        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            exitApplication();
//            return true;
//        } else if(keyCode == KeyEvent.KEYCODE_HOME){
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    public void exitApplication() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            //Toast.makeText(getApplicationContext(), getString(R.string.txt_exitinfo), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

            finish();
            System.exit(0);
            //AppList.getInstance().exit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
