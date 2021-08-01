package com.stetis.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jante
 * Date: 12/1/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class IgrUtil {
    private final static String TAG= "Utils";
    public static String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        // Return full string
        return total.toString();
    }

    @SuppressWarnings("unused")
    public static boolean isNetworkReachable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if(activeNetworkInfo == null){
            return false;
        }else{
            return (activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED);
        }

    }

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    public static boolean isInternetOn(Context context)
    {

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
                ||(connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
                ||(connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
                ||(connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED))
        {
            return true;
        }

        else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED))
        {
            return false;
        }

        return false;
    }

    public static Map<String, Date> getDates(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE,cal.getMinimum(Calendar.DATE));
        Date sd = cal.getTime();

        cal.set(Calendar.DATE,cal.getMaximum(Calendar.DATE));
        Date ed = cal.getTime();
        Map<String, Date> dates = new HashMap<String, Date>();
        dates.put("sd",sd);
        dates.put("ed",ed);
        return dates;
    }

    public static String convertToDate(DatePicker picker){
        return String.valueOf((picker.getMonth()+1))+"-"+picker.getDayOfMonth()+"-"+picker.getYear();
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
        }
        return false;
    }

    public static int getNextColor(){
        Random rand = new Random();
        return Color.rgb(rand.nextInt(225),rand.nextInt(255),rand.nextInt(225));
    }

    public static char getNairaSymbol(){
        char c = '\u20A6';
        return c;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isValidEmail(String enteredEmail){
        String EMAIL_REGIX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_REGIX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (enteredEmail!=null) && (matcher.matches()));
    }
}
