package com.example.jarvis;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WatchEventActivity extends Activity {

	private ListView watchList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		watchList = (ListView) this.findViewById(R.id.listView1);
		setContentView(R.layout.activity_watch_event);
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		int year = bundle.getInt("Year");
		int month = bundle.getInt("Month");
		int day = bundle.getInt("Day");
	}

	private List<String> getData(int year, int month, int day) {
		List<String> data = new ArrayList<String>();
		String title = year + "年" + month + "月" + day + "日";
		data.add(title + "的事件1");
		data.add(title + "的事件2");
		data.add(title + "的事件3");
		data.add(title + "的事件4");
		data.add(title + "的事件5");
		data.add(title + "的事件6");
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.watch_event, menu);
		return true;
	}

}
