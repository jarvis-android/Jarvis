package com.example.jarvis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.example.util.DatabaseUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView eventList;
	private GestureDetector gestureDetector;
	private int currentYear, currentMonth, currentDay;
	private SQLiteDatabase dataHelper;
	public static DatabaseUtil dataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBase = new DatabaseUtil(this);
		dataHelper = dataBase.getReadableDatabase();
		gestureDetector = new GestureDetector(new DefaultGestureDetector());
		setContentView(R.layout.activity_main);
		CalendarView calendarView = (CalendarView) findViewById(R.id.calendarViewMain);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				String date = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
				// List<String> days=new ArrayList<String>();
				currentYear = year;
				currentMonth = month;
				currentDay = dayOfMonth;
				eventList.setAdapter(new SimpleAdapter(MainActivity.this,
						getData(currentYear, currentMonth, currentDay),
						R.layout.event_list, new String[] { "eventName",
								"justSpace", "eventTime" },
						new int[] { R.id.eventName, R.id.justSpace,
								R.id.eventTime }));
				Toast.makeText(getApplicationContext(), date, 0).show();
			}
		});

		eventList = (ListView) this.findViewById(R.id.listViewEvent);
		// eventList.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,getData()));
		eventList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
	}


	private List<HashMap<String, Object>> getData(int year, int month, int day) {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		Cursor cursor = dataBase.Event_Select(year, month, day);
		while (cursor.moveToNext()) {
			Map<String, Object> item = new HashMap<String, Object>();
			String title = cursor.getString(cursor.getColumnIndex("Title"));
			// description=cursor.getString(cursor.getColumnIndex("Description"));
			String time = "" + cursor.getInt(cursor.getColumnIndex("Start_HH"))
					+ ":" + cursor.getString(cursor.getColumnIndex("Start_mm"));
			int type = cursor.getInt(cursor.getColumnIndex("Event_Type"));
			switch (type) {
			case 1:
				item.put("eventColor", R.drawable.blue);
				break;
			case 2:
				item.put("eventColor", R.drawable.green);
				break;
			case 3:
				item.put("eventColor", R.drawable.red);
				break;
			case 4:
				item.put("eventColor", R.drawable.yellow);
				break;
			}
			item.put("eventName", title);
			item.put("justSpace", " ");
			item.put("eventTime", time);
			data.add((HashMap<String, Object>) item);
		}
		cursor.close();
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "about");
		menu.add(0, 2, 1, "settings");
		menu.add(0, 3, 2, "Add Event");
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intentAbout = new Intent(this, AboutActivity.class);
			startActivity(intentAbout);
			return true;
		case 2:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivity(intentSettings);
			return true;
		case 3:
			Intent intentAddEvent = new Intent(this, ScheduleAddActivity.class);
			startActivity(intentAddEvent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class DefaultGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			final int FLING_MIN_DISTANCE = 100;// X或者y轴上移动的距离(像素)
			final int FLING_MIN_VELOCITY = 200;// x或者y轴上的移动速度(像素/秒)
			if ((e1.getX() - e2.getX()) > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				Intent openWatchEvent = new Intent(MainActivity.this,
						WatchEventActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("Year", currentYear);
				bundle.putInt("Month", currentMonth);
				bundle.putInt("Day", currentDay);
				openWatchEvent.putExtras(bundle);
				startActivity(openWatchEvent);
			} else if ((e2.getX() - e1.getX()) > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY)
				Toast.makeText(MainActivity.this, "向右滑动", Toast.LENGTH_SHORT)
						.show();
			return false;
		}
	}
}
