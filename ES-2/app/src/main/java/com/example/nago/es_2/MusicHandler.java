package com.example.nago.es_2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.nago.es_2.databases.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nago on 2016-11-17.
 */
public class MusicHandler {
    private ArrayList<String> musicList;
    private Context mContext;
    private DatabaseHelper mDBHelper;

    private MediaPlayer mediaPlayer;

    public MusicHandler(Context context, DatabaseHelper mDBHelper)
    {
        this.mContext = context;
        this.mDBHelper = mDBHelper;
    }

    private String getMusicName(String genre, int weatherCode)
    {
        if(isNetWorkConnected()==true)
        {
            if(genre.equals("Random")==true)
            {
                musicList = mDBHelper.getMusicName(weatherCode);
            }
            else
            {
                musicList = mDBHelper.getMusicName(genre);
            }
        }
        else
        {
            musicList = mDBHelper.getMusicName();
        }

        int listSize = musicList.size();
        Random random = new Random();
        String musicName = musicList.get(random.nextInt(listSize));

        return musicName;
    }

    @Nullable
    private String getFilePath(String genre, int weatherCode)
    {
        String FileName = getMusicName(genre,weatherCode);
        String FolderPath = Environment.getExternalStorageDirectory()+"/ES/";
        File file = new File(FolderPath);

        if(!file.exists())
        {
            file.mkdir();
        }

        String musicPath = FolderPath + FileName + ".mp3";
        File musicFile = new File(musicPath);

        if (!musicFile.exists()) {
            return null;
        }

        return musicPath;
    }

    public MediaPlayer startMediaPlayer(String genre, int weatherCode)
    {
        String PATH_TO_FILE = getFilePath(genre,weatherCode);

        if(PATH_TO_FILE == null)
        {
            Toast.makeText(mContext,PATH_TO_FILE + " is not exist",Toast.LENGTH_LONG);
            mediaPlayer = null;
        }
        else
        {
            mediaPlayer = new MediaPlayer();
            try
            {
                mediaPlayer.setDataSource(PATH_TO_FILE);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                mediaPlayer = null;
            }
        }

        return mediaPlayer;
    }

    private boolean isNetWorkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
