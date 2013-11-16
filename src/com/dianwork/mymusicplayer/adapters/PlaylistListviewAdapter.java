package com.dianwork.mymusicplayer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dianwork.mymusicplayer.MyMusicApplication;
import com.dianwork.mymusicplayer.R;
import com.dianwork.mymusicplayer.activity.ListActivity;
import com.dianwork.mymusicplayer.api.Song;
import com.dianwork.mymusicplayer.api.Song.Msg;

public class PlaylistListviewAdapter extends BaseAdapter {
	List<Msg> data;
	int layoutId;
	Context context;
	LayoutInflater inflater;
	Song song;
	MyMusicApplication application;

	public PlaylistListviewAdapter(Context context, List<Msg> data,
			int layoutId, Song song) {
		super();
		this.song = song;
		this.data = data;
		this.layoutId = layoutId;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(ListActivity.LAYOUT_INFLATER_SERVICE);
		this.application = (MyMusicApplication) this.song.getApplication();

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
			holder.songnameTextView = (TextView) layout
					.findViewById(R.id.textView1);
			holder.artistnametTextView = (TextView) layout
					.findViewById(R.id.textView2);
			layout.setTag(holder);
		} else {
			layout = (LinearLayout) convertView;
			holder = (ViewHolder) layout.getTag();
		}
		holder.songnameTextView.setText(data.get(position).getName());
		holder.artistnametTextView.setText(data.get(position).getArtist());
		ImageButton addButton = (ImageButton) layout
				.findViewById(R.id.remove_from_playlist_button);
		final int buttonPosition = position;
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(
						context,
						"从播放列表中删除了" + "\"" + data.get(buttonPosition).getName()
								+ "\"", Toast.LENGTH_SHORT).show();
				song.RemoveFromList(song.Now.getPlaylist().get(buttonPosition),
						song.Now);
				notifyDataSetChanged();
				song.Stop();
			}
		});
		return layout;
	}

	class ViewHolder {
		TextView songnameTextView;
		TextView artistnametTextView;
	}

}
