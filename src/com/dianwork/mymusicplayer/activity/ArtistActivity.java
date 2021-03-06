package com.dianwork.mymusicplayer.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianwork.mymusicplayer.MyMusicApplication;
import com.dianwork.mymusicplayer.R;
import com.dianwork.mymusicplayer.adapters.SongsListviewAdapter;
import com.dianwork.mymusicplayer.api.Song.Msg;

public class ArtistActivity extends Activity {
	ArrayList<Msg> artistSongList;
	TextView artistNameTextView, numTextView;
	ListView listView;
	MyMusicApplication application = (MyMusicApplication) ListActivity.song
			.getApplication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist);
		this.setTitle("啊！菠萝");
		artistNameTextView = (TextView) this.findViewById(R.id.textView1);
		numTextView = (TextView) this.findViewById(R.id.textView2);
		listView = (ListView) this.findViewById(R.id.listview_activity_artist);

		Intent intent = getIntent();
		String artistName = intent.getStringExtra("ArtistName");
		artistSongList = application.getArtistMap().get(artistName);

		artistNameTextView.setText(artistName);
		numTextView.setText("共有"
				+ application.getArtistMap().get(artistName).size() + "首歌");

		SongsListviewAdapter songsListviewAdapter = new SongsListviewAdapter(
				this, artistSongList, R.layout.listviewitem, ListActivity.song);
		listView.setAdapter(songsListviewAdapter);
		listView.setOnItemClickListener(new myOnItemClickListener());
	}

	class myOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// 曲目不存在时添加到playlist
			String songNameTemp = artistSongList.get(arg2).getName();
			String artistNameTemp = artistSongList.get(arg2).getArtist();
			int sum = 0, sintTemp = 0;
			for (int i = 0; i < ListActivity.song.Now.getPlaylist().size(); i++) {
				if (ListActivity.song.Now.getPlaylist().get(i).getName()
						.equals(songNameTemp)
						&& ListActivity.song.Now.getPlaylist().get(i)
								.getArtist().equals(artistNameTemp)) {
					sintTemp = i;
					sum++;
				}
			}
			if (sum == 0) {
				ListActivity.song.AddToList(artistSongList.get(arg2),
						ListActivity.song.Now);
				ListActivity.playlistListviewAdapter.notifyDataSetChanged();
				Toast.makeText(
						ArtistActivity.this,
						"向播放列表添加了\"" + artistSongList.get(arg2).getName()
								+ "\"", Toast.LENGTH_SHORT).show();
			} else {
				ListActivity.song.setSint(sintTemp);
			}

			String lrcTempString = ListActivity.song.Getlrc(artistSongList
					.get(arg2));
			if (lrcTempString.equals("")) {
				application
						.setLrcString("    歌词文件不存在，如果需要歌词，请把lrc文件放到歌曲文件的目录下，以同样的文件名命名。");
			} else {
				Toast.makeText(ArtistActivity.this, "找到歌词了，快去看看吧。",
						Toast.LENGTH_SHORT).show();
				application.setLrcString(lrcTempString);
			}

			setResult(RESULT_OK);
			// Intent intent=new Intent(ArtistActivity.this,
			// ListActivity.class);
			// startActivity(intent);
			finish();
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
		}
		return super.onKeyDown(keyCode, event);
	}
}
