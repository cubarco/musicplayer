package com.dianwork.mymusicplayer.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dianwork.mymusicplayer.MyMusicApplication;
import com.dianwork.mymusicplayer.R;
import com.dianwork.mymusicplayer.adapters.AlbumGridviewAdapter;
import com.dianwork.mymusicplayer.adapters.ArtistGridviewAdapter;
import com.dianwork.mymusicplayer.adapters.PlaylistListviewAdapter;
import com.dianwork.mymusicplayer.adapters.SongsListviewAdapter;
import com.dianwork.mymusicplayer.api.Song;
import com.dianwork.mymusicplayer.musicpayerservice.MusicPlayerService;

public class ListActivity extends Activity {
	public View view0, view1, view2, view3, view4;
	public ArrayList<View> viewList;
	// ListView listView;

	public static int REQUESTCODE_ALBUMACTIVITY = 100;
	public static int REQUESTCODE_ARTISTACTIVITY = 200;
	static boolean PLAY_BUTTON_PLAY = true;
	static boolean PLAY_BUTTON_PAUSE = false;
	static int STYLE_ALL_LOOP = 1;
	static int STYLE_ONE_LOOP = 2;
	static int STYLE_SHUFFLE = 3;
	private final String TO_SERVICE = "TO_SERVICE";
	private final String FROM_SERVICE = "FROM_SERVICE";
	private final int PLAY_START_NEW = 99;
	private final int PLAY_START = 100;
	private final int PLAY_PAUSE = 101;
	private final int PLAY_STOP = 102;
	private final int TO_SEND_BACK_DURATION_NOW = 103;
	private final int TO_SEND_BACK_INFO_NOW = 104;
	private final int PLAY_SEEKTO = 105;
	private final int SEEKBAR_SEEKTO = 106;
	private final int PLAY_PREVIOUS = 106;
	private final int PLAY_NEXT = 107;

	Boolean playButtonState = PLAY_BUTTON_PLAY;
	int chooseStyleButtonState = STYLE_ALL_LOOP;
	SongsListviewAdapter songsListviewAdapter;
	AlbumGridviewAdapter albumGridviewAdapter;
	ArtistGridviewAdapter artistGridviewAdapter;
	public static PlaylistListviewAdapter playlistListviewAdapter;

	MyMusicApplication application = null;
	public static Song song = null;
	ImageButton playButton, previousButton, nextButton, chooseStyleButton,
			shareButton, topshareButton;
	SeekBar seekBar;
	TextView songnameTextView, artistnameTextView;
	TextView lrcTextView;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("ListActivity", "Oncreat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		song = new Song();
		song.setApplication(this.getApplication());
		application = (MyMusicApplication) getApplication();
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		initPagarViews();
		song.setSaveP(this.getDir("File", MODE_PRIVATE));
		song.FindSong(this.getContentResolver());
		song.Now.setName("PlayingList");
		song.Readlist(song.Now, false).setInfoMapAndArraylist()
				.setStyle(STYLE_ALL_LOOP).setSint(0).setPreviousSint(0);

		Log.e("MainActivity", "to startService");

		playButton = (ImageButton) this.findViewById(R.id.play_button);
		previousButton = (ImageButton) this
				.findViewById(R.id.button_play_previous);
		nextButton = (ImageButton) this.findViewById(R.id.button_play_next);
		chooseStyleButton = (ImageButton) this
				.findViewById(R.id.button_choose_style);
		shareButton = (ImageButton) this.findViewById(R.id.button_share);

		playButton.setOnClickListener(new playButtonOnClickListener());
		previousButton.setOnClickListener(new previousButtonOnClickListener());
		nextButton.setOnClickListener(new nextButtonButtonOnClickListener());
		chooseStyleButton
				.setOnClickListener(new chooseStyleButtonOnClickListener());
		shareButton.setOnClickListener(new shareButtonOnClickListener());

		songnameTextView = (TextView) this
				.findViewById(R.id.bottom_playing_songname);
		artistnameTextView = (TextView) this
				.findViewById(R.id.bottom_playing_artistname);

		// song.SetListen();// 开多线程设置music监听
		song.setStyle(1);

		seekBar = (SeekBar) this.findViewById(R.id.seekBar_play_music);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// handler.removeCallbacks(updateThread);
				// song.Stop();
				// playButton.setImageResource(R.drawable.btn_playback_play);
				// playButtonState = PLAY_BUTTON_PLAY;

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

				if (fromUser == true) {
					// song.TimeGo(progress);
					sendBroadcast(new Intent().setAction(TO_SERVICE)
							.putExtra("COMMAND", PLAY_SEEKTO)
							.putExtra("progress", progress));
				}

			}
		});
		mSectionsPagerAdapter = new SectionsPagerAdapter();
		playlistListviewAdapter = new PlaylistListviewAdapter(this,
				song.Now.getPlaylist(), R.layout.playlistlistviewitem, song);

		songsListviewAdapter = new SongsListviewAdapter(this,
				song.getAllArrayList(), R.layout.listviewitem, song);

		albumGridviewAdapter = new AlbumGridviewAdapter(this,
				application.getAlbumNameList(), R.layout.gridviewitem);

		artistGridviewAdapter = new ArtistGridviewAdapter(this,
				application.getArtistNameList(), R.layout.gridviewitem, song);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);// 缓存页面数
		mViewPager.setCurrentItem(2);// 默认页面
		// mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
		// });
		IntentFilter filter = new IntentFilter();
		// 播出电话暂停音乐播放
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(new PhoneListener(), filter);
		TelephonyManager mager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mager.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);

		startService(new Intent(ListActivity.this, MusicPlayerService.class));
	}

	class MusicStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			int service_command = bundle.getInt("COMMAND");

			switch (service_command) {
			case SEEKBAR_SEEKTO:

				break;

			default:
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == REQUESTCODE_ARTISTACTIVITY || requestCode == REQUESTCODE_ALBUMACTIVITY)
				&& resultCode == RESULT_OK) {
			song.Stop();
			handler.removeCallbacks(updateThread);
			song.Play(song.Now.getPlaylist().get(0), true);
			seekBar.setMax(song.GetLen());
			handler.post(updateThread);

			song.SetListen();
			songnameTextView.setText(song.Now.getPlaylist().get(0).getName());
			artistnameTextView.setText(song.Now.getPlaylist().get(0)
					.getArtist());

			song.setSint(0);
			if (playButtonState == PLAY_BUTTON_PLAY) {
				playButton.setImageResource(R.drawable.btn_playback_pause);
				playButtonState = PLAY_BUTTON_PAUSE;
			}

			lrcTextView.setText(application.getLrcString());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (song.Isplay()) {
				moveTaskToBack(false);
				return true;
			} else {
				song.Stop();
			}
		}
		onDestroy();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(ListActivity.this, MusicPlayerService.class));
	};

	Handler handler = new Handler();
	Runnable updateThread = new Runnable() {
		public void run() {
			/*
			 * 效率极低的获取歌曲总长的方法
			 */
			if (!(seekBar.getMax() == song.GetLen())) {
				seekBar.setMax(song.GetLen());
			}
			/*
			 * 效率极低的获取歌曲信息的方法
			 */
			if (song.Now.getPlaylist().size() != 0) {
				String songName = song.Now.getPlaylist().get(song.getSint())
						.getName();
				String artistName = song.Now.getPlaylist().get(song.getSint())
						.getArtist();
				Boolean bool1 = songnameTextView.getText().toString()
						.equals(songName);
				Boolean bool2 = artistnameTextView.getText().toString()
						.equals(artistName);
				if (!(bool1 && bool2)) {
					songnameTextView.setText(songName);
					artistnameTextView.setText(artistName);
				}

			} else {
				songnameTextView.setText("");
				artistnameTextView.setText("");
			}
			// 获得歌曲现在播放位置并设置成播放进度条的值
			seekBar.setProgress(song.Getnow());
			// 每次延迟100毫秒再启动线程
			handler.postDelayed(updateThread, 100);
		}
	};

	private final class MyPhoneStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				song.Pause();
				playButtonState = PLAY_BUTTON_PLAY;
				playButton.setImageResource(R.drawable.btn_playback_play);
				handler.removeCallbacks(updateThread);
				break;

			case TelephonyManager.CALL_STATE_IDLE:
				song.Play(null, false);
				playButtonState = PLAY_BUTTON_PAUSE;
				playButton.setImageResource(R.drawable.btn_playback_pause);
				if (ListActivity.song.Now.getPlaylist().size() != 0) {
					handler.post(updateThread);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				song.Pause();
				playButtonState = PLAY_BUTTON_PLAY;
				playButton.setImageResource(R.drawable.btn_playback_play);
				handler.removeCallbacks(updateThread);
				break;
			default:
				break;
			}

		}
	}

	private final class PhoneListener extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			song.Pause();
			playButtonState = PLAY_BUTTON_PLAY;
			playButton.setImageResource(R.drawable.btn_playback_play);
			handler.removeCallbacks(updateThread);
		}
	}

	class playButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (playButtonState == PLAY_BUTTON_PLAY) {
				playButton.setImageResource(R.drawable.btn_playback_pause);
				playButtonState = PLAY_BUTTON_PAUSE;
				// song.Play(null, false);
				// Intent intent = new Intent();
				// intent.setAction(TO_SERVICE);
				// intent.putExtra("COMMAND", PLAY_START);
				sendBroadcast(new Intent().setAction(TO_SERVICE).putExtra(
						"COMMAND", PLAY_START));
				handler.post(updateThread);
			} else {
				playButton.setImageResource(R.drawable.btn_playback_play);
				playButtonState = PLAY_BUTTON_PLAY;
				handler.removeCallbacks(updateThread);
				// song.Pause();
				sendBroadcast(new Intent().setAction(TO_SERVICE).putExtra(
						"COMMAND", PLAY_PAUSE));
			}
		}
	}

	class shareButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (song.Isplay() || song.isPausing() == true) {
				song.ShareWith(ListActivity.this,
						song.Now.getPlaylist().get(song.getSint()));
			} else {
				Toast.makeText(ListActivity.this, "亲，先放一首你喜欢的歌吧。",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class previousButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (playButtonState == PLAY_BUTTON_PLAY) {
				playButton.setImageResource(R.drawable.btn_playback_pause);
				playButtonState = PLAY_BUTTON_PAUSE;
			}
			// song.priviousPlayStart();
			sendBroadcast(new Intent().setAction(TO_SERVICE).putExtra(
					"COMMAND", PLAY_PREVIOUS));
			handler.post(updateThread);
		}
	}

	class nextButtonButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (playButtonState == PLAY_BUTTON_PLAY) {
				playButton.setImageResource(R.drawable.btn_playback_pause);
				playButtonState = PLAY_BUTTON_PAUSE;
			}
			// song.nextPlayStart();
			sendBroadcast(new Intent().setAction(TO_SERVICE).putExtra(
					"COMMAND", PLAY_NEXT));
			handler.post(updateThread);
		}
	}

	class chooseStyleButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (chooseStyleButtonState) {
			case 1:
				chooseStyleButton
						.setImageResource(R.drawable.btn_playback_repeat_one);
				song.setStyle(STYLE_ONE_LOOP);
				chooseStyleButtonState = STYLE_ONE_LOOP;
				break;
			case 2:
				chooseStyleButton
						.setImageResource(R.drawable.btn_playback_shuffle_all);
				song.setStyle(STYLE_SHUFFLE);
				chooseStyleButtonState = STYLE_SHUFFLE;
				break;
			case 3:
				chooseStyleButton
						.setImageResource(R.drawable.btn_playback_repeat_all);
				song.setStyle(STYLE_ALL_LOOP);
				chooseStyleButtonState = STYLE_ALL_LOOP;
				break;
			default:
				break;
			}
		}
	}

	void initPagarViews() {
		@SuppressWarnings("static-access")
		LayoutInflater myInflater = getLayoutInflater().from(this);
		view0 = myInflater.inflate(R.layout.viewpager_playing, null);
		view1 = myInflater.inflate(R.layout.viewpager_playlist, null);
		view2 = myInflater.inflate(R.layout.viewpager_songs, null);
		view3 = myInflater.inflate(R.layout.viewpager_artist, null);
		view4 = myInflater.inflate(R.layout.viewpager_album, null);
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view0);
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.list, menu);
	// return true;
	// }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends PagerAdapter {

		// public SectionsPagerAdapter(FragmentManager fm) {
		// super(fm);
		// }

		public SectionsPagerAdapter() {
			// TODO Auto-generated constructor stub
		}

		// @Override
		// public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		// Fragment fragment = new DummySectionFragment();
		// Bundle args = new Bundle();
		// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		// fragment.setArguments(args);
		// return fragment;

		// Fragment =
		// }

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 5;// viewList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);

			View v = viewList.get(position);
			container.addView(v); // listview需要在这里初始化
			switch (position) {
			case 0:
				lrcTextView = (TextView) v.findViewById(R.id.lrc_textview);
				topshareButton = (ImageButton) v
						.findViewById(R.id.button_share_top);
				topshareButton
						.setOnClickListener(new shareButtonOnClickListener());
				break;
			case 1:
				ListView playlistView = (ListView) v
						.findViewById(R.id.listView_playlist);
				playlistView.setAdapter(playlistListviewAdapter);
				playlistView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						song.Stop();
						song.setSint(arg2);
						handler.removeCallbacks(updateThread);
						song.Play(song.Now.getPlaylist().get(arg2), true);
						// sendBroadcast(new Intent().setAction(TO_SERVICE)
						// .putExtra("COMMAND", PLAY_START_NEW)
						// .putExtra("Sint", arg2));
						seekBar.setMax(song.GetLen());
						handler.post(updateThread);
						song.SetListen();
						songnameTextView.setText(song.Now.getPlaylist()
								.get(arg2).getName());
						artistnameTextView.setText(song.Now.getPlaylist()
								.get(arg2).getArtist());

						String lrcTempString = song.Getlrc(song.Now
								.getPlaylist().get(arg2));
						if (lrcTempString.equals("")) {
							lrcTextView
									.setText("    歌词文件不存在，如果需要歌词，请把lrc文件放到歌曲文件的目录下，以同样的文件名命名。");
						} else {
							Toast.makeText(ListActivity.this, "找到歌词了，快去看看吧。",
									Toast.LENGTH_SHORT).show();

							lrcTextView.setText(lrcTempString);
						}

						if (playButtonState == PLAY_BUTTON_PLAY) {
							playButton
									.setImageResource(R.drawable.btn_playback_pause);
							playButtonState = PLAY_BUTTON_PAUSE;
						}
					}
				});

				break;
			case 2:
				ListView listView = (ListView) v
						.findViewById(R.id.listView_songs);
				listView.setAdapter(songsListviewAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// 曲目不存在时添加到playlist
						String songNameTemp = song.getAllArrayList().get(arg2)
								.getName();
						String artistNameTemp = song.getAllArrayList()
								.get(arg2).getArtist();
						int sum = 0, sintTemp = 0;
						for (int i = 0; i < song.Now.getPlaylist().size(); i++) {
							if (song.Now.getPlaylist().get(i).getName()
									.equals(songNameTemp)
									&& song.Now.getPlaylist().get(i)
											.getArtist().equals(artistNameTemp)) {
								sintTemp = i;
								sum++;
							}
						}
						if (sum == 0) {
							song.AddToList(song.getAllArrayList().get(arg2),
									song.Now);
							ListActivity.playlistListviewAdapter
									.notifyDataSetChanged();
							Toast.makeText(
									ListActivity.this,
									"向播放列表添加了\""
											+ song.getAllArrayList().get(arg2)
													.getName() + "\"",
									Toast.LENGTH_SHORT).show();
						} else {
							song.setSint(sintTemp);
						}

						song.Stop();
						handler.removeCallbacks(updateThread);
						song.Play(song.getAllArrayList().get(arg2), true);
						seekBar.setMax(song.GetLen());
						handler.post(updateThread);

						song.SetListen();
						songnameTextView.setText(song.getAllArrayList()
								.get(arg2).getName());
						artistnameTextView.setText(song.getAllArrayList()
								.get(arg2).getArtist());

						String lrcTempString = song.Getlrc(song
								.getAllArrayList().get(arg2));
						if (lrcTempString.equals("")) {
							lrcTextView
									.setText("    歌词文件不存在，如果需要歌词，请把lrc文件放到歌曲文件的目录下，以同样的文件名命名。");
						} else {
							Toast.makeText(ListActivity.this, "找到歌词了，快去看看吧。",
									Toast.LENGTH_SHORT).show();
							lrcTextView.setText(lrcTempString);
						}
						if (playButtonState == PLAY_BUTTON_PLAY) {
							playButton
									.setImageResource(R.drawable.btn_playback_pause);
							playButtonState = PLAY_BUTTON_PAUSE;
						}
					}
				});
				break;
			case 3:
				GridView artistGridView = (GridView) v
						.findViewById(R.id.gridView_artist);
				artistGridView.setAdapter(artistGridviewAdapter);
				artistGridView
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(ListActivity.this,
										ArtistActivity.class);
								intent.putExtra("ArtistName", application
										.getArtistNameList().get(arg2)
										.getArtist());
								startActivityForResult(intent,
										REQUESTCODE_ARTISTACTIVITY);
								overridePendingTransition(R.anim.in_from_top,
										R.anim.out_to_bottom);
							}
						});
				break;
			case 4:
				GridView albumGridView = (GridView) v
						.findViewById(R.id.gridView_albums);
				albumGridView.setAdapter(albumGridviewAdapter);
				albumGridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ListActivity.this,
								AlbumActivity.class);
						intent.putExtra("AlbumName", application
								.getAlbumNameList().get(arg2).getAlbum());
						startActivityForResult(intent,
								REQUESTCODE_ALBUMACTIVITY);
						overridePendingTransition(R.anim.in_from_top,
								R.anim.out_to_bottom);
					}
				});
				break;
			default:
				break;
			}

			return viewList.get(position);

		}

		public boolean isViewFromObject1(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
	}

	// /**
	// * A dummy fragment representing a section of the app, but that simply
	// * displays dummy text.
	// */
	// public static class DummySectionFragment extends Fragment {
	//
	// /**
	// * The fragment argument representing the section number for this
	// * fragment.
	// */
	// public static final String ARG_SECTION_NUMBER = "section_number";
	//
	// public DummySectionFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// View rootView = inflater.inflate(R.layout.fragment_list_dummy,
	// container, false);
	// TextView dummyTextView = (TextView) rootView
	// .findViewById(R.id.section_label);
	// dummyTextView.setText(Integer.toString(getArguments().getInt(
	// ARG_SECTION_NUMBER)));
	// return rootView;
	// }
	//
	// }
}
