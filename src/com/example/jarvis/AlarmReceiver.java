package com.example.jarvis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("alarm_message");
			for (int i = 0; i < 5; i++) {
				Toast.makeText(context, message + "时间到了！", Toast.LENGTH_SHORT)
						.show();
			}
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); 

			//创建media player
			MediaPlayer mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			            mMediaPlayer.setLooping(true);
			            mMediaPlayer.prepare();
			            mMediaPlayer.start();
			  }
		} catch (Exception e) {
			Toast.makeText(
					context,
					"There was an error somewhere, but we still received an alarm",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();

		}

	}
}
