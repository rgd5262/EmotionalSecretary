package com.example.nago.es_2;

import android.content.Context;

import com.example.nago.es_2.databases.DatabaseHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nago on 2016-11-17.
 */
public class ActivityHandler {
    private ArrayList<String> activityList;
    private Context mContext;
    private DatabaseHelper mDBHelper;

    public ActivityHandler(Context context, DatabaseHelper mDBHelper)
    {
        this.mContext = context;
        this.mDBHelper = mDBHelper;
    }

    public String getActivityName(int weatherCode)
    {
        activityList = mDBHelper.getActivityName(weatherCode);

        int listSize = activityList.size();
        Random random = new Random();
        String activityName = activityList.get(random.nextInt(listSize));

        return activityName;
    }

}
