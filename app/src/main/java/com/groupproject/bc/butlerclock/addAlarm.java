package com.groupproject.bc.butlerclock;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TimePicker;
import java.util.concurrent.TimeUnit;

public class addAlarm extends FragmentActivity{
    private TimePicker timePicker;
    public static String EXTRA_message = "com.groupproject.bc.butlerclock";
    private AlarmData DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_xml);
        timePicker = findViewById(R.id.timePicker);
        DB = new AlarmData(this);
    }

    public void setTime(View view){
        SQLiteDatabase db = DB.getWritableDatabase();
        Intent intent = new Intent(this, MainActivity.class);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        ContentValues values = new ContentValues();
        values.put("hour", hour);
        values.put("min", minute);
        db.insert("alarmtime", null, values);
        startActivity(intent);
   }
}
