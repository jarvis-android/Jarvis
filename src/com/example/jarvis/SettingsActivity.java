package com.example.jarvis;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	protected Button changebackground;
	protected CheckBox openwhenturnon1;
	protected CheckBox vibrator1;
	protected SeekBar soundBar1;
	protected Button savingchanges;
	private ImageButton imageButton_white1;
	private MediaPlayer mediaPlayer01;
	public AudioManager audiomanage;
	private TextView mVolume; // 显示当前音量
	public SeekBar soundBar;
	private int maxVolume, currentVolume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Connect interface elements to properties
		changebackground = (Button) findViewById(R.id.changebackground);
		openwhenturnon1 = (CheckBox) findViewById(R.id.openwhenturnon);
		vibrator1 = (CheckBox) findViewById(R.id.vibrator);
		soundBar1 = (SeekBar) findViewById(R.id.soundBar);
		savingchanges = (Button) findViewById(R.id.savingchanges);

		changebackground.setOnClickListener(this);
		openwhenturnon1.setOnClickListener(this);
		vibrator1.setOnClickListener(this);
		soundBar1.setOnClickListener(this);
		savingchanges.setOnClickListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		return;
	}


}