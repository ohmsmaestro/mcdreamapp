package com.stetis.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stetis.json.JsonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;


/**
 * Created by bizzdesk on 8/1/15.
 */
public class AppUtils {
    //KIRS Constant
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_USAGE = "usage";
    public static final String SOFTWARE_VERSION = "version";
    public static final String MODEL = "model";
    public static final String SDIR = Environment.getExternalStorageDirectory() + "/SAMS";
    private AppUtils() {
    }
    public static PackageInfo getPackageInfo(Context context) {
        try {

            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Should never happen.
            throw new RuntimeException(e);
        }
    }

    public static JSONObject getJsonObject(Context context) {
        try {
            JSONObject json = new JSONObject();
            json.put(JsonConstants.VERSION_NAME,getPackageInfo(context).versionName);
            json.put(JsonConstants.VERSION_CODE,getPackageInfo(context).versionCode);
            json.put(DEVICE_USAGE,"SAMS");
            json.put(MODEL,getDeviceName());

            return json;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }

    public static String getDeviceid() {
        //return getTelephoneManager(context).getDeviceId();
        return "354082061038568";
    }

    public static String getModelName() {
        String model = "";
        try{
            model = Build.MODEL;
        }
        catch (Exception ex) {
        }

        return model;
    }

    public static TelephonyManager getTelephoneManager(Context context) {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        return mngr;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static LinearLayout.LayoutParams layOutParams(View view){
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
        float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
        params.setMargins((int)pixel, 0, 0, 0);
        return  params;
    }

    public static String generateID(){
        String salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&*";
        Random random = new Random();

        UUID id = UUID.randomUUID();
        String[] cc = id.toString().replace("-", "").split("");
        List<String> charList = Arrays.asList(cc);
        Collections.shuffle(charList);
        Collections.shuffle(charList);

        String generatedString = "";
        char [] toChar = salt.toCharArray();
        for (int i = 0; i < charList.size(); i++) {
            if(i==8||i==13||i==18||i==23){
                generatedString+=""+toChar[random.nextInt(salt.length()-1)];
            }
            generatedString+=charList.get(i);
        }

        return generatedString;
    }

    public static int getNextUniqueId(Context context){
        int terminalId = PreferenceUtils.getInt(JsonConstants.TERMINALID, 0, context);
        int token = PreferenceUtils.getInt(JsonConstants.LAST_TX_CODE, 0, context);
        String terminalIDString = String.format("%d", terminalId);
        token = token+1;
        String tokenString = String.format("%06d",token);
        StringBuilder uniqueId = new StringBuilder();
        uniqueId.append(terminalIDString);
        uniqueId.append(tokenString);
        PreferenceUtils.putInt(JsonConstants.LAST_TX_CODE, token, context);
        return Integer.valueOf(uniqueId.toString());
    }

    public static String getBuildVersion(Context context){
        String applicationVersionName  = "";
        try{
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            applicationVersionName = manager.getPackageInfo(packageName, 0).versionName;
        }

        catch (Exception ex){

        }

        return applicationVersionName;
    }

    public static String UniqueIdNumber(String imei){
        String generatedValue = imei;
        generatedValue = generatedValue.substring(generatedValue.length()-11);
        return generatedValue;
    }

//    public static String getDeviceName() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return model;
//        }
//    }
//
//    private static String capitalize(String s) {
//        if (s == null || s.length() == 0) {
//            return "";
//        }
//        char first = s.charAt(0);
//        if (Character.isUpperCase(first)) {
//            return s;
//        } else {
//            return Character.toUpperCase(first) + s.substring(1);
//        }
//    }

    public static boolean chemSimAvailability(Context context){
        boolean available  = false;
        try{
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if(tm.getSimState()!= TelephonyManager.SIM_STATE_ABSENT){
                available = true;
            }
        }

        catch (Exception ex){
            available = false;
        }

        return  available;
    }

    public static Map<String, String> deviceSpecification(Context context){
        Map<String, String> map = null;
        try{

        }
        catch (Exception ex){
            return null;
        }
        return null;
    }

    public static Map<String, String> specification(){
        Map<String, String> map = new TreeMap<>();
        try{
        map.put(JsonConstants.DEVICE_SERIAL, Build.SERIAL);
        map.put(JsonConstants.DEVICE_MODEL, Build.MODEL);
        map.put(JsonConstants.VERSION_CODE, Build.VERSION.RELEASE);
        }

        catch (Exception ex){

        }
        return map;
    }

    public static long freeRamMemorySize(Context context) {
        long availableMegs = 0;
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            availableMegs = mi.availMem / 1048576L;
        }

        catch (Exception ex){
            return 0;
        }

        return availableMegs;
    }

    @SuppressLint("NewApi")
    public static long totalRamMemorySize(Context context) {
        long availableMegs = 0;
        try{
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        availableMegs = mi.totalMem / 1048576L;
        }

        catch (Exception ex){
            return 0;
        }
        return availableMegs;
    }

    public static long getAvailableInternalMemorySize() {
        long availableBlocks = 0;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }
        catch (Exception ex){
           availableBlocks =  0;
        }

        return availableBlocks;
    }

    public static long getTotalInternalMemorySize() {

        long blockSize = 0;
        long totalBlocks = 0;
        try{
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        blockSize = stat.getBlockSize();
        totalBlocks = stat.getBlockCount();
        }
        catch (Exception ex){
            return 0;
        }
        return totalBlocks * blockSize;
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static File getFile(String fileName){
        try {
            return new File(fileName);
        }
        catch (Exception ex){
            return null;
        }
    }

    public static void createFile(Context context, String filename){
        if(IsHaveSdCard()){
            File destDir = new File(SDIR);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            destDir = new File(SDIR+"/data");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            destDir = new File(SDIR+"/source");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        }

        else{

        }

        //CreateFile(SDIR+"/fingerlist.txt");
    }

    public static boolean IsHaveSdCard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) 	{
            return true;
        }else{
            return false;
        }
    }


    public static void CreateFile(String fileName){
        new File(fileName);
    }

    public static void saveDataToFile(String filename, byte[] data){
        File f=new File(filename);
        if(f.exists()){
            f.delete();
        }
        new File(filename);
        try {
            RandomAccessFile randomFile = new RandomAccessFile(filename, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(data);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String LoadDataFromFile(String filename){
        File f=new File(filename);
        if(!f.exists()){
            return null;
        }
        try {
            RandomAccessFile randomFile = new RandomAccessFile(filename, "rw");
            int fileLength = (int) randomFile.length();
            byte[] content=new byte[fileLength];
            randomFile.read(content);
            randomFile.close();
            return new String(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getBatteryPercentage(Context context) {

        int chargingType = -1;
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int isCharging = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

//    public static void insertErrorLog(Map<String, String> map, Context context){
//        offlineDataSource = new OfflineDataSource(context);
//        InsertErrorLog errorLog = new InsertErrorLog();
//        errorLog.execute(map);
//    }
//
//    private static class InsertErrorLog extends AsyncTask<Map<String, String>, Void, Void>{
//
//        @Override
//        protected Void doInBackground(Map<String, String>[] map) {
//            try{
//                 Map errorMap = map[0];
//
//                 String errorMessage = errorMap.get("errormessage").toString();
//                 String errorClass = errorMap.get("errorclass").toString();
//            }
//            catch (Exception ex){
//
//            }
//         return null;
//        }
//    }

    public static Map<String, Integer> batteryStatus(Context context) {
        Map<String, Integer> batteryMapSet = new TreeMap<>();
        try {
            int chargingType = -1;
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int isCharging = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (isCharging == BatteryManager.BATTERY_PLUGGED_AC || isCharging == BatteryManager.BATTERY_PLUGGED_USB) {
                chargingType = 1;
            } else chargingType = 0;

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            float batteryPct = ((level / (float) scale)*100);

            batteryMapSet.put("level", (int)(batteryPct));
            batteryMapSet.put("status", chargingType);
        }

        catch (Exception ex){

        }

        return batteryMapSet;
    }

    public static String getTwentyFourHourTimeFormat(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = calendar.getTime();

        String formattedDate = format.format(date);

        //Log.d("here", "Formatted Date: "+formattedDate);

        return formattedDate;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            return data;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static Bitmap convertBitmapToGrey(Bitmap original)
    {
        int width = original.getWidth();
        int height = original.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, original.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = original.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                }
                else{
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }

}
