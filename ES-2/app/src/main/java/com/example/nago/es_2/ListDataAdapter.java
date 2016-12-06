package com.example.nago.es_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nago on 2016-11-29.
 */
public class ListDataAdapter extends BaseAdapter{

    private ArrayList<alarmData> listViewItemList = null;

    public ListDataAdapter(ArrayList<alarmData> listViewItemList){

        this.listViewItemList = listViewItemList;
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) { return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView  = inflater.inflate(R.layout.alarm_view, parent, false);
        }

        TextView Title = (TextView)convertView.findViewById(R.id.ad_name);
        TextView week = (TextView)convertView.findViewById(R.id.ad_week);
        TextView time = (TextView)convertView.findViewById(R.id.ad_time);
        TextView Genre = (TextView)convertView.findViewById(R.id.ad_genre);

        alarmData ad = listViewItemList.get(position);

        String title = ad.getTitle();
        String genre = ad.getGenre();
        String timeText = "";

        if(ad.getHour()>12)
        {
            String hour = String.valueOf(ad.getHour()-12);
            String minute = String.valueOf(ad.getMinute());
            timeText = "오후 " + hour + "시 " + minute + "분";
        }
        else
        {
            String hour = String.valueOf(ad.getHour());
            String minute = String.valueOf(ad.getMinute());
            timeText = "오전 " + hour + "시 " + minute + "분";
        }

        Title.setText(title);
        Genre.setText(genre);
        time.setText(timeText);

        if(ad.getFlag() == false)
        {
            String repeatDay = getRepeatDay(ad.getWeek());
            week.setText(repeatDay);
        }
        else
        {
            int year = ad.getYear();
            int month = ad.getMonth();
            int day = ad.getDay();
            String year_month_day = getYearMonthDay(year,month,day);
            week.setText(year_month_day);
        }

        return convertView;
    }

    public void addItem(alarmData ad) {
        listViewItemList.add(ad);
    }

    private String getYearMonthDay(int year, int month, int day)
    {
        String result = "";

        String s_year = String.valueOf(year);
        String s_month = String.valueOf(month+1);
        String s_day = String.valueOf(day);

        result = s_year + "년 " + s_month + "월 " + s_day + "일";

        return result;
    }
    private String getRepeatDay(byte week)
    {
        String day = "";

        byte monday    =  (byte)64;
        byte tuesday   =  (byte)32;
        byte wednesday =  (byte)16;
        byte thursday  =  (byte)8;
        byte friday    =  (byte)4;
        byte saturday  =  (byte)2;
        byte sunday    =  (byte)1;

        if((week & monday)==monday) day += "월 ";
        if((week & tuesday)==tuesday) day += "화 ";
        if((week & wednesday)==wednesday) day += "수 ";
        if((week & thursday)==thursday) day += "목 ";
        if((week & friday)==friday) day += "금 ";
        if((week & saturday)==saturday) day += "토 ";
        if((week & sunday)==sunday) day += "일 ";


        return day;
    }
}
