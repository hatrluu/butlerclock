package com.groupproject.bc.butlerclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class alarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
    }
}
