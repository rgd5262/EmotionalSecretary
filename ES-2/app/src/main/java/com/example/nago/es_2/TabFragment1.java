package com.example.nago.es_2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nago.es_2.databases.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

public class TabFragment1 extends Fragment {
    private String temperature;
    private String WfKor;
    private String pop;
    private TextView tx;
    private TextView temp;
    private ImageView iv;
    private ImageView weatherKind;
    private ImageView activity;
    private ImageView activityText;
    private WeatherParser weatherParser;
    private ArrayList<WeatherData> weatherDatas;
    private DatabaseHelper mDBHelper;
    private int weatherCode;

    //
    private MediaPlayer mediaPlayer = null;
    //

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        mDBHelper = new DatabaseHelper(getContext());
        weatherParser = new WeatherParser(getContext());

        File database = getContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();
        }

        weatherDatas = weatherParser.getShortWeathers();
        weatherCode = weatherParser.getWeatherCode(mDBHelper);

        setWeatherImage(view,weatherDatas);
        setActivityImage(view);

        return view;
    }

    private void setWeatherImage(View view, ArrayList<WeatherData> weather)
    {
        tx = (TextView) view.findViewById(R.id.rainfall);
        temp=(TextView) view.findViewById(R.id.temperature);
        iv = (ImageView) view.findViewById(R.id.weather);
        weatherKind = (ImageView) view.findViewById(R.id.weatherKind);

        if(weatherCode != 0)
        {
            temperature = weather.get(0).getTemp();
            WfKor = weather.get(0).getWfKor();
            pop = weather.get(0).getPop();

            temp.setText("Temperature : " + temperature + "℃");
            tx.setText(pop+ "%");

            if (WfKor.equals("맑음")) {
                iv.setImageResource(R.drawable.sunny);
                weatherKind.setImageResource(R.drawable.sunnytext);
            }
            else if (WfKor.equals("구름 조금")) {
                iv.setImageResource(R.drawable.sun);
                weatherKind.setImageResource(R.drawable.cloudy);
            }
            else if (WfKor.equals("구름 많음")) {
                iv.setImageResource(R.drawable.cloud);
                weatherKind.setImageResource(R.drawable.cloudy);
            }
            else if (WfKor.equals("흐림")) {
                iv.setImageResource(R.drawable.cloudy2);
                weatherKind.setImageResource(R.drawable.cloudy);
            }
            else if (WfKor.equals("비")) {
                iv.setImageResource(R.drawable.rain);
                weatherKind.setImageResource(R.drawable.rainnytext);
            }
            else if (WfKor.equals("눈/비")) {
                iv.setImageResource(R.drawable.rainsnow);
                weatherKind.setImageResource(R.drawable.rainnytext);
            }
            else {
                iv.setImageResource(R.drawable.snowflake);
                weatherKind.setImageResource(R.drawable.snowytext);
            }
        }
        else
        {
            tx.setText("");
            iv.setImageResource(R.drawable.wifi);
        }
    }

    private void setActivityImage(View view)
    {
        activity = (ImageView)view.findViewById(R.id.activity);
        activityText = (ImageView)view.findViewById(R.id.activityText);

        if(weatherCode != 0) {
            ActivityHandler activityHandler = new ActivityHandler(getContext(),mDBHelper);
            String actName = activityHandler.getActivityName(weatherCode);

            if (actName.equals("새차")) {
                activity.setImageResource(R.drawable.washcar);
                activityText.setImageResource(R.drawable.washcartext);
            } else if (actName.equals("피크닉")) {
                activity.setImageResource(R.drawable.picnic);
                activityText.setImageResource(R.drawable.picnictext);
            } else if (actName.equals("쇼핑")) {
                activity.setImageResource(R.drawable.shopping);
                activityText.setImageResource(R.drawable.shoppingtext);
            } else if (actName.equals("청소")) {
                activity.setImageResource(R.drawable.cleaning);
                activityText.setImageResource(R.drawable.cleaningtext);
            } else if (actName.equals("산책")) {
                activity.setImageResource(R.drawable.walking);
                activityText.setImageResource(R.drawable.walkingtext);
            } else if (actName.equals("조깅")) {
                activity.setImageResource(R.drawable.jog);
                activityText.setImageResource(R.drawable.jogtext);
            } else if (actName.equals("독서")) {
                activity.setImageResource(R.drawable.book);
                activityText.setImageResource(R.drawable.readbooktext);
            } else if (actName.equals("등산")) {
                activity.setImageResource(R.drawable.climb);
                activityText.setImageResource(R.drawable.climbtext);
            } else if (actName.equals("공부")) {
                activity.setImageResource(R.drawable.study);
                activityText.setImageResource(R.drawable.studytext);
            } else if (actName.equals("영화감상")) {
                activity.setImageResource(R.drawable.movie);
                activityText.setImageResource(R.drawable.movietext);
            } else if (actName.equals("음악감상")) {
                activity.setImageResource(R.drawable.music);
                activityText.setImageResource(R.drawable.musictext);
            } else {
                activity.setImageResource(R.drawable.foodpoison);
                activityText.setImageResource(R.drawable.foodpoisontext);
            }
        }
        else
        {
            activityText.setImageResource(R.drawable.networkconnect);
        }
    }
}
