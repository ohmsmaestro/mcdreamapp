package com.stetis.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stetis.json.JsonConstants;


/**
 * Created by SnrMngrFinance on 27/07/2017.
 */

public class DataBase extends SQLiteOpenHelper {
    private static Context context;
    private static final String DATABASE_NAME = "samsgiver.db";
    private static final int DATABASE_VERSION = 1;
    private static StringBuilder transactions = new StringBuilder();
    private static StringBuilder recipients = new StringBuilder();
    private static StringBuilder dailyReport = new StringBuilder();
    private static StringBuilder passport = new StringBuilder();
    private static StringBuilder maxrow = new StringBuilder();
    static {

        recipients.append("CREATE TABLE ")
                .append(JsonConstants.RECIPIENT +" (")
                .append(JsonConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(JsonConstants.NAME+ " TEXT, ")
                .append(JsonConstants.RECIPIENTID + " TEXT, ")
                .append(JsonConstants.FIRSTNAME + " TEXT, ")
                .append(JsonConstants.LASTNAME + " TEXT, ")
                .append(JsonConstants.MIDDLENAME + " TEXT, ")
                .append(JsonConstants.SYNC + " INT DEFAULT 0,")
                .append(JsonConstants.RIGHTTHUMB + " TEXT, ")
                .append(JsonConstants.LEFTTHUMB + " TEXT, ")
                .append(JsonConstants.YEAR + " TEXT)");

        passport.append("CREATE TABLE ")
                .append(JsonConstants.PASSPORT + " (")
                .append(JsonConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(JsonConstants.RECIPIENTID + " TEXT, ")
                .append(JsonConstants.PASSPORT + " TEXT, ")
                .append(JsonConstants.SYNC + " INT DEFAULT 0)");

        transactions.append("CREATE TABLE ")
                .append(JsonConstants.TRANSACTIONS +" (")
                .append(JsonConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(JsonConstants.NAME+ " TEXT, ")
                .append(JsonConstants.FIRSTNAME + " TEXT, ")
                .append(JsonConstants.LASTNAME + " TEXT, ")
                .append(JsonConstants.MIDDLENAME + " TEXT, ")
                .append(JsonConstants.RIGHTTHUMB + " TEXT, ")
                .append(JsonConstants.LEFTTHUMB + " TEXT, ")
                .append(JsonConstants.PASSPORT + " TEXT, ")
                .append(JsonConstants.CREATEDBY + " INT, ")
                .append(JsonConstants.RECIPIENTID + " TEXT, ")
                .append(JsonConstants.DATECREATED + " TEXT, ")
                .append(JsonConstants.YEAR + " INT, ")
                .append(JsonConstants.PHONE + " TEXT, ")
                .append(JsonConstants.TAG +" INT, ")
                .append(JsonConstants.DATEVERIFIED +" TEXT, ")
                .append(JsonConstants.YEARVERIFIED +" TEXT, ")
                .append(JsonConstants.YEARACCREDITED + " TEXT, ")
                .append(JsonConstants.TIMEVERIFIED + " TEXT)");

        maxrow.append("CREATE TABLE ")
                .append(JsonConstants.MAXRECORD +" (")
                .append(JsonConstants.YEAR + " INT PRIMARY KEY, ")
                .append(JsonConstants.MAXRECORD + " INT)");

        dailyReport.append("CREATE TABLE ")
                .append(JsonConstants.REPORT + "(")
                .append(JsonConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(JsonConstants.NAME+ " TEXT, ")
                .append(JsonConstants.FIRSTNAME + " TEXT, ")
                .append(JsonConstants.LASTNAME + " TEXT, ")
                .append(JsonConstants.MIDDLENAME + " TEXT, ")
                .append(JsonConstants.RIGHTTHUMB + " TEXT, ")
                .append(JsonConstants.LEFTTHUMB + " TEXT, ")
                .append(JsonConstants.PASSPORT + " TEXT, ")
                .append(JsonConstants.CREATEDBY + " INT, ")
                .append(JsonConstants.RECIPIENTID + " TEXT, ")
                .append(JsonConstants.DATECREATED + " TEXT, ")
                .append(JsonConstants.YEAR + " INT, ")
                .append(JsonConstants.PHONE + " TEXT, ")
                .append(JsonConstants.TAG +" INT, ")
                .append(JsonConstants.DATEVERIFIED +" TEXT, ")
                .append(JsonConstants.YEARVERIFIED +" TEXT, ")
                .append(JsonConstants.YEARACCREDITED + " TEXT, ")
                .append(JsonConstants.TIMEVERIFIED + " TEXT)");
       // userIndex.append("create index on "+JsonConstants.RECIPIENT+"(id)");
    }
    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DATABASE_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d("here", "Upgrdadeoooooooooooooooooo");
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        try {
            if (oldVersion < 1) {
                db.execSQL(recipients.toString());
                db.execSQL(transactions.toString());
                db.execSQL(dailyReport.toString());
                db.execSQL(passport.toString());
                db.execSQL(maxrow.toString());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

