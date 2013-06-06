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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView eventList;
	private GestureDetector gestureDetector;
	private int currentYear, currentMonth, currentDay;
	private SQLiteDatabase dataHelper;
	public static DatabaseUtil dataBase;
	private TextView addEventView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		dataBase = new DatabaseUtil(this);
		dataHelper = dataBase.getReadableDatabase();
		gestureDetector = new GestureDetector(new DefaultGestureDetector());
		addEventView = (TextView) findViewById(R.id.addNewEvent);
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
								"justSpace", "eventTime", "eventColor" },
						new int[] { R.id.eventName, R.id.justSpace,
								R.id.eventTime, R.id.eventColor }));
				Toast.makeText(getApplicationContext(), date, 0).show();
			}
		});

		eventList = (ListView) this.findViewById(R.id.listViewEvent);
		eventList.setClickable(true);
		// eventList.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1,getData()));
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView parentView, View childView,
					int position, long id) {
				// TODO Auto-generated method stub
				Map<String,Object> item=(Map<String, Object>) eventList.getItemAtPosition(position);
				int eventId = (Integer) item.get("id");
				Bundle extraInfo=new Bundle();
				extraInfo.putInt("id", eventId);
				Intent eventInfoIntent=new Intent(MainActivity.this,WatchEventActivity.class);
				eventInfoIntent.putExtras(extraInfo);
				startActivity(eventInfoIntent);
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
			String time = String.format("%02d:%02d",
					cursor.getInt(cursor.getColumnIndex("Start_HH")),
					cursor.getInt(cursor.getColumnIndex("Start_mm")));
			int type = cursor.getInt(cursor.getColumnIndex("Event_Type"));
			int id = cursor.getInt(cursor.getColumnIndex("Event_id"));
			int colorId;
			switch (type) {
			case 1:
				colorId = R.drawable.blue;
				break;
			case 2:
				colorId = R.drawable.green;
				break;
			case 3:
				colorId = R.drawable.red;
				break;
			case 4:
				colorId = R.drawable.yellow;
				break;
			default:
				colorId = R.drawable.blue;
			}
			item.put("eventColor", colorId);
			item.put("eventName", title);
			item.put("justSpace", " ");
			item.put("eventTime", time);
			item.put("id", id);
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
