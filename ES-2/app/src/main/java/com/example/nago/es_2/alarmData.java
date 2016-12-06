package com.example.nago.es_2;

/**
 * Created by Administrator on 2016-11-23.
 */

public class alarmData {
    private boolean flag = false;
    private int id = 0;
    private String title = "";
    private String genre = "";
    private int year = 0;
    private int month = 0;
    private int day= 0;
    private int hour = 0;
    private int minute = 0;
    private byte week = (byte)0;

    public boolean getFlag() {return flag;}
    public String getTitle(){return title;}
    public int getYear(){return year;}
    public int getMonth(){return month;}
    public int getDay(){return day;}
    public int getHour(){return hour;}
    public int getMinute(){return minute;}
    public byte getWeek(){return week;}
    public String getGenre(){return genre;}
    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
    public void setFlag(int flag){this.flag = (flag != 0);}
    public void setTitle(String title){this.title =title;}
    public void setYear(int yaer){this.year = year;}
    public void setMonth(int month){this.month = month;}
    public void setDay(int day){this.day = day;}
    public void setHour(int hour){this.hour = hour;}
    public void setMinute(int minute){this.minute = minute;}
    public void setWeek(byte week){this.week = week;}
    public void setGenre(String genre){this.genre = genre;}

    public alarmData(){}
    public alarmData(int flag, int id, String title, int year, int month, int day, int hour, int minute, String genre, byte week)
    {
        this.flag = (flag != 0);
        this.id = id;
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.genre = genre;
        this.week = week;
    }


}

