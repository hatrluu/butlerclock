package com.groupproject.bc.butlerclock;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TimePicker;

public class addAlarm extends FragmentActivity{
    private TimePicker timePicker;
    private AlarmData DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_xml);

        timePicker = findViewById(R.id.timePicker);
    }

    public void setTime(View view){
        SQLiteDatabase db = DB.getWritableDatabase();
        Intent intent = new Intent(this, MainActivity.class);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String aa = " am";
        if(hour>=12){
            aa = " pm";
            if(hour>12) hour -= 12;
        }else if(hour==0){
            hour += 12;
        }
        String time = Integer.toString(hour)+":"+Integer.toString(minute)+aa;
        ContentValues values = new ContentValues();
        values.put("time", time);
        db.insert("alarmtime", null, values);
        /*
        intent.putExtra("hour",hour);
        intent.putExtra("minute",minute);
        */
        startActivity(intent);
    }
}
