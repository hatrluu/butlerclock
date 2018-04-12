package com.groupproject.bc.butlerclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TimePicker;

public class addAlarm extends FragmentActivity{
    private TimePicker timePicker;

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
        intent.putExtra(String.valueOf(hour),hour);
        intent.putExtra(String.valueOf(minute),minute);
        startActivity(intent);
    }
}
