package com.dianwork.mymusicplayer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianwork.mymusicplayer.MyMusicApplication;
import com.dianwork.mymusicplayer.R;
import com.dianwork.mymusicplayer.activity.ListActivity;
import com.dianwork.mymusicplayer.api.Song;
import com.dianwork.mymusicplayer.api.Song.Msg;

public class ArtistGridviewAdapter extends BaseAdapter {
	List<Msg> data;
	int layoutId;
	Context context;
	LayoutInflater inflater;
	Song song;
	MyMusicApplication application=null;

	public ArtistGridviewAdapter(Context context, List<Msg> data, int layoutId,
			Song song) {
		super();
		this.application=(MyMusicApplication) song.getApplication();
		this.song = song;
		this.data = data;
		this.layoutId = layoutId;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(ListActivity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	public List<Msg> getData() {
		return data;
	}

	public void setData(List<Msg> data) {
		this.data = data;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout layout;
		ViewHolder holder;
		if (convertView == null) {
			layout = (LinearLayout) inflater.inflate(layoutId, null);
			holder = new ViewHolder();
			holder.artistnameTextView = (TextView) layout
					.findViewById(R.id.textView1);
			holder.numberTextView = (TextView) layout
					.findViewById(R.id.textView2);
			holder.albumImageView = (ImageView) layout
					.findViewById(R.id.image_small);

			layout.setTag(holder);
		} else {
			layout = (LinearLayout) convertView;
			holder = (ViewHolder) layout.getTag();
		}
		String artistNameString = data.get(position).getArtist();
		holder.artistnameTextView.setText(artistNameString);
		holder.numberTextView.setText(application.getArtistMap()
				.get(artistNameString).size()
				+ "首歌");
		holder.albumImageView.setImageResource(R.drawable.album_default_small);
		return layout;
	}

	class ViewHolder {
		TextView artistnameTextView;
		TextView numberTextView;
		ImageView albumImageView;
	}

}