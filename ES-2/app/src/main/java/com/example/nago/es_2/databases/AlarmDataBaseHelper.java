package com.example.nago.es_2.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nago.es_2.alarmData;

import java.util.ArrayList;

/**
 * Created by Nago on 2016-11-30.
 */
public class AlarmDataBaseHelper extends SQLiteOpenHelper{

    public static final String DBNAME = "alarm";
    public static final String DBLOCATION = "/data/data/com.example.nago.es_2/databases/";
    private SQLiteDatabase alarmDataBase;

    public AlarmDataBaseHelper(Context context)
    {
        super(context,DBNAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE AlarmData (flag INTEGER, id INTEGER, title TEXT, year INTEGER, month INTEGER, day INTEGER," +
                " hour INTEGER, minute INTEGER, genre TEXT, week INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void openDatabase()
    {
        String dbPath = DBLOCATION + DBNAME;
        if(alarmDataBase == null)
        {
            alarmDataBase = getWritableDatabase();
        }
        if(alarmDataBase != null && alarmDataBase.isOpen())
        {
            return;
        }
        alarmDataBase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase()
    {
        if(alarmDataBase != null)
        {
            alarmDataBase.close();
        }
    }


    public void insertAlarmData(alarmData data)
    {
        openDatabase();

        String flag = data.getFlag() ? "1" : "0";
        String id = String.valueOf(data.getId());
        String day = String.valueOf(data.getDay());
        String genre = data.getGenre();
        String hour = String.valueOf(data.getHour());
        String minute = String.valueOf(data.getMinute());
        String month = String.valueOf(data.getMonth());
        String year = String.valueOf(data.getYear());
        String title = data.getTitle();
        String week = String.valueOf(data.getWeek());

        alarmDataBase.execSQL("INSERT INTO AlarmData (flag, id, title, year, month, day, hour, minute, genre, week)"
                +  " VALUES ('" + flag + "', '" + id + "', '" + title + "', '" + year +"', '" + month + "', '" + day + "', '"
                + hour + "', '" + minute + "', '" + genre + "', '" + week + "');");

        closeDatabase();
    }

    public void deleteAlarmData(int id)
    {
        openDatabase();
        alarmDataBase.execSQL("DELETE FROM AlarmData WHERE id = " + String.valueOf(id) + ";");
        closeDatabase();
    }

    public ArrayList<alarmData> getListData()
    {
        openDatabase();
        alarmData data = null;
        ArrayList<alarmData> alarmDataArrayList = new ArrayList<>();
        openDatabase();
        Cursor cursor = alarmDataBase.rawQuery("SELECT * FROM AlarmData",null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            data = new alarmData(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4)
                    ,cursor.getInt(5),cursor.getInt(6),cursor.getInt(7),cursor.getString(8),(byte)cursor.getInt(9));
            alarmDataArrayList.add(data);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return alarmDataArrayList;
    }
}

