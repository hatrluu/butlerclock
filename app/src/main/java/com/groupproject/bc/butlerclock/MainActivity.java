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
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AlarmData DB;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    //final static private long ONE_SECOND = 1000;
    //final static private long TWO_SECONDS = ONE_SECOND * 2;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
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
        if (c.moveToFirst()) {
            tmp = c.getLong(0);
        }
        hour = (int)TimeUnit.MILLISECONDS.toHours(tmp);
        minute = (int)TimeUnit.MILLISECONDS.toMinutes(tmp);
        alarms_textView.setText(String.valueOf(minute));
        startAlert(tmp);
    }

    public void startAlert(long time){
        //long time = System.currentTimeMillis()+5000;

        alarmReceiver aReceiver = new alarmReceiver();
        registerReceiver(aReceiver,new IntentFilter("com.groupproject.bc.butlerclock"));

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.groupproject.bc.butlerclock"), 0);

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, time ,alarmIntent);
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
        for(int i = 0; i<3; i++){
            if(c.moveToNext()){
                long time = c.getLong(0);
                output += sdf.format(time) + "\n";
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