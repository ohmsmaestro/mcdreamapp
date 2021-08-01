package com.stetis.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.SingleInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jante on 12/12/14.
 */
public class UploadService extends IntentService {
    private static  final String TAG = "UploadService";
    StringBuilder sb;
    OutputStream outputStream = null;
    public UploadService() {
        super("UploadService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
           // Log.d("here", "Before Request");
            String loads = intent.getStringExtra("load");
            //Log.d("here", "All for upload: " + loads.toString());
            JSONObject activity = new JSONObject(loads);
            JSONArray activityArray = activity.getJSONArray(JsonConstants.TRANSACTIONS);
            sb = new StringBuilder("");
            URL url = null;
            HttpURLConnection urlConnection = null;
            OutputStream outputStream = null;

            for(int i = 0; i< activityArray.length(); i++){

                try {
                    SingleInstance.getInstance().currentInstance().setBackgroundactive(true);
                    JSONObject payload = activityArray.getJSONObject(i);
                    //Log.d("here", "");
                    //System.out.println("Content: "+payload.toString());
                    String link = "";
                    if(payload.getInt(JsonConstants.TAG)==1){
                        link = String.format(HttpUtils.baseUrlV2(), HttpUtils.ENROLMENT);
                    }


                    else if(payload.getInt(JsonConstants.TAG)==3){
                        link = String.format(HttpUtils.baseUrlV2(), HttpUtils.ACCREDITATION);
                    }

                   // Log.d("here", "Before Url Initialization");
                    url = new URL(link);

                    //Log.d("here", "Before Url Initialization");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000 /*milliseconds*/);
                    urlConnection.setConnectTimeout(10000 /* milliseconds */);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    urlConnection.setFixedLengthStreamingMode(payload.toString().getBytes().length);
                    //Log.d("here", "After Url Initialization");
                    //make some HTTP header nicety
                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    //Log.d("here", "Request");
                    urlConnection.connect();

                    //setup send
                    outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    if(loads!=null&&!TextUtils.equals("", loads)){
                        outputStream.write(payload.toString().getBytes());
                        outputStream.flush();
                    }

                   //System.out.println("Response Code: "+urlConnection.getResponseCode());
                    //Log.d("here", "Response Code: " + urlConnection.getResponseCode());
                    if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        String response = HttpUtils.readStream(urlConnection.getInputStream());
                       //Log.d("here", "Server Response: " + response);
                        //System.out.println("Response Object: " + response);
                        if(AppUtils.isJSONValid(response)){
                        JSONObject json = new JSONObject(response);
                        //Log.d("here", "Response: "+json.toString());
                        JSONObject meta = json.getJSONObject(JsonConstants.META);
                        if(meta.getString(JsonConstants.STATUS).equals(JsonConstants.SUCCESS)) {
                            sb.append(payload.getInt(JsonConstants.ID)+"");
                           // System.out.println(sb.toString()+" From the result");
                        }
                        }

                    }else{
                        //Construct Response Message
                       //Log.d("here", "Unsuccessful HTTP Response Code: "+urlConnection.getResponseCode());
                    }

                }catch (Exception ex){
                    SingleInstance.getInstance().currentInstance().setBackgroundactive(false);
                    //Log.e(TAG, ex.toString());
                    ex.printStackTrace();
                }

                finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                        Log.e(TAG, ex.getLocalizedMessage(), ex);
                    }
                }

            }
            String s = sb.toString();
            //System.out.println("Returned from server: "+s.toString());
            //JSONObject jsonData = new JSONObject(s);
                //SingleInstance.getInstance().currentInstance().setBackgroundactive(false);
                Intent intentResponse = new Intent();
                intentResponse.putExtra("status", "off");
                intentResponse.putExtra("type",intent.getStringExtra("type"));
                intentResponse.setAction(Constants.ACTION_RESPONSE);
                intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
                intentResponse.putExtra(Constants.EXTRA_KEY_OUT, s);
                sendBroadcast(intentResponse);

        }catch (Exception ex){
            SingleInstance.getInstance().currentInstance().setBackgroundactive(false);
            //ex.printStackTrace();
            }
    }
}
