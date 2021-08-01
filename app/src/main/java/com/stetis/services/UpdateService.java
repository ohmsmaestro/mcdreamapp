package com.stetis.services;

import android.app.IntentService;
import android.content.Intent;

import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.PreferenceUtils;
import com.stetis.utils.SingleInstance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.annotation.Nullable;


/**
 * Created by SnrMngrFinance on 09/02/2018.
 */

public class UpdateService extends IntentService {
    boolean successfullDownload = false;
    public static boolean downloadStopped = false;
    public static boolean cancelled = false;
    public static boolean STOP_DOWNLOADING = false;
    public UpdateService(){
        super("UpdateService");
    }
    public UpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //SingleInstance.getInstance().currentInstance().setDownloadActive(true);
        OutputStream outputStream =null;
        InputStream input = null;
        try{
        try {
            String loads = intent.getStringExtra("load");
            //JSONObject activity = new JSONObject(loads);
            String link = PreferenceUtils.getString(JsonConstants.APPURL, "000", getApplicationContext());
            URL url = new URL(link);
            AppUtils.createFile(this, "sams");
           // AppUtils.createFile(this, "sams2");
            URLConnection connection = url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();

          //  InputStream
            File file = AppUtils.getFile(AppUtils.SDIR + "/data/sams.apk");
           // Log.d("here", "Size: "+fileLength);
            input = connection.getInputStream();
            if(input!=null) {
                outputStream = new FileOutputStream(AppUtils.getFile(AppUtils.SDIR + "/source/sams.apk"));
                byte data[] = new byte[1024];
                long total = 0;
                int count = 0;

                while ((count = input.read(data)) != -1) {

                    total += count;
                    int progressValue = ((int) (total * 100 / fileLength));
                    Intent intentResponse = new Intent();
                    intentResponse.putExtra("updateresponse", "updateresponse");
                    intentResponse.setAction("updateresponse");
                    intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                    intentResponse.putExtra("downloadlevel", progressValue);
                    sendBroadcast(intentResponse);
                    outputStream.write(data, 0, count);
                }

                //Log.d("here", "done!");
                outputStream.flush();
                successfullDownload = true;
            }

            else {
                successfullDownload = false;
            }
        }
        catch (Exception ex){
            //Log.d("here", "first catch");
            successfullDownload = false;
            ex.printStackTrace();
        }

        finally {
            try {
                if(outputStream!=null) {
                    outputStream.close();
                }

                if(input!=null){
                    input.close();
                }
            } catch (IOException e) {
               // SingleInstance.getInstance().currentInstance().setDownloadActive(false);
                e.printStackTrace();
            }
        }

//        if(downloadStopped) {
//            Intent downStatusResponse = new Intent();
//            downStatusResponse.putExtra("downloadstopped", downloadStopped);
//            downStatusResponse.putExtra(Constants.EXTRA_KEY_OUT_UPDATE, successfullDownload);
//            downStatusResponse.setAction("updatecancelled");
//            downStatusResponse.addCategory(Intent.CATEGORY_DEFAULT);
//            sendBroadcast(downStatusResponse);
//
//        }
//
//        else {
            Intent downStatusResponse = new Intent();

            downStatusResponse.putExtra(Constants.EXTRA_KEY_OUT_UPDATE, successfullDownload);
           downStatusResponse.addCategory(Intent.CATEGORY_DEFAULT);
            downStatusResponse.putExtra("downloadstopped", downloadStopped);
            downStatusResponse.setAction("updatecancelled");
               sendBroadcast(downStatusResponse);

            Intent intentResponse = new Intent();
            intentResponse.putExtra("update", "update");
            intentResponse.putExtra("type", intent.getStringExtra("type"));
            intentResponse.setAction(Constants.ACTION_RESPONSE_UPDATE);
            intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
            intentResponse.putExtra(Constants.EXTRA_KEY_OUT_UPDATE, successfullDownload);
            sendBroadcast(intentResponse);
       // }


        //successfullDownload = true;
    }
    catch (Exception ex){
            ex.printStackTrace();
        //Log.d("here", "second catch!");
        //SingleInstance.getInstance().currentInstance().setDownloadActive(false);
    }

    }

    @Override
    public void onDestroy() {
//        if(cancelled){
//            Intent downStatusResponse = new Intent();
//            downStatusResponse.putExtra("downloadstopped", true);
//            downStatusResponse.putExtra(Constants.EXTRA_KEY_OUT_UPDATE, successfullDownload);
//            downStatusResponse.setAction("updatecancelled");
//            downStatusResponse.addCategory(Intent.CATEGORY_DEFAULT);
//            sendBroadcast(downStatusResponse);
////            downloadStopped = false;
////            cancelled = false;
//        }
        super.onDestroy();
    }
}
