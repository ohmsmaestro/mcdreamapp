package com.stetis.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.stetis.utils.AppUtils;


/**
 * Created by jante on 12/12/14.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
   // private WeakReference<NetworkResponseCallback> callback;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(AppUtils.isInternetConnected(context)){
            //Toast.makeText(context, "SAMS: Internet connection is available", Toast.LENGTH_SHORT).show();
            context.sendBroadcast(new Intent("INTERNET_AVAILABLE"));
//            if(callback != null) {
//                callback.get().onConnectionAvailable(true);
//            }
        }else{
            context.sendBroadcast(new Intent("DEVICE_OFFLINE"));
            //Toast.makeText(context, "SAMS CONNECTIVITY:No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        //}
    }
}
