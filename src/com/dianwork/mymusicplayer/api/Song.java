package com.dianwork.mymusicplayer.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.provider.MediaStore;
import android.util.Log;

import com.dianwork.mymusicplayer.MyMusicApplication;

public class Song {

	MediaPlayer MP3 = new MediaPlayer();
	int Sint, previousSint;
	File SaveP;// SaveP=this.getDir("File", MODE_PRIVATE);must be used in the
				// Activity before use
	String url = null, Toast = null;
	int St = 1;
	boolean open = false;
	boolean isPausing = false;
	Random Rom = new Random();
	MyList Def = new MyList();
	public MyList Now = new MyList();
	MyList Like = new MyList();
	ArrayList<MyList> Self = new ArrayList<MyList>();
	ArrayList<Msg> All = new ArrayList<Msg>();
	ArrayList<Msg> albumNameList = new ArrayList<Song.Msg>();
	ArrayList<Msg> artistNameList = new ArrayList<Song.Msg>();
	ArrayList<String> NN = new ArrayList<String>();

	MyMusicApplication application = null;

	public void setApplication(Application application) {
		this.application = (MyMusicApplication) application;
	}

	public MyMusicApplication getApplication() {
		return application;
	}

	public Song setPreviousSint(int previousSint) {
		this.previousSint = previousSint;
		return this;
	}

	public int getSint() {
		return Sint;
	}

	public Song setSint(int sint) {
		previousSint = Sint;
		Sint = sint;
		return this;
	}

	public ArrayList<Msg> getAllArrayList() {
		return All;
	}

	public void setAllArrayList(ArrayList<Msg> all) {
		All = all;
	}

	public File getSaveP() {
		return SaveP;
	}

	public void setSaveP(File saveP) {
		SaveP = saveP;
	}

	public class Msg implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3119157805517901245L;
		String name, url, artist, album;
		int Duration;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public String getAlbum() {
			return album;
		}

		public void setAlbum(String album) {
			this.album = album;
		}

		public int getDuration() {
			return Duration;
		}

		public void setDuration(int duration) {
			Duration = duration;
		}

	}

	public class Lrc {
		ArrayList<String> Word = new ArrayList<String>();
		ArrayList<String> TimeS = new ArrayList<String>();
		ArrayList<Integer> Time = new ArrayList<Integer>();
	}

	public class MyList {
		ArrayList<Msg> Playlist = new ArrayList<Msg>();
		String name;

		public ArrayList<Msg> getPlaylist() {
			return Playlist;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		int count = 0;
	}

	public void SetListen() {
		MP3.setOnCompletionListener(new MyPlayerListener());
	}

	private final class MyPlayerListener implements OnCompletionListener {
		// 歌曲播放完后自动播放下一首歌曲
		@Override
		public void onCompletion(MediaPlayer mp) {
			if (MP3.isLooping() == false && open == true) {
				// Sint = Next(Now, Sint);
				// Stop();
				// Play(Now.Playlist.get(Sint), true);
				nextPlayStart();
			}
		}
	}

	public void priviousPlayStart() {
		Sint = previousSint;
		MP3.release();
		MP3 = null;
		MP3 = new MediaPlayer();
		Play(Now.Playlist.get(previousSint), true);
		MP3.setOnCompletionListener(new MyPlayerListener());
	}

	public void nextPlayStart() {
		previousSint = Sint;
		Sint = Next(Now, Sint);
		MP3.release();
		MP3 = null;
		MP3 = new MediaPlayer();
		Play(Now.Playlist.get(Sint), true);
		MP3.setOnCompletionListener(new MyPlayerListener());
	}

	public boolean Isplay() {
		return MP3.isPlaying();
	}

	public boolean isPausing() {
		return isPausing;
	}

	public Song setThreadMusicListen() {
		new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				SetListen();
			}
		}.start();
		return this;
	}

	public void Welcome(String Wpath) {
		url = Wpath;
		try {
			MP3.setDataSource(url);
			Toast = "欢迎来到**音乐";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Toast = "初始播放失败";
		}
		MP3.start();
	}

	public Song Getnow(MyList m) {
		Now = new MyList();
		Now.count = m.count;
		Now.name = m.name;
		for (int i = 0; i < m.Playlist.size(); i++) {
			Now.Playlist.add(m.Playlist.get(i));
		}
		Sint = m.count;
		return this;
	}

	public Song AddToList(Msg n, MyList m) {
		m.count++;
		m.Playlist.add(0, n);
		Savelist(m, false);
		return this;
	}

	public Song RemoveFromList(Msg n, MyList m) {
		m.count--;
		m.Playlist.remove(n);
		Savelist(m, false);
		return this;
	}

	public Song AddList(MyList m) {
		Self.add(m);
		SaveAll(false);
		return this;
	}

	public Song RemoveList(MyList m) {
		Self.remove(m);
		SaveAll(false);
		return this;
	}

	public Song Savelist(MyList m, boolean mode) {
		// 保存数据
		String rel = "";
		if (mode == false) {// c存放list
			if (!(m.name == null)) {
				File sl = new File(SaveP.getAbsolutePath() + File.separator
						+ m.name + ".music");
				try {
					sl.createNewFile();
					FileOutputStream fo = new FileOutputStream(sl);
					for (int i = 0; i < m.count; i++) {
						rel = rel + m.Playlist.get(i).album + "^"
								+ m.Playlist.get(i).artist + "^"
								+ m.Playlist.get(i).name + "^"
								+ m.Playlist.get(i).url + "^"
								+ m.Playlist.get(i).Duration + "*";
					}
					if (!rel.equals("")) {
						fo.write(rel.getBytes());
					}
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	public Song SaveAll(boolean mode) {
		// 保存列表信息
		String rel = null;
		if (mode == false) {// 存放list的名字
			File sz = new File(SaveP.getAbsolutePath() + File.separator
					+ "File_Save" + ".music");
			try {
				sz.createNewFile();
				FileOutputStream fo = new FileOutputStream(sz);
				rel = Def.name + "*";
				rel = rel + Like.name + "*";
				for (int i = 0; i < Self.size(); i++) {
					rel = rel + Self.get(i).name + "*";
				}
				fo.write(rel.getBytes());
				fo.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}

	public Song Readlist(MyList m, boolean mode) {
		Msg make = null;
		String rel = "";
		if (mode == false) {
			File sz = new File(SaveP.getAbsolutePath() + File.separator
					+ m.name + ".music");
			if (sz.exists()) {
				try {
					Scanner sc = new Scanner(sz);
					while (sc.hasNext()) {
						rel = sc.nextLine().toString();
					}
					sc.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (!rel.equals("")) {
					String[] q = rel.split("\\*");
					for (int i = 0; i < q.length; i++) {
						String[] p = q[i].split("\\^");
						make = new Msg();
						make.album = p[0];
						make.artist = p[1];
						make.name = p[2];
						make.url = p[3];
						make.Duration = Integer.parseInt(p[4]);
						m.Playlist.add(make);
						m.count++;
					}
				}
			}
		}
		return this;
	}

	public Song Getlist(boolean mode) {

		String rel = null;
		if (mode == false) {
			File sz = new File(SaveP.getAbsolutePath() + File.separator
					+ "File_Save" + ".music");
			try {
				Scanner sc = new Scanner(sz);
				while (sc.hasNext()) {
					rel = sc.nextLine().toString();
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String[] q = rel.split("\\*");
			Def.name = q[0];
			Like.name = q[1];
			for (int i = 2; i < q.length; i++) {
				MyList make = new MyList();
				make.name = q[i];
				Self.add(make);
			}
		}
		return this;
	}

	public Song FindSong(ContentResolver cr) {// 浏览找歌曲 ，保存在All里
		// Attention : ContentResolver cr = this.getContentResolver(); which you
		// give me
		// before use it

		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				Msg f = new Msg();
				f.name = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				f.album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				f.artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				f.url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				f.Duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

				All.add(f);
			}
			application.setAllList(All);
		}
		// listView结束后toast一下
		return this;
	}

	public Song Play(Msg n, boolean Change) {// 播放歌曲
		isPausing = false;
		if (Change == true) {
			previousSint = Sint;
			url = n.url;
			try {
				Log.i("url", url);
				MP3.setDataSource(url);
				MP3.prepare();
				open = true;
				Toast = "资源已加载";
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Toast = "加载失败";
			}
		}
		MP3.start();
		return this;
	}

	public Song Stop() {
		MP3.stop();
		MP3.release();
		MP3 = new MediaPlayer();
		return this;
	}

	public Song Pause() {
		MP3.pause();
		isPausing = true;
		return this;
	}

	// this index means the number of this song begin with 0
	public int Former(MyList m, int index) {
		if (index == 0) {
			return m.count - 1;
		} else {
			return index - 1;
		}
	}

	public int Next(MyList m, int index) {
		switch (St) {
		case 1:
			if (index == m.count - 1) {
				return 0;
			} else {
				return index + 1;
			}
		case 2:
			return index;
		case 3:
			return Rom.nextInt(m.count);
		default:
			break;
		}
		return index;

	}

	public Song TimeGo(int g) {
		MP3.seekTo(g);
		MP3.start();
		return this;
	}

	public int GetLen() {
		return MP3.getDuration();
	}

	public int Getnow() {
		return MP3.getCurrentPosition();
	}

	public Song Update(boolean mode) {
		if (mode == false) {
			try {
				MP3.prepare();
			} catch (IOException e) {
				e.printStackTrace();
				Toast = "已准备完毕或准备失败";
			}
		} else
			MP3.reset();
		return this;
	}

	public Song setStyle(int st) {
		St = st;
		MP3.setLooping(false);
		return this;
	}

	// public Song Style(int mode) {
	// switch (mode) {
	// case 1:
	// St = 1;
	// MP3.setLooping(false);
	// break;
	// case 2:
	// St=2;
	// MP3.setLooping(false);
	// break;
	// case 3:
	// St = 3;
	// MP3.setLooping(false);
	// break;
	// default:
	// break;
	// }
	// return this;
	// }

	//
	// public void getBar(){}
	// 获取歌词

	public String Getlrc(Msg n) {
		String lrc = null, ci = null;
		NN = null;
		NN = new ArrayList<String>();
		String[] s = n.url.split("\\.");
		Log.i("u.url", n.url);
		String Lpath = s[0] + ".lrc";
		Log.i("Lpath", Lpath);
		File lr = new File(Lpath);
		try {
			Scanner sc = new Scanner(lr);
			while (sc.hasNext()) {
				lrc = sc.nextLine().toString();
				String[] mylrc = lrc.split("\\]");
				// if (mylrc.length >= 2 && (!mylrc.equals(""))) {
				ci = mylrc[mylrc.length - 1];
				NN.add(ci);
				// }
				Log.i("Ci", ci);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		lrc = "";
		for (int i = 0; i < NN.size(); i++) {
			lrc = lrc + NN.get(i) + '\n';
		}
		Log.e("Lrc", lrc);

		return lrc;
	}

	public Str Getstr(Msg n) {
		String lrc = null;
		String[] s = n.url.split("\\.");
		String Lpath = s[0] + ".lrc";
		File lr = new File(Lpath);
		String regex = "\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d --> \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d";
		Str qu = new Str();
		;
		try {
			Scanner sc = new Scanner(lr);
			while (sc.hasNext()) {
				lrc = sc.nextLine().toString();
				if (lrc.equals(""))
					continue;
				if (!Pattern.matches(regex, lrc)) {
					qu.ci.add(lrc);
				} else {
					int second = Integer.parseInt(lrc.substring(6, 8));
					int minute = Integer.parseInt(lrc.substring(3, 5));
					int hour = Integer.parseInt(lrc.substring(0, 2));
					qu.Timea.add(second + 60 * minute + hour * 3600);
					second = Integer.parseInt(lrc.substring(23, 25));
					minute = Integer.parseInt(lrc.substring(20, 22));
					hour = Integer.parseInt(lrc.substring(17, 19));
					qu.Timeb.add(second + 60 * minute + hour * 3600);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return qu;
	}

	public class Str {
		ArrayList<Integer> Timea = new ArrayList<Integer>();
		ArrayList<Integer> Timeb = new ArrayList<Integer>();
		ArrayList<String> ci = new ArrayList<String>();
	}

	public void ShareWith(Context con, Msg n) {//
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, n.name);
		share.putExtra(Intent.EXTRA_TEXT, "这是我喜欢的来自专辑《" + n.album + "》的，由\""
				+ n.artist + "\"演唱的\"" + n.name + "\",希望你们也能喜欢");
		share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		con.startActivity(Intent.createChooser(share, n.name));
	}

	public Song setInfoMapAndArraylist() {
		ArrayList<Msg> listTemp;
		HashMap<String, ArrayList<Msg>> mapTemp1 = new HashMap<String, ArrayList<Msg>>();
		for (Msg i : All) {
			listTemp = mapTemp1.get(i.getAlbum());
			if (listTemp != null) {
				listTemp.add(i);
			} else {
				listTemp = new ArrayList<Song.Msg>();
				listTemp.add(i);
				mapTemp1.put(i.getAlbum(), listTemp);
			}
		}
		application.setAlbumMap(mapTemp1);

		HashMap<String, ArrayList<Msg>> mapTemp2 = new HashMap<String, ArrayList<Msg>>();
		for (Msg i : All) {
			listTemp = mapTemp2.get(i.getArtist());
			if (listTemp != null) {
				listTemp.add(i);
			} else {
				listTemp = new ArrayList<Song.Msg>();
				listTemp.add(i);
				mapTemp2.put(i.getArtist(), listTemp);
			}
		}
		application.setArtistMap(mapTemp2);

		ArrayList<Msg> listTemp1 = new ArrayList<Song.Msg>();
		for (String name : application.getAlbumMap().keySet()) {
			listTemp1.add(application.getAlbumMap().get(name).get(0));
		}
		application.setAlbumNameList(listTemp1);

		ArrayList<Msg> listTemp2 = new ArrayList<Song.Msg>();
		for (String name : application.getArtistMap().keySet()) {
			listTemp2.add(application.getArtistMap().get(name).get(0));
		}
		application.setArtistNameList(listTemp2);
		return this;
	}

}
