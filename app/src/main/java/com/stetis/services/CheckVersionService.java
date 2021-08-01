package com.stetis.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.PreferenceUtils;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.Nullable;


/**
 * Created by SnrMngrFinance on 09/02/2018.
 */

public class CheckVersionService extends IntentService {
    private boolean updateAvailable = false;
    private boolean errorOccurred = false;

    public CheckVersionService() {
        super("CheckVersionService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.d("here", "Landed in versionservice");
        OutputStream outputStream =null;
        try{
            try {
                String link = intent.getStringExtra("link");
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /*milliseconds*/);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                //if(TextUtils.equals("POST"))
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                urlConnection.connect();

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String response = HttpUtils.readStream(urlConnection.getInputStream());
                    if(AppUtils.isJSONValid(response)){
                        JSONObject responseData = new JSONObject(response);
                        JSONObject meta = responseData.getJSONObject(JsonConstants.META);
                        String status = meta.getString(JsonConstants.STATUS);
                        if(TextUtils.equals(status, JsonConstants.SUCCESS)){
                            JSONObject data = responseData.getJSONObject(JsonConstants.DATA);
                            updateAvailable = data.getBoolean(JsonConstants.UPDATE);
                            String appUrl = data.getString(JsonConstants.APPURL);
                            PreferenceUtils.putString(JsonConstants.APPURL, appUrl, getApplicationContext());
                        }
                    }

                    else {
                        updateAvailable = false;
                    }

                    //Log.d("here", response);
                }
                else{
                   // Log.d("here", "Response Code: "+urlConnection.getResponseCode());
                    errorOccurred = true;
                }


            }
            catch (Exception ex){
                //Log.d("here", "first catch");
                errorOccurred = true;
                ex.printStackTrace();
            }

            Intent intentResponse = new Intent();
            intentResponse.putExtra("update", "version");
            intentResponse.putExtra("type",intent.getStringExtra("type"));
            intentResponse.putExtra("erroroccured", errorOccurred);
            intentResponse.putExtra("updateavailable", updateAvailable);
            intentResponse.setAction(Constants.ACTION_RESPONSE_UPDATE);
            intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
            sendBroadcast(intentResponse);

            //successfullDownload = true
        }
        catch (Exception ex){
            ex.printStackTrace();
            //Log.d("here", "second catch!");
            errorOccurred = true;
        }
    }

}