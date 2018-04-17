package com.groupproject.bc.butlerclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    final static private long ONE_SECOND = 1000;
    final static private long TWO_SECONDS = ONE_SECOND * 2;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    Button button;
    String hour, minute;
    TextView alarms_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        alarms_textView = findViewById(R.id.alarms_textView);
        //Display the clock
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
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
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

        //Test alarm button
        startAlert();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TWO_SECONDS, pi );
                Toasty();
            }
        });

        //Get data from addAlarm.java
        Intent intent = getIntent();
        hour = intent.getStringExtra(addAlarm.EXTRA_message);
        minute = intent.getStringExtra(addAlarm.EXTRA_message);

        alarms_textView.setText(hour+":"+minute);
    }

    public void call_addAlarm(View view){
        Intent intent = new Intent(this,addAlarm.class );
        startActivity(intent);
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

}