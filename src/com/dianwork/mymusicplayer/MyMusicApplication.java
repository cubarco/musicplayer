package com.dianwork.mymusicplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

import com.dianwork.mymusicplayer.api.Song;
import com.dianwork.mymusicplayer.api.Song.Msg;

public class MyMusicApplication extends Application {
	HashMap<String, ArrayList<Msg>> albumMap = new HashMap<String, ArrayList<Msg>>();
	HashMap<String, ArrayList<Msg>> artistMap = new HashMap<String, ArrayList<Msg>>();
	ArrayList<Msg> albumNameList = new ArrayList<Song.Msg>();
	ArrayList<Msg> artistNameList = new ArrayList<Song.Msg>();
	ArrayList<Msg> allList = new ArrayList<Song.Msg>();
	Boolean REMOVING;
	String lrcString;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		albumMap = null;
		artistMap = null;
		albumNameList = null;
		artistNameList = null;
		REMOVING = false;
	}

	public void setAllList(ArrayList<Msg> allList) {
		this.allList = allList;
	}

	public ArrayList<Msg> getAllList() {
		return allList;
	}

	public void setLrcString(String lrcString) {
		this.lrcString = lrcString;
	}

	public String getLrcString() {
		return lrcString;
	}

	public Boolean getREMOVING() {
		return REMOVING;
	}

	public void setREMOVING(Boolean rEMOVING) {
		REMOVING = rEMOVING;
	}

	public ArrayList<Msg> getAlbumNameList() {
		return albumNameList;
	}

	public void setAlbumNameList(ArrayList<Msg> albumNameList) {
		this.albumNameList = albumNameList;
	}

	public ArrayList<Msg> getArtistNameList() {
		return artistNameList;
	}

	public void setArtistNameList(ArrayList<Msg> artistNameList) {
		this.artistNameList = artistNameList;
	}

	public HashMap<String, ArrayList<Msg>> getAlbumMap() {
		return albumMap;
	}

	public void setAlbumMap(HashMap<String, ArrayList<Msg>> albumMap) {
		this.albumMap = albumMap;
	}

	public HashMap<String, ArrayList<Msg>> getArtistMap() {
		return artistMap;
	}

	public void setArtistMap(HashMap<String, ArrayList<Msg>> artistMap) {
		this.artistMap = artistMap;
	}

}
