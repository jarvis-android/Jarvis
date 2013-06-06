package com.example.jarvis;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.example.jarvis.MainActivity;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WatchEventActivity extends Activity {

	private ImageView titleColor;
	private TextView titleText;
	private TextView dateText;
	private TextView timeText;
	private TextView recallText;
	private TextView repeatText;
	private TextView detailedText;
	private TextView editText;
	private TextView deleteText;
	private int eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_event);

		titleColor = (ImageView) findViewById(R.id.titleColor);
		titleText = (TextView) findViewById(R.id.view_of_title);
		dateText = (TextView) findViewById(R.id.view_date);
		timeText = (TextView) findViewById(R.id.view_time);
		recallText = (TextView) findViewById(R.id.view_reminder);
		repeatText = (TextView) findViewById(R.id.view_recurrence);
		detailedText = (TextView) findViewById(R.id.view_description);
		editText = (TextView)findViewById(R.id.view_edit);
		deleteText = (TextView)findViewById(R.id.view_del);
		
		
		editText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		deleteText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.dataBase.Events_Delete(eventId);
				WatchEventActivity.this.finish();
			}
		});
		
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		eventId = bundle.getInt("id");
		Cursor eventCursor = MainActivity.dataBase.Event_Select(eventId);
		while (eventCursor.moveToNext()) {
			int typeId = eventCursor.getInt(eventCursor
					.getColumnIndex("Event_Type"));
			int colorId;
			switch (typeId) {
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
			titleColor.setImageResource(colorId);
			titleText.setText(eventCursor.getString(eventCursor
					.getColumnIndex("Title")));
			String dateFormated = eventCursor.getString(eventCursor.getColumnIndex("Start_Date"));
			dateText.setText("日期  "+dateFormated);
			timeText.setText(String.format("具体时间  %02d:%02d",
					eventCursor.getInt(eventCursor.getColumnIndex("Start_HH")),
					eventCursor.getInt(eventCursor.getColumnIndex("Start_mm"))));
			recallText.setText(eventCursor.getInt(eventCursor
					.getColumnIndex("Reminder")) == 0 ? "此事件不提醒" : "此事件提醒");
			repeatText.setText(eventCursor.getInt(eventCursor
					.getColumnIndex("Recurrence")) == 0 ? "此事件不重复" : "此事件重复");
			detailedText.setText("具体详情："+eventCursor.getString(eventCursor
					.getColumnIndex("Description")));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.watch_event, menu);
		return true;
	}

}
