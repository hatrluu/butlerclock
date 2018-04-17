package com.groupproject.bc.butlerclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class addAlarm extends FragmentActivity{
    private TimePicker timePicker;
    public static String EXTRA_message = "com.groupproject.bc.butlerclock";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_xml);

        timePicker = findViewById(R.id.timePicker);

    }

    public void setTime(View view){
        Intent intent = new Intent(this, MainActivity.class);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        intent.putExtra(EXTRA_message,String.valueOf(hour));
        intent.putExtra(EXTRA_message,String.valueOf(minute));
        startActivity(intent);
    }
}
