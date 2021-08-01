package com.stetis.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtApi {

	public static String getStringDate() {
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static Map<String, String> getCurrentDate() {
		Map<String, String> map = new HashMap<>();
		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = formatter.format(currentTime);

		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		String dateString2 = formatter2.format(currentTime);
		map.put("t1", dateString);
		map.put("t2", dateString2);
		return map;
	}

	public static byte[] Base64ToBytes(String txt){
		return Base64.decode(txt, Base64.DEFAULT);
	}

	public static String BytesToBase64(byte[] ba, int size){
		return Base64.encodeToString(ba, Base64.DEFAULT);
	}


	public static String getDeviceID(Context context){
		String imei =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		//String IMEI =android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI)
		return imei;
	}

	public static boolean IsSupportNFC(Context context){
		if (NfcAdapter.getDefaultAdapter(context) == null) {
			return false;
		}
		return true;
	}

	public static String getTimeTwentyFourHourFormat(Time t){
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String time = timeFormat.format(t);

		return time;
	}
	public static byte[] LoadDataFromFile(String filename){
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
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean IsFileExists(String filename){
		File f=new File(filename);
		if(f.exists()){
			return true;
		}
		return false;
	}

	public static void DeleteFile(String filename){
		File f=new File(filename);
		if(f.exists()){
			f.delete();
		}
	}

	public static Bitmap LoadBitmap(Resources res, int id){
		//Resources res = getResources();
		return BitmapFactory.decodeResource(res, id);
	}

	public static boolean bytesEquals(byte[] src,int spos,byte[] dst,int dpos,int size){
		for(int i=0;i<size;i++){
			if(src[spos+i]!=dst[dpos+i])
				return false;
		}
		return true;
	}

}
