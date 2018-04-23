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
        long hour = TimeUnit.HOURS.toMillis(timePicker.getHour()+5);
        long minute = TimeUnit.MINUTES.toMillis(timePicker.getMinute());

        /*
        String aa = " am";
        if(hour>=12){
            aa = " pm";
            if(hour>12) hour -= 12;
        }else if(hour==0){
            hour += 12;
        }
        */
        long time = hour+minute;
        ContentValues values = new ContentValues();
        values.put("time", time);
        db.insert("alarmtime", null, values);
        startActivity(intent);
    }
}
