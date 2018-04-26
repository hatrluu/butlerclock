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
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
    Calendar calendar = Calendar.getInstance();
    int hour, minute;
    Button clear;
    TextView alarms_textView, oncoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new AlarmData(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oncoming = findViewById(R.id.textView);
        alarms_textView = findViewById(R.id.alarms_textView);
        clear = findViewById(R.id.clear);

        Cursor c = getData();
        oncoming.setText(showData(c));

        displayClock();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        Cursor c = getData();
        if (c.getCount() > 0) {
            if (c.moveToNext()) {
                hour = c.getInt(0);
                minute = c.getInt(1);
            }
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            startAlert();
        }
    }

    private void displayClock(){
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
    }
    public void startAlert(){
        alarmReceiver aReceiver = new alarmReceiver();
        registerReceiver(aReceiver,new IntentFilter("com.groupproject.bc.butlerclock"));

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.groupproject.bc.butlerclock"), 0);

        //setInexact does not work as well, testing with setExact
        //alarmMgr.setInexactRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        //To repeat, we need to update the SQL database (e.g. add 1 day in millisecond to have the alarm repeat once a day)
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmIntent);
        Toast.makeText(this, "Test started", Toast.LENGTH_SHORT).show();
    }

    private String showData(Cursor c) {
        String output = "";
        String aa = " AM";
        String minutes;
        //was going to do something with this....
        //?????
        int count = 3;
        while(count != 0){
            if(c.moveToNext()){
                hour = c.getInt(0);
                minute = c.getInt(1);
                if (hour >= 12) {
                    aa = " PM";
                    if (hour > 12) hour -= 12;
                } else if (hour == 0) {
                    hour += 12;
                }
                if (minute < 10){
                    minutes = "0"+minute;
                }
                else{
                    minutes = ""+minute;
                }
                count--;
                output += hour + ":" + minutes + " " + aa + "\n";
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
        if(alarmMgr!=null){
            alarmMgr.cancel(alarmIntent);
        }
        Cursor c = getData();
        oncoming.setText(showData(c));
    }

    private Cursor getData() {
        SQLiteDatabase db = DB.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM alarmtime",null);
        return c;
    }

    public void call_addAlarm(View view){
        Intent intent = new Intent(this,addAlarm.class );
        startActivity(intent);
    }
}