package com.groupproject.bc.butlerclock;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private AlarmData DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new AlarmData(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView oncoming = findViewById(R.id.textView);
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
    }

    private String showData(Cursor c) {
        String output = "";
        for(int i = 0; i<3; i++){
            if(c.moveToNext()){
                output += c.getString(0) + "\n";
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