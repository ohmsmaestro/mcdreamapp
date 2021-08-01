package com.stetis.asynctask;

import android.content.Context;
import android.os.AsyncTask;
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




public class HttpClient extends AsyncTask<String,Void,String> {
    private static final String TAG = "HttpClient";
    private String connect_error_message ;
    private String payload;
    private String method;
    private WeakReference<ResponseCallback> callback;
    private boolean isError = false;
    private Context context;
    public void setResponseCallback(ResponseCallback callBack) {
        callback = new WeakReference<ResponseCallback>(callBack);
    }

    public HttpClient(String method, Context context) {
        this.method = method;
        this.context = context;
    }

    public HttpClient(String payload, String method){
        this.payload = payload;
        this.method = method;
    }


    public HttpClient(String payload, String method, Context context){
        this.payload = payload;
        this.method = method;
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        OutputStream outputStream = null;
        this.connect_error_message = "Connectivity Error: Please check your Internet connection.";

        String response = null;
        try{
            // System.out.println("PATH >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + strings[0]);
            String path = strings[0];
            url = new URL(path);
            System.out.println(strings[0]);
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
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!= null && !result.isEmpty() && !isError) {
            callback.get().onRequestSuccess(result);
        } else {
            callback.get().onRequestFailed(connect_error_message);
        }
        super.onPostExecute(result);
    }
}