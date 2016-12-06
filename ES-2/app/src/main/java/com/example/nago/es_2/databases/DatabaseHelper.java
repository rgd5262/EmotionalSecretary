package com.example.nago.es_2.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Nago on 2016-11-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "sqlite";
    public static final String DBLOCATION = "/data/data/com.example.nago.es_2/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context)
    {
        super(context,DBNAME,null,1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void openDatabase()
    {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen())
        {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READONLY);
    }

    public void closeDatabase()
    {
        if(mDatabase != null)
        {
            mDatabase.close();
        }
    }

    public boolean copyDatabase(Context context)
    {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buff)) > 0)
            {
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            Log.e("DataBaseHelper","DB copied");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public int getWeatherCode(String pop, String temp, String Wfkor)
    {
        int weatherCode;
        int temperature;
        int rainfallPercent;
        Cursor cursor;

        openDatabase();

        if(Double.parseDouble(temp) <= 10)
        {
            temperature = 10;
        }
        else if(Double.parseDouble(temp) <= 20)
        {
            temperature = 20;
        }
        else
        {
            temperature = 30;
        }

        if(Integer.parseInt(pop) <= 60)
        {
            rainfallPercent = 60;
        }
        else if(Integer.parseInt(pop) <= 70)
        {
            rainfallPercent = 70;
        }
        else
        {
            rainfallPercent = 80;
        }

        if(Wfkor.equals("맑음")||Wfkor.equals("구름 조금"))
        {
            cursor = mDatabase.rawQuery("SELECT id FROM Weather WHERE temp=" + "'"+String.valueOf(temperature)+"' AND kind='맑음'",null);
            cursor.moveToFirst();
            weatherCode = cursor.getInt(0);
        }
        else if(Wfkor.equals("흐림")||Wfkor.equals("구름 많음"))
        {
            cursor = mDatabase.rawQuery("SELECT id FROM Weather WHERE temp=" + "'"+String.valueOf(temperature)+"' AND kind='흐림'",null);
            cursor.moveToFirst();
            weatherCode = cursor.getInt(0);
        }
        else
        {
            cursor = mDatabase.rawQuery("SELECT id FROM Weather WHERE pop=" + "'"+String.valueOf(rainfallPercent)+"' AND kind='비'",null);
            cursor.moveToFirst();
            weatherCode = cursor.getInt(0);
        }

        cursor.close();
        closeDatabase();

        return weatherCode;
    }

    public ArrayList<String> getMusicName(int weatherCode)
    {
        ArrayList<String> musicList = new ArrayList<String>();

        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT name FROM Music WHERE id=" + "'"+String.valueOf(weatherCode)+"'",null);
        cursor.moveToFirst();

        do
        {
            musicList.add(cursor.getString(0));
        }while(cursor.moveToNext());

        cursor.close();
        closeDatabase();

        return musicList;
    }

    public ArrayList<String> getMusicName(String genre)
    {
        ArrayList<String> musicList = new ArrayList<String>();

        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT name FROM Music WHERE genre =" + "'"+genre+"'",null);
        cursor.moveToFirst();
        do
        {
            musicList.add(cursor.getString(0));
        }while(cursor.moveToNext());

        cursor.close();
        closeDatabase();

        return musicList;
    }

    public ArrayList<String> getMusicName()
    {
        ArrayList<String> musicList = new ArrayList<String>();

        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT name FROM Music",null);
        cursor.moveToFirst();

        do
        {
            musicList.add(cursor.getString(0));
        }while(cursor.moveToNext());

        cursor.close();
        closeDatabase();

        return musicList;
    }

    public ArrayList<String> getActivityName(int weatherCode)
    {
        ArrayList<String> activityList = new ArrayList<String>();

        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT name FROM Activity WHERE id=" + "'"+String.valueOf(weatherCode)+"'",null);
        cursor.moveToFirst();

        do
        {
            activityList.add(cursor.getString(0));
        }while(cursor.moveToNext());

        cursor.close();
        closeDatabase();

        return activityList;
    }

}
