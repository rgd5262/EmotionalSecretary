package com.example.nago.es_2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.nago.es_2.databases.AlarmDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class TabFragment2 extends Fragment
{
    static final int REQ_ADD_CONTACT = 1;
    private AlarmDataBaseHelper aDBHelper;
    private ImageButton scheduleAdd = null;
    private ImageButton scheduleDel = null;
    private ListDataAdapter adapter = null;
    private ArrayList<alarmData> alarmDataArrayList = null;

    //alarm
    private AlarmManager mManager = null;
    private NotificationManager mNotification= null;
    private Calendar calendar = null;
    private int penCnt = 0;
    private alarmData ad = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, null);
        aDBHelper = new AlarmDataBaseHelper(getContext());
        alarmDataArrayList = aDBHelper.getListData();
        adapter = new ListDataAdapter(alarmDataArrayList);
        adapter.notifyDataSetChanged();
        sharedPreferences = getContext().getSharedPreferences("penCnt", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        penCnt = sharedPreferences.getInt("penCntValue",0);
        Log.e("penCnt", String.valueOf(penCnt));
        final ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        //delete btn
        scheduleDel = (ImageButton) view.findViewById(R.id.Delschedule);
        scheduleDel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        int delId = alarmDataArrayList.get(i).getId();
                        Log.e("delId", String.valueOf(delId));
                        aDBHelper.deleteAlarmData(delId);
                        alarmDataArrayList.remove(i) ;
                        mManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmView.class);
                        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), i, intent, 0);
                        mManager.cancel(pi);
                        pi.cancel();
                    }
                }
                // 모든 선택 상태 초기화.
                listView.clearChoices() ;

                adapter.notifyDataSetChanged();
            }

        });

        //add btn
        scheduleAdd = (ImageButton) view.findViewById(R.id.Addschedule);
        scheduleAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleFragment.class);
                startActivityForResult(intent, REQ_ADD_CONTACT);
            }
        });

        //alarm
        mNotification = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        calendar = Calendar.getInstance();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ADD_CONTACT) {
            //fragment 에는 RESULT_OK 이 없으므로, getActivity() 해줘야함
            if (resultCode == getActivity().RESULT_OK) {
                boolean alarmType = data.getBooleanExtra("alarmType", true);
                String title = data.getStringExtra("title");
                int hour = Integer.valueOf(data.getStringExtra("hour"));
                int minute = Integer.valueOf(data.getStringExtra("minute"));
                String genre = data.getStringExtra("genre");

                if (alarmType == false) // day repeat
                {
                    byte week = data.getByteExtra("dayRepeat", (byte) 0);
                    ad = new alarmData(0, penCnt, title, 0, 0, 0, hour, minute, genre, week);
                } else {
                    int year = data.getIntExtra("year", 0);
                    int month = data.getIntExtra("month", 0);
                    int day = data.getIntExtra("day", 0);
                    ad = new alarmData(1,penCnt, title, year, month, day, hour, minute, genre, (byte) 0);
                }

                aDBHelper.insertAlarmData(ad);
                adapter.addItem(ad);
                adapter.notifyDataSetChanged();

                setAlarm();
            }
        }
    }

    public void setAlarm()
    {
        mManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        calendar.set(Calendar.HOUR_OF_DAY, ad.getHour());
        calendar.set(Calendar.MINUTE, ad.getMinute());
        calendar.set(calendar.SECOND, 00);
        if(ad.getFlag() == false)
        {
            calendar.set(calendar.YEAR, calendar.get(Calendar.YEAR));
            calendar.set(calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(calendar.DATE, calendar.get(Calendar.DAY_OF_MONTH));
            long oneday = 24 * 60 * 60 * 1000;
            mManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), oneday, pendingIntent());
            Log.e("요일 반복", String.valueOf(ad.getHour())+"시" + String.valueOf(ad.getMinute())+"분");
        }
        else
        {
            calendar.set(calendar.YEAR, ad.getYear());
            calendar.set(calendar.MONTH, ad.getMonth());
            calendar.set(calendar.DATE, ad.getDay());
            mManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent());
            Log.e("특정 날짜", String.valueOf(ad.getHour())+"시" + String.valueOf(ad.getMinute())+"분");
        }
    }
    private PendingIntent pendingIntent() {
        Intent i = new Intent(getActivity().getApplicationContext(), AlarmView.class);
        i.putExtra("alarmType",ad.getFlag());
        i.putExtra("title", ad.getTitle()); //alarm title name 전달
        i.putExtra("week", getSelectedDay(ad.getWeek()));
        i.putExtra("genre",ad.getGenre());

        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), penCnt, i, 0);
        editor.putInt("penCntValue",++penCnt);
        Log.e("penCnt ++ : ", String.valueOf(penCnt));
        editor.commit();

        return pi;
    }

    private boolean[] getSelectedDay(byte week)
    {
        boolean[] day_list = new boolean[7];
        Arrays.fill(day_list,false);

        for(int i = 6; i>=0; i--)
        {
            if((week & (byte)Math.pow(2,i))!= (byte)0)
            {
                day_list[6-i] = true;
            }
        }
        return day_list;
    }
}