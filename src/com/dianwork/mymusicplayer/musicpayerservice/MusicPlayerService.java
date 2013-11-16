package com.dianwork.mymusicplayer.musicpayerservice;

import com.dianwork.mymusicplayer.MyMusicApplication;
import com.dianwork.mymusicplayer.activity.ListActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MusicPlayerService extends Service {

	private final String TAG = "Service";
	private final String TO_SERVICE = "TO_SERVICE";
	private final String FROM_SERVICE = "FROM_SERVICE";
	private final int PLAY_START_NEW = 99;
	private final int PLAY_START = 100;
	private final int PLAY_PAUSE = 101;
	private final int PLAY_STOP = 102;
	private final int TO_SEND_BACK_DURATION_NOW = 103;
	private final int TO_SEND_BACK_INFO_NOW = 104;
	private final int PLAY_SEEKTO = 105;
	private final int PLAY_PREVIOUS = 106;
	private final int PLAY_NEXT = 107;

	MyMusicApplication application = null;

	class MusicPlayerBinder extends Binder implements IBinder {
		public final MusicPlayerService getService() {
			return (MusicPlayerService.this);
		}
	}

	// TODO DO NOT laugh out after watching.
	class MyRceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 在这里写receiver的事件
			Bundle receiverBundle = intent.getExtras();
			int listactivity_command = receiverBundle.getInt("COMMAND");
			switch (listactivity_command) {
			case PLAY_START_NEW:
				break;
			case PLAY_START:
				Log.i("Service", "Going to play");
				ListActivity.song.Play(null, false);
				break;
			case PLAY_PREVIOUS:
				ListActivity.song.priviousPlayStart();
				break;
			case PLAY_NEXT:
				ListActivity.song.nextPlayStart();
				break;
			case PLAY_PAUSE:
				ListActivity.song.Pause();
				break;
			case PLAY_STOP:

				break;
			case PLAY_SEEKTO:
				int progress = receiverBundle.getInt("progress");
				ListActivity.song.TimeGo(progress);
				break;
			case TO_SEND_BACK_DURATION_NOW:

				break;
			case TO_SEND_BACK_INFO_NOW:

				break;
			default:
				break;
			}
		}
	}

	private final IBinder mMusicPlayerBinder = new MusicPlayerBinder();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(TAG, "MusicPlayerService onCreat.");
		application = (MyMusicApplication) ListActivity.song.getApplication();
		MyRceiver myRceiver = new MyRceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TO_SERVICE);
		registerReceiver(myRceiver, intentFilter);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.e(TAG, "MusicPlayerService onStart.");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e(TAG, "MusicPlayerService onDestroy.");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(TAG, "MusicPlayerService onBind.");
		return mMusicPlayerBinder;
	}

}
