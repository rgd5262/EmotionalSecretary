package com.example.nago.es_2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nago on 2016-11-02.
 */
public class ScheduleFragment extends Activity implements TimePickerDialog.OnTimeSetListener,
        RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener{

    private Calendar now;
    private TextView timeTextView;
    private ImageButton addBtn;
    private ImageButton cancelBtn;
    private EditText editText;
    private DatePickerDialog dpd;
    private ToggleButton[] daybutton;
    private RadioGroup rg;
    private RadioButton rd1;
    private RadioButton rd2;
    private Spinner spinner;
    private int year;
    private int month;
    private int day;
    private String genre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_detail);

        addBtn = (ImageButton)findViewById(R.id.addBtn);
        cancelBtn = (ImageButton)findViewById(R.id.cancelBtn);
        editText =  (EditText)findViewById(R.id.editText1);
        timeTextView = (TextView)findViewById(R.id.textview1);
        rg = (RadioGroup) findViewById(R.id.radiogroup1);
        rd1 = (RadioButton)findViewById(R.id.repeatSet);
        rd2 = (RadioButton)findViewById(R.id.daySet);
        spinner= (Spinner)findViewById(R.id.genreSpinner);
        rg.setOnCheckedChangeListener(this);

        String initial_time = getInitialTime();
        timeTextView.setText(initial_time);
        now = Calendar.getInstance();

        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);

        //시간 설정
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void onClick(View v) {
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                                ScheduleFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                tpd.getTextview(timeTextView);
                tpd.setThemeDark(true);
                tpd.vibrate(true);
                tpd.enableMinutes(true);
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        //addBtn
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //alarm data set
                Intent intent = getIntent();
                boolean check_alarm_type = false; // false = day repeat, true = select day

                String data = timeTextView.getText().toString();
                String delimiter = ":";
                String[] time = data.split(delimiter);

                String hour = time[0];
                String minute = time[1];
                String title = editText.getText().toString();

                byte week = getSelectedDay();

                intent.putExtra("title", title);
                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);
                intent.putExtra("genre",genre);

                //day repeat
                if(rd1.isChecked() == true)
                {
                    check_alarm_type = false;
                    intent.putExtra("alarmType",check_alarm_type);
                    intent.putExtra("dayRepeat",week);
                }
                //select day
                else
                {
                    check_alarm_type = true;
                    intent.putExtra("alarmType",check_alarm_type);
                    intent.putExtra("year",year);
                    intent.putExtra("month",month);
                    intent.putExtra("day",day);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //cancelBtn
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        //spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.genre, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) spinner.getSelectedItem();
                genre = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                genre = "Random";
            }
        });
    }

    private String getInitialTime() {
        long now_Time = System.currentTimeMillis();
        Date date = new Date(now_Time);

        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        String strCurHour = CurHourFormat.format(date);
        String strCurMinute = CurMinuteFormat.format(date);

        String time = strCurHour + ":" + strCurMinute;



        return time;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup.getCheckedRadioButtonId() == R.id.repeatSet)
        {
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout8);
            rl.setVisibility(View.VISIBLE);
        }
        else
        {
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout8);
            rl.setVisibility(View.GONE);
        }

        if(radioGroup.getCheckedRadioButtonId() == R.id.daySet)
        {
            Calendar now = Calendar.getInstance();
            dpd = DatePickerDialog.newInstance(
                    ScheduleFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }

    }

    private byte getSelectedDay()
    {
        byte weekchecked = (byte)0;

        daybutton = new ToggleButton[7];
        daybutton[0] = (ToggleButton)findViewById(R.id.monButton);
        daybutton[1] = (ToggleButton)findViewById(R.id.TuesButton);
        daybutton[2] = (ToggleButton)findViewById(R.id.WedButton);
        daybutton[3] = (ToggleButton)findViewById(R.id.ThurButton);
        daybutton[4] = (ToggleButton)findViewById(R.id.FriButton);
        daybutton[5] = (ToggleButton)findViewById(R.id.SatButton);
        daybutton[6] = (ToggleButton)findViewById(R.id.SunButton);

        for(int i = 6; i>=0; i--)
        {
            if(daybutton[6-i].isChecked())
            {
                weekchecked |= (byte)Math.pow(2,i);
            }
        }
        return weekchecked;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
    }
}
