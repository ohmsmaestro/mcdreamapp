package com.stetis.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.stetis.json.JsonConstants;
import com.stetis.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by SnrMngrFinance on 22/08/2017.
 */

public class OfflineDataSource {
//    private DataBase dbHelper = null;
//    private SQLiteDatabase database;
//    private Context context;
//
//    public OfflineDataSource(Context context) {
//        dbHelper = new DataBase(context);
//        this.context = context;
//    }
//
//
//    public int insertCollection(String tableName, ContentValues values) {
//        int result = -1;
//        open();
//        try {
//            //database.beginTransaction();
//            result = (int) database.insert(tableName, null, values);
//            //Log.d("here",result+" First");
//                result = (int) database.insert(JsonConstants.TRANSACTIONS, null, values);
//
//                result = (int) database.insert(JsonConstants.REPORT, null, values);
//                //Log.d("here",result+" Second");
//
//            //database.setTransactionSuccessful();
//            //database.endTransaction();
//        } catch (Exception ex) {
//            //Log.d("here", )
//            ex.printStackTrace();
//            // Log.d("here", "from insertData: "+ex.getMessage());
//        } finally {
//            close();
//        }
//
//        return result;
//    }
//
//    public JSONObject getAllForUpload() {
//
//        int count = 0;
//        JSONObject all = null;
//        boolean contained = false;
//        Cursor cc = null;
//        open();
//        try {
//            String imeiNumber = PreferenceUtils.getString(JsonConstants.IMEI, "", context);
//            int year = PreferenceUtils.getInt(JsonConstants.YEAR, 0, context);
//            all = new JSONObject();
//            //Cursor cc = database.rawQuery("select * from  user_profile where sync = ?", new String[]{"N"});
//            cc = database.rawQuery("select * from transactions order by id asc limit 1", null);
//            JSONArray logList = new JSONArray();
//            if (cc != null) {
//                if (cc.moveToNext()) {
//                    contained = true;
//                    cc.moveToPrevious();
//                }
//
//                while (cc.moveToNext()) {
//                    ++count;
//                    JSONObject logs = new JSONObject();
//                    int id = cc.getInt(cc.getColumnIndex(JsonConstants.ID));
//                    logs.put(JsonConstants.ID, cc.getInt(cc.getColumnIndex(JsonConstants.ID)));
//                    int tag = cc.getInt(cc.getColumnIndex(JsonConstants.TAG));
//                    // System.out.println("Tag: "+tag);
//                    logs.put(JsonConstants.TAG, tag);
//
//                    if (tag == 2 || tag == 1 || tag ==3) {
//                        if (tag == 1) {
//                            String f1 = cc.getString(cc.getColumnIndex(JsonConstants.LEFTTHUMB));
//                            String f2 = cc.getString(cc.getColumnIndex(JsonConstants.RIGHTTHUMB));
//                            logs.put(JsonConstants.LEFTTHUMB, f1);
//                            logs.put(JsonConstants.RIGHTTHUMB, f2);
//                            logs.put(JsonConstants.FIRSTNAME, cc.getString(cc.getColumnIndex(JsonConstants.FIRSTNAME)));
//                            logs.put(JsonConstants.LASTNAME, cc.getString(cc.getColumnIndex(JsonConstants.LASTNAME)));
//                            logs.put(JsonConstants.MIDDLENAME, cc.getString(cc.getColumnIndex(JsonConstants.MIDDLENAME)));
//                            logs.put(JsonConstants.PHONE, cc.getString(cc.getColumnIndex(JsonConstants.PHONE)));
//                            logs.put(JsonConstants.PASSPORT, cc.getString(cc.getColumnIndex(JsonConstants.PASSPORT)));
//                            logs.put(JsonConstants.RECIPIENTID, cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//
//                        }
//
//                        if (tag == 2) {
//                            logs.put(JsonConstants.RECIPIENTID, cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//                            logs.put(JsonConstants.DATEVERIFIED, cc.getString(cc.getColumnIndex(JsonConstants.DATEVERIFIED)));
//                            logs.put(JsonConstants.TIMEVERIFIED, cc.getString(cc.getColumnIndex(JsonConstants.TIMEVERIFIED)));
//                            //Log.d("here", logs.toString());
//                        }
//
//                        if (tag == 3) {
//                            logs.put(JsonConstants.RECIPIENTID, cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//                            logs.put(JsonConstants.DATECREATED, cc.getString(cc.getColumnIndex(JsonConstants.DATECREATED)));
//                            //Log.d("here", logs.toString());
//                        }
//                        logs.put(JsonConstants.CLIENTID, PreferenceUtils.getInt(JsonConstants.CLIENTID, 0, context));
//                        logs.put(JsonConstants.IMEI, PreferenceUtils.getString(JsonConstants.IMEI, "", context));
//                        logs.put(JsonConstants.CREATEDBY, cc.getInt(cc.getColumnIndex(JsonConstants.CREATEDBY)));
//                        logs.put(JsonConstants.DATECREATED, cc.getString(cc.getColumnIndex(JsonConstants.DATECREATED)));
//                        logs.put(JsonConstants.YEAR, PreferenceUtils.getInt(JsonConstants.YEAR, 0, context));
//
//
//                    }
//                    //logs.put(JsonConstants.LOCATION_ID, locationId);
//
//                    //Log.d("here", "From database....................................>>>>>>> with ID: "+id);
//                    ///Log.d("here", "All: "+logs.toString());
//
//                    logList.put(logs);
//                }
//            }
//
//            cc = database.rawQuery("select count(*) from transactions", null);
//            //Log.d("here", "Is Null: "+(cc==null));
//            if (cc != null) {
//                if (cc.moveToNext()) {
//                    count = cc.getInt(0);
//                }
//            }
//
//            // Log.d("here", "Size: "+count+" Contained: "+contained);
//
//            //cc = database.rawQuery("select count(*) from attendance", null);
//            all.put(JsonConstants.TRANSACTIONS, logList);
//            all.put(JsonConstants.COUNT, count);
//            all.put(JsonConstants.CONTAINED, contained);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (cc != null) {
//                cc.close();
//            }
//            close();
//        }
////
////        AppUtils.createFile(context, "superjson.txt");
////        AppUtils.CreateFile("superjson.txt");
////        try {
////            AppUtils.saveDataToFile(AppUtils.SDIR + "/data/superjson.txt", all.toString().getBytes("utf-8"));
////        }
////        catch (Exception ex){
////
////        }
//        // Log.d("here", "After Fetch");
//        //Log.d("here", "All for upload: " + all.toString());
//        return all;
//    }
//
//    private void open() {
//        if (database == null) {
//            database = dbHelper.getReadableDatabase();
//        }
//    }
//
//    private void close() {
//        if (database != null) {
//            database.close();
//            database = null;
//        }
//    }
//
//
//    public void updateAndDeleteAfterSync(String returned) {
//        //Log.d("here", "ID: "+returned);
//        //System.out.println("Returned IDs: "+returned);
//        try {
//            String[] returnedIDS = returned.split("\\|");
//            //String[] ids = null;
//            open();
//
////            String[] ids = null;
////            if (returnedIDS.length > 0) {
////                ids = new String[returnedIDS.length];
////                for (int i = 0; i < returnedIDS.length; i++) {
////                    String id = returnedIDS[i];
////                    ids[i] = id;
////                }
////
////                String args = TextUtils.join(", ", ids);
//            database.execSQL(String.format("delete from attendance where id = (%s);", returned));
//            // }
//        } catch (Exception ex) {
//            //Log.d("here", "from All for upload");
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//    }
//
//
//    public void deleteFingers() {
//        open();
//        try {
//            database.execSQL("delete from finger");
//        } catch (Exception ex) {
//
//        } finally {
//            close();
//        }
//    }
//
//    public boolean deleteYesterRecord() {
//        boolean success = false;
//        open();
//        try {
//
//            database.execSQL("delete from dailyreport where cast((julianday(date('now', 'localtime'))-julianday(clockdate)) as int)>=1");
//            success = true;
//            //Log.d("here", "Successfull");
//        } catch (Exception ex) {
//            success = false;
//            Log.d("here", "deleteYesterRecord");
//            //Log.d("here", "Failed");
//        } finally {
//            close();
//        }
//
//        return success;
//    }
//
//    public int getRecipientCount() {
//        int total = 0;
//        try {
//            open();
//            Cursor cc = database.rawQuery("select count(*) from recipient where sync = 1", null);
//            if (cc.moveToNext()) {
//                total = cc.getInt(0);
//            }
//        } catch (Exception ex) {
//            // Log.d("here", "from getTotalStaff");
//        } finally {
//            close();
//        }
//        return total;
//    }
//
//    public int getTransactionCount() {
//        int total = 0;
//        try {
//            open();
//            Cursor cc = database.rawQuery("select count(*) from transactions", null);
//            if (cc.moveToNext()) {
//                total = cc.getInt(0);
//            }
//        } catch (Exception ex) {
//
//        } finally {
//            close();
//        }
//        return total;
//    }
//
//    public int truncatedatabase() {
//        int total = 0;
//        try {
//            open();
//            //database.beginTransaction();
//            database.delete(JsonConstants.RECIPIENT, null, null);
//            database.delete(JsonConstants.TRANSACTIONS, null, null);
//            database.delete(JsonConstants.REPORT, null, null);
//            database.delete(JsonConstants.PASSPORT, null, null);
//            database.delete(JsonConstants.MAXRECORD, null, null);
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'recipient'");
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'transactions'");
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'passport'");
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'report'");
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'max_record'");
//            //database.setTransactionSuccessful();
//            //database.endTransaction();
//            total = 1;
//        } catch (Exception ex) {
//            // Log.d("here", "from truncateDB");
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//        return total;
//    }
//
//    public boolean insertFingerServerTwo(JSONArray profileArrays) {
//        //Log.d("here", profileArrays.toString());
//        boolean result = true;
//
//        return result;
//    }
//
//    public boolean resetFingerTable() {
//        boolean result = true;
//        try {
//            open();
//            database.execSQL("delete from finger");
//            database.execSQL("delete from SQLITE_SEQUENCE  where name = 'finger'");
//        } catch (Exception ex) {
//            //ex.printStackTrace();
//            //Log.d("here", "from setFinger");
//            result = false;
//        } finally {
//            close();
//        }
//
//        return result;
//    }
//
//    public ArrayList<Report> dailyReport() {
//        ArrayList<Report> reports = new ArrayList<>();
//        try {
//            open();
//            Cursor cc = database.rawQuery("select d.employeeid, name, d.clocktime, case type when 'CLOCKIN' then 1 else 0 end 'status' from dailyreport d join finger f on d.employeeid = f.employeeid group by d.employeeid, type having date(d.clockdate) = date('now', 'localtime') and (type = 'CLOCKIN' or type = 'CLOCKOUT') order by d.employeeid asc", null);
//
//            while (cc.moveToNext()) {
//                Report report = new Report();
//                report.setFullName(cc.getString(1));
//                report.setLoginTime(cc.getString(2));
//                int type = cc.getInt(3);
//                report.setType(type);
//                //Log.d("here", type+"  ");
//                reports.add(report);
//            }
//        } catch (Exception ex) {
//            // Log.d("here", "from DailyReport");
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//        return reports;
//    }
//
//    public boolean checkActivity(String type, String employeeid) {
//        boolean exist = false;
//        try {
//            open();
//
//            //Log.d("here", "Today's date: "+ExtApi.getStringDate());
//            Cursor cc = database.rawQuery("select 1 from dailyreport where clockdate = date('now') and type = ? and employeeid =?", new String[]{type, employeeid});
//            exist = cc.moveToNext();
//            //Log.d("here", "Activity Exist: "+exist);
//        } catch (Exception ex) {
//            //Log.d("here", "CheckActivity");
//            exist = false;
//        } finally {
//            close();
//        }
//
//        return exist;
//    }
//
//    public int insertForSingular(String tableName, ContentValues values) {
//        int result = -1;
//        open();
//        try {
//            //database.beginTransaction();
//            result = (int) database.insert(tableName, null, values);
//
//        } catch (Exception ex) {
//            //Log.d("here", )
//            ex.printStackTrace();
//            //Log.d("here", "from insertData");
//        } finally {
//            close();
//        }
//
//        return result;
//    }
//
//    public int insertEnroll(String tableName, ContentValues values) {
//        int result = -1;
//        open();
//        try {
//            result = (int) database.insert(tableName, null, values);
//        } catch (Exception ex) {
//            Log.d("here",ex.getMessage() );
//            ex.printStackTrace();
//            Log.d("here", "from insertData");
//        } finally {
//            close();
//        }
//
//        return result;
//    }
//
//    public int insertAttendance(String tableName, ContentValues values) {
//        int result = -1;
//        open();
//        try {
//            result = (int) database.insert(tableName, null, values);
//        } catch (Exception ex) {
//            //Log.d("here", )
//            ex.printStackTrace();
//            //Log.d("here", "from insertData");
//        } finally {
//            close();
//        }
//
//        return result;
//    }
//
//    public int updateDatabase(String tableName, ContentValues values, int id) {
//        int result = 1;
//        open();
//        try {
//            result = database.update(tableName, values, "id = ?", new String[]{String.valueOf(id)});
//        } catch (Exception ex) {
//
//        } finally {
//            close();
//        }
//        return result;
//    }
//
//    public boolean checkNFC(String nfc, String employeeid) {
//        boolean exist = false;
//        try {
//            open();
//            Cursor cc = database.rawQuery("select 1 from finger where nfc = ?", new String[]{nfc});
//            if (cc != null) {
//                exist = cc.moveToNext();
//            }
//        } catch (Exception ex) {
//            exist = false;
//        } finally {
//            close();
//        }
//
//        return exist;
//    }
//
//
//    public boolean deleteOffline(int id) {
//        try {
//            open();
//            database.delete(JsonConstants.TRANSACTIONS, "id"
//                    + " = " + id, null);
//
//            return true;
//        } catch (Exception ex) {
//            return false;
//        } finally {
//            close();
//        }
//
//    }
//
//    public ArrayList<Recipient> fetchRecipients(int offset) {
//        open();
//        ArrayList<Recipient> recipientArrayList = null;
//
//        //Cursor cc = db.rawQuery("select id,  full_name, clock_out, phone, case when (strftime('%s', time('now', '3600 seconds'))-strftime('%s', resumption_time))/60>0 then 'Late' else 'Early' end login_status, f1, f2 from "+ JsonConstants.RECIPIENT, null);
//        Cursor cc = database.rawQuery("select id, recipientid, rightthumb, leftthumb, name from " + JsonConstants.RECIPIENT + " where sync = 1 limit 100 offset " + offset, null);
//        try {
//            if (cc != null) {
//                recipientArrayList = new ArrayList<>();
//                while (cc.moveToNext()) {
//                    Recipient recipient = new Recipient();
//                    recipient.setId(cc.getInt(cc.getColumnIndex(JsonConstants.ID)));
//                    recipient.setRecipientId(cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//                    recipient.setLeftThumb(cc.getString(cc.getColumnIndex(JsonConstants.LEFTTHUMB)));
//                    recipient.setRightThumb(cc.getString(cc.getColumnIndex(JsonConstants.RIGHTTHUMB)));
//                    recipient.setName(cc.getString(cc.getColumnIndex(JsonConstants.NAME)));
//
//                    recipientArrayList.add(recipient);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//        return recipientArrayList;
//    }
//
//    public boolean checkIFItemCollected(String recipientId, String year) {
//        boolean exist = false;
//        try {
//            open();
//            Cursor cc = database.rawQuery("select 1 from report where recipientid = ? and year =?", new String[]{recipientId, year});
//            exist = cc.moveToNext();
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//            exist = false;
//        } finally {
//            close();
//        }
//
//        return exist;
//    }
//
//    public String retrieveFarmerPassport(String recipientid) {
//        String passport = null;
//        try {
//            open();
//            Cursor cc = database.rawQuery("select passport from passport where sync = 1 and recipientid = ? limit 1", new String[]{recipientid});
//            if (cc != null && cc.moveToNext()) {
//                cc.moveToPrevious();
//                cc.moveToNext();
//                passport = cc.getString(cc.getColumnIndex(JsonConstants.PASSPORT));
//            } else {
//                passport = null;
//            }
//        } catch (Exception ex) {
//            passport = null;
//        } finally {
//            close();
//        }
//
//        return passport;
//    }
//
//    public JSONObject getRecipientIds(String tableName, String conditionField) {
//        int count = 0;
//        JSONObject all = null;
//        boolean contained = false;
//        Cursor cc = null;
//        open();
//        String query = "select recipientid from " + tableName + " where %s  = 0";
//        query = String.format(query, conditionField);
//        try {
//            all = new JSONObject();
//            cc = database.rawQuery(query, null);
//            JSONArray recipientArray = new JSONArray();
//            if (cc != null) {
//                if (cc.moveToNext()) {
//                    contained = true;
//                    cc.moveToPrevious();
//                }
//                while (cc.moveToNext()) {
//                    ++count;
//                    recipientArray.put(cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//                }
//            }
//            all.put(JsonConstants.RECIPIENTIDS, recipientArray);
//            all.put(JsonConstants.COUNT, count);
//            all.put(JsonConstants.CONTAINED, contained);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (cc != null) {
//                cc.close();
//            }
//            close();
//        }
//        return all;
//    }
//
//    public boolean syncRecipientId(JSONArray recipientidArrays, int maxRow) {
//        boolean result = true;
//        SQLiteStatement stmt = null;
//        String insertRecipientIdIntoRecipient = "insert into recipient (recipientid) values (?)";
//        String insertRecipientIdIntoPassport = "insert into passport (recipientid) values (?)";
//        String insertMaxRecordQuery = "insert or replace into maxrecord (year, maxrecord) values (?, ?)";
//        open();
//        try {
//            database.beginTransaction();
//
//            for (int i = 0; i < recipientidArrays.length(); i++) {
//                String recipientId = recipientidArrays.getString(i);
//                stmt = database.compileStatement(insertRecipientIdIntoRecipient);
//                stmt.bindString(1, recipientId);
//                stmt.execute();
//                stmt.clearBindings();
//
//                stmt = database.compileStatement(insertRecipientIdIntoPassport);
//                stmt.bindString(1, recipientId);
//                stmt.execute();
//                stmt.clearBindings();
//            }
//
//            stmt = database.compileStatement(insertMaxRecordQuery);
//            stmt.bindLong(1, 1);
//            stmt.bindLong(2, maxRow);
//            stmt.execute();
//            stmt.clearBindings();
//
//            //database.endTransaction();
//            database.setTransactionSuccessful();
//            database.endTransaction();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            result = false;
//        } finally {
//            close();
//        }
//        return result;
//    }
//
//    public int getMaxRowForTerminal() {
//        int maxRowId = 0;
//        try {
//            open();
//
//            //Log.d("here", "Today's date: "+ExtApi.getStringDate());
//            Cursor cc = database.rawQuery("select maxrecord from maxrecord", null);
//            if(cc!=null){
//                if(cc.moveToNext()){
//                    maxRowId = cc.getInt(cc.getColumnIndex(JsonConstants.MAXRECORD));
//                }
//            }
//            //Log.d("here", "Activity Exist: "+exist);
//        } catch (Exception ex) {
//            //Log.d("here", "CheckActivity");
//
//        } finally {
//            close();
//        }
//
//        return maxRowId;
//    }
//
//    public void updateBiometric(String name, String recipientId, String rightthumb, String leftthumb, String passport) {
//        try {
//            open();
//            database.beginTransaction();
//            //String updateFarmer = String.format("update farmers set name = ?, rightthumb = ?, leftthumb = ? %s", nfc==null?"":", nfc = ?");
//            database.execSQL("update recipient set name = ?, rightthumb = ?, leftthumb = ?, sync = 1 where recipientid = ?", new String[]{name, rightthumb, leftthumb, recipientId});
//            database.execSQL("update passport set passport = ?, sync = 1 where recipientid = ?", new String[]{passport, recipientId});
//            database.setTransactionSuccessful();
//            database.endTransaction();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//
//    }
//
//    public ArrayList<Recipient> getSyncedRecipients() {
//
//        ArrayList<Recipient> recipientArrayList = null;
//        open();
//        //Cursor cc = db.rawQuery("select id,  full_name, clock_out, phone, case when (strftime('%s', time('now', '3600 seconds'))-strftime('%s', resumption_time))/60>0 then 'Late' else 'Early' end login_status, f1, f2 from "+ JsonConstants.CANDIDATEPROFILE, null);
//        Cursor cc = database.rawQuery("select recipientid, name from recipient where sync = 1", null);
//        try {
//            if (cc != null) {
//                recipientArrayList = new ArrayList<>();
//                while (cc.moveToNext()) {
//                    Recipient recipient = new Recipient();
//                    recipient.setName(cc.getString(cc.getColumnIndex(JsonConstants.NAME)));
//                    recipient.setRecipientId(cc.getString(cc.getColumnIndex(JsonConstants.RECIPIENTID)));
//                    recipientArrayList.add(recipient);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            close();
//        }
//        return recipientArrayList;
//    }
}
