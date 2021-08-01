package com.stetis.asynctask;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.stetis.json.JsonConstants;
import com.stetis.utils.AppUtils;
import com.stetis.utils.HttpUtils;
import com.stetis.utils.PreferenceUtils;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkTask extends BaseTask {
    private static final String TAG = "HttpClient";
    private String connect_error_message ;
    private String payload;
    private String method;
    private boolean isError = false;
    private NetworkCallResponseCallback networkCallResponseCallback;
    private String link;
    private Context context;
    public void setNetworkResponseCallback(NetworkCallResponseCallback networkCallResponseCallback) {
        this.networkCallResponseCallback = networkCallResponseCallback;
    }

    public NetworkTask(String method, String link,  Context context) {
        this.method = method;
        this.link = link;
        this.context = context;
    }

    public NetworkTask(String payload, String method, String link,  Context context) {
        this.payload = payload;
        this.method = method;
        this.link = link;
        this.context = context;
    }

    @Override
    public Object call() throws Exception {

        URL url = null;
        HttpURLConnection urlConnection = null;
        OutputStream outputStream = null;
        this.connect_error_message = "Connectivity Error: Please check your Internet connection.";

        String response = "Connectivity Error: Please check your Internet connection.";
        try{
            // System.out.println("PATH >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + strings[0]);
            String path = link;
            url = new URL(path);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod(method);
            urlConnection.setUseCaches(false);


            if(!path.contains(HttpUtils.LOGIN)){
                urlConnection.setRequestProperty("Authorization","Bearer "+ PreferenceUtils.getString(JsonConstants.JWT, "", context));
            }

            //if(TextUtils.equals("POST"))
            urlConnection.setDoInput(true);

            // Log.d("here", "PayLoad: "+payload.toString());

            if(payload!=null&&!TextUtils.equals("",payload)|| !TextUtils.equals("GET", method)){
                urlConnection.setDoOutput(true);
            }

            if(payload!=null&&!TextUtils.equals("",payload)&& !TextUtils.equals("GET", method)){
                urlConnection.setFixedLengthStreamingMode(payload.getBytes().length);
            }

            //make some HTTP header nicety
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            urlConnection.connect();

            //setup send
            outputStream = null;
            if(payload!=null&&!TextUtils.equals("",payload)&& (TextUtils.equals("POST", method)||TextUtils.equals("PUT", method))){
                outputStream =  new BufferedOutputStream(urlConnection.getOutputStream());
                outputStream.write(payload.getBytes());
                //clean up
                outputStream.flush();
            }


            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                response = HttpUtils.readStream(urlConnection.getInputStream());
                if(AppUtils.isJSONValid(response)){
                    isError = false;
                }

                else {
                    isError = true;
                    connect_error_message="Please make sure you have an active data connection";
                }

                //Log.d("here", response);
            }else{
                isError = true;
                //Construct Response Message
                connect_error_message = "Sorry, Operation could not be completed at the moment."+urlConnection.getResponseCode() ;
                //Log.d("here", "Server Response Code: "+urlConnection.getResponseCode());
            }

        }catch (Exception ex){
            isError = true;
            //ex.printStackTrace();
            Log.e(TAG, ex.getLocalizedMessage(), ex);
        }
        finally {
            try{
                if(outputStream != null){
                    outputStream.close();}
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }catch (Exception ex){
                Log.e(TAG, ex.getLocalizedMessage(), ex);
                //ex.printStackTrace();
            }
        }
        return response;
    }

    @Override
    public void setDataAfterLoading(Object result) {
        if (result!= null && !result.toString().isEmpty() && !isError) {
            networkCallResponseCallback.onRequestSuccess(result.toString());
        } else {
            networkCallResponseCallback.onRequestFailed(connect_error_message);
        }
    }
}


