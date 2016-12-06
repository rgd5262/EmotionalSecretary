package com.example.nago.es_2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nago.es_2.databases.DatabaseHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Administrator on 2016-12-01.
 */

public class AlarmView extends Activity
{
    private MediaPlayer mediaPlayer;
    private DatabaseHelper mDBHelper;
    private WeatherParser weatherParser;
    private MusicHandler musicHandler;
    private int weatherCode;
    private String genre;
    private boolean alarmType;
    private boolean[] day_list = new boolean[7];


    public AlarmView()
    {
        Arrays.fill(day_list,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent i = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);
        alarmType = i.getBooleanExtra("alarmType",false);
        day_list = i.getBooleanArrayExtra("week");
        genre = i.getStringExtra("genre");

        if((checkDay() == true && alarmType == false) || alarmType == true)
        {
            mediaPlayer = setAlarmMusic();
            mediaPlayer.start();

            TextView titleName = (TextView) findViewById(R.id.titleName);

            titleName.setText(i.getStringExtra("title"));
            Button cancel_btn = (Button)findViewById(R.id.cancel_btn);

            //cancel button
            cancel_btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    mediaPlayer.stop();
                    finish();

                }
            });
        }
        else {finish();}
    }

    public boolean checkDay()
    {
        Calendar oCalendar = Calendar.getInstance( );

        switch (oCalendar.get(Calendar.DAY_OF_WEEK))
        {
            case 1: //일
                if(day_list[6] == true) return true;
                else return false;
            case 2: // 월
                if(day_list[0] == true) return true;
                else return false;
            case 3: // 화
                if(day_list[1] == true) return true;
                else return false;
            case 4: // 수
                if(day_list[2] == true) return true;
                else return false;
            case 5: // 목
                if(day_list[3] == true) return true;
                else return false;
            case 6: // 금
                if(day_list[4] == true) return true;
                else return false;
            case 7: // 토
                if(day_list[5] == true) return true;
                else return false;
        }
        return false;
    }

    private MediaPlayer setAlarmMusic()
    {
        MediaPlayer tempPlayer;

        mDBHelper = new DatabaseHelper(this);
        weatherParser = new WeatherParser(this);

        File database = this.getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();
        }

        weatherCode = weatherParser.getWeatherCode(mDBHelper);
        musicHandler = new MusicHandler(this,mDBHelper);
        tempPlayer = musicHandler.startMediaPlayer(genre,weatherCode);

        return tempPlayer;
    }

}