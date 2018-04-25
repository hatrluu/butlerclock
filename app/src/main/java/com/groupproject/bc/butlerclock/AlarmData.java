package com.groupproject.bc.butlerclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmData  extends SQLiteOpenHelper{
    private static final String DB_NAME = "alarm.db";
    private static final int DB_VERSION = 5;

    //Database table name and columns
    private static final String TABLE_NAME = "alarmtime";
    //Save time as millisecond
    private static final String TIME = "time";

    public AlarmData(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //crate a new table
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                TIME + " int UNIQUE" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
