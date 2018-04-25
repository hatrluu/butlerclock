package com.groupproject.bc.butlerclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AlarmData DB;
    final static private long ONE_SECOND = 1000;
    final static private long TWO_SECONDS = ONE_SECOND * 2;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    Button button;
    String hour, minute;
    long tmp = 0, curr = SystemClock.elapsedRealtime();
    TextView alarms_textView, oncoming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new AlarmData(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oncoming = findViewById(R.id.textView);
        button = findViewById(R.id.button);
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
            tmp = c.getLong(0);
        }

        //Test alarm button
        startAlert();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, curr + tmp - ((curr%TimeUnit.HOURS.toMillis(24))+TimeUnit.HOURS.toMillis(8)), pi );
                Toasty();
            }
        });

        //Get data from addAlarm.java

    }

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

    public void Toasty(){
        Toast.makeText(this, "Alarm with start in " + TWO_SECONDS/1000 + " seconds.", Toast.LENGTH_SHORT).show();
    }
    private String showData(Cursor c) {
        String output = "";
        for(int i = 0; i<3; i++){
            if(c.moveToNext()){
                long time = c.getLong(0);
                //output+= String.valueOf(time);
                output += sdf.format(time) + "\n";
            }
        }
        return output;
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