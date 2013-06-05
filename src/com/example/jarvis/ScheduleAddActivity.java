package com.example.jarvis;

import java.util.Calendar;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Handler;
import com.example.util.*;
import com.example.jarvis.MainActivity;;

public class ScheduleAddActivity extends Activity {

        private TextView saveText;
        private static final String[] rep = { "不重复", "每天", "每工作日", "每周", "每两周",
                        "每月", "每年" };
        private static final String[] rem = { "不提醒", "事件发生时", "15分钟前", "30分钟前",
                        "一小时前", "一天前" };
        private Spinner spinnerrep;
        private Spinner spinnerrem;
        private ArrayAdapter<String> adapterrep;
        private ArrayAdapter<String> adapterrem;
        private EditText showDate = null;
        private Button pickDate = null;
        private EditText showTime = null;
        private Button pickTime = null;
        private RatingBar rateBar;
        private EditText titleText;
        private EditText contentText;
        private RadioGroup radiogroup;
/*      private RadioButton radioWork;
        private RadioButton radioStudy;
        private RadioButton radioLife;
        private RadioButton radioOthers;*/

        private static final int SHOW_DATAPICK = 0;
        private static final int DATE_DIALOG_ID = 1;
        private static final int SHOW_TIMEPICK = 2;
        private static final int TIME_DIALOG_ID = 3;

        private int mYear;
        private int mMonth;
        private int mDay;
        private int mHour;
        private int mMinute;
        private int reminder;
        private int recurrence;

        // 设置保存各个参数的变量

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_schedule_add);
                // 调用读取各个参数的函数，设置默认值
                rateBar = (RatingBar)findViewById(R.id.ratingBar1);
                titleText = (EditText)findViewById(R.id.titletext);
                contentText = (EditText)findViewById(R.id.editText1);
                radiogroup = (RadioGroup)findViewById(R.id.radioGroup1);
                // 处理重复下拉框
                spinnerrep = (Spinner) findViewById(R.id.repeat);
                adapterrep = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, rep);
                adapterrep
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerrep.setAdapter(adapterrep);
                spinnerrep.setOnItemSelectedListener(new SpinnerSelectedListener());
                spinnerrep.setVisibility(View.VISIBLE);
                spinnerrep.setPrompt("请选择事件周期");

                // 处理提醒下拉框
                spinnerrem = (Spinner) findViewById(R.id.reminder);
                adapterrem = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, rem);
                adapterrem
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerrem.setAdapter(adapterrem);
                spinnerrem.setOnItemSelectedListener(new SpinnerSelectedListener());
                spinnerrem.setVisibility(View.VISIBLE);
                spinnerrem.setPrompt("请选择提醒时间");

/*              radioStudy=(RadioButton)findViewById(R.id.radioStudy);
                radioLife=(RadioButton)findViewById(R.id.radioLife);
                radioWork=(RadioButton)findViewById(R.id.radioWork);
                radioOthers=(RadioButton)findViewById(R.id.radioOthers);*/

        //      radioStudy.setOnCheckedChangeListener();


                initializeViews();

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                setDateTime();
                setTimeOfDay();

                // 处理save
                saveText = (TextView) this.findViewById(R.id.save);
                saveText.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // 各变量=从各个控件处get参数
                                // 调用存入数据库函数将各变量存入数据库
                                int type=getType(radiogroup.getCheckedRadioButtonId());
                                int rate=ScheduleAddActivity.this.rateBar.getNumStars();
                                String title = titleText.getText().toString();
                                String description = contentText.getText().toString();
                                int state=0;
                                MainActivity.dataBase.Events_Add(type, rate, mYear, mMonth, mDay, mHour, mMinute, title, description, reminder, recurrence, state);
                                ScheduleAddActivity.this.finish();
                        }

                        private int getType(int checkedRadioButtonId) {
                                // TODO Auto-generated method stub
                                int type = -1;
                                switch(checkedRadioButtonId){
                                case R.id.radioWork:
                                        type = 1;
                                        break;
                                case R.id.radioLife:
                                        type = 2;
                                        break;
                                case R.id.radioStudy:
                                        type = 3;
                                        break;
                                case R.id.radioOthers:
                                        type = 4;
                                        break;
                                }
                                return type;
                        }
                });

        }

        public void setAlarm(int year,int month,int day,int hour,int minute,int repeat,String title)
        {
                Intent alarmIntent = new Intent("MYALARMRECEIVER"); 
                alarmIntent.putExtra("title", title);
                PendingIntent pi = PendingIntent.getBroadcast(this,0,alarmIntent,0);
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
                am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),600*1000,pi);  
        }


        class SpinnerSelectedListener implements OnItemSelectedListener {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
                        recurrence = arg2 + 1; 
                reminder = arg2 + 1;
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
        }

        private void initializeViews() {
                showDate = (EditText) findViewById(R.id.showdate);
                pickDate = (Button) findViewById(R.id.pickdate);
                showTime = (EditText) findViewById(R.id.showtime);
                pickTime = (Button) findViewById(R.id.picktime);

                pickDate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                Message msg = new Message();
                                if (pickDate.equals((Button) v)) {
                                        msg.what = ScheduleAddActivity.SHOW_DATAPICK;
                                }
                                ScheduleAddActivity.this.dateandtimeHandler.sendMessage(msg);
                        }
                });

                pickTime.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                                Message msg = new Message();
                                if (pickTime.equals((Button) v)) {
                                        msg.what = ScheduleAddActivity.SHOW_TIMEPICK;
                                }
                                ScheduleAddActivity.this.dateandtimeHandler.sendMessage(msg);
                        }
                });
        }

        /**
         * 设置日期
         */
        private void setDateTime() {
                final Calendar c = Calendar.getInstance();

                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                updateDateDisplay();
        }

        /**
         * 更新日期显示
         */
        private void updateDateDisplay() {
                showDate.setText(new StringBuilder().append(mYear).append("-")
                                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                .append("-").append((mDay < 10) ? "0" + mDay : mDay));
        }

        /**
         * 日期控件的事件
         */
        private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        updateDateDisplay();
                }
        };

        /**
         * 设置时间
         */
        private void setTimeOfDay() {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                updateTimeDisplay();
        }

        /**
         * 更新时间显示
         */
        private void updateTimeDisplay() {
                showTime.setText(new StringBuilder().append(mHour).append(":")
                                .append((mMinute < 10) ? "0" + mMinute : mMinute));
        }

        /**
         * 时间控件事件
         */
        private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        updateTimeDisplay();
                }
        };

        @Override
        protected Dialog onCreateDialog(int id) {
                switch (id) {
                case DATE_DIALOG_ID:
                        return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                                        mDay);
                case TIME_DIALOG_ID:
                        return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
                                        true);
                }

                return null;
        }

        @Override
        protected void onPrepareDialog(int id, Dialog dialog) {
                switch (id) {
                case DATE_DIALOG_ID:
                        ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                        break;
                case TIME_DIALOG_ID:
                        ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                        break;
                }
        }

        /**
         * 处理日期和时间控件的Handler
         */
        Handler dateandtimeHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                        switch (msg.what) {
                        case ScheduleAddActivity.SHOW_DATAPICK:
                                showDialog(DATE_DIALOG_ID);
                                break;
                        case ScheduleAddActivity.SHOW_TIMEPICK:
                                showDialog(TIME_DIALOG_ID);
                                break;
                        }
                }
        };

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.schedule_add, menu);
                return true;
        }
}