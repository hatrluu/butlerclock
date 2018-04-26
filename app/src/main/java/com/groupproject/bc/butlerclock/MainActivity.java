package com.groupproject.bc.butlerclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AlarmData DB;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    //final static private long ONE_SECOND = 1000;
    //final static private long TWO_SECONDS = ONE_SECOND * 2;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    Calendar calendar = Calendar.getInstance();
    int hour, minute;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    Button clear,test;
    long tmp = 0, curr = SystemClock.elapsedRealtime();
    TextView alarms_textView, oncoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new AlarmData(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oncoming = findViewById(R.id.textView);
        alarms_textView = findViewById(R.id.alarms_textView);
        clear = findViewById(R.id.clear);
        test = findViewById(R.id.test);

        Cursor c = getData();
        oncoming.setText(showData(c));
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = findViewById(R.id.clock);
                                long date = System.currentTimeMillis();
                                String dateString = sdf.format(date);
                                textView.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        c = getData();
        if (c.moveToNext()) {
            hour = c.getInt(0);
            minute = c.getInt(1);
        }
        //hour = (int)TimeUnit.MILLISECONDS.toHours(tmp);
        //minute = (int)TimeUnit.MILLISECONDS.toMinutes(tmp);
        //alarms_textView.setText(String.valueOf(minute));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        startAlert();
    }

    public void startAlert(){
        //long time = System.currentTimeMillis()+5000;

        alarmReceiver aReceiver = new alarmReceiver();
        registerReceiver(aReceiver,new IntentFilter("com.groupproject.bc.butlerclock"));

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.groupproject.bc.butlerclock"), 0);

        alarmMgr.setInexactRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        Toast.makeText(this, "Test started", Toast.LENGTH_SHORT).show();
    }
/*
    private void startAlert() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(br, new IntentFilter("com.groupproject.bc.butlerclock"));
        pi = PendingIntent.getBroadcast(this,0,new Intent("com.groupproject.bc.butlerclock"),0);
        am = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }
    */

    public String showData(Cursor c) {
        String output = "";
        String aa = " am";
        //was going to do something with this....
        int count = 3;
        while(count != 0){
            if(c.moveToNext()){
                hour = c.getInt(0);
                minute = c.getInt(1);
                if (hour >= 12) {
                    aa = " pm";
                    if (hour > 12) hour -= 12;
                } else if (hour == 0) {
                    hour += 12;
                }
                count--;
                output += hour + ":" + minute + " " + aa + "\n";
            }else{
                count--;
            }
        }
        return output;
    }

    public void dropData(View view){
        SQLiteDatabase db = DB.getReadableDatabase();
        try{
            db.execSQL("DELETE FROM alarmtime");
            Toast.makeText(this,"Data dropped",Toast.LENGTH_SHORT).show();
        }catch(SQLException e){
            Toast.makeText(this,"No data",Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = DB.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM alarmtime",null);
        return c;
    }

    public void call_addAlarm(View view){
        Intent intent = new Intent(this,addAlarm.class );
        startActivity(intent);
    }
}